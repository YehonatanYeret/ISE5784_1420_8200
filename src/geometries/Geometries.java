package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.LinkedList;

public class Geometries implements Intersectable{
    private final LinkedList<Intersectable> geometries = new LinkedList<Intersectable>();

    public Geometries() { }

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    public void add(Intersectable... geometries) {
        for (Intersectable geometry : geometries)
            this.geometries.add(geometry);

    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = null;
        for (Intersectable geometry : geometries) {
            List<Point> geometryIntersections = geometry.findIntersections(ray);
            if (geometryIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<Point>();
                intersections.addAll(geometryIntersections);
            }
        }
        return intersections;
    }

}