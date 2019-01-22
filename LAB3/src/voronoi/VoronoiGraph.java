package voronoi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Vector2f;

import entities.Line;
import entities.Point;
import entities.Triangle;

public class VoronoiGraph {

	private List<Point> points = new ArrayList<>();
	private List<Line> edges = new ArrayList<>();
	
	public static VoronoiGraph createFromDelaunay(DelaunayTriangulation delaunay, int width, int height) {
		VoronoiGraph voronoi = new VoronoiGraph();
		for(Point p : delaunay.getPoints()) {
			voronoi.points.add(p);
		}
		Map<Line, List<Triangle>> lineToTriangles = delaunay.getLineToTriangles();
		for(Entry<Line, List<Triangle>> entry : lineToTriangles.entrySet()) {
			List<Triangle> triangles = entry.getValue();
			if(triangles.size() == 2) {
				Point c1 = triangles.get(0).getCircumcircle().getCenter();
				Point c2 = triangles.get(1).getCircumcircle().getCenter();
				voronoi.edges.add(new Line(c1, c2));
			} else if(triangles.size() == 1) {
				Line line = entry.getKey();
				Triangle t = triangles.get(0);
				Point center = t.getCircumcircle().getCenter();
				Vector2f c = new Vector2f(center.getX(), center.getY());
				Vector2f lineMiddle = new Vector2f((line.getP1().getX()+line.getP2().getX())/2, (line.getP1().getY()+line.getP2().getY())/2);
				List<Point> remaining = t.getPoints();
				remaining.removeAll(line.getPoints());
				Vector2f v1;
				if(arePointsOnTheSameSideOfLine(line, center, remaining.get(0))) {
					v1 = Vector2f.sub(lineMiddle, c, null);
				} else {
					v1 = Vector2f.sub(c, lineMiddle, null);
				}
				Vector2f intersection = findIntersectionWithScreenInDirection(width, height, c, v1);
				if(intersection != null) {
					voronoi.edges.add(new Line(center, new Point(intersection.getX(), intersection.getY())));
				}
			} else {
				System.err.println("Invalid lineToTriangles entry, with " + triangles.size() + " triangles.");
			}
		}
		
		return voronoi;
	}
	
	private static boolean arePointsOnTheSameSideOfLine(Line l, Point p1, Point p2) {
		double v1 = implicitLineEquationValue(l, p1);
		double v2 = implicitLineEquationValue(l, p2);
		return (v1 >= 0 && v2 >= 0) || (v1 <= 0 && v2 <= 0);
	}
	
	private static double implicitLineEquationValue(Line l, Point p) {
		Point p1 = l.getP1();
		Point p2 = l.getP2();
		return (p1.getY()-p2.getY())*p.getX() - (p1.getX()-p2.getX())*p.getY() - (p1.getY()-p2.getY())*p1.getX()+(p1.getX()-p2.getX())*p1.getY();
	}
	
	private static Vector2f findIntersectionWithScreenInDirection(int width, int height, Vector2f t1, Vector2f v1) {
		List<Vector2f> corners = new ArrayList<>();
		corners.add(new Vector2f(0, 0));
		corners.add(new Vector2f(0, height));
		corners.add(new Vector2f(width, height));
		corners.add(new Vector2f(width, 0));
		List<Vector2f> directions = new ArrayList<>();
		directions.add(new Vector2f(0, 1));
		directions.add(new Vector2f(1, 0));
		directions.add(new Vector2f(0, -1));
		directions.add(new Vector2f(-1, 0));
		
		float t = Float.POSITIVE_INFINITY;
		for(int i = 0; i < 4; i++) {
			Vector2f t2 = corners.get(i);
			Vector2f v2 = directions.get(i);
			
			float tNew = (t2.getX()*v2.getY() - t2.getY()*v2.getX() - t1.getX()*v2.getY() + v2.getX()*t1.getY()) /
					   (v1.getX()*v2.getY() - v1.getY()*v2.getX());
			
			if(tNew > 0 && tNew < t) {
				t = tNew;
			}
		}
		if(t == Float.POSITIVE_INFINITY) {
			return null;
		}
		return Vector2f.add(t1, (Vector2f)v1.scale(t), null);
	}
	
	public List<Line> getEdges() {
		return edges;
	}
}
