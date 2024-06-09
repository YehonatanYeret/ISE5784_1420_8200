package scene;

import geometries.Geometries;
import lighting.LightSource;
import primitives.Color;
import lighting.AmbientLight;

import java.util.LinkedList;
import java.util.List;

/**
 * Scene class represents a scene in the 3D space

 */
public class Scene {

    /**
     * lights in the scene
     */
    public List<LightSource> lights = new LinkedList<>();
    /**
     * name of the scene
     */
    public String name;
    /**
     * background color of the scene
     */
    public Color background = Color.BLACK;
    /**
     * ambient light of the scene
     */
    public AmbientLight ambientLight = AmbientLight.NONE;
    /**
     * geometries in the scene
     */
    public Geometries geometries = new Geometries();

    /**
     * Constructor for Scene
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Scene setter
     * @param background the background color of the scene
     * @return the scene
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Scene setter
     * @param ambientLight the ambient light of the scene
     * @return the scene
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Scene setter
     * @param geometries the geometries in the scene
     * @return the scene
     */
    public Scene setGeometries(Geometries geometries){
        this.geometries = geometries;
        return this;
    }

    /**
     * Scene setter
     * @param lights the lights in the scene
     * @return the scene
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

}
