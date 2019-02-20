package c2g2.engine.graph;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Iterator;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class OBJLoader {

	static private void extractFace(String line, ArrayList<Vector3f> f, TreeMap<Integer, ArrayList<Integer>> map) {
		String[] args = line.split("\\s+");
		int length = args[1].split("/").length;

		int[] vIdx = new int[3];
		int[] vtIdx = new int[3];
		int[] vnIdx = new int[3];

		// Extract vertex index, texture index and normal index information
		for (int i = 1; i <= 3; ++i) {
			String[] infoGroup = args[i].split("/");

			vIdx[i - 1] = Integer.parseInt(infoGroup[0]);
			// Texture and normal index info is optional
			if (length > 1) {
				vtIdx[i - 1] = infoGroup[1].length() > 0 ? Integer.parseInt(infoGroup[1]) : 0;
				vnIdx[i - 1] = infoGroup[2].length() > 0 ? Integer.parseInt(infoGroup[2]) : 0;
			}
		}
		
		f.add(new Vector3f(vIdx[0] - 1, vIdx[1] - 1, vIdx[2] - 1));

		// Add entry to map
		if (length > 1) {
			for (int i = 0; i < 3; ++i) {
				ArrayList<Integer> arr = new ArrayList<Integer>();

				arr.add(vtIdx[i]);
				arr.add(vnIdx[i]);
				map.put(vIdx[i], arr);
			}
		}
	}
	
    public static Mesh loadMesh(String fileName) throws Exception {
    	//// --- student code ---
        float[] positions = null;
        float[] textCoords = null;
        float[] norms = null;
        int[] indices = null;

        //your task is to read data from an .obj file and fill in those arrays.
        //the data in those arrays should use following format.
        //positions[0]=v[0].position.x positions[1]=v[0].position.y positions[2]=v[0].position.z positions[3]=v[1].position.x ...
        //textCoords[0]=v[0].texture_coordinates.x textCoords[1]=v[0].texture_coordinates.y textCoords[2]=v[1].texture_coordinates.x ...
        //norms[0]=v[0].normals.x norms[1]=v[0].normals.y norms[2]=v[0].normals.z norms[3]=v[1].normals.x...
        //indices[0]=face[0].ind[0] indices[1]=face[0].ind[1] indices[2]=face[0].ind[2] indices[3]=face[1].ind[0]...(assuming all the faces are triangle face)
	
	System.out.println("Loading mesh from " + fileName);

	// Begin reading from the OBJ file
	BufferedReader br = new BufferedReader(new FileReader(fileName));
	String line;

	ArrayList<Vector3f> v = new ArrayList<Vector3f>();
	ArrayList<Vector2f> vt = new ArrayList<Vector2f>();
	ArrayList<Vector3f> vn = new ArrayList<Vector3f>();
	ArrayList<Vector3f> f = new ArrayList<Vector3f>();
	TreeMap<Integer, ArrayList<Integer>> map = new TreeMap<Integer, ArrayList<Integer>>();

	// Populate ArrayLists
	while ((line = br.readLine()) != null) {
		String[] args = line.split("\\s+");

		if (args[0].equals("v")) {
			v.add(new Vector3f(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3])));
		}
		else if (args[0].equals("vt")) {
			vt.add(new Vector2f(Float.parseFloat(args[1]), Float.parseFloat(args[2])));
		}
		else if (args[0].equals("vn")) {
			vn.add(new Vector3f(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3])));
		}
		else if (args[0].equals("f")) {
			extractFace(line, f, map);
		}

	}

	br.close();

	positions = new float[v.size() * 3];
	textCoords = new float[v.size() * 2];
	norms = new float[v.size() * 3];
	indices = new int[f.size() * 3];

	// Populate position and index array
	for (int i = 0; i < positions.length / 3; ++i) {
		Vector3f vertex = v.get(i);

		positions[3 * i] = vertex.x;
		positions[(3 * i) + 1] = vertex.y;
		positions[(3 * i) + 2] = vertex.z;
	}
	for (int i = 0; i < indices.length / 3; ++i) {
		Vector3f face = f.get(i);

		indices[3 * i] = (int) face.x;
		indices[(3 * i) + 1] = (int) face.y;
		indices[(3 * i) + 2] = (int) face.z;
	}
	
	// Populate texture and normal arrays using the map
	Set s = map.entrySet();
	Iterator it = s.iterator();
	Set set = map.keySet();

	while (it.hasNext()) {
		// Unpack info from map
		Map.Entry entry = (Map.Entry) it.next();

		int vIdx = (Integer) entry.getKey() - 1;
		ArrayList<Integer> arr = (ArrayList<Integer>) entry.getValue();

		int vtIdx = arr.get(0) - 1;
		int vnIdx = arr.get(1) - 1;

		textCoords[2 * vIdx] = vtIdx >= 0 ? vt.get(vtIdx).x : 0;
		textCoords[(2 * vIdx) + 1] = vtIdx >= 0 ? vt.get(vtIdx).y : 0;

		norms[3 * vIdx] = vnIdx >= 0 ? vn.get(vnIdx).x : 0;
		norms[(3 * vIdx) + 1] = vnIdx >= 0 ? vn.get(vnIdx).y : 0;
		norms[(3 * vIdx) + 2] = vnIdx >= 0 ? vn.get(vnIdx).z : 0;

	}

        return new Mesh(positions, textCoords, norms, indices);
    }

}
