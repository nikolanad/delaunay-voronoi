package entities;

import org.lwjgl.util.vector.Vector3f;

public abstract class Camera {

	protected Vector3f position;
	//protected Vector4f ORIENTATION = new Vector4f(0, 0, -1, 0);
	
	protected float pitch;
	protected float yaw;
	protected float roll;

	public Camera(Vector3f position) {
		this.position = position;
	}
	
	public Camera() {
		this(new Vector3f(0.0f, 0.0f, 0.0f));
	}
	
	public abstract void update();

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
//	public Vector3f getOrientation() {
//		Matrix4f rotMatrix = new Matrix4f();
//		rotMatrix.setIdentity();
//		rotMatrix.rotate((float)Math.toRadians(pitch), new Vector3f(1, 0, 0));
//		rotMatrix.rotate((float)Math.toRadians(yaw),   new Vector3f(0, 1, 0));
//		rotMatrix.rotate((float)Math.toRadians(roll),  new Vector3f(0, 0, 1));
//		Vector4f result = Maths.multiplyMatrixAndVector(ORIENTATION, rotMatrix);
//		Vector3f orientation = new Vector3f(result.x, result.y, result.z);
//		return orientation;
//	}
}
