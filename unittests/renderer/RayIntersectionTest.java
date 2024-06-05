package renderer;

import geometries.Geometry;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class that represent the ray tracer
 */
public class RayIntersectionTest {

    private final Vector yAxis = new Vector(0, -1, 0);
    private final Vector zAxis = new Vector(0, 0, -1);

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 1, 1))
            .setDirection(zAxis, yAxis)
            .setVpDistance(1)
            .setVpSize(3, 3);

    private final Camera camera = cameraBuilder.setLocation(new Point(0, 0, 0.5)).build();

    /**
     * helper function to test the amount of intersections
     */
    private void amountOfIntersections(Camera camera, Geometry geometry, int expectedAmount) {
        int intersections = 0;
        for (int j = 0; j < 3; j++)
            for (int i = 0; i < 3; i++) {
                List<Point> intersectionsList = geometry.findIntersections(camera.constructRay(3, 3, j, i));
                intersections += intersectionsList != null ? intersectionsList.size() : 0;
            }

        assertEquals(expectedAmount, intersections, "Wrong amount of intersections");
    }


    /**
     * Test method for
     * {@link renderer.Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testSphereIntersection() {
        // TC01: 2 intersections
        amountOfIntersections(cameraBuilder.setLocation(Point.ZERO).build(), new Sphere(1, new Point(0, 0, -3)), 2);

        // TC02: 18 intersections
        amountOfIntersections(camera, new Sphere(2.5, new Point(0, 0, -2.5)), 18);

        // TC03: 10 intersections
        amountOfIntersections(camera, new Sphere(2, new Point(0, 0, -2)), 10);

        // TC04: 9 intersections
        amountOfIntersections(camera, new Sphere(4, new Point(0, 0, -1)), 9);

        // TC05: 0 intersections
        amountOfIntersections(camera, new Sphere(0.5, new Point(0, 0, 1)), 0);
    }

    /**
     * Test method for
     * {@link renderer.Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testPlaneIntersection() {
        // TC01: 9 intersections
        amountOfIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, 0, -1)), 9);

        // TC02: 9 intersections
        amountOfIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, 1, -10)), 9);

        // TC03: 6 intersections
        amountOfIntersections(camera, new geometries.Plane(new Point(0, 0, -1), new Vector(0, -1, -1)), 6);
    }

    /**
     * Test method for
     * {@link renderer.Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void testTriangleIntersection() {
        // TC01: 1 intersections
        amountOfIntersections(camera, new geometries.Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 1);

        // TC02: 2 intersections
        amountOfIntersections(camera, new geometries.Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2)), 2);
    }
}

