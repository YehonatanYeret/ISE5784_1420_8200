package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * Triangle class represents a triangle in 3D Cartesian coordinate system.
 */
public class Triangle extends Polygon {

    /**
     * Constructor to initialize a triangle based on three vertices.
     * @param p1 first Point
     * @param p2 second Point
     * @param p3 third Point
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Vector dir = ray.getDirection();

        Vector v1 = vertices.get(0).subtract(ray.getPoint(0));
        Vector v2 = vertices.get(1).subtract(ray.getPoint(0));
        Vector v3 = vertices.get(2).subtract(ray.getPoint(0));

        // Calculate normal vectors for the triangle's sides
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        // Dot products of the ray direction with the normal vectors
        double s1 = dir.dotProduct(n1);
        double s2 = dir.dotProduct(n2);
        double s3 = dir.dotProduct(n3);

        // Check if the ray intersects the plane of the triangle
        if (Util.isZero(s1) || Util.isZero(s2) || Util.isZero(s3)) {
            return null; // The ray is parallel to one of the edges
        }

        // Check if the signs of the dot products are consistent
        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0)) {
            // Create a plane from the triangle's vertices
            Plane plane = new Plane(vertices.get(0), vertices.get(1), vertices.get(2));
            // Find intersections with the plane
            return plane.findIntersections(ray);
        }

        return null; // No intersection with the triangle
    }
}
