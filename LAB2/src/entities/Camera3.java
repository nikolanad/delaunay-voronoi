package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera3 {
	
	private static float SPEED = 0.02f;

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera3() {	
	}
	
	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			position.z -= SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			position.z += SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			position.x -= SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			position.x += SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			position.y -= SPEED;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			position.y += SPEED;
		}
	}

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
}
