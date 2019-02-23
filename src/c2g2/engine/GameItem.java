package c2g2.engine;

import org.joml.Vector3f;
import c2g2.engine.graph.Mesh;

public class GameItem {

    private final Mesh mesh;
    
    private final Vector3f position;
    
    private float scale;

    private final Vector3f rotation;

    private float maxLength;

    public GameItem(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(Vector3f p) {
        this.position.x = p.x;
        this.position.y = p.y;
        this.position.z = p.z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void setRotation(Vector3f r) {
        this.rotation.x = r.x;
        this.rotation.y = r.y;
        this.rotation.z = r.z;
    }
    
    public Mesh getMesh() {
        return mesh;
    }
}
