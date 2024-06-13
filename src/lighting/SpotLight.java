package lighting;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * SpotLight class represents a light source with a specific position in the scene
 */
public class SpotLight extends PointLight{
    private final Vector direction;
    private Double narrowBeam = 1d;

    @Override
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    @Override
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    @Override
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }

    public SpotLight setNarrowBeam(double narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

    /**
     * get intensity of the light at a specific point
     * @param color color of the light
     * @param direction direction of the light
     * @param position position of the light source
     */
    public SpotLight(Color color, Vector direction, Point position) {
        super(color, position);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point point) {
        Color oldColor = super.getIntensity(point);
        if(narrowBeam != 1d)
            return oldColor.scale(Math.pow(Math.max(0d, direction.dotProduct(getL(point))),narrowBeam));
        return oldColor.scale(Math.max(0d, direction.dotProduct(getL(point))));
    }
}
