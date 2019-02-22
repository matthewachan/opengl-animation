package c2g2.game;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

import c2g2.engine.GameItem;
import c2g2.engine.IGameLogic;
import c2g2.engine.MouseInput;
import c2g2.engine.Window;
import c2g2.engine.graph.Camera;
import c2g2.engine.graph.DirectionalLight;
import c2g2.engine.graph.Material;
import c2g2.engine.graph.Mesh;
import c2g2.engine.graph.OBJLoader;
import c2g2.engine.graph.PointLight;

/*
 * The first first Game in W4160.
 */
public class HelloGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    
    private static final float SCALE_STEP = 0.01f;
    
    private static final float TRANSLATE_STEP = 0.01f;
    
    private static final float ROTATION_STEP = 0.3f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;

    private Vector3f ambientLight;

    private PointLight pointLight;

    private DirectionalLight directionalLight;

    private float lightAngle;

    private static final float CAMERA_POS_STEP = 0.05f;
    
    private int currentObj;
    
    private int nBranches = 3;

    private boolean hasLeaves = false;

    private float reflectance = 0.2f;

    private Vector3f brown = new Vector3f(0.5f, 0.2f, 0);

    private Vector3f green = new Vector3f(0.4f, 0.7f, 0.2f);

    private Vector3f blue = new Vector3f(0.0f, 0.7f, 1);
    
    public HelloGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;
        currentObj=0;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        // NOTE: 
        // //   please uncomment following lines to test your OBJ Loader.
	Mesh plane = OBJLoader.loadMesh("src/resources/models/plane.obj");
	plane.setMaterial(new Material(green, 0.4f));
	plane.rotateMesh(new Vector3f(1, 0, 0), 80);
	plane.translateMesh(new Vector3f(0, -5, 0));
	// plane.scaleMesh(0.1f, 0.1f, 0.1f);
	GameItem floor = new GameItem(plane);
	floor.setPosition(0, 0, 3);

	Mesh bg = OBJLoader.loadMesh("src/resources/models/plane.obj");
	bg.setMaterial(new Material(blue, 0.8f));
	GameItem sky = new GameItem(bg);
	sky.setPosition(0, 0, 20);

	String objFile = "src/resources/models/cone2.obj";
	Mesh mesh = OBJLoader.loadMesh(objFile);
	// mesh.translateMesh(new Vector3f(0, 0, 5));
	mesh.scaleMesh(0.15f, 1, 0.15f);
        // // Mesh mesh = new Mesh();  // comment this line when you enable OBJLoader
        Material material = new Material(brown, reflectance);
        
        mesh.setMaterial(material);
        GameItem trunk = new GameItem(mesh);
	trunk.setPosition(0, 0, 3);
	// trunk.setScale(5);

	// int maxBranches = 0;
	gameItems = new GameItem[]{trunk, floor, sky};

        // Set up lights in the scene
        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

        lightPosition = new Vector3f(-1, 0, 0);
        lightColour = new Vector3f(1, 1, 1);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
    }

    /*
     * This function is to process key events.
     * @see c2g2.engine.IGameLogic#input(c2g2.engine.Window, c2g2.engine.MouseInput)
     */
    @Override
    public void input(Window window, MouseInput mouseInput) {

        if(window.isKeyPressed(GLFW_KEY_Q)){
            //select current object
            currentObj = currentObj + 1;
            currentObj = currentObj % gameItems.length;
        }
        else if(window.isKeyPressed(GLFW_KEY_W)){
            //select current object
            currentObj = currentObj - 1;
            currentObj = currentObj % gameItems.length;
        }
        else if(window.isKeyPressed(GLFW_KEY_E)){
            //scale object
            float curr = gameItems[currentObj].getScale();
            gameItems[currentObj].setScale(curr+SCALE_STEP);
        }
        else if(window.isKeyPressed(GLFW_KEY_R)){
            //scale object
            float curr = gameItems[currentObj].getScale();
            gameItems[currentObj].setScale(curr-SCALE_STEP);
        }
        else if(window.isKeyPressed(GLFW_KEY_T)){
            //move object x by step
            Vector3f curr = gameItems[currentObj].getPosition();
            gameItems[currentObj].setPosition(curr.x+TRANSLATE_STEP, curr.y, curr.z);
        }
        else if(window.isKeyPressed(GLFW_KEY_Y)){
            //move object x by step
            Vector3f curr = gameItems[currentObj].getPosition();
            gameItems[currentObj].setPosition(curr.x-TRANSLATE_STEP, curr.y, curr.z);
        }
        else if(window.isKeyPressed(GLFW_KEY_U)){
            //move object y by step
            Vector3f curr = gameItems[currentObj].getPosition();
            gameItems[currentObj].setPosition(curr.x, curr.y+TRANSLATE_STEP, curr.z);
        }
        else if(window.isKeyPressed(GLFW_KEY_I)){
            //move object y by step
            Vector3f curr = gameItems[currentObj].getPosition();
            gameItems[currentObj].setPosition(curr.x, curr.y-TRANSLATE_STEP, curr.z);
        }
        else if(window.isKeyPressed(GLFW_KEY_O)){
            //move object z by step
            Vector3f curr = gameItems[currentObj].getPosition();
            gameItems[currentObj].setPosition(curr.x, curr.y, curr.z+TRANSLATE_STEP);
        }
        else if(window.isKeyPressed(GLFW_KEY_P)){
            //move object z by step
            Vector3f curr = gameItems[currentObj].getPosition();
            gameItems[currentObj].setPosition(curr.x, curr.y, curr.z-TRANSLATE_STEP);
        }
        else if(window.isKeyPressed(GLFW_KEY_A)){
            //rotate object at x axis
            Vector3f curr = gameItems[currentObj].getRotation();
            gameItems[currentObj].setRotation(curr.x+ROTATION_STEP, curr.y, curr.z);
        }
        else if(window.isKeyPressed(GLFW_KEY_S)){
            //rotate object at x axis
            Vector3f curr = gameItems[currentObj].getRotation();
            gameItems[currentObj].setRotation(curr.x-ROTATION_STEP, curr.y, curr.z);
        }
        else if(window.isKeyPressed(GLFW_KEY_D)){
            //rotate object at x axis
            Vector3f curr = gameItems[currentObj].getRotation();
            gameItems[currentObj].setRotation(curr.x, curr.y+ROTATION_STEP, curr.z);
        }
        else if(window.isKeyPressed(GLFW_KEY_F)){
            //rotate object at x axis
            Vector3f curr = gameItems[currentObj].getRotation();
            gameItems[currentObj].setRotation(curr.x, curr.y-ROTATION_STEP, curr.z);
        }
        else if(window.isKeyPressed(GLFW_KEY_G)){
            //rotate object at x axis
            Vector3f curr = gameItems[currentObj].getRotation();
            gameItems[currentObj].setRotation(curr.x, curr.y, curr.z+ROTATION_STEP);
        }
        else if(window.isKeyPressed(GLFW_KEY_H)){
            //rotate object at x axis
            Vector3f curr = gameItems[currentObj].getRotation();
            gameItems[currentObj].setRotation(curr.x, curr.y, curr.z-ROTATION_STEP);
        }
        else if(window.isKeyPressed(GLFW_KEY_0)){
            //rotation by manipulating mesh
            gameItems[currentObj].getMesh().translateMesh(new Vector3f(0f,0.05f,1f));
        }
        else if(window.isKeyPressed(GLFW_KEY_9)){
            //rotation by manipulating mesh
            gameItems[currentObj].getMesh().rotateMesh(new Vector3f(1,1,1), 30);
        }
        else if(window.isKeyPressed(GLFW_KEY_8)){
            //rotation by manipulating mesh
            gameItems[currentObj].getMesh().scaleMesh(1.0f,1.001f,1.0f);
        }
        else if(window.isKeyPressed(GLFW_KEY_7)){
            //rotation by manipulating mesh
            gameItems[currentObj].getMesh().reflectMesh(new Vector3f(0f,1f,0f), new Vector3f(0f, 1f, 0f));
        }
        else if(window.isKeyPressed(GLFW_KEY_1)){
            //get screenshot
            renderer.writePNG(window);
        }
    }

    /*
     * Update the scene at each time step. This is useful for making an animation. 
     * As time goes, you can move objects or change the scene, and render it again.
     * See GameEngine.gameLoop() to see how this function is used. 
     * 
     * @see c2g2.engine.IGameLogic#update(float, c2g2.engine.MouseInput)
     */
    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

	// Student code below
	boolean fullyGrown = true;

	Random rng = new Random();
	int rand = rng.nextInt(100);

	// int index = gameItems[0].getTipIdx();

	// Vector3f vpos = gameItems[0].getMesh().getVertPos(index);
	// gameItems[0].getMesh().setVertPos(index, vpos.add(new Vector3f(0, 1, 0)));

	// Create new branch
	if (rand % 2 == 0  && nBranches > 0) {
		// Resize GameItems array
		GameItem[] items = new GameItem[gameItems.length + 1];
		for (int i = 0; i < gameItems.length; ++i) {
			items[i] = gameItems[i];
		}
		
		// Parent new branch to a random branch
		int range = gameItems.length - 1;
		int idx = range < 5 ? 0 : rng.nextInt(range);
		GameItem parent = gameItems[idx];

		// Create new mesh
		Mesh bMesh = new Mesh(parent.getMesh());
		Material material = new Material(brown, reflectance);
		bMesh.setMaterial(material);

		// Rotate mesh slightly
		float angle = rng.nextInt(70);
		angle += 20;
		int neg = rng.nextInt(3);
		float x = (float) rng.nextDouble();
		float z = (float) rng.nextDouble();
		if (neg == 0 || neg == 1)
			x = -x;
		if (neg == 0 || neg == 2)
			z = -z;

		Vector3f axis = new Vector3f(x, 0, z);
		bMesh.rotateMesh(axis, angle);

		// Change child GameItem accordingly
		GameItem child = new GameItem(bMesh);
		Vector3f center = parent.getCenter();
		child.setPosition(parent.getPosition());
		child.setScale(parent.getScale());
		child.setRotation(parent.getRotation());
	
		// Push cone in the direction of it's tip
		Vector3f tip = child.getTip();
		tip = tip.rotateAxis((float) Math.toRadians(angle), axis.x, axis.y, axis.z);
		child.setTip(tip);
		bMesh.translateMesh(tip);
		center = center.add(tip);
		

		// Scale cone down slightly
		float scale = 0.3f + (float) rng.nextDouble() * (0.5f - 0.3f);
		bMesh.scaleMesh(.5f, .5f, .5f);

		child.setLength(parent.getLength() * 0.5f);
		int tipIdx = bMesh.getTipIdx();
		int baseIdx = bMesh.getBaseIdx();

		Vector3f tipVert = bMesh.getVertPos(tipIdx);
		Vector3f base = bMesh.getVertPos(baseIdx);

		Vector3f up = new Vector3f();
		tipVert.sub(base, up);

		up = up.normalize();
		up = up.mul(scale * child.getLength());

		tipVert = tipVert.sub(up);

		child.getMesh().setVertPos(tipIdx, tipVert);

		child.setLength(child.getLength() - scale * child.getLength());

		child.setMaxLength(parent.getLength() * scale);

		// Push cone up or down along parent's y axis
		float distance = -0.8f + (float) rng.nextDouble() * (0.1f + 0.5f);
		Vector3f shiftUp = new Vector3f(0, child.getLength() * distance, 0);
		bMesh.translateMesh(shiftUp);
		center.add(shiftUp);

		child.setCenter(center);

		items[items.length - 1] = child;
		gameItems = items;

		nBranches--;
		System.out.println("Creating branch...");
	}

	// @TODO: Instead of scaling, translate the tip's coordinates
	for (GameItem item : gameItems) {
		// scale branch if not maxed out
		if (item.getLength() < item.getMaxLength()) {
			float speed = .1f;
			float scaleFactor = (speed / 100) * 1f + (float) rng.nextDouble() * (2f - 1f) * (speed / 100);

			Mesh mesh = item.getMesh();

			int tipIdx = mesh.getTipIdx();
			int baseIdx = mesh.getBaseIdx();

			Vector3f tip = mesh.getVertPos(tipIdx);
			Vector3f base = mesh.getVertPos(baseIdx);

			Vector3f up = new Vector3f();
			tip.sub(base, up);
			System.out.println(tipIdx);
			System.out.println(baseIdx);

			up = up.normalize();
			up = up.mul(scaleFactor);
			
			tip = tip.add(up);


			item.getMesh().setVertPos(tipIdx, tip);

			// item.getMesh().scaleMesh(scaleFactor, scaleFactor, scaleFactor);
			item.setLength(item.getLength() + scaleFactor);
			fullyGrown = false;
		}
	}

	if (nBranches == 0 && fullyGrown && !hasLeaves) {
		String objFile = "src/resources/models/cloud.obj";
		try {
			Mesh mesh = OBJLoader.loadMesh(objFile);
			mesh.scaleMesh(0.5f, 0.5f, 0.5f);
			mesh.translateMesh(gameItems[0].getTip());
			mesh.translateMesh(new Vector3f(0, -0.6f, 0));
			Material material = new Material(green, reflectance);
			mesh.setMaterial(material);

			// Resize GameItems array
			GameItem[] items = new GameItem[gameItems.length + 1];
			for (int i = 0; i < gameItems.length; ++i) {
				items[i] = gameItems[i];
			}

			GameItem leaves = new GameItem(mesh);

			leaves.setPosition(gameItems[0].getPosition());

			items[items.length - 1] = leaves;
			gameItems = items;
			hasLeaves = true;
		} 
		catch (Exception e) {
			System.out.println("Failed to load " + objFile);
		}
	}
	

        // Update camera based on mouse            
        if (mouseInput.isLeftButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            System.out.println(rotVec);
            Vector3f curr = gameItems[0].getRotation();
            gameItems[0].setRotation(curr.x+ rotVec.x * MOUSE_SENSITIVITY, curr.y+rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        // Update directional light direction, intensity and color
        // lightAngle += 1.1f;
	lightAngle = 20;
        
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 90) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems, ambientLight, pointLight, directionalLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }

}
