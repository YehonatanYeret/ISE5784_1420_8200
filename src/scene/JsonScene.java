package scene;

import geometries.Geometries;
import lighting.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import primitives.*;
import geometries.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * JsonScene class represents a scene in the 3D space and provides methods to import a scene from a JSON file.
 */
public class JsonScene {

    /**
     * Imports a scene from a JSON file.
     *
     * @param path the path to the JSON file
     * @return the scene imported from the JSON file
     * @throws IOException    if there is an error reading the file
     * @throws ParseException if there is an error parsing the JSON
     */
    public static Scene importScene(String path) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(path));
        JSONObject sceneObj = (JSONObject) jsonObject.get("scene");

        String name = (String) sceneObj.get("name");
        Scene scene = new Scene(name);
        if(sceneObj.containsKey("background-color"))
            scene.setBackground(parseColor((String) sceneObj.get("background-color")));
        if(sceneObj.containsKey("ambient-light"))
        {
            JSONObject ambientLightObj = (JSONObject) sceneObj.get("ambient-light");
            Color ambientLight = parseColor((String) ambientLightObj.get("color"));
            double ka = ((Number) ambientLightObj.get("ka")).doubleValue();
            scene.setAmbientLight(new AmbientLight(ambientLight, ka));
        }
        if(sceneObj.containsKey("geometries"))
            scene.setGeometries(parseGeometries((JSONArray) sceneObj.get("geometries")));

        if(sceneObj.containsKey("lights"))
            scene.setLights(parseLights((JSONArray) sceneObj.get("lights")));

        return scene;
    }

    private static List<LightSource> parseLights(JSONArray lights) {
        List<LightSource> lightSources = new LinkedList<>();
        for (Object obj : lights) {
            JSONObject lightObj = (JSONObject) obj;
            if (lightObj.containsKey("point")) {
                lightSources.add(parsePointLight((JSONObject) lightObj.get("point")));
            } else if (lightObj.containsKey("directional")) {
                lightSources.add(parseDirectionalLight((JSONObject) lightObj.get("directional")));
            } else if (lightObj.containsKey("spot")) {
                lightSources.add(parseSpotLight((JSONObject) lightObj.get("spot")));
            } else {
                throw new IllegalArgumentException("Unknown light type");
            }
        }
        return lightSources;
    }

    private static LightSource parseSpotLight(JSONObject lightObj) {
        Color color = parseColor((String) lightObj.get("color"));
        Point position = parsePoint((String) lightObj.get("position"));
        Vector direction = parseVector((String) lightObj.get("direction"));
        SpotLight spotLight = new SpotLight(color, direction, position);
        if (lightObj.containsKey("kc")) {
            spotLight.setKc(((Number) lightObj.get("kc")).doubleValue());
        }
        if (lightObj.containsKey("kl")) {
            spotLight.setKl(((Number) lightObj.get("kl")).doubleValue());
        }
        if (lightObj.containsKey("kq")) {
            spotLight.setKq(((Number) lightObj.get("kq")).doubleValue());
        }
        if (lightObj.containsKey("narrow-beam")) {
            spotLight.setNarrowBeam(((Number) lightObj.get("narrow-beam")).doubleValue());
        }
        return spotLight;
    }

    private static LightSource parseDirectionalLight(JSONObject lightObj) {
        Color color = parseColor((String) lightObj.get("color"));
        Vector direction = parseVector((String) lightObj.get("direction"));
        return new DirectionalLight(color, direction);
    }

    private static LightSource parsePointLight(JSONObject lightObj) {
        Color color = parseColor((String) lightObj.get("color"));
        Point position = parsePoint((String) lightObj.get("position"));
        PointLight pointLight = new PointLight(color, position);
        if (lightObj.containsKey("kc")) {
            pointLight.setKc(((Number) lightObj.get("kc")).doubleValue());
        }
        if (lightObj.containsKey("kl")) {
            pointLight.setKl(((Number) lightObj.get("kl")).doubleValue());
        }
        if (lightObj.containsKey("kq")) {
            pointLight.setKq(((Number) lightObj.get("kq")).doubleValue());
        }
        return pointLight;
    }

    private static Geometries parseGeometries(JSONArray geometriesArray) {
        Geometries geometries = new Geometries();
        for (Object obj : geometriesArray) {
            JSONObject geometryObj = (JSONObject) obj;
            Geometry geometry;
            if (geometryObj.containsKey("sphere")) {
                geometry = parseSphere((JSONObject) geometryObj.get("sphere"));
            } else if (geometryObj.containsKey("triangle")) {
                geometry = parseTriangle((JSONArray) geometryObj.get("triangle"));
            } else if (geometryObj.containsKey("plane")) {
                geometry = parsePlane((JSONObject) geometryObj.get("plane"));
            } else if (geometryObj.containsKey("polygon")) {
                geometry = parsePolygon((JSONArray) geometryObj.get("polygon"));
            } else if (geometryObj.containsKey("cylinder")) {
                geometry = parseCylinder((JSONObject) geometryObj.get("cylinder"));
            } else if (geometryObj.containsKey("tube")) {
                geometry = parseTube((JSONObject) geometryObj.get("tube"));
            } else {
                throw new IllegalArgumentException("Unknown geometry type");
            }

            if (geometryObj.containsKey("material"))
                parseMaterial(geometryObj, geometry);

            if(geometryObj.containsKey("emission"))
                geometry.setEmission(parseColor((String) geometryObj.get("emission")));

            geometries.add(geometry);
        }
        return geometries;
    }

    private static void parseMaterial(JSONObject geometryObj, Geometry geometry) {
        JSONObject materialObj = (JSONObject) geometryObj.get("material");
        Material material = new Material();
        if (materialObj.containsKey("kd")) {
            material.setKd(((Number) materialObj.get("kd")).doubleValue());
        }
        if (materialObj.containsKey("ks")) {
            material.setKs(((Number) materialObj.get("ks")).doubleValue());
        }
        if (materialObj.containsKey("ns")) {
            material.setShininess(((Number) materialObj.get("ns")).intValue());
        }
        geometry.setMaterial(material);
    }

    private static Geometry parseTube(JSONObject tube) {
        double radius = ((Number) tube.get("radius")).doubleValue();
        Ray axis = parseRay((JSONObject) tube.get("axis"));
        return new Tube(axis, radius);
    }

    private static Geometry parseCylinder(JSONObject cylinder) {
        double radius = ((Number) cylinder.get("radius")).doubleValue();
        double height = ((Number) cylinder.get("height")).doubleValue();
        Ray axis = parseRay((JSONObject) cylinder.get("axis"));
        return new Cylinder(axis, radius, height);
    }

    private static Ray parseRay(JSONObject axis) {
        Point point = parsePoint((String) axis.get("origin"));
        Vector direction = parseVector((String) axis.get("direction"));
        return new Ray(point, direction);
    }

    private static Geometry parsePolygon(JSONArray polygon) {
        return new Polygon(parseVertices(polygon));
    }

    private static Geometry parseSphere(JSONObject sphereObj) {
        Point center = parsePoint((String) sphereObj.get("center"));
        double radius = ((Number) sphereObj.get("radius")).doubleValue();
        return new Sphere(radius, center);
    }

    private static Geometry parseTriangle(JSONArray triangleObj) {
        Point[] points = parseVertices(triangleObj);
        return new Triangle(points[0], points[1], points[2]);
    }

    private static Geometry parsePlane(JSONObject planeObj) {
        Point point = parsePoint((String) planeObj.get("point"));
        Vector normal = parseVector((String) planeObj.get("normal"));
        return new Plane(point, normal);
    }

    private static Point[] parseVertices(JSONArray vertices) {
        Point[] points = new Point[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            points[i] = parsePoint((String) vertices.get(i));
        }
        return points;
    }

    private static double[] parseCoordinates(String coordStr) {
        return Arrays.stream(coordStr.split(" "))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    private static Color parseColor(String rgb) {
        double[] colors = parseCoordinates(rgb);
        return new Color(colors[0], colors[1], colors[2]);
    }

    private static Vector parseVector(String vector) {
        double[] coords = parseCoordinates(vector);
        return new Vector(coords[0], coords[1], coords[2]);
    }

    private static Point parsePoint(String pointStr) {
        double[] coords = parseCoordinates(pointStr);
        return new Point(coords[0], coords[1], coords[2]);
    }
}