package c2g2.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Matrix4f;

import org.lwjgl.system.MemoryUtil;

public class Mesh {

	private int vaoId;

	private List<Integer> vboIdList;

	private int vertexCount;

	private Material material;

	private float[] pos;
	private float[] textco;
	private float[] norms;
	private int[] inds;

	// Index of base and tip vertices in the pos array
	private int baseIdx;
	private int tipIdx;

	public Mesh(){
		this(new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.5f,0.0f,0.5f,0.0f,0.0f,0.5f,0.5f,0.5f,0.0f,0.0f,0.5f,0.0f,0.5f,0.5f,0.5f,0.0f,0.5f,0.5f,0.5f}, 
				new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f}, 
				new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f}, 
				new int[]{0,6,4,0,2,6,0,3,2,0,1,3,2,7,6,2,3,7,4,6,7,4,7,5,0,4,5,0,5,1,1,5,7,1,7,3});
	}

	public Mesh(Mesh m) {
		pos = new float[m.pos.length];
		for (int i = 0; i < m.pos.length; ++i) {
			pos[i] = m.pos[i];
		}
		textco = new float[m.textco.length];
		for (int i = 0; i < m.textco.length; ++i) {
			textco[i] = m.textco[i];
		}
		norms = new float[m.norms.length];
		for (int i = 0; i < m.norms.length; ++i) {
			norms[i] = m.norms[i];
		}
		inds = new int[m.inds.length];
		for (int i = 0; i < m.inds.length; ++i) {
			inds[i] = m.inds[i];
		}
		setMesh(pos, textco, norms, inds);
	}


	// Get vertex position array
	public float[] getPos() {
		return pos;
	}

	// Get the index of the cone tip vertex in the position array
	public int getTipIdx() {
		return tipIdx;
	}

	// Get the index of the cone base vertex in the position array
	public int getBaseIdx() {
		return baseIdx;
	}

	// Get position info for a vertex
	public Vector3f getVertPos(int idx) {
		Vector3f v = new Vector3f(pos[3 * idx], pos[3 * idx + 1], pos[3 * idx + 2]);
		return v;
	}

	// Set the position info for a vertex
	public void setVertPos(int idx, Vector3f v) {
		pos[3 * idx] = v.x;
		pos[3 * idx + 1] = v.y;
		pos[3 * idx + 2] = v.z;
		setMesh(pos, textco, norms, inds);
	}

	// Locate the tip and base position vertices (only checked when the object is first spawned)
	private void parseIndices(float[] pos) {
		for (int i = 0; i < pos.length / 3; ++i) {
			Vector3f v = new Vector3f(pos[3 * i], pos[3 * i + 1], pos[3 * i + 2]);
			if (tipIdx == 0 && v.y == 1)
				tipIdx = i;
			if (baseIdx == 0 && v.x == 0 && v.y == -1 && v.z == 0)
				baseIdx = i;
		}
	}

	public void setMesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
		pos = positions;
		textco = textCoords;
		norms = normals;
		inds = indices;

		for (int i = 0; i < pos.length / 3; ++i) {
			Vector3f v = new Vector3f(pos[3 * i], pos[3 * i + 1], pos[3 * i + 2]);
			if (tipIdx == 0 && v.y == 1)
				tipIdx = i;
			if (baseIdx == 0 && v.x == 0 && v.y == -1 && v.z == 0)
				baseIdx = i;
		}


		FloatBuffer posBuffer = null;
		FloatBuffer textCoordsBuffer = null;
		FloatBuffer vecNormalsBuffer = null;
		IntBuffer indicesBuffer = null;
		System.out.println("create mesh:");
		System.out.println("v: "+positions.length+" t: "+textCoords.length+" n: "+normals.length+" idx: "+indices.length);
		try {
			vertexCount = indices.length;
			vboIdList = new ArrayList<Integer>();

			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);

			// Position VBO
			int vboId = glGenBuffers();
			vboIdList.add(vboId);
			posBuffer = MemoryUtil.memAllocFloat(positions.length);
			posBuffer.put(positions).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

			// Texture coordinates VBO
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
			textCoordsBuffer.put(textCoords).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

			// Vertex normals VBO
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
			vecNormalsBuffer.put(normals).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

			// Index VBO
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		} finally {

			if (posBuffer != null) {
				MemoryUtil.memFree(posBuffer);
			}
			if (textCoordsBuffer != null) {
				MemoryUtil.memFree(textCoordsBuffer);
			}
			if (vecNormalsBuffer != null) {
				MemoryUtil.memFree(vecNormalsBuffer);
			}
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}

	public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
		setMesh(positions, textCoords, normals, indices);        
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public int getVaoId() {
		return vaoId;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void render() {
		// Draw the mesh
		glBindVertexArray(getVaoId());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);

		// Delete the VBOs
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (int vboId : vboIdList) {
			glDeleteBuffers(vboId);
		}

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}

	public void scaleMesh(float sx, float sy, float sz){
		cleanUp(); //clean up buffer
		//Reset position of each point
		//Do not change textco, norms, inds

		//student code 
		for (int i = 0; i < pos.length/3; i++) {
			pos[3 * i] *= sx;
			pos[3 * i + 1] *= sy;
			pos[3 * i + 2] *= sz;

		}   	
		setMesh(pos, textco, norms, inds);
	}

	public void translateMesh(Vector3f trans){
		cleanUp();
		//reset position of each point
		//Do not change textco, norms, inds

		//student code
		for(int i=0; i< pos.length/3; i++){
			pos[3 * i] += trans.x;
			pos[3 * i + 1] += trans.y;
			pos[3 * i + 2] += trans.z;
		}
		setMesh(pos, textco, norms, inds);
	}

	public void rotateMesh(Vector3f axis, float angle){
		cleanUp();
		//reset position of each point
		//Do not change textco, norms, inds
		
		//student code
		for(int i=0; i< pos.length/3; i++){
			Vector3f v = new Vector3f(pos[3 * i], pos[3 * i + 1], pos[3 * i + 2]);
			// Rotate the point about an axis by the given angle
			v.rotateAxis((float) Math.toRadians(angle), axis.x, axis.y, axis.z);
			pos[3 * i] = v.x;
			pos[3 * i + 1] = v.y;
			pos[3 * i + 2] = v.z;
		}
		setMesh(pos, textco, norms, inds);
	}

	public void reflectMesh(Vector3f p, Vector3f n){
		cleanUp();
		//reset position of each point
		//Do not change textco, norms, inds

		//student code
		// n should be a unit vector
		n.normalize();

		// Apply formula for reflecting about a plane 
		Matrix4f m = new Matrix4f();
		m.identity();

		float a = n.x;
		float b = n.y;
		float c = n.z;

		m.m00((float) (1 - 2 * Math.pow(a, 2)));
		m.m01(-2 * a * b);
		m.m02(-2 * a * c);

		m.m10(-2 * a * b);
		m.m11((float) (1 - 2 * Math.pow(b, 2)));
		m.m12(-2 * b * c);

		m.m20(-2 * a * c);
		m.m21(-2 * b * c);
		m.m22((float) (1 - 2 * Math.pow(c, 2)));

		// Add translation component so plane is through the origin
		m.m30(-2 * p.dot(n) * a);
		m.m31(-2 * p.dot(n) * b);
		m.m32(-2 * p.dot(n) * c);

		for(int i=0; i< pos.length/3; i++){
			Vector4f v = new Vector4f(pos[3 * i], pos[3 * i + 1], pos[3 * i + 2], 1);
			v.mul(m);
			pos[3 * i] = v.x;
			pos[3 * i + 1] = v.y;
			pos[3 * i + 2] = v.z;
		}
		setMesh(pos, textco, norms, inds);
	}
}
