package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 */
class CylinderTests {

private final Cylinder cylinder = new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1, 1);
private final Cylinder cylinder2 = new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 8, 2);

    /**
     * Test method for {@link geometries.Cylinder#Cylinder(primitives.Ray, double, double)}.
     */
    @Test
    void testConstructor() {
    // ============ Equivalence Partitions Tests ==============
    // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1, 1));

    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
    // ============ Equivalence Partitions Tests ==============
        // TC01: the point is on the top outer surface of the cylinder
        assertEquals(new Vector(0, 1, 0),
                cylinder.getNormal(new Point(0, 1, 0.5)),
                "Bad normal to cylinder");

        // TC02: the point is on the bottom outer surface of the cylinder
        assertEquals(new Vector(0,0,-1),
                cylinder.getNormal(new Point(0, 0.5, 0)),
                "Bad normal to cylinder");

        // TC03: the point is on the side outer surface of the cylinder
        assertEquals(new Vector(0,0,1),
                cylinder.getNormal(new Point(0, 0.5, 1)),
                "Bad normal to cylinder");

        // =============== Boundary Values Tests ==================
        // TC04: the point is on the top edge of the cylinder;
        assertEquals(new Vector(0, 0, 1),
                cylinder.getNormal(new Point(0, 1, 1)),
                "Bad normal to cylinder");

        // TC05: the point is on the bottom edge of the cylinder
        assertEquals(new Vector(0,0,-1),
                cylinder.getNormal(new Point(0, 1, 0)),
                "Bad normal to cylinder");

        // TC06: the point is in the middle bottom outer surface of the cylinder
        assertEquals(new Vector(0,0,-1),
                cylinder.getNormal(new Point(0, 0, 0)),
                "Bad normal to cylinder");

        // TC07: the point is in the middle top edge of the cylinder
        assertEquals(new Vector(0, 0, 1),
                cylinder.getNormal(new Point(0, 0, 1)),
                "Bad normal to cylinder");

    }

    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's intersection with the cylinder is in the middle of the cylinder
        assertEquals(List.of(new Point(2, 2, 1), new Point(-2, -2, 1)),
                cylinder2.findIntersections(new Ray(new Point(3, 3, 1), new Vector(-1, -1, 0))),
                "Bad intersection to cylinder");

        // TC02: Ray's intersection with the cylinder surface
        assertEquals(List.of(new Point(2, 2, 2), new Point(2, 2, 0)),
                cylinder2.findIntersections(new Ray(new Point(2, 2, -1), new Vector(0, 0, 1))),
                "Bad intersection to cylinder");

        // TC03: Ray's intersection with the cylinder one of the bases and the other with the surface
        assertEquals(List.of(new Point(-2, -2, 1), new Point(-1, -1, 0)),
                cylinder2.findIntersections(new Ray(new Point(3, 3, -4), new Vector(-1, -1, 1))),
                "Bad intersection to cylinder");

        // TC04: Ray's does not intersect the cylinder
        assertNull(cylinder2.findIntersections(new Ray(new Point(3, 3, 0), new Vector(1, 1, 1))),
                 "Bad intersection to cylinder");

        // =============== Boundary Values Tests ==================
        // TC05: Ray's intersection with the cylinder is in the middle of the two bases
        assertEquals(2, cylinder2.findIntersections(new Ray(new Point(0, 0, -2), new Vector(0, 0, 1))).size(),
                "Bad intersection to cylinder");

        // TC06: Ray's intersection with the cylinder is in the middle of the top base
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 0, 1))).size(),
                "Bad intersection to cylinder");

        // TC07: Ray's intersection with the cylinder is in the middle of the bottom base
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 0, -1))).size(),
                "Bad intersection to cylinder");

        // TC08: The ray starts at the top of the cylinder and intersects the bottom base
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, -1))).size(),
                "Bad intersection to cylinder");

        // TC09: The ray starts at the bottom of the cylinder and intersects the top base
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1))).size(),
                "Bad intersection to cylinder");

        // TC10: The ray intersects the cylinder at two points, and he is not parallel to the cylinder
        assertEquals(2, cylinder2.findIntersections(new Ray(new Point(3, 3, 0.5), new Vector(-1, -1, 0.2))).size(),
                "Bad intersection to cylinder");

        // TC11: The ray intersects the cylinder at one point, and he is not parallel to the cylinder
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(1, 1, 0.5), new Vector(1, 1, 0.5))).size(),
                "Bad intersection to cylinder");

        // TC12: The ray is parallel to the cylinder
        assertNull(cylinder2.findIntersections(new Ray(new Point(3, 3, 0), new Vector(0, 0, 1))),
                "Bad intersection to cylinder");

        // TC13: The ray start from the middle of the cylinder ray and intersect the base
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 0, -1))).size(),
                "Bad intersection to cylinder");

        // TC14: The ray start from the middle of the cylinder ray and intersect the surface
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(0, 0, 1), new Vector(1, 0, 0))).size(),
                "Bad intersection to cylinder");

        // TC15: The ray start from the surface of the cylinder and intersect the base
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(2, 2, 1), new Vector(-1, -1, -1))).size(),
                "Bad intersection to cylinder");

        // TC16: The ray start from the surface of the cylinder and intersect the surface
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(2, 2, 1), new Vector(-1, -1, 0))).size(),
                "Bad intersection to cylinder");

        // TC17: The ray start from the base of the cylinder and intersect the base
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(1, 1, 2), new Vector(-1, -1, -1))).size(),
                "Bad intersection to cylinder");

        // TC18: The ray start from the base of the cylinder and intersect the surface
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(1, 1, 2), new Vector(-1, -1, -0.5))).size(),
                "Bad intersection to cylinder");

        // TC19: The ray intersect the cylinder at two points, at the corners of the cylinder
        assertEquals(2, cylinder2.findIntersections(new Ray(new Point(3, 3, 2.5), new Vector(-1, -1, -0.5))).size(),
                "Bad intersection to cylinder");

        // TC20: The ray start from one of the corners of the cylinder and intersect the base
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(2, 2, 2), new Vector(-1, -1, -1))).size(),
                "Bad intersection to cylinder");

        // TC21: The ray start from one of the corners of the cylinder and intersect the surface
        assertEquals(1, cylinder2.findIntersections(new Ray(new Point(2, 2, 2), new Vector(-3, -3, -1))).size(),
                "Bad intersection to cylinder");

        // TC22: The ray is parallel to the base of the cylinder
        assertNull(cylinder2.findIntersections(new Ray(new Point(3, 3, 2), new Vector(-1, -1, 0))),
                "Bad intersection to cylinder");

        // TC23: The ray is parallel to the surface of the cylinder
        assertNull(cylinder2.findIntersections(new Ray(new Point(2, 2, 3), new Vector(0, 0, -1))),
                "Bad intersection to cylinder");

        // TC24: The ray start from the surface of the cylinder and don't intersect the cylinder
        assertNull(cylinder2.findIntersections(new Ray(new Point(2, 2, 1), new Vector(1, 1, 1))),
                "Bad intersection to cylinder");

        // TC25: The ray start from the base of the cylinder and don't intersect the cylinder
        assertNull(cylinder2.findIntersections(new Ray(new Point(1, 1, 2), new Vector(1, 1, 1))),
                "Bad intersection to cylinder");

    }
}