package primitives;

/**
 * Class Vector is the basic class representing a vector in the 3D space
 */
public class Vector extends Point {

    /**
     * Constructor to initialize a vector based on three coordinates
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be zero vector");
        }
    }

    /**
     * Constructor to initialize a vector based on a Double3
     * @param xyz coordinates
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector cannot be zero vector");
        }
    }

    /**
     * Copy constructor for a vector
     * @param v vector to copy
     */
    public Vector add(Vector v) {
        return new Vector(this.xyz.add(v.xyz));
    }

    /**
     * Subtract a vector from a vector
     * @param v vector to subtract
     * @return new vector
     */
    public Vector scale(double scalar) {
        return new Vector(this.xyz.scale(scalar));
    }

    /**
     * Calculate the squared length of a vector
     * @return squared length
     */
    public double dotProduct(Vector v) {
        return this.xyz.d1 * v.xyz.d1 + this.xyz.d2 * v.xyz.d2 + this.xyz.d3 * v.xyz.d3;
    }

    /**
     * Calculate the cross product of two vectors
     * @param v vector to cross
     * @return new vector
     */
    public Vector cross(Vector v) {
        return new Vector(this.xyz.d2 * v.xyz.d3 - this.xyz.d3 * v.xyz.d2,
                this.xyz.d3 * v.xyz.d1 - this.xyz.d1 * v.xyz.d3,
                this.xyz.d1 * v.xyz.d2 - this.xyz.d2 * v.xyz.d1);
    }

    /**
     * Calculate the squared length of a vector
     * @return squared length
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Calculate the length of a vector
     * @return length
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalize a vector
     * @return new vector
     */
    public Vector normalize() {
        return new Vector(this.xyz.reduce(this.length()));
    }
}
