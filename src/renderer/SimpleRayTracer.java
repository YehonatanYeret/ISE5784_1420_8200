package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * SimpleRayTracer class is the basic class for ray tracing
 */
public class SimpleRayTracer extends RayTracerBase{
    /**
     * Constructor to initialize the scene
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);
        if (intersections == null) {
            return scene.background;
        }

        Point closestPoint = ray.findClosestPoint(intersections);
        return calcColor(closestPoint);
    }

    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }
}
