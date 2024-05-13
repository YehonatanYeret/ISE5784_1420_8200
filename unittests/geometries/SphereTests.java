package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.Comparator;
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
        assertEquals(List.of(new Point(0, Math.sqrt(0.75), 1.5)), sphere.findIntsersections(new Ray(new Point(0, 0, 1.5), new Vector(0, 1, 0))), "Failed to find the intersection point when the ray start inside the sphere");

        // TC02 The ray never intersect the sphere
        assertNull(sphere.findIntsersections(new Ray(new Point(0, 0, 3), new Vector(1, 1, 1))), "Failed to find the intersection point when the ray never intersect the sphere");

        // TC03 The ray start outside the sphere and intersect the sphere twice
        assertEquals(List.of(new Point(0, Math.sqrt(0.75), 1.5), new Point(0, -Math.sqrt(0.75), 1.5)), sphere.findIntsersections(new Ray(new Point(0, 2, 1.5), new Vector(0, -1, 0))), "Failed to find the intersection points when the ray start outside the sphere and intersect the sphere twice");

        // TC04 The ray start outside the sphere and the ray does not intersect the sphere
        assertNull(sphere.findIntsersections(new Ray(new Point(0, -2, 1.5), new Vector(0, -1, 0))), "Failed to find the intersection points when the ray start outside the sphere and not intersect the sphere");

        // =============== Boundary Values Tests =================

        // Test orthogonal rays:

        // TC05 The ray is orthogonal to the sphere and start before the sphere
        assertNull(sphere.findIntsersections(new Ray(new Point(0, 2, 1), new Vector(0, 0, 1))), "Failed to find the intersection point when the ray never intersect the sphere");

        // TC06 The ray is orthogonal to the sphere and start in the sphere
        assertEquals(List.of(new Point(0, 0.5, 1-Math.sqrt(0.75))), sphere.findIntsersections(new Ray(new Point(0, 0.5, 1), new Vector(0, 0, -1))), "Failed to find the intersection point when the ray start inside the sphere");

        //tests for tangential rays:

        // TC07 The ray is tangential to the sphere and start before the sphere
        assertNull(sphere.findIntsersections(new Ray(new Point(-1, 1, 0), new Vector(1, 0, 1))), "Failed to find the intersection point when the ray never intersect the sphere");

        // TC08 The ray is tangential to the sphere and start on the sphere
        assertNull(sphere.findIntsersections(new Ray(new Point(0, 1, 1), new Vector(1, 0, 1))), "Failed to find the intersection point when the ray never intersect the sphere");

        // TC09 The ray is tangential to the sphere and start after the sphere
        assertNull(sphere.findIntsersections(new Ray(new Point(1, 1, 2), new Vector(1, 0, 1))), "Failed to find the intersection point when the ray never intersect the sphere");


        //tests for rays that are not orthogonal nor tangential to the sphere(not reach middle of the sphere):

        // TC10 The ray start on the sphere and intersect the sphere
        assertEquals(List.of(new Point(-2.0/3, 1.0/3, 1.0/3)), sphere.findIntsersections(new Ray(new Point(0, 1, 1), new Vector(-1, -1, -1))), "Failed to find the intersection point when the ray start on the sphere and intersect the sphere");

        // TC11 The ray start on the sphere and does not intersect the sphere
        assertNull(sphere.findIntsersections(new Ray(new Point(0, 1, 1), new Vector(1, 1, 1))), "Failed to find the intersection point when the ray start on the sphere and doesn't intersect the sphere");

        //test that reach the middle of the sphere:

        // TC12 The ray start on the sphere and reach the middle of the sphere
        assertEquals(List.of(new Point(0, -1, 1)), sphere.findIntsersections(new Ray(new Point(0, 1, 1), new Vector(0, -1, 0))), "Failed to find the intersection point when the ray start on the sphere and reach the middle of the sphere");

        // TC13 The ray start before the sphere and reach the middle of the sphere
        assertEquals(List.of(new Point(0, 1, 1), new Point(0,-1,1)), sphere.findIntsersections(new Ray(new Point(0, 2, 1), new Vector(0, -1, 0))).stream().sorted(Comparator.comparingDouble(p -> p.distance(new Point(-1,0,0)))).toList(), "Failed to find the intersection point when the ray start before the sphere and reach the middle of the sphere");

        //TC14 The ray start in the middle of the sphere
        assertEquals(List.of(new Point(0, 1, 1)), sphere.findIntsersections(new Ray(new Point(0, 0, 1), new Vector(0, 1, 0))), "Failed to find the intersection point when the ray start in the middle of the sphere");

        //TC15 the run on the sphere and does not reach the middle of the sphere because the direction is opposite
        assertNull(sphere.findIntsersections(new Ray(new Point(0, 1, 1), new Vector(0, 1, 0))), "Failed to find the intersection point when the ray start on the sphere and doesn't reach the middle of the sphere");

        //TC16 the run after the sphere and does not reach the middle of the sphere because the direction is opposite
        assertNull(sphere.findIntsersections(new Ray(new Point(0, 2, 1), new Vector(0, 1, 0))), "Failed to find the intersection point when the ray start after the sphere and doesn't reach the middle of the sphere");

        //TC17 the run in the sphere and does not reach the middle of the sphere because the direction is opposite
        assertNull(sphere.findIntsersections(new Ray(new Point(0, 0.5, 1), new Vector(0, 1, 0))), "Failed to find the intersection point when the ray start in the sphere and doesn't reach the middle of the sphere");
    }
}