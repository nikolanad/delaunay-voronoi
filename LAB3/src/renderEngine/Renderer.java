package renderEngine;

import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import entities.Circle;
import entities.Line;
import entities.Point;
import entities.Triangle;

public class Renderer {
	
	public void initialize() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0.05f, 0.05f, 0.05f, 1);
	}
	
	public void renderLine(Line line, float r, float g, float b) {
		GL11.glColor3f(r, g, b);
		GL11.glLineWidth(1.5f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(line.getP1().getX(), line.getP1().getY());
		GL11.glVertex2f(line.getP2().getX(), line.getP2().getY());
		GL11.glEnd();
	}
	
	public void renderLines(List<Line> lines, float r, float g, float b) {
		for(Line line : lines) {
			renderLine(line, r, g, b);
		}		
	}
	
	public void renderPoint(Point point, float r, float g, float b) {
		GL11.glColor3f(r, g, b);
		GL11.glPointSize(5f);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex2f(point.getX(), point.getY());
		GL11.glEnd();
	}
	
	public void renderVector(Vector2f start, Vector2f end, float r, float g, float b) {
		GL11.glColor3f(r, g, b);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2f(start.x, start.y);
		GL11.glVertex2f(start.x + end.x, start.y + end.y);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex2f(start.x + end.x, start.y + end.y);
		GL11.glEnd();
	}
	
	public void renderPoints(List<Point> points, float r, float g, float b) {
		GL11.glColor3f(r, g, b);
		GL11.glPointSize(5f);
		GL11.glBegin(GL11.GL_POINTS);
		for(Point point : points) {
			GL11.glVertex2f(point.getX(), point.getY());
		}
		GL11.glEnd();
	}
	
	public void renderCircles(List<Circle> circles, int pointCount, float r, float g, float b) {
		for(Circle circle : circles) {
			renderCircle(circle, pointCount, r, g, b);
		}
	}
	
	public void renderCircle(Circle circle, int pointCount, float r, float g, float b) {
		GL11.glColor3f(r, g, b);
		GL11.glLineWidth(1f);
		double step = 2*Math.PI / pointCount;
		double t = 0;
		Point center = circle.getCenter();
		float radius = (float)circle.getRadius();
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for(int i = 0; i <= pointCount; i++, t+=step) {
			Vector2f d = new Vector2f((float)Math.cos(t), (float)Math.sin(t));
			GL11.glVertex2f(center.getX() + d.x * radius, center.getY() + d.y * radius);
		}
		
		GL11.glEnd();
	}
	
	public void renderTriangle(Triangle t, float r, float g, float b) {
		GL11.glColor3f(r, g, b);
		GL11.glLineWidth(1.5f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2f(t.getA().getX(), t.getA().getY());
		GL11.glVertex2f(t.getB().getX(), t.getB().getY());
		GL11.glVertex2f(t.getC().getX(), t.getC().getY());
		GL11.glVertex2f(t.getA().getX(), t.getA().getY());
		GL11.glEnd();
	}
	
	public void renderTriangles(List<Triangle> triangles, float r, float g, float b) {
		for(Triangle t : triangles) {
			renderTriangle(t, r, g, b);
		}
	}
}
