package voronoi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import entities.Line;
import entities.Point;
import entities.Triangle;

public class DelaunayTriangulation {

	private Triangle superTriangle;
	private List<Triangle> allTriangles = new ArrayList<>();
	private List<Point> points = new ArrayList<>();
	private Map<Line, List<Triangle>> lineToTriangles = new HashMap<>();
	
	public DelaunayTriangulation(Triangle superTriangle) {
		this.superTriangle = superTriangle;
		allTriangles.add(superTriangle);
	}
	
	public DelaunayTriangulation copy() { //TODO: deep copy
		DelaunayTriangulation copy = new DelaunayTriangulation(superTriangle);
		for(Triangle t : allTriangles) {
			copy.allTriangles.add(t);
		}
		for(Point p : points) {
			copy.points.add(p);
		}
		for(Entry<Line, List<Triangle>> entry : lineToTriangles.entrySet()) {
			for(Triangle t : entry.getValue()) {
				copy.putTriangleInMap(entry.getKey(), t);
			}
		}
		return copy;
	}
	
	public void addPoint(Point p) {
		if(points.contains(p)) {
			return;
		}
		points.add(p);
		
		List<Line> lineBuffer = new ArrayList<>();
		Iterator<Triangle> iter = allTriangles.iterator();
		while(iter.hasNext()) {
			Triangle t = iter.next();
			if(t.getCircumcircle().isInCircle(p)) {
				for(Line l : t.getLines()) {
					if(lineBuffer.contains(l)) {
						lineBuffer.remove(l);
						lineToTriangles.remove(l);
					} else {
						lineBuffer.add(l);
						removeTriangleFromMap(l, t);
					}
				}
				
				iter.remove();
			}
		}
		
		for(Line line : lineBuffer) {
			Triangle newTriangle = new Triangle(line.getP1(), line.getP2(), p);
			if(!superTriangleContainsPoints(newTriangle)) {
				putTriangleInMap(line, newTriangle);	
				putTriangleInMap(new Line(line.getP1(), p), newTriangle);
				putTriangleInMap(new Line(line.getP2(), p), newTriangle);
			}
			allTriangles.add(newTriangle);
		}
	}
	
	private boolean superTriangleContainsPoints(Triangle t) {
		return superTriangle.isOneOfVertices(t.getA()) || superTriangle.isOneOfVertices(t.getB()) || superTriangle.isOneOfVertices(t.getC());
	}
	
	private void removeTriangleFromMap(Line l, Triangle t) {
		List<Triangle> triangles = lineToTriangles.get(l);
		if(triangles == null) {
			return;
		}
		triangles.remove(t);
		if(triangles.isEmpty()) {
			lineToTriangles.put(l, null);
		}
	}
	
	private void putTriangleInMap(Line l, Triangle t) {
		List<Triangle> triangles = lineToTriangles.get(l);
		if(triangles == null) {
			triangles = new ArrayList<>();
			lineToTriangles.put(l, triangles);
		}
		triangles.add(t);
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public List<Triangle> getTriangles() {
		List<Triangle> triangles = new ArrayList<>();
		for(Triangle t : allTriangles) {
			if(superTriangleContainsPoints(t)) {
				continue;
			}
			triangles.add(t);
		}
		return triangles;
	}
	
	public Map<Line, List<Triangle>> getLineToTriangles() {
		return lineToTriangles;
	}
}
