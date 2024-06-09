package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class Cylinder is the basic class representing a cylinder in the 3D space.
 */
public class Cylinder extends Tube {
    private final double height;

    /**
     * Constructor for a Cylinder object receiving a Ray, a radius and a height.
     *
     * @param axis   the axis of the cylinder
     * @param radius the radius of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.getPoint(0);
        Vector dir = this.axis.getDirection();

        //  If p0 is the head of the axis
        if (point.equals(p0))
            return dir.scale(-1);

        // If p1 is the end of the axis
        if (point.equals(axis.getPoint(height)))
            return dir;

        // If the point is on the top or bottom surface of the cylinder
        if (Util.isZero(p0.subtract(point).dotProduct(dir)))
            return dir.scale(-1);

        if (Util.isZero(axis.getPoint(height).subtract(point).dotProduct(dir)))
            return dir;

        // Otherwise, call the superclass method
        return super.getNormal(point);
    }

        @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        // Initialize intersections list
        List<Point> intersections = new LinkedList<>();

        // Find intersections with the infinite cylinder
        Tube tube = new Tube(axis, radius);
        List<Point> infiniteCylinderIntersections = tube.findIntersections(ray);
        if (infiniteCylinderIntersections != null) {
            intersections.addAll(infiniteCylinderIntersections);
        }

        // Remove intersections outside the cylinder height
        Iterator<Point> iterator = intersections.iterator();
        while (iterator.hasNext()) {
            Point intersection = iterator.next();
            double t = axis.getDirection().dotProduct(intersection.subtract(axis.getPoint(0)));
            if (t <= 0 || t >= height) {
                iterator.remove();
            }
        }

        // Define planes for the bottom and top bases
        Plane bottomBase = new Plane(axis.getPoint(0), axis.getDirection());
        Plane topBase = new Plane(axis.getPoint(height), axis.getDirection());

        // Return intersections if there are exactly 2 (so they are on the sides of the cylinder)
        if (intersections.size() == 2) {
            return List.of(new GeoPoint(this, intersections.get(0)), new GeoPoint(this, intersections.get(1)));
        }


        // Find intersections with the bottom base
        List<Point> bottomBaseIntersections = bottomBase.findIntersections(ray);
        if (bottomBaseIntersections != null) {
            Point intersection = bottomBaseIntersections.getFirst();
            if (axis.getPoint(0).distanceSquared(intersection) <= radius * radius) {
                intersections.add(intersection);
            }
        }

        // Find intersections with the top base
        List<Point> topBaseIntersections = topBase.findIntersections(ray);
        if (topBaseIntersections != null) {
            Point intersection = topBaseIntersections.getFirst();
            if (axis.getPoint(height).distanceSquared(intersection) <= radius * radius) {
                intersections.add(intersection);
            }
        }

        // if the ray is tangent to the cylinder
        if (intersections.size() == 2 && axis.getPoint(0).distanceSquared(intersections.get(0)) == radius * radius &&
                axis.getPoint(height).distanceSquared(intersections.get(1)) == radius * radius) {
            Vector v = intersections.get(1).subtract(intersections.get(0));
            if (v.normalize().equals(axis.getDirection()) || v.normalize().equals(axis.getDirection().scale(-1)))
                return null;
        }

        // Return null if no valid intersections found
        List<GeoPoint> geoPoints = new LinkedList<>();
        for (Point p : intersections) {
            geoPoints.add(new GeoPoint(this, p));
        }

        return geoPoints.isEmpty() ? null : geoPoints;
    }
}