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
        //can be problematic if the param can be changed from outside
        this.head = head;
        this.direction = direction;
    }
}
