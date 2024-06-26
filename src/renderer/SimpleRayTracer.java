package renderer;

import geometries.Intersectable.GeoPoint;
import geometries.Triangle;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

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
        Color color = calcLocalEffects(geoPoint, ray, k);
        return level == 1 ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }

    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray, Double3 k) {
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

            if (nl * nv > 0d) {
                Double3 ktr = transparency(geoPoint, lightSource, l, n);
                if(ktr.product(k).greaterThan(MIN_CALC_COLOR_K)){
                    Color lightIntensity = lightSource.getIntensity(point).scale(ktr);
                    color = color.add(calcDiffusive(kD, nl, lightIntensity))
                            .add(calcSpecular(kS, l, n, nl, direction, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }

    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Material material = gp.geometry.getMaterial();
        return calcGlobalEffect(constractReflectedRay(gp, ray), material.kR, level, k)
                .add(calcGlobalEffect(constractRefractedRay(gp, ray), material.kT, level, k));
    }

    private Ray constractRefractedRay(GeoPoint gp, Ray ray) {
        return new Ray(gp.point, ray.getDirection(), gp.geometry.getNormal(gp.point));
    }

    private Ray constractReflectedRay(GeoPoint gp, Ray ray) {
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        double vn = v.dotProduct(n);

        // Ensure the normal vector is correctly oriented
        if (vn > 0) {
            n = n.scale(-1);
            vn = -vn;
        }

        Vector r = v.subtract(n.scale(2 * vn));

        return new Ray(gp.point, r, n);
    }

    private Color calcGlobalEffect(Ray ray, Double3 kx, int level, Double3 k) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        return gp == null ? scene.background : calcColor(gp, ray, level - 1, kkx).scale(kx);
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

//    private boolean unshaded(GeoPoint gp, LightSource lightSource, Vector l, Vector n, double nl) {
//        Ray lightRay = new Ray(gp.point, l.scale(-1), n);
//        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, lightSource.getDistance(gp.point));
//
//
//        if (intersections == null)
//            return true;
//
//        for(GeoPoint point : intersections){
//            if (point.geometry.getMaterial().kT.equals(Double3.ZERO))
//                return false;
//        }
//        return true;
//
//    }

    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) return null;
        return ray.findClosestGeoPoint(intersections);
    }

    private Double3 transparency(GeoPoint geoPoint, LightSource ls, Vector l, Vector n){
        Ray lightRay = new Ray(geoPoint.point, l.scale(-1), n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, ls.getDistance(geoPoint.point));
        if (intersections == null) return Double3.ONE;

        Double3 ktr = Double3.ONE;
        for (GeoPoint gp : intersections) {
            ktr = ktr.product(gp.geometry.getMaterial().kT);

            // If the intensity of the light ray is too small, the object is opaque
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
        }
        return ktr;


    }
}