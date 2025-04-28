package renderer;

import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;
import scene.JsonScene;
import scene.Scene;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MINIPTest {
    /**
     * Camera builder of the tests
     */
    private final Camera.Builder camera = Camera.getBuilder()
            .setLocation(Point.ZERO).setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpDistance(100)
            .setVpSize(500, 500);

    @Test
    public void DiamondBox() {
        assertDoesNotThrow(() -> {
            Scene scene1 = JsonScene.importScene("jsonScenes/multydiamonds.json");
            camera
                    .setImageWriter(new ImageWriter("multi diamond", 1000, 1000))
                    .setRayTracer(new SimpleRayTracer(scene1))
//                    .setMultithreading(3)
                    .setDebugPrint(0.1)
                    .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 1, 10).normalize())
                    .setLocation(new Point(0, -350, 45))//Point(0, 130, 30)
                    .setVpDistance(500)
                    .setVpSize(150, 150)
                    .setMultithreading(-1)
                    .build()
                    .renderImage()
                    .writeToImage();

        }, "Failed to render image");
    }

    @Test
    public void diamondRing() {
        assertDoesNotThrow(() -> {
                    Scene scene = JsonScene.importScene("jsonScenes/diamondRing.json");
                    final Camera.Builder camera = Camera.getBuilder()
                            .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 0.1, 1).normalize())
                            .setLocation(new Point(0, -350, 60))//Point(0, 130, 30)
                            .setVpDistance(500)
                            .setVpSize(150, 150)
                            .setRayTracer(new SimpleRayTracer(scene))
                            .setImageWriter(new ImageWriter("diamond ring", 1000, 1000));

                    camera
                            .build()
                            .renderImage()
                            .writeToImage();
                }, "Failed to render image"
        );
    }

    @Test
    public void snooker() {
        assertDoesNotThrow(() -> {
            Scene scene = JsonScene.importScene("jsonScenes/snooker.json");
            camera
                    .setImageWriter(new ImageWriter("snooker", 1000, 1000))
                    .setRayTracer(new SimpleRayTracer(scene))

                    .setDirection(new Vector(1, 0.2, 0).normalize(), new Vector(0, 0, 1).normalize())
                    .setLocation(new Point(-180, -35, 30))//Point(0, 130, 30)
                    .setVpDistance(200)
                    .setVpSize(150, 150)

                    .build()
                    .renderImage()
                    .writeToImage();

        }, "Failed to render image");
    }

    @Test
    public void crown() {
        assertDoesNotThrow(() -> {
            Scene scene = JsonScene.importScene("jsonScenes/crown.json");


            camera
                    .setImageWriter(new ImageWriter("crown", 1000, 1000))
                    .setRayTracer(new SimpleRayTracer(scene))

                    .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 1, 10).normalize())
                    .setLocation(new Point(0, -320, 40))
                    .setVpDistance(500)
                    .setVpSize(150, 150)
                    .setMultithreading(-1)
                    .build()
                    .renderImage()
                    .writeToImage();

        }, "Failed to render image");
    }

    @Test
    public void house() {
        assertDoesNotThrow(() -> {
            Scene scene = JsonScene.importScene("jsonScenes/house.json");


            camera
                    .setImageWriter(new ImageWriter("house", 1000, 1000))
                    .setRayTracer(new SimpleRayTracer(scene))

                    .setDirection(new Vector(0, 1, -0.1).normalize(), new Vector(0, 1, 10).normalize())
                    .setLocation(new Point(0, -320, 40))
                    .setVpDistance(500)
                    .setVpSize(150, 150)
                    .setMultithreading(-1)
                    .build()
                    .renderImage()
                    .writeToImage();

        }, "Failed to render image");
    }
}
