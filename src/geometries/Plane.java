package geometries;

import primitives.Point;
import primitives.Vector;

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
                .cross(p3.subtract(p1))
                .normalize();
    }

    /**
     * Constructor to initialize a plane based on a point and a normal vector
     * @param q point on the plane
     * @param normal normal vector to the plane
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    public Vector getNormal(Point point) {
        return normal;
    }
}
