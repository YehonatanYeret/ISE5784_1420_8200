package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Tube class
 */
class TubeTests {

    /**
     * Test method for {@link geometries.Tube#Tube(primitives.Ray, double)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Tube(new Ray(new Point(0, 0 , 0), new Vector(1, 0 ,0 )), 1), "Failed to create a proper tube");
    }
    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple test for the getNormal function
        Tube tube = new Tube(new Ray(
                new Point(0, 0, 0),
                new Vector(0, 0, 1)), 1);
        assertEquals(new Vector(1, 0, 0), tube.getNormal(new Point(1, 0, 1)), "Bad normal to tube");
        // =============== Boundary Values Tests =================
        // TC02: (p-p0) is orthogonal to the axis of the tube
        assertEquals(new Vector(1, 0, 0), tube.getNormal(new Point(1, 0, 0)), "Bad normal to tube");
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test if the point is vertical to the axis
        assertThrows(IllegalArgumentException.class, () -> new Tube(new Ray(new Point(0, 0 , 0), new Vector(1, 0 ,0 )), 1).getNormal(new Point(0, 1, 0)), "Failed to throw an exception when the point is vertical to the axis");
    }
}