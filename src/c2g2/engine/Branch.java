package c2g2.engine;

import org.joml.Vector3f;
import c2g2.engine.graph.Mesh;

// Branch class adds additional fields and methods to make
// 3D transformations like scaling, translation, and rotation easier
public class Branch extends GameItem  {

	private float length;
	private float maxLength;

	private Vector3f center;

	public Branch(Mesh mesh) {
		super(mesh);

		center = new Vector3f(0, 0, 0);
		length = 2;
		maxLength = 2;
	}

	public float getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(float max) {
		maxLength = max;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float len) {
		length = len;
	}

	public Vector3f getCenter() {
		return center;
	}

	public void setCenter(Vector3f newCenter) {
		center = newCenter;
	}
}

