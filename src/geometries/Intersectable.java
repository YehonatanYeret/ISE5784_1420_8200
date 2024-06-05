package geometries;
import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * Interface Intersectable is the basic interface for all geometries in the scene
 */
public abstract class Intersectable {

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
        return findGeoIntersectionsHelper(ray);
    }

    /**
     * Find intersections of a ray with the geometry
     * @param ray the ray to find intersections with
     * @return a list of intersection points
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

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
