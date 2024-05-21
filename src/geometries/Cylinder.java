package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

/**
 * Class Cylinder is the basic class representing a cylinder in the 3D space.
 */
public class Cylinder extends Tube {
    private final double height;

    /**
     * Constructor for a Cylinder object receiving a Ray, a radius and a height.
     *
     * @param axis   the axis of the cylinder
     * @param radius the radius of the cylinder
     * @param height the height of the cylinder
     */
    public Cylinder(Ray axis, double radius, double height) {
        super(axis, radius);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.getPoint(0);
        Vector dir = this.axis.getDirection();

       //  If p0 is the head of the axis
        if (point.equals(p0))
            return dir.scale(-1);

        // If p1 is the end of the axis
        if (point.equals(p0.add(dir.scale(height))))
            return dir;

        // If the point is on the top or bottom surface of the cylinder
        if (p0.subtract(point).dotProduct(dir) == 0)
            return dir.scale(-1);

        if (p0.add(dir.scale(height)).subtract(point).dotProduct(dir) == 0)
            return dir;

        // Otherwise, call the superclass method
        return super.getNormal(point);

    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Vector oc = ray.getPoint(0).subtract(this.axis.getPoint(0));
        Vector axis = this.axis.getDirection();

        Vector w = oc.subtract(axis.scale(oc.dotProduct(axis)));
        Vector u = ray.getDirection().subtract(axis.scale(ray.getDirection().dotProduct(axis)));

        double b = 2 * u.dotProduct(w);
        double c = w.dotProduct(w) - this.radius * this.radius;

        double discriminant = b * b - 4 * c;
        if (discriminant < 0) {
            return null;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double t1 = (-b - sqrtDiscriminant) / (2);
        double t2 = (-b + sqrtDiscriminant) / (2);

        LinkedList<Point> intersections = new LinkedList<>();
        if (t1 > 0) {
            Point p1 = ray.getPoint(t1);
            if (p1.subtract(this.axis.getPoint(0)).dotProduct(this.axis.getDirection()) > 0 &&
                    p1.subtract(this.axis.getPoint(0).add(this.axis.getDirection().scale(this.height))).dotProduct(this.axis.getDirection()) < 0) {
                intersections.add(p1);
            }
        }
        if (t2 > 0) {
            Point p2 = ray.getPoint(t2);
            if (p2.subtract(this.axis.getPoint(0)).dotProduct(this.axis.getDirection()) > 0 &&
                    p2.subtract(this.axis.getPoint(0).add(this.axis.getDirection().scale(this.height))).dotProduct(this.axis.getDirection()) < 0) {
                intersections.add(p2);
            }
        }

        return intersections.isEmpty() ? null : intersections;
    }
}
