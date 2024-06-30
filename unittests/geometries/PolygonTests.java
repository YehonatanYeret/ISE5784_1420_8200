package geometries;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * Testing Polygons
 *
 * @author Maor
 */
public class PolygonTests {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;
    private final Polygon mesh = new Polygon(new Point(1, 1, 0), new Point(1, 0, 0), new Point(-1, -1, 0), new Point(0, 1, 0));

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}.
     */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertex on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with vertices on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with vertices on a side");

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)};
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link geometries.Polygon#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: the intersection point is inside the Polygon
        assertEquals(1, mesh.findIntersections(
                        new Ray(new Point(-0.5, -0.5, 1), new Vector(0, 0, -1))).size(),
                "Failed to find the intersection point when the intersection point is inside the Polygon");

        // TC02: the intersection point is outside the Polygon and against an edge
        assertNull(mesh.findIntersections(
                        new Ray(new Point(0.5, 2, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is outside the Polygon and against an edge");

        // TC03: the intersection point is outside the Polygon and against a vertex
        assertNull(mesh.findIntersections(
                        new Ray(new Point(2, 2, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is outside the Polygon and against an edge");

        // ================= Boundary Values Tests =================
        // TC04: the intersection point is on the edge of the Polygon
        assertNull(mesh.findIntersections(
                        new Ray(new Point(0.5, 1, -1), new Vector(0, 0, 1))),
                "Failed to find the intersection point when the intersection point is on the edge of the Polygon");

        // TC05: the intersection point is on the vertex of the Polygon
        assertNull(mesh.findIntersections(
                        new Ray(new Point(1, 1, 1), new Vector(0, 0, -1))),
                "Failed to find the intersection point when the intersection point is on the vertex of the Polygon");

        // TC06: the intersection point is outside the Polygon but in the path of the edge
        assertNull(mesh.findIntersections(
                        new Ray(new Point(2, 1, -1), new Vector(0, 0, 1))),
                "Failed to find the intersection point when the intersection point is outside the Polygon but in the path of the edge");

        // ================= external Tests =================
        // TC07: the Polygon is in an angle
        Polygon mesh2 = new Polygon(new Point(0, 1, 1), new Point(1, 1, 0), new Point(1, 0, 1), new Point(-1, -1, 4));
        assertEquals(1, mesh2.findIntersections(
                        new Ray(new Point(-1, -1, -1), new Vector(1, 1, 1))).size(),
                "Failed to find the intersection point when the intersection point is inside the Polygon");

        // TC08: the Polygon with 6 vertices
        Polygon mesh3 = new Polygon(
                new Point(1, 0, 0),
                new Point(1, 1, 0),
                new Point(0, 1, 0),
                new Point(-1, 0, 0),
                new Point(0, -1, 0));
        assertEquals(1, mesh3.findIntersections(
                        new Ray(new Point(-1, -1, -1), new Vector(1, 1, 1))).size(),
                "Failed to find the intersection point when the intersection point is inside the Polygon");
    }

    /**
     * Test method for {@link geometries.Polygon#findGeoIntersections(primitives.Ray, double)}.
     */
    @Test
    void testIntersectionWithDistance() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: The distance between the ray intersection point and the ray's start point is more than the distance(0 points)
        assertNull(mesh.findGeoIntersections(new Ray(new Point(-0.5, -0.5, 2), new Vector(0, 0, -1)), 1),
                "Ray's intersection point is out of the distance");

        // TC02: The distance between the ray intersection point and the ray's start point is less than the distance(1 point)
        assertEquals(1, mesh.findGeoIntersections(new Ray(new Point(-0.5, -0.5, 2), new Vector(0, 0, -1)),
                10).size(), "Ray's intersection points is in the distance");
    }
}
