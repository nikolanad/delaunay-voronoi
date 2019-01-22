package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Point;

public class Maths {
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	public static double distance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
	}
	
	public static double distance(Vector2f v1, Vector2f v2) {
		return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2));
	}
	
	public static Vector2f lineLineIntersection(Vector2f t1, Vector2f v1, Vector2f t2, Vector2f v2) {
		float t = (t2.getX()*v2.getY() - t2.getY()*v2.getX() - t1.getX()*v2.getY() + v2.getX()*t1.getY()) /
				   (v1.getX()*v2.getY() - v1.getY()*v2.getX());
		
		return Vector2f.add(t1, (Vector2f)v1.scale(t), null);
	}
}
