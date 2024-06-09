package primitives;

/**
 * Material class represents the material of a geometry
 */
public class Material {
    /**
     * kD is the diffuse factor
     */
    public Double3 kD = Double3.ZERO;
    /**
     * kS is the specular factor
     */
    public Double3 kS = Double3.ZERO;
    /**
     * kT is the transparency factor
     */
    public int nShininess = 0;

    /**
     * Constructor for Material
     * @param kD the diffuse factor
     * @return the material
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Material setter
     * @param kD the diffuse factor
     * @return the material
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Material setter
     * @param kS the specular factor
     * @return the material
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Material setter
     * @param kS the specular factor
     * @return the material
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Material setter
     * @param nShininess the shininess factor
     * @return the material
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
