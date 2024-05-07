package primitives;

public class Point {
    protected Double3 xyz;
    public static final Point ZERO = new Point(0, 0, 0);

    public Point(double x, double y, double z) {
        this.xyz = new Double3(x, y, z);
    }

    public Point(Double3 xyz) {
        this(xyz.d1, xyz.d2, xyz.d3);
    }

    public Vector subtract(Point p1){
        //TODO: implement
        return null;
    }

    public Point add(Vector v) {
    //TODO: implement
        return null;
    }

    public double distanceSquared(Point p1) {
        Double3 temp = this.xyz.subtract(p1.xyz);
        temp = temp.product(temp);
        return temp.d1 + temp.d2 + temp.d3;
    }

    public double distance(Point p1) {
        return Math.sqrt(distanceSquared(p1));
    }
}
