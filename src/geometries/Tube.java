package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * Class Tube is the basic class representing a tube in the 3D space.
 */
public class Tube extends RadialGeometry {
    /**
     * The axis of the tube
     */
    protected final Ray axis;

    /**
     * Constructor for a Tube object receiving a Ray and a radius.
     *
     * @param axis   the axis of the tube
     * @param radius the radius of the tube
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        //calculate the projection of the point on the axis
        double t = alignZero(this.axis.getDirection().dotProduct(point.subtract(this.axis.getPoint(0d))));

        //find center of the tube
        //return the normalized vector from the center of the tube to the point
        return point.subtract(this.axis.getPoint(t)).normalize();
    }


    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {

        Vector vAxis = axis.getDirection();
        Vector v = ray.getDirection();
        Vector deltaP;

        try {
            deltaP = ray.getPoint(0).subtract(axis.getPoint(0));
        } catch (IllegalArgumentException e) {
            deltaP = null;
        }

        double a, b, c;

        a = v.dotProduct(v) - Math.pow(v.dotProduct(vAxis), 2);

        if (deltaP == null) {
            // Special case: deltaP is zero, meaning the ray starts on the axis of the tube
            b = 0;
            c = -radius * radius;
        } else {
            b = 2 * (v.dotProduct(deltaP) - (v.dotProduct(vAxis) * deltaP.dotProduct(vAxis)));
            c = deltaP.dotProduct(deltaP) - Math.pow(deltaP.dotProduct(vAxis), 2) - radius * radius;
        }

        double discriminant = alignZero(b * b - 4 * a * c);

        if (discriminant <= 0) {
            return null;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);

        List<Point> intersections = new LinkedList<>();
        // Two intersection points
        double t1 = alignZero((-b - sqrtDiscriminant) / (2d * a));
        double t2 = alignZero((-b + sqrtDiscriminant) / (2d * a));

        if (t1 > 0d && alignZero(t1 - maxDistance) <= 0d) {
            intersections.add(ray.getPoint(t1));
        }

        if (t2 > 0d && alignZero(t2 - maxDistance) <= 0d) {
            intersections.add(ray.getPoint(t2));
        }
        return intersections.isEmpty() ? null : intersections.stream().map(p -> new GeoPoint(this, p)).toList();
    }
}