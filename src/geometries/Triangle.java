package geometries;
import primitives.Point;
import primitives.Ray;
import java.util.List;

/**
 * Triangle class represents a triangle in 3D Cartesian coordinate system
 */
public class Triangle extends Polygon {

    /**
     * Constructor to initialize a triangle based on three vertices
     * @param p1 first Point
     * @param p2 second Point
     * @param p3 third Point
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }


    @Override
    public List<Point> findIntsersections(Ray ray) {
        return List.of();
    }
}
