package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Triangles
 */
class TriangleTest {
    private final Vector v1 = new Vector(0, 0, -1);
    private final Vector v2 = new Vector(0, 0, 1);
    private final Point p1 = new Point(0, 0, 1);
    private final Point p2 = new Point(0, 1, 0);
    private final Point p3 = new Point(1, 0, 0);


    /**
     * Test method for {@link geometries.Triangle#Triangle(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(new Point(1, 1, 0), p3, p2);
        // ============ Equivalence Partitions Tests ==============
        // TC01: the intersection point is inside the triangle
        assertEquals(1, triangle.findIntersections(
                        new Ray(new Point(1.8, 1.8, 1), new Vector(-1, -1, -1))).size(),
                "Failed to find the intersection point when the intersection point is inside the triangle");

        // TC02: the intersection point is outside the triangle and against an edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 2, 1), v1)),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");

        // TC03: the intersection point is outside the triangle and against a vertex
        assertNull(triangle.findIntersections(
                        new Ray(new Point(2, 2, 1), v1)),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");

        // ================= Boundary Values Tests =================
        // TC04: the intersection point is on the edge of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 1, -1), v2)),
                "Failed to find the intersection point when the intersection point is on the edge of the triangle");

        // TC05: the intersection point is on the vertex of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(1, 1, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is on the vertex of the triangle");

        // TC06: the intersection point is outside the triangle but in the path of the edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(2, 1, -1), v2)),
                "Failed to find the intersection point when the intersection point is outside the triangle but in the path of the edge");

        // ================= external Tests =================
        // TC07: the triangle is in an angle
        Triangle triangle2 = new Triangle(p3, p2, p1);
        assertEquals(1, triangle2.findIntersections(
                        new Ray(new Point(-1, -1, -1), new Vector(1, 1, 1))).size(),
                "Failed to find the intersection point when the intersection point is inside the triangle");
    }

    /**
     * Test method for {@link geometries.Triangle#getNormal(Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                {p1, p3, p2};
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(p1), "");
        // generate the test result
        Vector result = pol.getNormal(p1);
        // ensure |result| = 1
        assertEquals(1, result.length(), "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 2 : i - 1])),
                    "Polygon's normal is not orthogonal to one of the edges");

        // =============== Boundary Values Tests ==================
        // TC02: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(p1, p3, p2,
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertex on a side");

        // TC03: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(p1, p3, p2, p1),
                "Constructed a polygon with vertices on a side");

        // TC04: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(p1, p3, p2, p2),
                "Constructed a polygon with vertices on a side");

    }
}