package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

/**
 * SimpleRayTracer class is the basic class for ray tracing
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;
    private static final Double3 INITIAL_K = Double3.ONE;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;

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
        GeoPoint closestPoint = findClosestIntersection(ray);
        if (closestPoint == null) return Color.BLACK;
        return calcColor(closestPoint, ray);
    }

    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return calcColor(geoPoint, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
    }

    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(geoPoint, ray);
        return level == 1 ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }

    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray) {
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        Vector direction = ray.getDirection();

        Color color = geoPoint.geometry.getEmission();

        Point point = geoPoint.point;

        //store the values of the material
        int nShininess = geoPoint.geometry.getMaterial().nShininess;
        Double3 kD = geoPoint.geometry.getMaterial().kD;
        Double3 kS = geoPoint.geometry.getMaterial().kS;

        double nv = Util.alignZero(n.dotProduct(direction));
        if (nv == 0d) return Color.BLACK;

        // Calculate the color of the point by adding the diffusive and specular components
        for (var lightSource : scene.lights) {
            Vector l = lightSource.getL(point).normalize();
            double nl = n.dotProduct(l);

            if (nl * nv > 0d && unshaded(geoPoint, lightSource, l, n, nl)) {
                Color lightIntensity = lightSource.getIntensity(geoPoint.point);
                color = color.add(calcDiffusive(kD, nl, lightIntensity))
                        .add(calcSpecular(kS, l, n, nl, direction, nShininess, lightIntensity));
            }
        }
        return color;
    }

    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Material material = gp.geometry.getMaterial();
        return calcGlobalEffect(constractReflectedRay(gp, ray), material.kR, level, k)
                .add(calcGlobalEffect(constractReflectedRay(gp, ray), material.kT, level, k));
    }

    private Ray constractReflectedRay(GeoPoint gp, Ray ray) {
        double nv = Util.alignZero(gp.geometry.getNormal(gp.point).dotProduct(ray.getDirection()));
        Vector r = ray.getDirection().subtract(gp.geometry.getNormal(gp.point).scale(2 * nv));
        return new Ray(gp.point, gp.geometry.getNormal(gp.point), r);
    }

    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        return gp == null ? scene.background.scale(kx) : calcColor(gp, ray, level - 1, kkx).scale(kx);
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

    private boolean unshaded(GeoPoint gp, LightSource lightSource, Vector l, Vector n, double nl) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector delta = n.scale(nl < 0 ? DELTA : -DELTA);
        Ray lightRay = new Ray(gp.point.add(delta), lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, lightSource.getDistance(gp.point));


        if (intersections == null)
            return true;

        for(GeoPoint point : intersections){
            if (point.geometry.getMaterial().kT.equals(Double3.ZERO))
                return false;
        }
        return true;

    }

    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) return null;
        return ray.findClosestGeoPoint(intersections);
    }
}
