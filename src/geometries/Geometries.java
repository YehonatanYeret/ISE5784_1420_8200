package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.LinkedList;

/**
 * Geometries class represents a collection of geometries
 */
public class Geometries extends Intersectable{
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Default constructor
     */
    public Geometries() { }

    /**
     *
     * @param geometries the geometries to add
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Add geometries to the list
     * @param geometries the geometries to add
     */
    public void add(Intersectable... geometries) {
        for (Intersectable geometry : geometries)
            this.geometries.add(geometry);
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> intersections = null;
        for (Intersectable geometry : geometries) {
            List<GeoPoint> geometryIntersections = geometry.findGeoIntersectionsHelper(ray);
            if (geometryIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                intersections.addAll(geometryIntersections);
            }
        }
        return intersections;
    }

}