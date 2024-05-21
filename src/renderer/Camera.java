package renderer;

import primitives.*;

import java.util.MissingResourceException;


/**
 * Camera class represents a camera in the 3D space
 */
public class Camera implements Cloneable {
    private Point p0;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;

    private double width = 0d;
    private double height = 0d;
    private double distance = 0d;

    /**
     * Camera builder
     */
    public static class Builder {
        private final Camera camera = new Camera();

        /**
         * Set the location of the camera
         *
         * @param p0 the location of the camera
         */
        public Builder setLocation(Point p0) {
            camera.p0 = p0;
            return this;
        }

        /**
         * Set the direction of the camera
         *
         * @param vTo the direction of the camera
         *            (the vector from the camera to the "look-at" point)
         * @param vUp the up direction of the camera
*                     (the vector from the camera to the up direction)
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (Util.isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Set the size of the view plane
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         */
        public Builder setVpSize(double width, double height) {
            if(width <= 0 || height <= 0) {
                throw new IllegalArgumentException("width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Set the distance between the camera and the view plane
         *
         * @param distance the distance between the camera and the view plane
         */
        public Builder setVPDistance(double distance) {
            if(distance <= 0) {
                throw new IllegalArgumentException("distance from camera to view must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Build the camera
         *
         * @return the camera
         */
        public Camera build() {
            String className = "Camera";
            String description = "values not set";

            if(camera.p0 == null)
                throw new MissingResourceException(className, description, "p0");
            if(camera.vUp == null)
                throw new MissingResourceException(className, description, "vUp");
            if(camera.vTo == null)
                throw new MissingResourceException(className, description, "vTo");
            if(camera.width == 0d)
                throw new MissingResourceException(className, description, "width");
            if(camera.height == 0d)
                throw new MissingResourceException(className, description, "height");
            if(camera.distance == 0d)
                throw new MissingResourceException(className, description, "distance");

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            if(Util.isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    Util.isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    Util.isZero(camera.vRight.dotProduct(camera.vUp)))
                throw new IllegalArgumentException("vTo, vUp and vRight must be orthogonal");

            if(camera.vTo.length() != 1 || camera.vUp.length() != 1 || camera.vRight.length() != 1)
                throw new IllegalArgumentException("vTo, vUp and vRight must be normalized");

            if(camera.width <= 0 || camera.height <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            if(camera.distance <= 0)
                throw new IllegalArgumentException("distance from camera to view must be positive");

            try {
                return  (Camera)camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Camera constructor
     */
    private Camera() {}

    /**
     * Builder getter
     *
     * @return the camera builder
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * construct a ray through a pixel
     *
     * @param nX the number of pixels in the x direction
     * @param nY the number of pixels in the y direction
     * @param j  the x index of the pixel
     * @param i  the y index of the pixel
     * @return the ray that passes through the pixel
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        return null;
    }


}
