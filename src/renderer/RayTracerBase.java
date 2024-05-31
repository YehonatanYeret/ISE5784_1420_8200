package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract class for ray tracing
 */
public abstract class RayTracerBase {
    /**
     * The scene to be rendered
     */
    protected Scene scene;

    /**
     * Constructor to initialize the scene
     * @param scene the scene to be rendered
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Method to trace the rays
     * @param ray the ray to be traced
     * @return the color of the ray
     */
    abstract public Color traceRay(Ray ray);
}
