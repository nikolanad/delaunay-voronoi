package entities;

import toolbox.Maths;

public class Circle {
	
	private Point center;
	private double radius;
	
	public Circle(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public boolean isInCircle(Point point) {
		return Maths.distance(center, point) <= radius;
	}
	
	public Point getCenter() {
		return center;
	}
	
	public double getRadius() {
		return radius;
	}
}
