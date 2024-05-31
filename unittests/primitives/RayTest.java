package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Ray
 * Author: Maor
 */
class RayTest {

    private final Point p1 = new Point(1, 2, 3);
    private final Point p2 = new Point(2, 2, 3);
    private final Point p3 = new Point(3, 2, 3);
    private final Point p4 = new Point(4, 2, 3);
    private final Ray ray = new Ray(p1, new Vector(1, 0, 0));

    /**
     * Test method for {@link primitives.Ray#equals(Object)}.
     */
    @Test
    void testGetPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: t is a negative number
        assertEquals(new Point(-1, 2, 3), ray.getPoint(-2), "Bad getPoint with negative t");

        // TC02: t is a positive number
        assertEquals(new Point(3, 2, 3), ray.getPoint(2), "Bad getPoint with positive t");

        // =============== Boundary Values Tests =================
        // TC03: t is zero
        assertEquals(p1, ray.getPoint(0), "Bad getPoint with t=0");
    }

    @Test
    void testFindClosestPoint(){
        // ============ Equivalence Partitions Tests ==============
        // TC01: the point in the middle of the list is the closest
        assertEquals(new Point(2, 2, 3), ray.findClosestPoint(List.of(p3, p2, p4)), "Bad findClosestPoint");

        // ================= Boundary Values Tests =================
        // TC02: the list is empty
        assertNull(ray.findClosestPoint(List.of()), "Bad findClosestPoint with empty list");

        // TC03: the point in the beginning of the list is the closest
        assertEquals(p2, ray.findClosestPoint(List.of(p2, p3, p4)), "Bad findClosestPoint");

        // TC04: the point in the end of the list is the closest
        assertEquals(p2, ray.findClosestPoint(List.of(p4, p3, p2)), "Bad findClosestPoint");

    }
}