package entities;

import java.util.ArrayList;
import java.util.List;

public class Line {

	private Point p1;
	private Point p2;
	
	public Line(Point p1, Point p2) {
		if(p1.getX() < p2.getX()) {
			this.p1 = p1;
			this.p2 = p2;
		} else if(p1.getX() == p2.getX()) {
			if(p1.getY() < p2.getY()) {
				this.p1 = p1;
				this.p2 = p2;
			} else {
				this.p1 = p2;
				this.p2 = p1;
			}
		} else {
			this.p1 = p2;
			this.p2 = p1;
		}
	}
	
	public Point getP1() {
		return p1;
	}
	
	public Point getP2() {
		return p2;
	}
	
	public List<Point> getPoints() {
		List<Point> points = new ArrayList<>();
		points.add(p1);
		points.add(p2);
		return points;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (p1 == null) {
			if (other.p1 != null)
				return false;
		} else if (!p1.equals(other.p1))
			return false;
		if (p2 == null) {
			if (other.p2 != null)
				return false;
		} else if (!p2.equals(other.p2))
			return false;
		return true;
	}
}
