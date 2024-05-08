package geometries;

/**
 * Abstract class RadialGeometry is the basic class representing a radial geometry in the 3D space
 */
public abstract class RadialGeometry implements Geometry{
    protected double radius;

    /**
     * Constructor to initialize the radius of the geometry
     * @param radius radius of the geometry
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }


}