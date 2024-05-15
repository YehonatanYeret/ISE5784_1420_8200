package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Vector class
 */
class VectorTests {
    private final double DELTA = 0.00001;

    /**
     * Test method for {@link primitives.Vector#Vector(double, double, double)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertDoesNotThrow(() -> new Vector(1, 2, 3), "Failed constructing a vector with 3 coordinates");

        // TC02: current vector with the other ctor
        assertDoesNotThrow(() -> new Vector(new Double3(1, 2, 3)), "Failed constructing a vector with Double3 param");

        // =============== Boundary Values Tests ==================
        // TC03: Zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "Constructed a zero vector");

        // TC04: Zero vector with the other ctor
        assertThrows(IllegalArgumentException.class, () -> new Vector(new Double3(0, 0, 0)), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 2, -3);
        Vector v3 = new Vector(-1, -2, -3);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(new Vector(5, 4, 0), v1.add(v2), "Wrong result of adding two vectors");

        // =============== Boundary Values Tests ==================
        // TC02: result is zero vector
        assertThrows(IllegalArgumentException.class, () -> v1.add(v3), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)})}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(new Vector(2, 4, 6), new Vector(1, 2, 3).scale(2), "Wrong result of scaling a vector");

        // =============== Boundary Values Tests ==================
        // TC02: scale by zero
        assertThrows(IllegalArgumentException.class, () -> new Vector(2, 2, 3).scale(0), "Scaled by zero");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(7, new Vector(1, 2, 3).dotProduct(new Vector(2, 1, 1)), DELTA, "Wrong result of dot product");

        // =============== Boundary Values Tests ==================

        // TC02: one of the vectors is the singularity vector
        assertEquals(1, new Vector(1, 2, 3).dotProduct(new Vector(1, 0, 0)), DELTA, "Dot product with singularity vector");

        // TC03: dot product with zero vector
        assertEquals(0, new Vector(1, 0, 0).dotProduct(new Vector(0, 0, 1)), DELTA, "Dot product with zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(new Vector(1, 0, 0), new Vector(0, 1, 0).crossProduct(new Vector(0, 0, 1)), "Wrong result of cross product");

        // =============== Boundary Values Tests ==================

        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(2, 1, 1);
        Vector vr = v1.crossProduct(v2);

        // TC02: the vectors are parallel
        assertThrows(IllegalArgumentException.class, () -> new Vector(1, 2, 3).crossProduct(new Vector(2, 4, 6)), "did not constructed a zero vector");

        // TC03: the vector are equal
        assertThrows(IllegalArgumentException.class, () -> new Vector(1, 2, 3).crossProduct(new Vector(1, 2, 3)), "did not constructed a zero vector");

        // TC04: the cross product is not orthogonal to its operands
        assertTrue(isZero(vr.dotProduct(v1)) && isZero(vr.dotProduct(v2)), "Cross product is not orthogonal to its operands");

        // TC05: the vectors are the same length but not parallel
        assertEquals(new Vector(1,7,-5), new Vector(1, 2, 3).crossProduct(new Vector(3, 1, 2)), "Wrong result of cross product");

        }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(14, new Vector(1, 2, 3).lengthSquared(), DELTA, "Wrong result of length squared");

        // =============== Boundary Values Tests ==================
        // TC02: positive and negative vector
        assertEquals(9, new Vector(-1, 2, -2).lengthSquared(), DELTA, "Wrong result of length squared");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(3, new Vector(-1, -2, -2).length(), DELTA, "Wrong result of length");

        // =============== Boundary Values Tests ==================
        // TC02: positive and negative vector
        assertEquals(3, new Vector(-1, 2, -2).length(), DELTA, "Wrong result of length");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        //============ Equivalence Partitions Tests ==============
        // TC01: Correct vector
        assertEquals(1, new Vector(2, 3, 4).normalize().length(), DELTA,"Wrong result of normalization");

     // =============== Boundary Values Tests ==================
        // TC02: zero vector
        Vector v1 = new Vector(1, 4, 7);
        assertThrows(IllegalArgumentException.class, () -> v1.normalize().crossProduct(v1), "Normalized a zero vector");

        //TC03: Correct vector normalized is the same vector
        assertEquals(new Vector(1, 0, 0), new Vector(1, 0, 0).normalize(), "Wrong result of normalization");

        //TC04: v.normalized*v bigger than 0
        assertTrue(v1.normalize().dotProduct(v1) > 0, "Normalized vector is not parallel to the original one");
    }
}