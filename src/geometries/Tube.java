package geometries;

import primitives.Ray;
import primitives.Point;
import primitives.Vector;

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
     * @param axis the axis of the tube
     * @param radius the radius of the tube
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        //calculate the projection of the point on the axis
        double t = this.axis.getDirection().dotProduct(point.subtract(this.axis.getHead()));
        if(t == 0)//if the vector is orthogonal to the axis
            return point.subtract(this.axis.getHead()).normalize();
        //find center of the tube
        Point o = this.axis.getHead().add(this.axis.getDirection().scale(t));
        //return the normalized vector from the center of the tube to the point
        return point.subtract(o).normalize();
        }
}