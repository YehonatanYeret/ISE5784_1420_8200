package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Util;
import primitives.Vector;

/**
 * PointLight class represents a light source with a specific position in the scene
 */
public class PointLight extends Light implements LightSource{
    /**
     * position of the light source
     */
    protected Point position;
    private double kC = 1d;
    private double kL = 0d;
    private double kQ = 0d;

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
        double factor = kC + kL * d + kQ * d * d;
        if(Util.isZero(factor))
            return intensity.scale(Double.POSITIVE_INFINITY);

        return intensity.scale(1d/factor);
    }

    @Override
    public Vector getL(Point point) {
        return point.subtract(position).normalize();
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }
}
