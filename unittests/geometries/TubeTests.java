package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Tube class
 */
class TubeTests {

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
    }
}