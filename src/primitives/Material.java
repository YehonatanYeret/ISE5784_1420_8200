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
     * kR is the reflection factor
     */
    public Double3 kR = Double3.ZERO;

    /**
     * kT is the transparency factor
     */
    public Double3 kT = Double3.ZERO;

    /**
     * setter for transparency factor
     * @param kt the transparency factor
     * @return the material
     */
    public  Material setKt(Double3 kt){
        this.kT = kt;
        return this;
    }

    /**
     * setter for transparency factor
     * @param kt the transparency factor
     * @return the material
     */
    public Material setKt(double kt){
        this.kT = new Double3(kt);
        return this;
    }

    /**
     * setter for reflection factor
     * @param kr the reflection factor
     * @return the material
     */
    public  Material setKr(Double3 kr){
        this.kR = kr;
        return this;
    }

    /**
     * setter for reflection factor
     * @param kr the reflection factor
     * @return the material
     */
    public Material setKr(double kr){
        this.kR = new Double3(kr);
        return this;
    }

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
