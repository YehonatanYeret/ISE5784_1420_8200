package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import primitives.*;
import geometries.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JsonScene class represents a scene in the 3D space and provides methods to import a scene from a JSON file.
 */
public class JsonScene {

    /**
     * Imports a scene from a JSON file.
     *
     * @param path the path to the JSON file
     * @param name the name of the scene
     * @return the scene imported from the JSON file
     * @throws IOException if there is an error reading the file
     * @throws ParseException if there is an error parsing the JSON
     */
    public static Scene importScene(String path, String name) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(path));
        JSONObject sceneObj = (JSONObject) jsonObject.get("scene");

        Color background = parseColor((String) sceneObj.get("background-color"));
        JSONObject ambientLightObj = (JSONObject) sceneObj.get("ambient-light");
        Color ambientLight = parseColor((String) ambientLightObj.get("color"));
        double ka = ((Number) ambientLightObj.get("ka")).doubleValue();

        Geometries geometries = new Geometries(parseGeometries((JSONArray) sceneObj.get("geometries")).toArray(new Geometry[0]));

        return new Scene(name)
                .setGeometries(geometries)
                .setBackground(background)
                .setAmbientLight(new AmbientLight(ambientLight, ka));
    }

    /**
     * Parses a list of geometries from a JSON array.
     *
     * @param geometriesArray the JSON array representing geometries
     * @return the list of parsed geometries
     */
    private static List<Geometry> parseGeometries(JSONArray geometriesArray) {
        List<Geometry> geometries = new ArrayList<>();
        for (Object obj : geometriesArray) {
            JSONObject geometryObj = (JSONObject) obj;

            if (geometryObj.containsKey("sphere")) {
                geometries.add(parseSphere((JSONObject) geometryObj.get("sphere")));
            } else if (geometryObj.containsKey("triangle")) {
                geometries.add(parseTriangle((JSONObject) geometryObj.get("triangle")));
            }else if(geometryObj.containsKey("polygon")) {
                geometries.add(parsePolygon((JSONObject) geometryObj.get("polygon")));
            }else if (geometryObj.containsKey("plane")) {
                geometries.add(parsePlane((JSONObject) geometryObj.get("plane")));
            }else{
                throw new IllegalArgumentException("Unknown geometry type");
            }
        }
        return geometries;
    }

    /**
     * Parses a polygon from a JSON object.
     * @param polygon the JSON object representing a polygon
     * @return the parsed polygon
     */
    private static Geometry parsePolygon(JSONObject polygon) {
        return new Polygon(parseVertices(polygon));
    }

    /**
     * Parses a sphere from a JSON object.
     *
     * @param sphereObj the JSON object representing a sphere
     * @return the parsed sphere
     */
    private static Sphere parseSphere(JSONObject sphereObj) {
        Point center = parsePoint((String) sphereObj.get("center"));
        double radius = ((Number) sphereObj.get("radius")).doubleValue();
        return new Sphere(radius, center);
    }

    /**
     * Parses a triangle (polygon) from a JSON object.
     *
     * @param triangleObj the JSON object representing a triangle
     * @return the parsed triangle
     */
    private static Polygon parseTriangle(JSONObject triangleObj) {
        Point[] points = parseVertices(triangleObj);
        return new Triangle(points[0], points[1], points[2]);
    }

    /**
     * Parses a plane from a JSON object.
     * @param planeObj the JSON object representing a plane
     * @return the parsed plane
     */
    private static Plane parsePlane(JSONObject planeObj) {
        Point point = parsePoint((String) planeObj.get("point"));
        Vector normal = parseVector((String) planeObj.get("normal"));
        return new Plane(point, normal);
    }

    /**
     * Parses an array of vertices from a JSON array.
     * @param vertices the JSON object representing vertices
     * @return the parsed array of vertices
     */
    private static Point[] parseVertices(JSONObject vertices) {
        JSONArray verticesArray = (JSONArray) vertices.get("vertices");
        Point[] points = new Point[verticesArray.size()];
        for (int i = 0; i < verticesArray.size(); i++) {
            points[i] = parsePoint((String) verticesArray.get(i));
        }
        return points;
    }

    /**
     * Parses coordinates from a string in the format "X Y Z".
     *
     * @param coordStr the string representing the coordinates
     * @return the array of parsed coordinates
     */
    private static double[] parseCoordinates(String coordStr) {
        return Arrays.stream(coordStr.split(" "))
                .mapToDouble(Double::parseDouble)
                .toArray();    }

    /**
     * Parses a color from a string in the format "R G B".
     *
     * @param rgb the string representing the color
     * @return the parsed color
     */
    private static Color parseColor(String rgb) {
        double[] colors = parseCoordinates(rgb);
        return new Color(colors[0], colors[1], colors[2]);
    }

    /**
     * Parses a vector from a string in the format "X Y Z".
     * @param vector the string representing the vector
     * @return the parsed vector
     */
    private static Vector parseVector(String vector) {
        double[] coords = parseCoordinates(vector);
        return new Vector(coords[0], coords[1], coords[2]);
    }

    /**
     * Parses a point from a string in the format "X Y Z".
     *
     * @param pointStr the string representing the point
     * @return the parsed point
     */
    private static Point parsePoint(String pointStr) {
        double[] coords = parseCoordinates(pointStr);
        return new Point(coords[0], coords[1], coords[2]);
    }

}
