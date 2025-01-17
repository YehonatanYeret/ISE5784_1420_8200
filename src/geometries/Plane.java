package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Class Plane is the basic class representing a plane in the 3D space
 */
public class Plane extends Geometry {

    private final Point q;
    private final Vector normal;

    /**
     * Constructor to initialize a plane based on a point and a normal vector. <br>
     * takes 3 points on the plane and set the normal vector and a point on the plane
     *
     * @param p1 point on the plane
     * @param p2 point on the plane
     * @param p3 point on the plane
     */
    public Plane(Point p1, Point p2, Point p3) {
        this.q = p1;
        this.normal = p2.subtract(p1)
                .crossProduct(p3.subtract(p1))
                .normalize();
    }

    /**
     * Constructor to initialize a plane based on a point and a normal vector
     *
     * @param point  point on the plane
     * @param normal normal vector to the plane
     */
    public Plane(Point point, Vector normal) {
        this.q = point;
        this.normal = normal.normalize();
    }

    /**
     * Getter for the normal of the plane, as we asked
     *
     * @return the normal vector of the plane
     */
    public Vector getNormal() {
        return normal;
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Vector direction = ray.getDirection();
        Point p0 = ray.getPoint(0d);
        // if the ray is parallel to the plane or the ray starts on the plane at the point q
        if (Util.isZero(direction.dotProduct(normal)) || q.equals(p0))
            return null;

        // calculate the intersection point
        double t = normal.dotProduct(q.subtract(p0)) / normal.dotProduct(direction);

        return Util.alignZero(t) <= 0d || alignZero(t - maxDistance) > 0d ? null : List.of(new GeoPoint(this, ray.getPoint(t)));
    }
}
