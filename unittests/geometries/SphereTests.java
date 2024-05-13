package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;
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

    @Test
    void testFindIntsersections(){
        Sphere sphere = new Sphere(1, new Point(0, 0, 1));

        // ============ Equivalence Partitions Tests ==============
        // TC01 The ray start inside the sphere
        assertEquals(List.of(new Point(0, 1, 1)), sphere.findIntsersections(new Ray(new Point(0, 0, 1), new Vector(0, 1, 0))), "Failed to find the intersection point when the ray start inside the sphere");

        // TC02 The ray never intersect the sphere
        assertNull(sphere.findIntsersections(new Ray(new Point(0, 0, 3), new Vector(1, 1, 1))), "Failed to find the intersection point when the ray never intersect the sphere");

        // TC03 The ray start outside the sphere and intersect the sphere twice
        assertEquals(List.of(new Point(0, Math.sqrt(0.75), 1.5), new Point(0, -Math.sqrt(0.75), 1.5)), sphere.findIntsersections(new Ray(new Point(0, 2, 1.5), new Vector(0, -1, 0))), "Failed to find the intersection points when the ray start outside the sphere and intersect the sphere twice");

        // TC04 The ray start outside the sphere and the ray does not intersect the sphere
        assertNull(sphere.findIntsersections(new Ray(new Point(0, -2, 1.5), new Vector(0, -1, 0))), "Failed to find the intersection points when the ray start outside the sphere and intersect the sphere twice");

        // =============== Boundary Values Tests =================

        // Test orthogonal rays:

        // TC05 The ray is orthogonal to the sphere and start before the sphere
        assertNull(sphere.findIntsersections(new Ray(new Point(0, 2, 1), new Vector(0, 0, 1))), "Failed to find the intersection point when the ray never intersect the sphere");

        // TC06 The ray is orthogonal to the sphere and start in the sphere
        assertEquals(List.of(new Point(0, 0.5, 1-Math.sqrt(0.75))), sphere.findIntsersections(new Ray(new Point(0, 0.5, 1), new Vector(0, 0, -1))), "Failed to find the intersection point when the ray start inside the sphere");


    }
}