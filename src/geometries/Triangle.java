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
         * after some proofs, we found the matrix equation:
         *
         * |d00  d01| |v|= |d02|
         * |d01  d11| |v|= |d12|
         *
         * so we can use Kermer's rule to solve the equations and found v, u and w
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

//    @Override
//    public List<Point> findIntersections(Ray ray) {
//        Point p1 = vertices.getFirst();
//        Point p2 = vertices.get(1);
//        Point p3 = vertices.getLast();
//
//        Vector ab = p2.subtract(p1);
//        Vector ac = p3.subtract(p1);
//        Vector n = ab.crossProduct(ac);
//
//        double nd = n.dotProduct(ray.getDirection());
//        if (Util.isZero(n.dotProduct(ray.getPoint(0).subtract(p1)))) {
//            return null;
//        }
//
//        double t = n.dotProduct(p1.subtract(ray.getPoint(0))) / nd;
//        if (t < 0) {
//            return null;
//        }
//        Point p = ray.getPoint(t);
//        if(p.equals(vertices.get(0)) || p.equals(vertices.get(1)) || p.equals(vertices.get(2)))
//            return null;
//
//        Vector ap = ray.getPoint(t).subtract(p1);
//        // Compute dot products
//        double dot00 = ab.dotProduct(ab);
//        double dot01 = ab.dotProduct(ac);
//        double dot02 = ap.dotProduct(ac);
//        double dot11 = ac.dotProduct(ac);
//        double dot12 = ap.dotProduct(ac);
//
//        /*
//         * Compute barycentric coordinates:
//         * to use the barycentric coordinates to determine if a point is inside a triangle,
//         * we need to compute the barycentric coordinates of the point with respect to the triangle.
//         * after some proofs, we found the matrix equation:
//         *
//         * |d00  d01| |v|= |d02|
//         * |d01  d11| |v|= |d12|
//         *
//         * so we can use Kermer's rule to solve the equations and found v, u and w
//         */
//
//        // Compute Barycentric coordinates
//        double invdet = 1 / (dot00 * dot11 - dot01 * dot01);// 1 / determinant of the equation before
//        double u = (dot11 * dot02 - dot01 * dot12) * invdet;
//        double v = (dot00 * dot12 - dot01 * dot02) * invdet;
//        double w = 1.0 - u - v;
//
//        // Check if point is in triangle
//        if (u > 0 && v > 0 && w > 0 && u + v + w <= 1) {
//            return List.of(p);
//        }
//        return null;
//    }
}
