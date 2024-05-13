package geometries;

import primitives.Point;
import primitives.Ray;

/**
 * Abstract class RadialGeometry is the basic class representing a radial geometry in the 3D space
 */
public abstract class RadialGeometry implements Geometry {
    protected final double radius;

    /**
     * Constructor to initialize the radius of the geometry
     *
     * @param radius radius of the geometry
     */
    public RadialGeometry(double radius) {
        if (radius <= 0)
            throw new IllegalArgumentException("radius must be positive");
        this.radius = radius;
    }
}
