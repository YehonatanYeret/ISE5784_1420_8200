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


    @Override
    public String toString() {
        return "head=" + head + ", direction=" + direction;
    }
}
