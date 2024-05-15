package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    private final Ray ray = new Ray(new Point(1, 2, 3), new Vector(1, 0, 0));
    /**
     * Test method for {@link primitives.Ray#equals(Object)}.
     */
    @Test
    void getPoint() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: t is a negative number
        assertEquals(new Point(-1,2,3), ray.getPoint(-2));

        // TC02: t is a positive number
        assertEquals(new Point(3,2,3), ray.getPoint(2));

        // =============== Boundary Values Tests =================
        // TC03: t is zero
        assertEquals(new Point(1,2,3), ray.getPoint(0));
    }
}