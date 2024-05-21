package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

import java.util.List;

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
        Point p0 = this.axis.getPoint(0);

        //calculate the projection of the point on the axis
        double t = this.axis.getDirection().dotProduct(point.subtract(p0));
        if (t == 0)//if the vector is orthogonal to the axis
            return point.subtract(p0).normalize();

        //find center of the tube
        //return the normalized vector from the center of the tube to the point
        return point.subtract(this.axis.getPoint(t)).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}