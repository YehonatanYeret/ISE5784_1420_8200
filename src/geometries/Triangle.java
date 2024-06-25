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
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point p1 = vertices.get(0);
        Point p2 = vertices.get(1);
        Point p3 = vertices.get(2);

        Vector ab = p2.subtract(p1);
        Vector ac = p3.subtract(p1);
        Vector n = ab.crossProduct(ac);

        double nd = n.dotProduct(ray.getDirection());
        if (Util.isZero(nd)) {
            return null; // The ray is parallel to the plane of the triangle
        }

        double t = Util.alignZero(n.dotProduct(p1.subtract(ray.getPoint(0d))) / nd);
        if (t < 0d) {
            return null; // The intersection is behind the ray's origin
        }

        Point p = ray.getPoint(t);
        if (p.equals(p1) || p.equals(p2) || p.equals(p3)) {
            return null; // The intersection point is one of the triangle's vertices
        }

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
        Vector ap = p.subtract(p1);
        double dot00 = ab.dotProduct(ab);
        double dot01 = ab.dotProduct(ac);
        double dot02 = ab.dotProduct(ap);
        double dot11 = ac.dotProduct(ac);
        double dot12 = ac.dotProduct(ap);

        double invDenom = Util.alignZero(1d / (dot00 * dot11 - dot01 * dot01));
        double u = Util.alignZero((dot11 * dot02 - dot01 * dot12) * invDenom);
        double v = Util.alignZero((dot00 * dot12 - dot01 * dot02) * invDenom);
        double w = Util.alignZero(1d - u - v);

        // Check if the point is inside the triangle
        if (u > 0d && v > 0d && w > 0d && Util.alignZero(ray.getPoint(0).distanceSquared(p)) <= maxDistance) {
            return List.of(new GeoPoint(this, p));
        }
        return null;
    }
}