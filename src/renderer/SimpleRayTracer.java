package renderer;

import geometries.Intersectable.GeoPoint;
import primitives.*;
import scene.Scene;

/**
 * SimpleRayTracer class is the basic class for ray tracing
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Constructor to initialize the scene
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        var intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) return scene.background;
        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
        return calcColor(closestPoint, ray.getDirection());
    }

    private Color calcColor(GeoPoint point, Vector direction) {
        Color color = point.geometry.getEmission().add(scene.ambientLight.getIntensity()); // Ia * ka + Ie
        Vector n = point.geometry.getNormal(point.point);

        //store the values of the material
        int nShininess = point.geometry.getMaterial().nShininess;
        Double3 kD = point.geometry.getMaterial().kD;
        Double3 kS = point.geometry.getMaterial().kS;

        double nv = Util.alignZero(n.dotProduct(direction));
        if (nv == 0d) return Color.BLACK;

        // Calculate the color of the point by adding the diffusive and specular components
        for (var lightSource : scene.lights) {
            Vector l = lightSource.getL(point.point).normalize();
            double nl = n.dotProduct(l);

            if (Util.alignZero(nl * nv) > 0d) { // Only if nl and nv have the same sign
                Color lightIntensity = lightSource.getIntensity(point.point);
                color = color.add(calcDiffusive(kD, nl, lightIntensity))
                        .add(calcSpecular(kS, l, n, nl, direction, nShininess, lightIntensity));
            }
        }

        return color;
    }

    private Color calcSpecular(Double3 ks, Vector l, Vector n, double nl, Vector v, int nShininess, Color lightIntensity) {
        //calculate the reflection vector
        Vector r = l.subtract(n.scale(2d * nl));
        double vr = Util.alignZero(v.scale(-1d).dotProduct(r));
        if (vr <= 0d) return Color.BLACK;
        //calculate the specular component
        return lightIntensity.scale(ks.scale(Math.pow(vr, nShininess)));
    }

    private Color calcDiffusive(Double3 kd, double nl, Color lightIntensity) {
        //calculate the diffusive component
        return lightIntensity.scale(kd.scale(Math.abs(nl)));
    }
}
