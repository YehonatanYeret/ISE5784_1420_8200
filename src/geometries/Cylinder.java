package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        Vector dirP1 = dir.scale(height);

       //  If p0 is the head of the axis
        if (point.equals(p0))
            return dir.scale(-1);

        // If p1 is the end of the axis
        if (point.equals(p0.add(dirP1)))
            return dir;

        // If the point is on the top or bottom surface of the cylinder
        if (p0.subtract(point).dotProduct(dir) == 0)
            return dir.scale(-1);

        if (p0.add(dirP1).subtract(point).dotProduct(dir) == 0)
            return dir;

        // Otherwise, call the superclass method
        return super.getNormal(point);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
        }
}
