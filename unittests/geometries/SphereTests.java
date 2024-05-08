package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Sphere class
 */
class SphereTests {

    /**
     * Test method for {@link geometries.Sphere#Sphere(double, primitives.Point)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Sphere(1, new Point(1, 0, 0)), "Failed to create a proper sphere");

        // =============== Boundary Values Tests =================
        // TC02: Test for a sphere with a negative radius
        assertThrows(IllegalArgumentException.class, () -> new Sphere(-1, new Point(1, 0, 0)), "Failed to throw an exception when creating a sphere with a negative radius");
    }
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertEquals(new Point(1, 0, 0), new Sphere(1, new Point(1, 0, 0)).getNormal(new Point(2, 0, 0)), "Failed to get the normal vector of the sphere");
    }
}