package geometries;
import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * Interface Intersectable is the basic interface for all geometries in the scene
 */
public abstract class Intersectable {

    /**
     * The bounding box of the geometry
     */
    protected BoundingBox box;

    /**
     * Constructs an Intersectable object with the specified bounding box.
     */
    public BoundingBox getBoundingBox() {
        return box;
    }

    /**
     * The color of the geometry
     */
    public static class GeoPoint {
        public Geometry geometry;
        public Point point;

        /**
         * Constructor for GeoPoint
         * @param geometry the geometry
         * @param point the point
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return obj instanceof GeoPoint other &&
                    geometry.equals(other.geometry) && point.equals(other.point);
        }

        @Override
        public String toString() {
            return "GeoPoint{" + "geometry=" + geometry + ", point=" + point + '}';
        }
    }

    /**
     * Find intersections of a ray with the geometry
     * @param ray the ray to find intersections with
     * @return a list of intersection points
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray){
        return findGeoIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * Find intersections of a ray with the geometry
     * @param ray the ray to find intersections with
     * @param maxDistance the maximum distance to find intersections
     * @return a list of intersection points
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance){
        // Checks if the object's bounding box exists and if the ray intersects with it
        if (box != null && !box.hasIntersection(ray)) {
            return null; // No intersections if the bounding box check fails
        }
        // If the bounding box check passes, proceed to find intersections
        return findGeoIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Find intersections of a ray with the geometry
     * @param ray the ray to find intersections with
     * @param maxDistance the maximum distance to find intersections
     * @return a list of intersection points
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);

    /**
     * Find intersections of a ray with the geometry
     * @param ray the ray to find intersections with
     * @return a list of intersection points
     */
    public List<Point> findIntersections(Ray ray){
        var GeoList = findGeoIntersections(ray);
        return  GeoList == null ? null : GeoList.stream().map(gp -> gp.point).toList();
    }


}
