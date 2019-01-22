package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;

public class FloatingCamera extends Camera {

	private static final float DEFAULT_ROTATION_SPEED = 12.5f;	
	private static final float DEFAULT_SPEED = 2f;
	
	private final float movementSpeed;
	private final float rotationSpeed;
	
	public FloatingCamera(Vector3f position, float movementSpeed, float rotationSpeed) {
		super(position);
		this.rotationSpeed = rotationSpeed;
		this.movementSpeed = movementSpeed;
		Mouse.setGrabbed(true);
	}
	
	public FloatingCamera(Vector3f position) {
		this(position, DEFAULT_SPEED, DEFAULT_ROTATION_SPEED);
	}
	
	public FloatingCamera() {
		this(new Vector3f());
	}

	@Override
	public void update() {
		rotate();
		move();
	}
	
	private void rotate() {
		pitch -= Mouse.getDY() * DisplayManager.getFrameTimeSeconds() * rotationSpeed;
		if(pitch > 90) pitch = 90;
		if(pitch < -90) pitch = -90;
		
		yaw += Mouse.getDX() * DisplayManager.getFrameTimeSeconds() * rotationSpeed;
	}

	private void move() {
		float distance = movementSpeed * DisplayManager.getFrameTimeSeconds();
		float xzDistance = (float) (distance * Math.cos(Math.toRadians(pitch)));
		float yDistance = - (float) (distance * Math.sin(Math.toRadians(pitch)));
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveForward(xzDistance, yDistance);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveBack(xzDistance, yDistance);
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			moveUp(distance);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			moveDown(distance);
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			strafeRight(xzDistance);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			strafeLeft(xzDistance);
		}
	}
	
	private void moveForward(float xzDistance, float yDistance) {
		position.x += xzDistance * Math.sin(Math.toRadians(yaw));
		position.z -= xzDistance * Math.cos(Math.toRadians(yaw));
		position.y += yDistance;
	}
	
	private void moveBack(float xzDistance, float yDistance) {
		moveForward(-xzDistance, -yDistance);
	}
	
	private void strafeRight(float distance) {
		position.x += distance * Math.sin(Math.toRadians(yaw + 90));
		position.z -= distance * Math.cos(Math.toRadians(yaw + 90));
	}
	
	private void strafeLeft(float distance) {
		strafeRight(-distance);
	}
	
	private void moveUp(float distance) {
		position.y += distance;
	}
	
	private void moveDown(float distance) {
		moveUp(-distance);
	}
}
