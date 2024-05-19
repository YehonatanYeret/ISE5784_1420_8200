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
     * add a vector to a vector
     * @param v vector to add
     * @return new vector that is the sum of the two vectors
     */
    public Vector add(Vector v) {
        return new Vector(this.xyz.add(v.xyz));
    }

    /**
     * Multiply a vector by a scalar
     * @param scalar scalar to multiply
     * @return new vector scaled by scalar
     */
    public Vector scale(double scalar) {
        return new Vector(this.xyz.scale(scalar));
    }

    /**
     * dot product of two vectors
     * @param v vector to dot
     * @return dot product of the two vectors
     */
    public double dotProduct(Vector v) {
        return this.xyz.d1 * v.xyz.d1 + this.xyz.d2 * v.xyz.d2 + this.xyz.d3 * v.xyz.d3;
    }

    /**
     * Calculate the crossProduct product of two vectors
     * @param v vector to crossProduct
     * @return new vector that is the crossProduct product of the two vectors
     */
    public Vector crossProduct(Vector v) {
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
     * @return new vector that is the normalized vector
     */
    public Vector normalize() {
        return new Vector(this.xyz.reduce(this.length()));
    }

    @Override
    public String toString() {
        return "->" + super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj instanceof Vector other && super.equals(other);
    }
}
