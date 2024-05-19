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
    /**
     * Test method for {@link geometries.Triangle#Triangle(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(new Point(1, 1, 0), new Point(1, 0, 0), new Point(0, 1, 0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: the intersection point is inside the triangle
        assertEquals(1, triangle.findIntersections(
                        new Ray(new Point(1.8, 1.8, 1), new Vector(-1, -1, -1))).size(),
                "Failed to find the intersection point when the intersection point is inside the triangle");

        // TC02: the intersection point is outside the triangle and against an edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 2, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");

        // TC03: the intersection point is outside the triangle and against a vertex
        assertNull(triangle.findIntersections(
                        new Ray(new Point(2, 2, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is outside the triangle and against an edge");

        // ================= Boundary Values Tests =================
        // TC04: the intersection point is on the edge of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(0.5, 1, -1), new Vector(0, 0, 1))),
                "Failed to find the intersection point when the intersection point is on the edge of the triangle");

        // TC05: the intersection point is on the vertex of the triangle
        assertNull(triangle.findIntersections(
                        new Ray(new Point(1, 1, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is on the vertex of the triangle");

        // TC06: the intersection point is outside the triangle but in the path of the edge
        assertNull(triangle.findIntersections(
                        new Ray(new Point(2, 1, -1), new Vector(0, 0, 1))),
                "Failed to find the intersection point when the intersection point is outside the triangle but in the path of the edge");

        // ================= external Tests =================
        // TC07: the triangle is in an angle
        Triangle triangle2 = new Triangle(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1));
        assertEquals(1, triangle2.findIntersections(
                        new Ray(new Point(-1, -1, -1), new Vector(1, 1, 1))).size(),
                "Failed to find the intersection point when the intersection point is inside the triangle");
    }
}