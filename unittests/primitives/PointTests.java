package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class
 */
class PointTests {
    private final double DELTA = 0.00001;
    private final Vector v1 = new Vector(1, 2, 3);
    private final Point p1 = new Point(1, 2, 3);

    /**
     * Test method for {@link primitives.Point#Point(double, double, double)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct point
        assertDoesNotThrow(() -> p1, "Failed constructing a correct point");

        // TC02: Correct point
        assertDoesNotThrow(() -> new Point(new Double3(1, 2, 3)), "Failed constructing a correct point");
    }

    /**
     * Test method for {@link primitives.Point#subtract(Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Subtract two points
        assertEquals(v1, new Point(2, 4, 6).subtract(p1), "Subtract two points does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Subtract equal points
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1), "Subtract equal points does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#add(Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Add a vector to a point
        assertEquals(new Point(2, 4, 6), p1.add(v1), "Add a vector to a point does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Add a vector to a point
        assertEquals(Point.ZERO, p1.add(new Vector(-1, -2, -3)), "Add a vector to a point does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Calculate the squared distance between two points
        assertEquals(13, p1.distanceSquared(new Point(1, 5, 1)), DELTA,"Calculate the squared distance between two points does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Calculate the squared distance between two points
        assertEquals(0, p1.distanceSquared(p1), DELTA,"Calculate the squared distance between two points does not work correctly");
    }

    /**
     * Test method for {@link primitives.Point#distance(Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Calculate the distance between two points
        assertEquals(5, new Point(0, 4, 0).distance(new Point(0, 0, 3)), DELTA,"Calculate the distance between two points does not work correctly");

        // ============ Boundary Values Tests ==================
        // TC02: Calculate the distance between two points
        assertEquals(0, p1.distance(p1), DELTA, "Calculate the distance between two points does not work correctly");
    }
}