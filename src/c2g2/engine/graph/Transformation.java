package c2g2.engine.graph;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import c2g2.engine.GameItem;

public class Transformation {

    private final Matrix4f projectionMatrix;
    
    private final Matrix4f viewMatrix;
    
    private final Matrix4f modelMatrix;

    public Transformation() {
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        projectionMatrix.identity();
        //// --- student code ---
	float aspect = width / height;
	float tanFov = (float) Math.tan(fov / 2);
        
	projectionMatrix.zero();
	projectionMatrix.m00(1 / (aspect * tanFov));
	projectionMatrix.m11(1 / tanFov);
	projectionMatrix.m22((zNear + zFar) / (zNear - zFar));
	projectionMatrix.m23(-1);
	projectionMatrix.m32((2 * zFar * zNear) / (zNear - zFar));

	return projectionMatrix;
    }
    
    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f cameraTarget = camera.getTarget();
        Vector3f up = camera.getUp();
        viewMatrix.identity();
        //// --- student code ---
	Vector3f x = new Vector3f();
	Vector3f y = new Vector3f();
	Vector3f z = new Vector3f();
	// Scale z to be a unit vector (-z is towards the target)
	cameraPos.sub(cameraTarget, z);
	z = z.div(z.length());

	// Take the cross product of z and up to get x
	z.cross(up, x);
	x = x.div(x.length());

	// Take the cross product of z and x to get y
	z.cross(x, y);
	y = y.div(y.length());

	Vector3f trans = new Vector3f(x.negate().dot(cameraPos), y.negate().dot(cameraPos), z.negate().dot(cameraPos));
	viewMatrix.m00(x.x());
	viewMatrix.m01(x.y());
	viewMatrix.m02(x.z());
	viewMatrix.m03(trans.x);

	viewMatrix.m10(y.x());
	viewMatrix.m11(y.y());
	viewMatrix.m12(y.z());
	viewMatrix.m13(trans.y);

	viewMatrix.m20(z.x());
	viewMatrix.m21(z.y());
	viewMatrix.m22(z.z());
	viewMatrix.m23(trans.z);

	return viewMatrix;
    }
    
    public Matrix4f getModelMatrix(GameItem gameItem){
        Vector3f rotation = gameItem.getRotation();
        Vector3f position = gameItem.getPosition();
        modelMatrix.identity();
        //// --- student code ---
        return modelMatrix.translate(position).rotateXYZ(rotation);
    }

    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(getModelMatrix(gameItem));
    }
}
