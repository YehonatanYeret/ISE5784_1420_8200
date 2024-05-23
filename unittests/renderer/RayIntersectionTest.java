package renderer;

import geometries.Geometries;
import geometries.Geometry;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RayIntersectionTest {

    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, 1), new Vector(0, 1, 0))
            .setVpDistance(1);

    /**
     * helper function to test the amount of intersections
     */
    private void amountOfIntersections(Camera camera, Geometries geometry, int expectedAmount) {
        int intersections = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                intersections += geometry.findIntersections(camera.constructRay(3, 3, i, j)).size();

        assertEquals(expectedAmount, intersections, "Wrong amount of intersections");
    }


    /**
     * Test method for
     * {@link renderer.Camera#constructRay(int, int, int, int)}.
     */
    @Test
    void sphereIntersectionTest() {

    }

}
