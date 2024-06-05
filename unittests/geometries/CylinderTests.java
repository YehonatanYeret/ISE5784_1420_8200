package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 */
class CylinderTests {

    private final Vector v1 = new Vector(0,0,-1);
    private final Vector v2 = new Vector(0,0,1);
    private final Cylinder cylinder = new Cylinder(new Ray(Point.ZERO, v2), 1, 1);
    /**
     * Test method for {@link geometries.Cylinder#Cylinder(primitives.Ray, double, double)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Cylinder(new Ray(Point.ZERO, v2), 1, 1));
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
        assertEquals(v1, cylinder.getNormal(new Point(0, 0.5, 0)),
                "Bad normal to cylinder");

        // TC03: the point is on the side outer surface of the cylinder
        assertEquals(v2, cylinder.getNormal(new Point(0, 0.5, 1)),
                "Bad normal to cylinder");

        // =============== Boundary Values Tests ==================
        // TC04: the point is on the top edge of the cylinder;
        assertEquals(v2, cylinder.getNormal(new Point(0, 1, 1)),
                "Bad normal to cylinder");

        // TC05: the point is on the bottom edge of the cylinder
        assertEquals(v1, cylinder.getNormal(new Point(0, 1, 0)),
                "Bad normal to cylinder");

        // TC06: the point is in the middle bottom outer surface of the cylinder
        assertEquals(v1, cylinder.getNormal(Point.ZERO),
                "Bad normal to cylinder");

        // TC07: the point is in the middle top edge of the cylinder
        assertEquals(v2, cylinder.getNormal(new Point(0, 0, 1)),
                "Bad normal to cylinder");

    }

    /**
     * Test method for {@link geometries.Cylinder#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Cylinder cylinder2 = new Cylinder(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), 2, 2);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the cylinder (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, 1))),
                "Ray's line out of cylinder");

        // TC02: Ray starts before and crosses the cylinder (2 points)
        List<Point> result = cylinder2.findIntersections(new Ray(new Point(0, 0, -1), new Vector(0, 0, 1)));
        assertEquals(2, result.size(), "Wrong number of points");

        // TC03: Ray starts inside the cylinder (1 point)
        result = cylinder2.findIntersections(new Ray(new Point(0, 0, 1), new Vector(0, 0, 1)));
        assertEquals(1, result.size(), "Wrong number of points");

        // TC04: Ray starts after the cylinder (0 points)
        assertNull(cylinder2.findIntersections(new Ray(new Point(0, 0, 3), new Vector(0, 0, 1))),
                "Ray's line out of cylinder");

        // TC05: Ray starts at the cylinder and goes outside (0 points)
        assertNull(cylinder2.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 0, -1))),
                "Ray's line out of cylinder");

        // TC06: Ray starts at the cylinder and goes inside (1 point)
        result = cylinder2.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)));
        assertEquals(1, result.size(), "Wrong number of points");

        // TC07: Ray intersects the cylinder's top surface (1 point)
        result = cylinder2.findIntersections(new Ray(new Point(0, 0, 3), new Vector(0, 0, -1)));
        assertEquals(2, result.size(), "Wrong number of points");

        // TC10: Ray starts at the cylinder's top surface and goes inside (1 point)
        result = cylinder2.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 0, 1)));
        assertNull(result, "Wrong number of points");

        // TC11: Ray intersects the tube but not the cylinder (0 points)
        assertNull(cylinder2.findIntersections(new Ray(new Point(0, 0, 3), new Vector(0, 1, 0))),
                "Ray's line out of cylinder");

        // TC12: Ray tangent to the cylinder's top surface (0 points)
        assertNull(cylinder2.findIntersections(new Ray(new Point(0, 0, 2), new Vector(0, 1, 0))),
                "Ray's line out of cylinder");

        // TC13: Ray tangent to the cylinder's bottom surface (0 points)
        assertNull(cylinder2.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 1, 0))),
                "Ray's line out of cylinder");

        // TC14: Ray tangent to the cylinder's side surface (0 points)
        assertNull(cylinder2.findIntersections(new Ray(new Point(0, 2, -1), new Vector(0, 0, 1))),
                "Ray's line out of cylinder");
    }

}