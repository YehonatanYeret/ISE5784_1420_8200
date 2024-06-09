package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * class for directional light
 */
public class DirectionalLight extends Light implements LightSource{
    private Vector direction;

    /**
     * get intensity of the light at a specific point
     * @param color color of the light
     * @param direction direction of the light
     */
    public DirectionalLight(Color color, Vector direction) {
        super(color);
        this.direction = direction.normalize();
    }

    @Override
    public Vector getL(Point point) {
        return direction;
    }

    @Override
    public Color getIntensity(Point point) {
        return intensity;
    }
}
