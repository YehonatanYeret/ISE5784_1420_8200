package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * PointLight class represents a light source with a specific position in the scene
 */
public class PointLight extends Light implements LightSource{
    /**
     * position of the light source
     */
    protected Point position;
    private double kC = 1;
    private double kL = 0;
    private double kQ = 0;

    /**
     * get intensity of the light at a specific point
     * @param kC attenuation factor
     * @return intensity of the light at a specific point
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * get intensity of the light at a specific point
     * @param kL attenuation factor
     * @return intensity of the light at a specific point
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * get intensity of the light at a specific point
     * @param kQ attenuation factor
     * @return intensity of the light at a specific point
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * get intensity of the light at a specific point
     * @param color color of the light
     * @param position position of the light source
     */
    public PointLight(Color color, Point position) {
        super(color);
        this.position = position;
    }

    @Override
    public Color getIntensity(Point point) {
        double d = position.distance(point);
        return intensity.scale(1 / (kC + kL * d + kQ * d * d));
    }

    @Override
    public Vector getL(Point point) {
        return point.subtract(position).normalize();
    }
}
