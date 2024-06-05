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
        double t = this.axis.getDirection().dotProduct(point.subtract(this.axis.getPoint(0)));

        //find center of the tube
        //return the normalized vector from the center of the tube to the point
        return point.subtract(this.axis.getPoint(t)).normalize();
    }


    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        Vector rayDirection = ray.getDirection();
        Vector axisDirection = this.axis.getDirection();
        Point rayOrigin = ray.getPoint(0);

        double dirDotAxis = alignZero(rayDirection.dotProduct(axisDirection)); // (D|V)

        Vector dirMinusDVV;
        if (dirDotAxis == 0) {
            // that means dir - (D|V)*V = dir, so we can use dir as is
            dirMinusDVV = rayDirection;
        } else {
            // calculate (D|V)*V (DV is not zero)
            Vector dirDV = axisDirection.scale(dirDotAxis);
            try {
                // Subtract the scaled vector from the ray direction
                dirMinusDVV = rayDirection.subtract(dirDV);
            } catch (IllegalArgumentException e1) {
                return null; // if the ray direction is the same as (D|V)*V
            }
        }

        // Calculate the squared length of dir - (D|V)*V
        double A = dirMinusDVV.lengthSquared();
        // Calculate the parameter t
        double t = Math.sqrt(this.radius * this.radius / A);

        Vector deltaP;
        try {
            // Calculate the vector from the axis origin to the ray origin
            deltaP = rayOrigin.subtract(axis.getPoint(0));
        } catch (IllegalArgumentException e1) {
            // If the ray starts at the head of the axis
            if (dirDotAxis == 0) {
                // Return intersection at distance radius if (D|V) is zero
                return alignZero(t) <= 0 ? null : List.of(new GeoPoint(this, ray.getPoint(radius)));
            } else {
                // Return intersection at distance t otherwise
                return alignZero(t) <= 0 ? null : List.of(new GeoPoint(this, ray.getPoint(t)));
            }
        }

        // Calculate the dot product of deltaP and the axis direction
        double deltaP_dot_axis = alignZero(deltaP.dotProduct(axisDirection));
        Vector deltaPMinusDPV;
        if (deltaP_dot_axis == 0) {
            // If the dot product is zero, use deltaP as is
            deltaPMinusDPV = deltaP;
        } else {
            // Scale the axis direction by the dot product
            Vector deltaP_VV = axisDirection.scale(deltaP_dot_axis);
            try {
                // Subtract the scaled vector from deltaP
                deltaPMinusDPV = deltaP.subtract(deltaP_VV);
            } catch (IllegalArgumentException e1) {
                // Return intersection at distance t if subtraction is not possible
                return alignZero(t) <= 0 ? null : List.of(new GeoPoint(this, ray.getPoint(t)));
            }
        }

        // Calculate the coefficients B and C for the quadratic equation
        double B = 2 * alignZero(dirMinusDVV.dotProduct(deltaPMinusDPV));
        double C = deltaPMinusDPV.lengthSquared() - radius * radius;

        // Calculate the discriminant of the quadratic equation
        double discriminant = alignZero(B * B - 4 * A * C);
        if (discriminant <= 0) {
            // If the discriminant is negative, there are no intersection points
            // if the discriminant is zero, there is one intersection point which is not possible because we intersect
            // infinite ray
            return null;
        }

        // Calculate the square root of the discriminant
        double discriminantSqrt = Math.sqrt(discriminant);

        // Create a list to store potential intersection points
        List<Point> intersections = new LinkedList<>();
        // Calculate the two potential intersection distances
        double t1 = alignZero((-B - discriminantSqrt) / (2 * A));
        double t2 = alignZero((-B + discriminantSqrt) / (2 * A));

        if (t1 > 0) {
            intersections.add(ray.getPoint(t1));
        }

        if (t2 > 0) {
            intersections.add(ray.getPoint(t2));
        }

        // Return the list of intersection points, or null if there are none
        return intersections.isEmpty() ? null : intersections.stream().map(p -> new GeoPoint(this, p)).toList();
    }
}