package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private static float SPEED = 0.02f;

	private Vector3f position = new Vector3f(0, 0, 0);
	
	public Camera(Vector3f position) {
		this.position = position;
	}
	
	public Camera() {	
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
}
