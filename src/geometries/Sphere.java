package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Class Sphere is the basic class representing a sphere in the 3D space
 */
public class Sphere extends RadialGeometry{
    private final Point center;

    /**
     * Constructor to initialize a sphere based on a radius and a center point
     * @param radius radius of the sphere
     * @param center center point of the sphere
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }

    @Override
    public List<Point> findIntsersections(Ray ray) {
        return List.of();
    }
}
