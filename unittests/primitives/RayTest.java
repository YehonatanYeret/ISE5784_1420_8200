package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Ray
 * Author: Maor
 */
class RayTest {

    private final Point p1 = new Point(1, 2, 3);
    private final Ray ray = new Ray(p1, new Vector(1, 0, 0));

    /**
     * Test method for {@link primitives.Ray#equals(Object)}.
     */
    @Test
    void getPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: t is a negative number
        assertEquals(new Point(-1, 2, 3), ray.getPoint(-2), "Bad getPoint with negative t");

        // TC02: t is a positive number
        assertEquals(new Point(3, 2, 3), ray.getPoint(2), "Bad getPoint with positive t");

        // =============== Boundary Values Tests =================
        // TC03: t is zero
        assertEquals(p1, ray.getPoint(0), "Bad getPoint with t=0");
    }
}