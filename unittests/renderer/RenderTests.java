package renderer;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import geometries.*;
import org.junit.jupiter.api.Test;

import lighting.*;
import lighting.AmbientLight;
import primitives.*;
import scene.JsonScene;
import scene.Scene;

/**
 * Test rendering a basic image
 *
 * @author Dan
 */
public class RenderTests {
    /**
     * Scene of the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder of the tests
     */
    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(Point.ZERO).setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpDistance(100)
            .setVpSize(500, 500);

    /**
     * Produce a scene with basic 3D model and render it into a png image with a
     * grid
     */
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

    // For stage 6 - please disregard in stage 5

    /**
     * Produce a scene with basic 3D model - including individual lights of the
     * bodies and render it into a png image with a grid
     */
    @Test
    public void renderMultiColorTest() {
        scene.geometries.add( // center
                new Sphere(50, new Point(0, 0, -100)),
                // up left
                new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
                        .setEmission(new Color(GREEN)),
                // down left
                new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
                        .setEmission(new Color(RED)),
                // down right
                new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
                        .setEmission(new Color(BLUE)));
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.2, 0.2, 0.2))); //

        camera
                .setImageWriter(new ImageWriter("color render test", 1000, 1000))
                .build()
                .renderImage()
                .printGrid(100, new Color(WHITE))
                .writeToImage();
    }

    /**
     * Test for JSON based scene - for bonus
     */
    @Test
    public void basicRenderJson() {
        assertDoesNotThrow(() -> {
            Scene scene1 = JsonScene.importScene("jsonScenes/multydiamonds.json");
            camera
                    .setImageWriter(new ImageWriter("multi diamond", 1000, 1000))
                    .setRayTracer(new SimpleRayTracer(scene1))
                    .setMultithreading(3)
                    .setDebugPrint(0.1)
                    .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 1, 10).normalize())
                    .setLocation(new Point(0, -350, 45))//Point(0, 130, 30)
                    .setVpDistance(500)
                    .setVpSize(150, 150)
                    .build()
                    .renderImage()
                    .writeToImage();

        }, "Failed to render image");
    }

    @Test
    public void basicRenderJson2() {
        assertDoesNotThrow(() -> {
                    final Camera.Builder camera = Camera.getBuilder()
                            .setRayTracer(new SimpleRayTracer(scene))
                            .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 0.1, 1).normalize())
//                            .setDirection(new Vector(-0.5, 1, -0.1).normalize(), new Vector(0.05, 0.1, 0.75).normalize())
                            .setLocation(new Point(0, -350, 60))//Point(0, 130, 30)
                            .setVpDistance(500)
                            .setVpSize(150, 150);
                    Scene scene = JsonScene.importScene("jsonScenes/diamondRing.json");

                    camera
                            .setRayTracer(new SimpleRayTracer(scene))
                            .setImageWriter(new ImageWriter("diamond ring", 1000, 1000))
                            .build()
                            .renderImage()
                            .writeToImage();
                }, "Failed to render image"
        );
    }

    @Test
    public void basicRenderJson3() {
        assertDoesNotThrow(() -> {
            Scene scene1 = JsonScene.importScene("jsonScenes/snooker.json");
            camera
                    .setImageWriter(new ImageWriter("multi diamond depth", 1000, 1000))
                    .setRayTracer(new SimpleRayTracer(scene1))

                    .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 1, 10).normalize())
                    .setLocation(new Point(0, -350, 45))//Point(0, 130, 30)
                    .setVpDistance(500)
                    .setVpSize(150, 150)

                    .setAmountOfRays(5)
                    .setDepthOfField(320)
                    .setAperture(3)

                    .build()
                    .renderImage()
                    .writeToImage();

        }, "Failed to render image");
    }
}