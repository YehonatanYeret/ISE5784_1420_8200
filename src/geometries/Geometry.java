package geometries;
import primitives.*;
import java.util.List;
/**
 * Interface Geometry is the basic interface for all geometries in the scene
 */
public abstract class Geometry extends Intersectable {

    /**
     * The color of the geometry
     */
    protected Color emission = Color.BLACK;

    /**
     * The material of the geometry
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * Set the emission color of the geometry
     * @param emission the emission color to set
     * @return the geometry
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * Get the normal to the geometry at a given point
     * @param point point to get the normal at
     * @return the normal to the geometry at the given point
     */
    public abstract Vector getNormal(Point point);
//
//    @Override
//    List<Point> findIntersections(Ray ray);
}
