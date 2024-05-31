package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * AmbientLight class represents the ambient light in the scene
 */
public class AmbientLight {
    /**
     * NONE is a constant for no ambient light
     */
    public final static AmbientLight NONE = new AmbientLight(Color.BLACK, 0.0);
    private final Color intensity;

    /**
     * Constructor for AmbientLight
     * @param ia the intensity of the ambient light
     * @param ka the coefficient of the ambient light
     */
    public AmbientLight(Color ia, Double3 ka) {
        intensity = ia.scale(ka);
    }

    /**
     * Constructor for AmbientLight
     * @param ia the intensity of the ambient light
     * @param ka the coefficient of the ambient light
     */
    public AmbientLight(Color ia, double ka) {
        intensity = ia.scale(ka);
    }

    /**
     * Getter for the intensity of the ambient light
     * @return the intensity of the ambient light
     */
    public Color getIntensity() {
        return intensity;
    }

}
