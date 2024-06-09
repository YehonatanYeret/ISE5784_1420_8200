package lighting;

import primitives.Color;

/**
 * Abstract class Light is the basic class representing a light source in the 3D space.
 */
public abstract class Light {
    /**
     * the intensity of the light
     */
    protected Color intensity;

    /**
     * Constructor for a Light object receiving a Color.
     *
     * @param intensity the intensity of the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the intensity field.
     *
     * @return the intensity of the light
     */
    public Color getIntensity() {
        return intensity;
    }
}
