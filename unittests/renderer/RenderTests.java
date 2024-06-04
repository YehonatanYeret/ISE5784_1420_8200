package renderer;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.AmbientLight;
import primitives.*;
import scene.JsonScene;
import scene.Scene;

/** Test rendering a basic image
 * @author Dan */
public class RenderTests {
    /** Scene of the tests */
    private final Scene          scene  = new Scene("Test scene");
    /** Camera builder of the tests */
    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            //.setLocation(Point.ZERO).setDirection(new Point(0, 0, -1), Vector.Y)
            .setLocation(Point.ZERO).setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpDistance(100)
            .setVpSize(500, 500);

    /** Produce a scene with basic 3D model and render it into a png image with a
     * grid */
    @Test
    public void renderTwoColorTest() {
        scene.geometries.add(new Sphere(50d, new Point(0, 0, -100)),
                new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)), // up
                // left
                new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100),
                        new Point(-100, -100, -100)), // down
                // left
                new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))); // down
        scene.setAmbientLight(new AmbientLight(new Color(255, 191, 191), Double3.ONE))
                .setBackground(new Color(75, 127, 90));

        // right
        camera
                .setImageWriter(new ImageWriter("base render test", 1000, 1000))
                .build()
                .renderImage()
                .printGrid(100, new Color(YELLOW))
                .writeToImage();
    }

    /** Test for XML based scene - for bonus */
    @Test
    public void basicRenderJson() {
        // This is the test for the bonus part of the exercise
        //try to load a scene from a json file and render it
        //fail if the function throws
        assertDoesNotThrow(() -> {
            Scene scene1 = JsonScene.importScene("images/test1.json", "Test scene");

            camera
                    .setImageWriter(new ImageWriter("json render test", 1000, 1000))
                    .setRayTracer(new SimpleRayTracer(scene1))
                    .build()
                    .renderImage()
                    .printGrid(100, new Color(YELLOW))
                    .writeToImage();

        }, "Failed to render image");
    }
}

