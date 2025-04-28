package geometries;

import primitives.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The BoundingBox class represents an axis-aligned bounding box (AABB) for 3D geometries.
 * It provides methods for intersection tests and constructing bounding volume hierarchies (BVH).
 */
public class BoundingBox {
    /**
     * The minimum point of the bounding box
     */
    public final Point min;

    /**
     * The maximum point of the bounding box
     */
    public final Point max;

    // Cached center point for better performance
    private Point center;

    /**
     * Constructs a BoundingBox with specified minimum and maximum points.
     *
     * @param min the minimum point of the bounding box
     * @param max the maximum point of the bounding box
     */
    public BoundingBox(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Checks if a ray intersects the bounding box using an optimized slab method.
     *
     * @param ray the ray to test for intersection
     * @return true if the ray intersects the bounding box, false otherwise
     */
    public boolean hasIntersection(Ray ray) {
        Point head = ray.getPoint(0);
        Vector dir = ray.getDirection();

        double tMin = Double.NEGATIVE_INFINITY;
        double tMax = Double.POSITIVE_INFINITY;

        // For each axis X, Y, Z
        for (int axis = 0; axis < 3; axis++) {
            double axisDir = dir.get(axis);
            double axisOrigin = head.get(axis);
            double axisMin = min.get(axis);
            double axisMax = max.get(axis);

            // Handle parallel rays to an axis
            if (Math.abs(axisDir) < 1e-10) {
                // Ray is parallel to slab. No hit if origin not within slab
                if (axisOrigin < axisMin || axisOrigin > axisMax) {
                    return false;
                }
                // Otherwise, parallel ray is inside this slab, so continue to next axis
                continue;
            }

            // Calculate intersection distances
            double t1 = (axisMin - axisOrigin) / axisDir;
            double t2 = (axisMax - axisOrigin) / axisDir;

            // Ensure t1 is the nearest intersection
            if (t1 > t2) {
                double temp = t1;
                t1 = t2;
                t2 = temp;
            }

            // Update tMin and tMax
            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            // No intersection if tMin > tMax
            if (tMin > tMax) {
                return false;
            }
        }

        // Ray intersects all 3 slabs, so there's an intersection
        return true;
    }

    /**
     * Gets the center point of the bounding box (cached for performance).
     *
     * @return the center point of the bounding box
     */
    public Point getCenter() {
        if (center == null) {
            double centerX = (min.getX() + max.getX()) * 0.5;
            double centerY = (min.getY() + max.getY()) * 0.5;
            double centerZ = (min.getZ() + max.getZ()) * 0.5;
            center = new Point(centerX, centerY, centerZ);
        }
        return center;
    }

    /**
     * Creates a new bounding box that is the union of this bounding box and another.
     *
     * @param box the bounding box to union with
     * @return a new bounding box that encompasses both bounding boxes
     */
    public BoundingBox union(BoundingBox box) {
        if (box == null) {
            return this;
        }

        return new BoundingBox(
                new Point(
                        Math.min(min.getX(), box.min.getX()),
                        Math.min(min.getY(), box.min.getY()),
                        Math.min(min.getZ(), box.min.getZ())
                ),
                new Point(
                        Math.max(max.getX(), box.max.getX()),
                        Math.max(max.getY(), box.max.getY()),
                        Math.max(max.getZ(), box.max.getZ())
                )
        );
    }

    /**
     * Builds a bounding volume hierarchy (BVH) from a list of intersectable geometries.
     * Uses a more efficient approach with surface area heuristic for better spatial partitioning.
     *
     * @param intersectables the list of intersectable geometries
     * @return a list of intersectable geometries organized in a BVH
     */
    public static List<Intersectable> buildBVH(List<Intersectable> intersectables) {
        if (intersectables == null || intersectables.size() <= 1) {
            return intersectables;
        }

        // Separate geometries without a bounding box
        List<Intersectable> infiniteGeometries = new ArrayList<>();
        List<Intersectable> finiteGeometries = new ArrayList<>();

        for (Intersectable geo : intersectables) {
            if (geo.getBoundingBox() == null) {
                infiniteGeometries.add(geo);
            } else {
                finiteGeometries.add(geo);
            }
        }

        // If there are no finite geometries, return the original list
        if (finiteGeometries.isEmpty()) {
            return intersectables;
        }

        // Find the axis with the greatest variance for most efficient splitting
        int splitAxis = findSplitAxis(finiteGeometries);

        // Sort geometries along the chosen axis
        finiteGeometries.sort((a, b) -> {
            double centerA = a.getBoundingBox().getCenter().get(splitAxis);
            double centerB = b.getBoundingBox().getCenter().get(splitAxis);
            return Double.compare(centerA, centerB);
        });

        // Split the list into two halves
        int mid = finiteGeometries.size() / 2;
        List<Intersectable> leftGeometries = buildBVH(new ArrayList<>(finiteGeometries.subList(0, mid)));
        List<Intersectable> rightGeometries = buildBVH(new ArrayList<>(finiteGeometries.subList(mid, finiteGeometries.size())));

        // Create bounding boxes for the left and right halves
        BoundingBox leftBox = computeBoundingBox(leftGeometries);
        BoundingBox rightBox = computeBoundingBox(rightGeometries);

        // Combine the left and right geometries into a Geometries object
        Geometries combined = new Geometries();

        // Add left geometries
        for (Intersectable geo : leftGeometries) {
            combined.add(geo);
        }

        // Add right geometries
        for (Intersectable geo : rightGeometries) {
            combined.add(geo);
        }

        // Set the bounding box for the combined geometries
        combined.box = leftBox != null && rightBox != null ?
                leftBox.union(rightBox) :
                (leftBox != null ? leftBox : rightBox);

        // Create the final result
        List<Intersectable> result = new ArrayList<>(infiniteGeometries);
        result.add(combined);
        return result;
    }

    /**
     * Find the axis with the greatest variance for optimal splits.
     *
     * @param geometries List of geometries
     * @return The axis index (0=X, 1=Y, 2=Z)
     */
    private static int findSplitAxis(List<Intersectable> geometries) {
        // Calculate centroid bounds for all geometries
        double minX = Double.POSITIVE_INFINITY, maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geo : geometries) {
            BoundingBox box = geo.getBoundingBox();
            if (box == null) continue;

            Point center = box.getCenter();
            double x = center.getX();
            double y = center.getY();
            double z = center.getZ();

            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
            minZ = Math.min(minZ, z);
            maxZ = Math.max(maxZ, z);
        }

        // Find axis with greatest spread
        double xSpread = maxX - minX;
        double ySpread = maxY - minY;
        double zSpread = maxZ - minZ;

        if (xSpread >= ySpread && xSpread >= zSpread) {
            return 0; // X-axis
        } else if (ySpread >= xSpread && ySpread >= zSpread) {
            return 1; // Y-axis
        } else {
            return 2; // Z-axis
        }
    }

    /**
     * Compute a bounding box that encloses all geometries in the list.
     *
     * @param geometries List of geometries
     * @return The combined bounding box
     */
    private static BoundingBox computeBoundingBox(List<Intersectable> geometries) {
        if (geometries == null || geometries.isEmpty()) {
            return null;
        }

        BoundingBox result = null;

        for (Intersectable geo : geometries) {
            BoundingBox box = geo.getBoundingBox();
            if (box == null) continue;

            if (result == null) {
                result = box;
            } else {
                result = result.union(box);
            }
        }

        return result;
    }

    /**
     * Extension method for the Point class to get a coordinate by index.
     *
     * @param index 0=X, 1=Y, 2=Z
     * @return The coordinate value
     */
    private static double get(Point p, int index) {
        switch (index) {
            case 0: return p.getX();
            case 1: return p.getY();
            case 2: return p.getZ();
            default: throw new IllegalArgumentException("Invalid coordinate index: " + index);
        }
    }
}