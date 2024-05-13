package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 */
class CylinderTests {

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
        Cylinder cylinder = new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1, 1);

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
//        // ============ Equivalence Partitions Tests ==============
//        // TC01: Ray's line is outside the cylinder (0 points)
//        Cylinder cylinder = new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1, 1);
//        assertEquals(null, cylinder.findIntsersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, 1))),
//                "Ray's line out of cylinder");
//
//        // TC02: Ray starts before and crosses the cylinder (2 points)
//        assertEquals(List.of(new Point(0, 0, 1), new Point(0, 0, 2)),
//                cylinder.findIntsersections(new Ray(new Point(0, 0, -1), new Vector(0, 0, 1))),
//                "Wrong number of points");
//
//        // TC03: Ray starts inside the cylinder (1 point)
//        assertEquals(List.of(new Point(0, 0, 2)),
//                cylinder.findIntsersections(new Ray(new Point(0, 0, 1), new Vector(0, 0, 1))),
//                "Wrong number of points");
//
//        // TC04: Ray starts after the cylinder (0 points)
//        assertEquals(null, cylinder.findIntsersections(new Ray(new Point(0, 0, 3), new Vector(0, 0, 1))),
//                "Ray's line out of cylinder");
//
//        // TC05: Ray starts before and crosses the cylinder (2 points)
//        cylinder = new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1, 2);
//        assertEquals(List.of(new Point(0, 0, 1), new Point(0, 0, 3)),
//                cylinder.findIntsersections(new Ray(new Point(0, 0, -1), new Vector(0, 0, 1))),
//                "Wrong number of points");
//
//        // TC06: Ray starts inside the cylinder (1 point)
//        assertEquals(List.of(new Point(0, 0, 3)),
//                cylinder.findIntsersections(new Ray(new Point(0, 0, 1), new Vector(0, 0, 1))),
//                "Wrong number of points");

        // TC07: Ray starts after the cylinder (0 points)
        Cylinder cylinder = new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 1, 2);
        assertEquals(null, cylinder.findIntsersections(new Ray(new Point(0, 0, 4), new Vector(0, 0, 1))),
                "Ray's line out of cylinder");

    }
}