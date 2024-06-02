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

        return new Scene(name).setGeometries(geometries).setBackground(background).setAmbientLight(new AmbientLight(ambientLight, ka));
    }

    /**
     * Parses a color from a string in the format "R G B".
     *
     * @param rgb the string representing the color
     * @return the parsed color
     */
    private static Color parseColor(String rgb) {
        String[] rgbArr = rgb.split(" ");
        return new Color(Double.parseDouble(rgbArr[0]), Double.parseDouble(rgbArr[1]), Double.parseDouble(rgbArr[2]));
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
            }
        }
        return geometries;
    }

    /**
     * Parses a sphere from a JSON object.
     *
     * @param sphereObj the JSON object representing a sphere
     * @return the parsed sphere
     */
    private static Sphere parseSphere(JSONObject sphereObj) {
        Point center = parsePoint((String) sphereObj.get("center"));
        double radius = Double.parseDouble((String) sphereObj.get("radius"));
        return new Sphere(radius, center);
    }

    /**
     * Parses a triangle (polygon) from a JSON object.
     *
     * @param triangleObj the JSON object representing a triangle
     * @return the parsed triangle
     */
    private static Polygon parseTriangle(JSONObject triangleObj) {
        JSONArray verticesArray = (JSONArray) triangleObj.get("vertices");
        Point[] vertices = new Point[verticesArray.size()];
        for (int i = 0; i < verticesArray.size(); i++) {
            vertices[i] = parsePoint((String) verticesArray.get(i));
        }
        return new Polygon(vertices);
    }

    /**
     * Parses a point from a string in the format "X Y Z".
     *
     * @param pointStr the string representing the point
     * @return the parsed point
     */
    private static Point parsePoint(String pointStr) {
        String[] coords = pointStr.split(" ");
        return new Point(
                Double.parseDouble(coords[0]),
                Double.parseDouble(coords[1]),
                Double.parseDouble(coords[2])
        );
    }
}
