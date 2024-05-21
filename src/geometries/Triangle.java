package geometries;

import primitives.Point;
import primitives.Ray;
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
        List<Point> points = plane.findIntersections(ray);
        if (points == null) {
            return null;
        }
        Point p = points.getFirst();
        if(p.equals(vertices.get(0)) || p.equals(vertices.get(1)) || p.equals(vertices.get(2)))
            return null;

        // Calculate vectors
        Vector v0 = vertices.get(1).subtract(vertices.get(0));
        Vector v1 = vertices.get(2).subtract(vertices.get(0));
        Vector v2 = p.subtract(vertices.get(0));

        // Compute dot products
        double dot00 = v0.dotProduct(v0);
        double dot01 = v0.dotProduct(v1);
        double dot02 = v0.dotProduct(v2);
        double dot11 = v1.dotProduct(v1);
        double dot12 = v1.dotProduct(v2);

        /*
         * Compute barycentric coordinates:
         * to use the barycentric coordinates to determine if a point is inside a triangle,
         * we need to compute the barycentric coordinates of the point with respect to the triangle.
         * after some proofs, we found that:
         * d00 * v + d01 * u = d02
         * d01 * v + d11 * u = d12
         * so we can use kermers rule to solve the equations and found v, u and w
         */
        
        // Compute Barycentric coordinates
        double invdet = 1 / (dot00 * dot11 - dot01 * dot01);// 1 / determinant of the equation before
        double u = (dot11 * dot02 - dot01 * dot12) * invdet;
        double v = (dot00 * dot12 - dot01 * dot02) * invdet;
        double w = 1.0 - u - v;

        // Check if point is in triangle
        if (u > 0 && v > 0 && w > 0 && u + v + w <= 1) {
            return points;
        }
        return null;
    }
}
