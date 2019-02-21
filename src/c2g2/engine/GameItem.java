package c2g2.engine;

import org.joml.Vector3f;
import c2g2.engine.graph.Mesh;

public class GameItem {

    private final Mesh mesh;
    
    private final Vector3f position;
    
    private float scale;

    private final Vector3f rotation;

    private Vector3f tipPos;

    private float length;

    private Vector3f center;

    private float maxLength;

    public GameItem(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
	tipPos = new Vector3f(0, 1, 0);
	length = 2;
	center = new Vector3f(0, 0, 0);
	maxLength = 1;
    }

    public float getMaxLength() {
	    return maxLength;
    }
    public void setMaxLength(float max) {
	    maxLength = max;
    }
    public Vector3f getCenter() {
	    return center;
    }

    public void setCenter(Vector3f c) {
	    center = c;
    }

    public float getLength() {
	    return length;
    }

    public void setLength(float len) {
	    length = len;
    }

    public Vector3f getTip() {
	    return tipPos;
    }

    public void setTip(Vector3f v) {
	    tipPos = v;
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

    public void setRotation(Vector3f r) {
        this.rotation.x = r.x;
        this.rotation.y = r.y;
        this.rotation.z = r.z;

    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
    
    public Mesh getMesh() {
        return mesh;
    }
}
