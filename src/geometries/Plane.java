package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

/**
 * Class Plane is the basic class representing a plane in the 3D space
 */
public class Plane implements Geometry{

    private final Point q;
    private final Vector normal;

    /**
     * Constructor to initialize a plane based on a point and a normal vector. <br>
     * takes 3 points on the plane and set the normal vector and a point on the plane
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
     * @param point point on the plane
     * @param normal normal vector to the plane
     */
    public Plane(Point point, Vector normal) {
        this.q = point;
        this.normal = normal.normalize();
    }

    /**
     * Getter for the normal of the plane, as we asked
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
    public List<Point> findIntersections(Ray ray) {
        if(Util.isZero(ray.getDirection().dotProduct(normal)) || q.equals(ray.getPoint(0))) {
            return null;
        }
        double t = normal.dotProduct(q.subtract(ray.getPoint(0))) / normal.dotProduct(ray.getDirection());
        if (t <= 0) {
            return null;
        }
        return List.of(ray.getPoint(0).add(ray.getDirection().scale(t)));
    }
}
