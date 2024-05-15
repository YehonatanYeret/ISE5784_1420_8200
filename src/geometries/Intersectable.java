package geometries;
import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * Interface Intersectable is the basic interface for all geometries in the scene
 */
public interface Intersectable {

    /**
     * Find intersections of a ray with the geometry
     * @param ray the ray to find intersections with
     * @return a list of intersection points
     */
    public List<Point> findIntersections(Ray ray);
}
