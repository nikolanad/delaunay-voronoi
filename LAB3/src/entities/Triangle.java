package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import toolbox.Maths;

public class Triangle {
	
	private Point a;
	private Point b;
	private Point c;
	private Circle circumcircle;
	
	public Triangle(Point a, Point b, Point c) {
		this.a = a;
		this.b = b;
		this.c = c;
		
		Vector2f ab = new Vector2f(b.getX() - a.getX(), b.getY() - a.getY());
		Vector2f ac = new Vector2f(c.getX() - a.getX(), c.getY() - a.getY());
		
		Vector2f t1 = new Vector2f(a.getX() + ab.getX() * 0.5f, a.getY() + ab.getY() * 0.5f);
		Vector2f t2 = new Vector2f(a.getX() + ac.getX() * 0.5f, a.getY() + ac.getY() * 0.5f);
		
		Vector2f v1 = new Vector2f(-ab.getY(), ab.getX());
		Vector2f v2 = new Vector2f(-ac.getY(), ac.getX());
		
		//float t = (t2.getX()*v2.getY() - t2.getY()*v2.getX() - t1.getX()*v2.getY() + v2.getX()*t1.getY()) /
		//		   (v1.getX()*v2.getY() - v1.getY()*v2.getX());
		
		//Vector2f cross = Vector2f.add(t1, (Vector2f)v1.scale(t), null);
		Vector2f cross = Maths.lineLineIntersection(t1, v1, t2, v2);
		circumcircle = new Circle(new Point(cross.x, cross.y), Maths.distance(cross, new Vector2f(a.getX(), a.getY())));
	}
	
	public Point getA() {
		return a;
	}
	
	public Point getB() {
		return b;
	}
	
	public Point getC() {
		return c;
	}
	
	public Circle getCircumcircle() {
		return circumcircle;
	}
	
	public boolean isPointInTriangle(Point point) {
		Vector2f va = new Vector2f(a.getX(), a.getY());
		Vector2f v0 = Vector2f.sub(new Vector2f(c.getX(), c.getY()), va, null);
		Vector2f v1 = Vector2f.sub(new Vector2f(b.getX(), b.getY()), va, null);
		Vector2f v2 = Vector2f.sub(new Vector2f(point.getX(), point.getY()), va, null);

		// Compute dot products
		double dot00 = Vector2f.dot(v0, v0);
		double dot01 = Vector2f.dot(v0, v1);
		double dot02 = Vector2f.dot(v0, v2);
		double dot11 = Vector2f.dot(v1, v1);
		double dot12 = Vector2f.dot(v1, v2);

		// Compute barycentric coordinates
		double invDenom = 1.0f / (dot00 * dot11 - dot01 * dot01);
		double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

		// Check if point is in triangle
		return (u >= 0) && (v >= 0) && (u + v < 1);
	}
	
	public List<Line> getLines() {
		List<Line> lines = new ArrayList<>();
		lines.add(new Line(a, b));
		lines.add(new Line(b, c));
		lines.add(new Line(c, a));
		
		return lines;
	}
	
	public List<Point> getPoints() {
		List<Point> points = new ArrayList<>();
		points.add(a);
		points.add(b);
		points.add(c);
		return points;
	}
	
	public boolean isOneOfVertices(Point p) {
		return p.equals(a) || p.equals(b) || p.equals(c);
	}
}
