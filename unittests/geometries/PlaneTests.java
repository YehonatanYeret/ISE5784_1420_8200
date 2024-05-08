package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class
 */
class PlaneTests {

    /**
     * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Plane(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)), "Failed to create a proper plane");

        // =============== Boundary Values Tests =================
        // TC02: Test for a plane with the same point
        assertThrows(IllegalArgumentException.class, () -> new Plane(new Point(1, 0, 0), new Point(1, 0, 0), new Point(1, 0, 0)), "Failed to throw an exception when creating a plane with the same point");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        Vector normal = new Plane(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)).getNormal();
        assertTrue(normal.equals(new Vector(0, 0, 1)) || normal.equals(new Vector(0, 0, -1)), "Failed to get the normal vector of the plane");;
    }
}