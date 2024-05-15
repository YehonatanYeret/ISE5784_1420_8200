package primitives;

/**
 * Class Ray is the basic class representing a ray in the 3D space
 */
public class Ray {
    private final Point head;
    private final Vector direction;

    /**
     * Constructor to initialize a ray based on a head point and a direction vector
     * @param head head point
     * @param direction direction vector
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && head.equals(other.head)
                && direction.equals(other.direction);
    }

    /**
     * Getter for the point on the ray at a certain distance from the head
     * @param t the distance from the head
     * @return the point on the ray at the distance t from the head
     */
    public Point getPoint(double t) {
        // if t is zero, return the head point
        if(Util.isZero(t))
            return head;
        return head.add(direction.scale(t));
    }

    @Override
    public String toString() {
        return "head=" + head + ", direction=" + direction;
    }

    /**
     * Getter for the head point of the ray
     * @return the head point of the ray
     */
    public Vector getDirection() {
        return direction;
    }
}
