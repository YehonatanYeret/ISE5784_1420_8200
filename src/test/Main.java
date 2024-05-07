package test;

import static java.lang.System.out;

import static primitives.Util.isZero;

import primitives.*;

/**
 * Test program for the 1st stage
 * @author Dan Zilberstein
 */
public final class Main {

   /**
    * Main program to tests initial functionality of the 1st stage
    * @param args irrelevant here
    */
   public static void main(String[] args) {
      Point  p1         = new Point(1, 2, 3);
      Point  p2         = new Point(2, 4, 6);
      Point  p3         = new Point(2, 4, 5);

      Vector v1         = new Vector(1, 2, 3);
      Vector v1Opposite = new Vector(-1, -2, -3);
      Vector v2         = new Vector(-2, -4, -6);
      Vector v3         = new Vector(0, 3, -2);
      Vector v4         = new Vector(1, 2, 2);

      // test zero vector =====================================================
      try {
         new Vector(0, 0, 0);
         new Vector(Double3.ZERO);
         out.println("ERROR: zero vector does not throw an exception");
      } catch (IllegalArgumentException ignore) {} catch (Exception ignore) {
         out.println("ERROR: zero vector throws wrong exception");
      }

      // Test point operations ================================================

      // Subtract points
      if (!p2.subtract(p1).equals(v1))
         out.println("ERROR: (point2 - point1) does not work correctly");
      try {
         p1.subtract(p1);
         out.println("ERROR: (point - itself) does not throw an exception");
      } catch (IllegalArgumentException ignore) {} catch (Exception ignore) {
         out.println("ERROR: (point - itself) throws wrong exception");
      }

      // Add vector to point
      if (!(p1.add(v1).equals(p2)))
         out.println("ERROR: (point + vector) = other point does not work correctly");
      if (!(p1.add(v1Opposite).equals(Point.ZERO)))
         out.println("ERROR: (point + vector) = center of coordinates does not work correctly");

      // distances
      if (!isZero(p1.distanceSquared(p1)))
         out.println("ERROR: point squared distance to itself is not zero");
      if (!isZero(p1.distance(p1)))
         out.println("ERROR: point distance to itself is not zero");
      if (!isZero(p1.distanceSquared(p3) - 9))
         out.println("ERROR: squared distance between points is wrong");
      if (!isZero(p3.distanceSquared(p1) - 9))
         out.println("ERROR: squared distance between points is wrong");
      if (!isZero(p1.distance(p3) - 3))
         out.println("ERROR: distance between points to itself is wrong");
      if (!isZero(p3.distance(p1) - 3))
         out.println("ERROR: distance between points to itself is wrong");

      // Test Vector operations ===============================================

      // test length
      if (!isZero(v4.lengthSquared() - 9))
         out.println("ERROR: lengthSquared() wrong value");
      if (!isZero(v4.length() - 3))
         out.println("ERROR: length() wrong value");

      // Test add & subtract
      try {
         v1.add(v1Opposite);
         out.println("ERROR: Vector + -itself does not throw an exception");
      } catch (IllegalArgumentException ignore) {} catch (Exception ignore) {
         out.println("ERROR: Vector + itself throws wrong exception");
      }
      try {
         v1.subtract(v1);
         out.println("ERROR: Vector - itself does not throw an exception");
      } catch (IllegalArgumentException ignore) {} catch (Exception ignore) {
         out.println("ERROR: Vector + itself throws wrong exception");
      }
      if (!v1.add(v2).equals(v1Opposite))
         out.println("ERROR: Vector + Vector does not work correctly");
      if (!v1.subtract(v2).equals(new Vector(3, 6, 9)))
         out.println("ERROR: Vector + Vector does not work correctly");

      // test Dot-Product
      if (!isZero(v1.dotProduct(v3)))
         out.println("ERROR: dotProduct() for orthogonal vectors is not zero");
      if (!isZero(v1.dotProduct(v2) + 28))
         out.println("ERROR: dotProduct() wrong value");

      // test Cross-Product
      try { // test zero vector
         v1.crossProduct(v2);
         out.println("ERROR: crossProduct() for parallel vectors does not throw an exception");
      } catch (Exception e) {}
      Vector vr = v1.crossProduct(v3);
      if (!isZero(vr.length() - v1.length() * v3.length()))
         out.println("ERROR: crossProduct() wrong result length");
      if (!isZero(vr.dotProduct(v1)) || !isZero(vr.dotProduct(v3)))
         out.println("ERROR: crossProduct() result is not orthogonal to its operands");

      // test vector normalization vs vector length and cross-product
      Vector v = new Vector(1, 2, 3);
      Vector u = v.normalize();
      if (!isZero(u.length() - 1))
         out.println("ERROR: the normalized vector is not a unit vector");
      try { // test that the vectors are co-lined
         v.crossProduct(u);
         out.println("ERROR: the normalized vector is not parallel to the original one");
      } catch (Exception e) {}
      if (v.dotProduct(u) < 0)
         out.println("ERROR: the normalized vector is opposite to the original one");

      out.println("If there were no any other outputs - all tests succeeded!");
   }
}
