package primitives;

import java.util.Objects;

/**
 * Class Point is the basic class representing a point in the 3D space
 * @author Maor
 */
public class Point {
    protected final Double3 xyz;

    /**
     * Zero point (0,0,0)
     */
    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Constructor to initialize a point based on three coordinates
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    /**
     * Constructor to initialize a point based on a Double3
     * @param xyz coordinates
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Subtract a point from a point
     * @param p1 point to copy
     * @return new vector
     */
    public Vector subtract(Point p1){
        return new Vector(this.xyz.subtract(p1.xyz));
    }

    /**
     * Add a vector to a point
     * @param v vector to add
     * @return new point
     */
    public Point add(Vector v) {
        return new Point(this.xyz.add(v.xyz));
    }

    /**
     * Calculate the squared distance between two points
     * @param p1 point to calculate the distance to
     * @return squared distance
     */
    public double distanceSquared(Point p1) {
        Double3 temp = this.xyz.subtract(p1.xyz);
        temp = temp.product(temp);
        return temp.d1 + temp.d2 + temp.d3;
    }

    /**
     * Calculate the distance between two points
     * @param p1 point to calculate the distance to
     * @return distance
     */
    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point point)) return false;
        return xyz.equals(point.xyz);
    }

    @Override
    public String toString() {
        return "xyz=" + xyz;
    }
}
