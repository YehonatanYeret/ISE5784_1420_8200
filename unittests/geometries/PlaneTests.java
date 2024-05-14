package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class
 */
class PlaneTests {
    private final Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
    private final Point p1 = new Point(0, 1, 1);
    private final Point p2 = new Point(0, 0, 2);
    private final Vector v1 = new Vector(0, 1, 1);
    private final Vector v2 = new Vector(0, 0, 1);
    private final Vector v3 = new Vector(0, 1, 0);

    /**
     * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Plane(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)), "Failed to create a proper plane");

        // =============== Boundary Values Tests =================
        // TC02: Test for a plane that the points are on the same line
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 0, 0), new Point(2, 0, 0), new Point(3, 0, 0)), "Failed to throw an exception when creating a plane with points on the same line");

        // TC03: Test for a plane that the points are converge
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(0, 0 ,0), new Point(1, 1, 1), new Point(1, 1, 1)), "Failed to throw an exception when creating a plane with points that converge");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        Vector normal = new Plane(new Point(1, 0, 0), new Point(0, 1, 0), new Point(1, 1, 0)).getNormal();
        assertTrue(normal.equals(new Vector(0, 0, 1)) || normal.equals(new Vector(0, 0, -1)), "Failed to get the normal vector of the plane");
    }

    /**
     * Test method for {@link geometries.Plane#findIntsersections(primitives.Ray)}.
     */
    @Test
    void testGetIntersection() {

        // ============ Equivalence Partitions Tests ==============

        // TC01: Test that the ray does not intersect the plane
        assertNull(plane.findIntsersections(new Ray(p2, v1)), "Failed to find the intersection point when the ray does not intersect the plane");

        // TC02: Test that the ray intersect the plane
        assertEquals(List.of(new Point(0, 1, 1)), plane.findIntsersections(new Ray(p2, new Vector(0, 1, -1))), "Failed to find the intersection point when the ray intersect the plane");

        // =============== Boundary Values Tests =================

        // TC03: Test that the ray is parallel to the plane
        assertNull(plane.findIntsersections(new Ray(p2, v3)), "Failed to find the intersection point when the ray is parallel to the plane");

        // TC04: Test that the ray is parallel to the plane and included in the plane
        assertNull(plane.findIntsersections(new Ray(p1, v3)), "Failed to find the intersection point when the ray is parallel to the plane and included in the plane");

        // TC05: Test that the ray is orthogonal to the plane
        assertEquals(List.of(new Point(0, 0, 1)), plane.findIntsersections(new Ray(new Point(0, 0, -1), v2)), "Failed to find the intersection point when the ray is orthogonal to the plane");

        // TC06: Test that the ray is orthogonal to the plane and start in the plane
        assertNull(plane.findIntsersections(new Ray(new Point(0, 0, 1), v2)), "Failed to find the intersection point when the ray is orthogonal to the plane and start in the plane");

        // TC07: Test that the ray is orthogonal to the plane and start outside the plane
        assertNull(plane.findIntsersections(new Ray(p2, v2)), "Failed to find the intersection point when the ray is orthogonal to the plane and start outside the plane");

        // TC08: Test that the ray start at the plane
        assertNull(plane.findIntsersections(new Ray(p1, v1)), "Failed to find the intersection point when the ray start at the plane");

        // TC09: Test that the ray start at the plane at the point that sent to the constructor
        assertNull(plane.findIntsersections(new Ray(new Point(0, 0, 1), v1)), "Failed to find the intersection point when the ray start at the plane at the point that sent to the constructor");
    }
}