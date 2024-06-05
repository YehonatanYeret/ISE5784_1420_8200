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

    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;

    /**
     * Camera getter
     * @return the location of the camera
     */
    public Point getP0() {
        return p0;
    }

    /**
     * Camera getter
     * @return the up direction of the camera
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * Camera getter
     * @return the direction of the camera
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * Camera getter
     * @return the right direction of the camera
     */
    public Vector getVRight() {
        return vRight;
    }

    /**
     * Camera getter
     * @return the width of the view plane
     */
    public double getWidth() {
        return width;
    }

    /**
     * Camera getter
     *
     * @return the height of the view plane
     */
    public double getHeight() {
        return height;
    }

    /**
     * Camera getter
     *
     * @return the distance between the camera and the view plane
     */
    public double getDistance() {
        return distance;
    }


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
         *            (the vector from the camera to the up direction)
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (!Util.isZero(vTo.dotProduct(vUp))) {
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
            if (width <= 0 || height <= 0) {
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
        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("distance from camera to view must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Set the image writer
         * @param imageWriter the image writer
         * @return the camera builder
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Set the ray tracer
         * @param rayTracer the ray tracer
         * @return the camera builder
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Build the camera
         *
         * @return the camera
         */
        public Camera build() {
            final String className = "Camera";
            final String description = "values not set: ";

            if(camera.p0 == null)
                throw new MissingResourceException(description, className, "p0");
            if(camera.vUp == null)
                throw new MissingResourceException(description, className, "vUp");
            if(camera.vTo == null)
                throw new MissingResourceException(description, className, "vTo");
            if(camera.width == 0d)
                throw new MissingResourceException(description, className, "width");
            if(camera.height == 0d)
                throw new MissingResourceException(description, className, "height");
            if(camera.distance == 0d)
                throw new MissingResourceException(description, className, "distance");
            if(camera.imageWriter == null)
                throw new MissingResourceException(description, className, "imageWriter");
            if(camera.rayTracer == null)
                throw new MissingResourceException(description, className, "rayTracer");

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            if (!Util.isZero(camera.vTo.dotProduct(camera.vRight)) ||
                    !Util.isZero(camera.vTo.dotProduct(camera.vUp)) ||
                    !Util.isZero(camera.vRight.dotProduct(camera.vUp)))
                throw new IllegalArgumentException("vTo, vUp and vRight must be orthogonal");

            if (camera.vTo.length() != 1 || camera.vUp.length() != 1 || camera.vRight.length() != 1)
                throw new IllegalArgumentException("vTo, vUp and vRight must be normalized");

            if (camera.width <= 0 || camera.height <= 0)
                throw new IllegalArgumentException("width and height must be positive");

            if (camera.distance <= 0)
                throw new IllegalArgumentException("distance from camera to view must be positive");

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Camera constructor
     */
    private Camera() {
    }

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
        Point pIJ = p0;
        double yI = -(i - (nY - 1) / 2d) * height / nY;
        double xJ = (j - (nX - 1) / 2d) * width / nX;

        //check if xJ or yI are not zero, so we will not add zero vector
        if (!Util.isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!Util.isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

        // we need to move the point in the direction of vTo by distance
        pIJ = pIJ.add(vTo.scale(distance));

        return new Ray(p0, pIJ.subtract(p0).normalize());
    }

    /**
     * Render the image
     */
    public Camera renderImage() {
        int ny = imageWriter.getNy();
        int nx = imageWriter.getNx();
        for (int i = 0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                castRay(nx, ny, i, j);
            }
        }
        return this;
    }

    /**
     * Print a grid on the image
     * @param interval the interval between the lines of the grid
     * @param color the color of the grid
     */
    public Camera printGrid(int interval, Color color) {
        for(int i = 0; i < imageWriter.getNy(); i++) {
            for(int j = 0; j < imageWriter.getNx(); j++) {
                if(i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Write the image to a file
     */
    public void writeToImage() {
        imageWriter.writeToImage();
    }

    /**
     * Cast a ray through a pixel
     * @param nx the number of pixels in the x direction
     * @param ny the number of pixels in the y direction
     * @param i the y index of the pixel
     * @param j the x index of the pixel
     */
    private void castRay(int nx, int ny, int i, int j) {
        Ray ray = constructRay(nx, ny, j, i);
        Color color = rayTracer.traceRay(ray);
        imageWriter.writePixel(j, i, color);

    }
}
