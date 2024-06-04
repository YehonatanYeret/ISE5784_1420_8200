package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Tube class
 */
class TubeTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;
    private final Ray r1 = new Ray(Point.ZERO, new Vector(1, 0, 0));
    private final Tube tube = new Tube(new Ray(Point.ZERO,new Vector(0, 0, 1)), 1);

    /**
     * Test method for {@link geometries.Tube#Tube(primitives.Ray, double)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test for a proper result
        assertDoesNotThrow(() -> new Tube(r1, 1), "Failed to create a proper tube");
    }
    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple test for the getNormal function
        assertEquals(new Vector(1, 0, 0), tube.getNormal(new Point(1, 0, 1)), "Bad normal to tube");
        // =============== Boundary Values Tests =================
        // TC02: (p-p0) is orthogonal to the axis of the tube
        assertEquals(new Vector(1, 0, 0), tube.getNormal(new Point(1, 0, 0)), "Bad normal to tube");
        // ============ Equivalence Partitions Tests ==============
        // TC03: Test if the point is vertical to the axis
        assertDoesNotThrow(() -> new Tube(r1, 1).getNormal(new Point(0, 1, 0)), "Failed to throw an exception when the point is vertical to the axis");
    }

    @Test
    public void testFindIntersectionsRay() {
        /** Test error message */
        String MUST_BE_1_INTERSECTION    = "must be 1 intersection";
        /** Test error message */
        String BAD_INTERSECTIONS         = "Bad intersections";
        /** Test error message */
        String MUST_BE_2_INTERSECTIONS   = "must be 2 intersections";
        /** Test error message */
        String MUST_NOT_BE_INTERSECTIONS = "Must not be intersections";
        /** Test error message */
        String MUST_BE_INTERSECTIONS     = "must be intersections";

        Tube   tube1 = new Tube(new Ray(new Point(1, 0, 0), new Vector(0, 1, 0)), 1d);
        Vector vAxis = new Vector(0, 0, 1);
        Tube   tube2 = new Tube(new Ray(new Point(1, 1, 1), vAxis), 1d);
        Ray    ray;

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the tube (0 points)
        ray = new Ray(new Point(1, 1, 2), new Vector(1, 1, 0));
        assertNull(tube1.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC02: Ray's crosses the tube (2 points)
        ray = new Ray(new Point(0, 0, 0), new Vector(2, 1, 1));
        List<Point> result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_BE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(0.4, 0.2, 0.2), new Point(2, 1, 1)), result, BAD_INTERSECTIONS);

        // TC03: Ray's starts within tube and crosses the tube (1 point)
        ray    = new Ray(new Point(1, 0.5, 0.5), new Vector(2, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1)), result, BAD_INTERSECTIONS);

        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line is parallel to the axis (0 points) *****************
        // TC11: Ray is inside the tube (0 points)
        ray = new Ray(new Point(0.5, 0.5, 0.5), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC12: Ray is outside the tube
        ray = new Ray(new Point(0.5, -0.5, 0.5), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC13: Ray is at the tube surface
        ray = new Ray(new Point(2, 1, 0.5), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC14: Ray is inside the tube and starts against axis head
        ray = new Ray(new Point(0.5, 0.5, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC15: Ray is outside the tube and starts against axis head
        ray = new Ray(new Point(0.5, -0.5, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC16: Ray is at the tube surface and starts against axis head
        ray = new Ray(new Point(2, 1, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC17: Ray is inside the tube and starts at axis head
        ray = new Ray(new Point(1, 1, 1), vAxis);
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // **** Group: Ray is orthogonal but does not begin against the axis head
        // *****************
        // TC21: Ray starts outside and the line is outside (0 points)
        ray = new Ray(new Point(0, 2, 2), new Vector(1, 1, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC22: The line is tangent and the ray starts before the tube (0 points)
        ray = new Ray(new Point(0, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC23: The line is tangent and the ray starts at the tube (0 points)
        ray = new Ray(new Point(1, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC24: The line is tangent and the ray starts after the tube (0 points)
        ray = new Ray(new Point(2, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC25: Ray starts before (2 points)
        ray    = new Ray(new Point(0, 0, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_BE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(0.4, 0.2, 2), new Point(2, 1, 2)), result, BAD_INTERSECTIONS);

        // TC26: Ray starts at the surface and goes inside (1 point)
        ray    = new Ray(new Point(0.4, 0.2, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 2)), result, BAD_INTERSECTIONS);

        // TC27: Ray starts inside (1 point)
        ray    = new Ray(new Point(1, 0.5, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 2)), result, BAD_INTERSECTIONS);

        // TC28: Ray starts at the surface and goes outside (0 points)
        ray    = new Ray(new Point(2, 1, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);
        // TC29: Ray starts after
        ray    = new Ray(new Point(4, 2, 2), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC30: Ray starts before and crosses the axis (2 points)
        ray    = new Ray(new Point(1, -1, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_BE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(1, 0, 2), new Point(1, 2, 2)), result, BAD_INTERSECTIONS);

        // TC31: Ray starts at the surface and goes inside and crosses the axis
        ray    = new Ray(new Point(1, 0, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, BAD_INTERSECTIONS);

        // TC32: Ray starts inside and the line crosses the axis (1 point)
        ray    = new Ray(new Point(1, 0.5, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, BAD_INTERSECTIONS);

        // TC33: Ray starts at the surface and goes outside and the line crosses the
        // axis (0 points)
        ray    = new Ray(new Point(1, 2, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC34: Ray starts after and crosses the axis (0 points)
        ray    = new Ray(new Point(1, 3, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC35: Ray start at the axis
        ray    = new Ray(new Point(1, 1, 2), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, BAD_INTERSECTIONS);

        // **** Group: Ray is orthogonal to axis and begins against the axis head
        // *****************
        // TC41: Ray starts outside and the line is outside (
        ray = new Ray(new Point(0, 2, 1), new Vector(1, 1, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC42: The line is tangent and the ray starts before the tube
        ray = new Ray(new Point(0, 2, 1), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC43: The line is tangent and the ray starts at the tube
        ray = new Ray(new Point(1, 2, 1), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC44: The line is tangent and the ray starts after the tube
        ray = new Ray(new Point(2, 2, 2), new Vector(1, 0, 0));
        assertNull(tube2.findIntersections(ray), MUST_NOT_BE_INTERSECTIONS);

        // TC45: Ray starts before
        ray    = new Ray(new Point(0, 0, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_BE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(0.4, 0.2, 1), new Point(2, 1, 1)), result, BAD_INTERSECTIONS);

        // TC46: Ray starts at the surface and goes inside
        ray    = new Ray(new Point(0.4, 0.2, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1)), result, BAD_INTERSECTIONS);

        // TC47: Ray starts inside
        ray    = new Ray(new Point(1, 0.5, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1)), result, BAD_INTERSECTIONS);

        // TC48: Ray starts at the surface and goes outside
        ray    = new Ray(new Point(2, 1, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC49: Ray starts after
        ray    = new Ray(new Point(4, 2, 1), new Vector(2, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC50: Ray starts before and goes through the axis head
        ray    = new Ray(new Point(1, -1, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_BE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(1, 0, 1), new Point(1, 2, 1)), result, BAD_INTERSECTIONS);

        // TC51: Ray starts at the surface and goes inside and goes through the axis
        // head
        ray    = new Ray(new Point(1, 0, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 1)), result, BAD_INTERSECTIONS);

        // TC52: Ray starts inside and the line goes through the axis head
        ray    = new Ray(new Point(1, 0.5, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 1)), result, BAD_INTERSECTIONS);

        // TC53: Ray starts at the surface and the line goes outside and goes through
        // the axis head
        ray    = new Ray(new Point(1, 2, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC54: Ray starts after and the line goes through the axis head
        ray    = new Ray(new Point(1, 3, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC55: Ray start at the axis head
        ray    = new Ray(new Point(1, 1, 1), new Vector(0, 1, 0));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 1)), result, BAD_INTERSECTIONS);

        // **** Group: Ray's line is neither parallel nor orthogonal to the axis and
        // *****************
        // begins against axis head
        Point p0 = new Point(0, 2, 1);
        // TC61: Ray's line is outside the tube
        ray    = new Ray(p0, new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC62: Ray's line crosses the tube and begins before
        ray    = new Ray(p0, new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_BE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(2, 1, 2), new Point(0.4, 1.8, 1.2)), result, BAD_INTERSECTIONS);

        // TC63: Ray's line crosses the tube and begins at surface and goes inside
        ray    = new Ray(new Point(0.4, 1.8, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1.8)), result, BAD_INTERSECTIONS);

        // TC64: Ray's line crosses the tube and begins inside
        ray    = new Ray(new Point(1, 1.5, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(2, 1, 1.5)), result, BAD_INTERSECTIONS);

        // TC65: Ray's line crosses the tube and begins at the axis head
        ray    = new Ray(new Point(1, 1, 1), new Vector(0, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(1, 2, 2)), result, BAD_INTERSECTIONS);

        // TC66: Ray's line crosses the tube and begins at surface and goes outside
        ray    = new Ray(new Point(2, 1, 1), new Vector(2, -1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC67: Ray's line is tangent and begins before
        ray    = new Ray(p0, new Vector(0, 2, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC68: Ray's line is tangent and begins at the tube surface
        ray    = new Ray(new Point(1, 2, 1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC69: Ray's line is tangent and begins after
        ray    = new Ray(new Point(2, 2, 1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // **** Group: Ray's line is neither parallel nor orthogonal to the axis and
        // *****************
        // does not begin against axis head
        double sqrt2      = Math.sqrt(2);
        double denomSqrt2 = 1 / sqrt2;
        double value1     = 1 - denomSqrt2;
        double value2     = 1 + denomSqrt2;

        // TC71: Ray's crosses the tube and the axis
        ray    = new Ray(new Point(0, 0, 2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_BE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(value1, value1, 2 + value1), new Point(value2, value2, 2 + value2)), result,
                BAD_INTERSECTIONS);

        // TC72: Ray's crosses the tube and the axis head
        ray    = new Ray(new Point(0, 0, 0), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(2, result.size(), MUST_BE_2_INTERSECTIONS);
        if (result.get(0).getY() > result.get(1).getY())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(new Point(value1, value1, value1), new Point(value2, value2, value2)), result,
                BAD_INTERSECTIONS);

        // TC73: Ray's begins at the surface and goes inside
        ray    = new Ray(new Point(value1, value1, 2 + value1), new Vector(1, 0, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value1, 2 + value2)), result, BAD_INTERSECTIONS);

        // TC74: Ray's begins at the surface and goes inside crossing the axis
        ray    = new Ray(new Point(value1, value1, 2 + value1), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, 2 + value2)), result, BAD_INTERSECTIONS);

        // TC75: Ray's begins at the surface and goes inside crossing the axis head
        ray    = new Ray(new Point(value1, value1, value1), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, value2)), result, BAD_INTERSECTIONS);

        // TC76: Ray's begins inside and the line crosses the axis
        ray    = new Ray(new Point(0.5, 0.5, 2.5), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, 2 + value2)), result, BAD_INTERSECTIONS);

        // TC77: Ray's begins inside and the line crosses the axis head
        ray    = new Ray(new Point(0.5, 0.5, 0.5), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, value2)), result, BAD_INTERSECTIONS);

        // TC78: Ray's begins at the axis
        ray    = new Ray(new Point(1, 1, 3), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNotNull(result, MUST_BE_INTERSECTIONS);
        assertEquals(1, result.size(), MUST_BE_1_INTERSECTION);
        assertEquals(List.of(new Point(value2, value2, 2 + value2)), result, BAD_INTERSECTIONS);

        // TC79: Ray's begins at the surface and goes outside
        ray    = new Ray(new Point(2, 1, 2), new Vector(2, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC80: Ray's begins at the surface and goes outside and the line crosses the
        // axis
        ray    = new Ray(new Point(value2, value2, 2 + value2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

        // TC81: Ray's begins at the surface and goes outside and the line crosses the
        // axis head
        ray    = new Ray(new Point(value2, value2, value2), new Vector(1, 1, 1));
        result = tube2.findIntersections(ray);
        assertNull(result, BAD_INTERSECTIONS);

    }
}