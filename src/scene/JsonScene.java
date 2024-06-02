package scene;

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
 * JsonScene class represents a scene in the 3D space
 */
public class JsonScene {

    /**
     * Import a scene from a json file
     * @param path the path to the json file
     * @return the scene imported from the json file
     */
    public static Scene importScene(String path, String name) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(path));
        JSONObject sceneObj = (JSONObject) jsonObject.get("scene");

        // Get the background color from the json object
        String rgb = (String) sceneObj.get("background-color");
        String[] rgbArr = rgb.split(" ");
        Color background = new Color(Double.parseDouble(rgbArr[0]), Double.parseDouble(rgbArr[1]), Double.parseDouble(rgbArr[2]));

        // Get the ambient light from the json object
        JSONObject ambientLightObj = (JSONObject) sceneObj.get("ambient-light");
        rgb = (String) ambientLightObj.get("color");
        rgbArr = rgb.split(" ");

        AmbientLight ambientLight = new AmbientLight(new Color(Double.parseDouble(rgbArr[0]), Double.parseDouble(rgbArr[1]), Double.parseDouble(rgbArr[2])),(double)(ambientLightObj.get("ka")));

        // Get the objects from the json object
        JSONArray geometries = (JSONArray) sceneObj.get("geometries");
        List<Geometry> geometryList = new ArrayList<>();
        for (Object obj : geometries) {
            JSONObject geometryObj = (JSONObject) obj;

            if (geometryObj.containsKey("polygon")) {
                JSONObject polygonObj = (JSONObject) geometryObj.get("polygon");
                JSONArray verticesArray = (JSONArray) polygonObj.get("vertices");
                List<Point> vertices = new ArrayList<>();
                for (Object vertex : verticesArray) {
                    String[] vertexCoords = ((String) vertex).split(" ");
                    vertices.add(new Point(
                            Double.parseDouble(vertexCoords[0]),
                            Double.parseDouble(vertexCoords[1]),
                            Double.parseDouble(vertexCoords[2])
                    ));
                }
                geometryList.add(new Polygon(vertices.toArray(new Point[0])));
            } else if (geometryObj.containsKey("sphere")) {
                JSONObject sphereObj = (JSONObject) geometryObj.get("sphere");
                String[] centerCoords = ((String) sphereObj.get("center")).split(" ");
                Point center = new Point(
                        Double.parseDouble(centerCoords[0]),
                        Double.parseDouble(centerCoords[1]),
                        Double.parseDouble(centerCoords[2])
                );
                double radius = Double.parseDouble((String) sphereObj.get("radius"));
                geometryList.add(new Sphere(radius, center));
            }else if (geometryObj.containsKey("triangle")) {
                JSONObject triangleObj = (JSONObject) geometryObj.get("triangle");
                JSONArray verticesArray = (JSONArray) triangleObj.get("vertices");
                List<Point> vertices = new ArrayList<>();
                for (Object vertex : verticesArray) {
                    String[] vertexCoords = ((String) vertex).split(" ");
                    vertices.add(new Point(
                            Double.parseDouble(vertexCoords[0]),
                            Double.parseDouble(vertexCoords[1]),
                            Double.parseDouble(vertexCoords[2])
                    ));
                }
                geometryList.add(new Triangle(vertices.get(0), vertices.get(1), vertices.get(2)));
            }
        }

        // Create and return the scene
        Scene scene = new Scene(name).setGeometries(new Geometries(geometryList.toArray(new Geometry[0])))
                .setBackground(background)
                .setAmbientLight(ambientLight);

        return scene;
    }
}
