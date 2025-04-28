package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
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

    public Geometries(List<Intersectable> geometries) {
        add(geometries);
    }

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
        this.geometries.addAll(Arrays.asList(geometries));
    }

    /**
     * Adds a list of Intersectable objects to the Geometries collection.
     *
     * @param geometries A list of Intersectable objects to be added.
     */
    public void add(List<Intersectable> geometries) {
        this.geometries.addAll(geometries);
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> intersections = null;
        for (Intersectable geometry : geometries) {
            List<GeoPoint> geometryIntersections = geometry.findGeoIntersectionsHelper(ray, maxDistance);
            if (geometryIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>();
                intersections.addAll(geometryIntersections);
            }
        }
        return intersections;
    }

    /**
     * Creates a bounding volume hierarchy (BVH) for the geometries in the collection.
     * This method optimizes the intersection tests by organizing the geometries into a hierarchical structure.
     */
    public void makeBVH() {
        List<Intersectable> intersectables = BoundingBox.buildBVH(geometries);
        geometries.clear();
        geometries.addAll(intersectables);
    }
}