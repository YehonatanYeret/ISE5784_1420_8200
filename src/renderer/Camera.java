package renderer;

import primitives.*;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


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

    private int amountOfRays_DOF = 1;// the number of rays in the grid for the depth of field
    private int amountOfRays_AA = 1; // the number of rays in the grid for the depth of field
    private double aperture = 0; // the radius of the circle of the camera
    private double depthOfField = 100; // the distance between the camera and the focus _focusPoint

    private int threadsCount = 0; // -2 auto, -1 range/stream, 0 no threads, 1+ number of threads
    private final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
    private double printInterval = 0; // printing progress percentage interval

    /**
     * Camera getter
     *
     * @return the location of the camera
     */
    public Point getP0() {
        return p0;
    }

    /**
     * Camera getter
     *
     * @return the up direction of the camera
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * Camera getter
     *
     * @return the direction of the camera
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * Camera getter
     *
     * @return the right direction of the camera
     */
    public Vector getVRight() {
        return vRight;
    }

    /**
     * Camera getter
     *
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
     * @return the aperture of the camera
     */
    public double getAperture() {
        return aperture;
    }

    /**
     * Camera getter
     *
     * @return the depth of field of the camera
     */
    public double getDepthOfField() {
        return depthOfField;
    }

    /**
     * Camera getter
     *
     * @return the number of rays in the grid for the depth of field
     */
    public int getAmountOfRaysDOF() {
        return amountOfRays_DOF;
    }

    /**
     * Camera getter
     *
     * @return the number of rays in the grid for the depth of field
     */
    public int getAmountOfRaysAA() {
        return amountOfRays_AA;
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
            if (!isZero(vTo.dotProduct(vUp))) {
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
         *
         * @param imageWriter the image writer
         * @return the camera builder
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Set the ray tracer
         *
         * @param rayTracer the ray tracer
         * @return the camera builder
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**
         * Set the number of rays in the grid for the depth of field
         *
         * @param n the number of rays in the grid
         * @return the camera builder
         */
        public Builder setAmountOfRaysDOF(int n) {
            camera.amountOfRays_DOF = n;
            return this;
        }

        /**
         * Set the aperture of the camera
         *
         * @param aperture the aperture of the camera
         * @return the camera builder
         */
        public Builder setAperture(double aperture) {
            camera.aperture = aperture;
            return this;
        }

        /**
         * Set the depth of field of the camera
         *
         * @param depthOfField the depth of field of the camera
         * @return the camera builder
         */
        public Builder setDepthOfField(double depthOfField) {
            camera.depthOfField = depthOfField;
            return this;
        }

        /**
         * Set the number of rays in the grid for the anti-aliasing
         *
         * @param n the number of rays in the grid
         * @return the camera builder
         */
        public Builder setAmountOfRaysAA(int n) {
            camera.amountOfRays_AA = n;
            return this;
        }

        /**
         * Set the amount of threads to use for rendering
         *
         * @param threads the amount of threads to use
         * @return the camera builder
         */
        public Builder setMultithreading(int threads) {
            if (threads < -2) throw new IllegalArgumentException("Multithreading must be -2 or higher");
            if (threads >= -1) camera.threadsCount = threads;
            else { // == -2
                int cores = Runtime.getRuntime().availableProcessors() - camera.SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            }
            return this;
        }

        /**
         * Set the interval for printing debug information
         *
         * @param interval the interval for printing debug information
         * @return the camera builder
         */
        public Builder setDebugPrint(double interval) {
            camera.printInterval = interval;
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

            if (camera.p0 == null) throw new MissingResourceException(description, className, "p0");
            if (camera.vUp == null) throw new MissingResourceException(description, className, "vUp");
            if (camera.vTo == null) throw new MissingResourceException(description, className, "vTo");
            if (camera.width == 0d) throw new MissingResourceException(description, className, "width");
            if (camera.height == 0d) throw new MissingResourceException(description, className, "height");
            if (camera.distance == 0d) throw new MissingResourceException(description, className, "distance");
            if (camera.imageWriter == null) throw new MissingResourceException(description, className, "imageWriter");
            if (camera.rayTracer == null) throw new MissingResourceException(description, className, "rayTracer");

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            if (!isZero(camera.vTo.dotProduct(camera.vRight)) || !isZero(camera.vTo.dotProduct(camera.vUp)) || !isZero(camera.vRight.dotProduct(camera.vUp)))
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
        if (!isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

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
        Pixel.initialize(ny, nx, printInterval);

        if (threadsCount == 0) {
            for (int i = 0; i < ny; ++i)
                for (int j = 0; j < nx; ++j)
                    castRay(nx, ny, j, i);
            return this;
        }
        List<Thread> threads = new LinkedList<>();
        int availableProcessors = threadsCount == -1 ? Runtime.getRuntime().availableProcessors()
                : threadsCount;

        for (int t = 0; t < availableProcessors; t++) {
            threads.add(new Thread(() -> {
                Pixel pixel;
                while ((pixel = Pixel.nextPixel()) != null)
                    castRay(nx, ny, pixel.col(), pixel.row());
            }));
        }
        for (var thread : threads)
            thread.start();
        try {
            for (var thread : threads)
                thread.join();
        } catch (InterruptedException ignore) {
        }

        return this;
    }

    /**
     * Print a grid on the image
     *
     * @param interval the interval between the lines of the grid
     * @param color    the color of the grid
     */
    public Camera printGrid(int interval, Color color) {
        for (int i = 0; i < imageWriter.getNy(); i++) {
            for (int j = 0; j < imageWriter.getNx(); j++) {
                if (i % interval == 0 || j % interval == 0) {
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
     *
     * @param nx the number of pixels in the x direction
     * @param ny the number of pixels in the y direction
     * @param i  the y index of the pixel
     * @param j  the x index of the pixel
     */
    private void castRay(int nx, int ny, int i, int j) {
        Ray mainRay = constructRay(nx, ny, j, i);
        Color accumulatedColor = Color.BLACK;

        // Anti-aliasing loop
        for (int k = 0; k < amountOfRays_AA; k++) {
            for (int l = 0; l < amountOfRays_AA; l++) {
                Ray aaRay = mainRay;
                if (amountOfRays_AA > 1) {
                    //anti-aliasing jitter
                    double xJitter = Util.random(-0.5, 0.5);
                    double yJitter = Util.random(-0.5, 0.5);

                    Point pIJ = p0;

                    double yI = -(i + (yJitter + k) / amountOfRays_AA - (ny - 1) / 2d) * height / ny;
                    double xJ = (j + (xJitter + l) / amountOfRays_AA - (nx - 1) / 2d) * width / nx;

                    if (!Util.isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
                    if (!Util.isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

                    pIJ = pIJ.add(vTo.scale(distance));
                    aaRay = new Ray(p0, pIJ.subtract(p0));
                }

                // Depth of field loop
                if (amountOfRays_DOF > 1 && aperture > 0) {
                    List<Ray> dofRays = constructRaysGridFromCamera(amountOfRays_DOF, aaRay);
                    Color dofAccumulatedColor = Color.BLACK;
                    for (Ray dofRay : dofRays) {
                        dofAccumulatedColor = dofAccumulatedColor.add(rayTracer.traceRay(dofRay));
                    }
                    accumulatedColor = accumulatedColor.add(dofAccumulatedColor.scale(1d / dofRays.size()));
                } else {
                    accumulatedColor = accumulatedColor.add(rayTracer.traceRay(aaRay));
                }
            }
        }
        Color averageColor = accumulatedColor.scale(1d / (amountOfRays_AA * amountOfRays_AA));
        imageWriter.writePixel(j, i, averageColor);
        Pixel.pixelDone();
    }


    /**
     * Construct a grid of rays from the camera
     *
     * @param n   the number of rays in the grid
     * @param ray the ray from the camera
     * @return the list of rays in the circle
     */
    public List<Ray> constructRaysGridFromCamera(int n, Ray ray) {
        List<Ray> myRays = new LinkedList<>(); //the list of all the rays

        double t = depthOfField / (vTo.dotProduct(ray.getDirection())); // distance from the focusPoint on the aperture grid to the focus focusPoint ( found with the cosinus)
        Point focusPoint = ray.getPoint(t); // we found the focus focusPoint

        double pixelSize = alignZero((aperture * 2) / n); // the size of each pixel

        // we construct a ray from each pixel of the grid, and we select only the rays in the circle
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Point tmpPoint = constructJitteredPoint(n, n, j, i, pixelSize); // we construct a point from a pixel
                Ray tmpRay = new Ray(tmpPoint, focusPoint.subtract(tmpPoint)); // we construct a ray from the point to the focus focusPoint
                if (tmpRay.getPoint(0).distanceSquared(p0) <= aperture * aperture) // we check if the ray is in the circle
                    myRays.add(tmpRay); // we add the ray to the list
            }
        }

        return myRays; // we return  the list of all my rays in the circle
    }

    /**
     * Construct a jittered ray from a pixel
     *
     * @param nX        the number of pixels in the x direction
     * @param nY        the number of pixels in the y direction
     * @param j         the x index of the pixel
     * @param i         the y index of the pixel
     * @param pixelSize the size of the pixel
     * @return the jittered point
     */
    private Point constructJitteredPoint(int nX, int nY, double j, double i, double pixelSize) {
        double jitterX = Util.random(-0.5, 0.5);
        double jitterY = Util.random(-0.5, 0.5);
        Point pIJ = p0;

        double yI = -(i + jitterY - (nY - 1) / 2d) * pixelSize;
        double xJ = (j + jitterX - (nX - 1) / 2d) * pixelSize;

        if (!isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));

        return pIJ;
    }
}
