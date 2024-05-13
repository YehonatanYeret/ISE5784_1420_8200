package geometries;
import primitives.*;
import java.util.List;
/**
 * Interface Geometry is the basic interface for all geometries in the scene
 */
public interface Geometry extends Intersectable {
    /**
     * Get the normal to the geometry at a given point
     * @param point point to get the normal at
     * @return the normal to the geometry at the given point
     */
    Vector getNormal(Point point);

    @Override
    List<Point> findIntsersections(Ray ray);
}
