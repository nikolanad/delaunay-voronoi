package demo;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import entities.Point;
import entities.Triangle;
import input.VisibilityControls;
import renderEngine.DisplayManager;
import renderEngine.Renderer;
import voronoi.DelaunayTriangulation;
import voronoi.VoronoiGraph;

public class DelaunayAndVoronoiGenerator {
	
	private static boolean mb0down = false;

	public static void main(String[] args) throws LWJGLException {
		DisplayManager.createDisplay("Delaunay and Voronoi");

		Renderer renderer = new Renderer();
		renderer.initialize();
		
		VisibilityControls controls = new VisibilityControls();
		
		Triangle superTriangle = new Triangle(new Point(0, 0), new Point(0, 2 * Display.getHeight()), new Point(2 * Display.getWidth(), 0));
		DelaunayTriangulation savedStateDelaunay = new DelaunayTriangulation(superTriangle);
		DelaunayTriangulation  delaunay = savedStateDelaunay.copy();
		VoronoiGraph voronoi = VoronoiGraph.createFromDelaunay(savedStateDelaunay, Display.getWidth(), Display.getHeight());
		while(!Display.isCloseRequested()) {
			renderer.prepare();
			controls.update();
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				savedStateDelaunay = new DelaunayTriangulation(superTriangle);
				delaunay = savedStateDelaunay.copy();
				voronoi = VoronoiGraph.createFromDelaunay(delaunay, Display.getWidth(), Display.getHeight());
			}
			if(Mouse.isButtonDown(0)) {
				mb0down = true;
				delaunay = savedStateDelaunay.copy();
				delaunay.addPoint(new Point(Mouse.getX(), Mouse.getY()));
				voronoi = VoronoiGraph.createFromDelaunay(delaunay, Display.getWidth(), Display.getHeight());
			} else if(!Mouse.isButtonDown(0) && mb0down) {
				savedStateDelaunay.addPoint(new Point(Mouse.getX(), Mouse.getY()));
				voronoi = VoronoiGraph.createFromDelaunay(savedStateDelaunay, Display.getWidth(), Display.getHeight());
			}
			if(!Mouse.isButtonDown(0)) {
				mb0down = false;
			}
			
			if(controls.areCircumcirclesVisible()) {
				for(Triangle t : delaunay.getTriangles()) {
					renderer.renderCircle(t.getCircumcircle(), 30, 0.4f, 0.4f, 0.4f);
				}
			}
			
			if(controls.isDelaunayVisible()) {
				renderer.renderTriangles(delaunay.getTriangles(), 1f, 0.2f, 0.2f);
			}
			
			if(controls.isVoronoiVisible()) {
				renderer.renderLines(voronoi.getEdges(), 1f, 1f, 0.3f);
			}
			renderer.renderPoints(delaunay.getPoints(), 1f, 1f, 1f);
			DisplayManager.updateDisplay();
		}
		
		DisplayManager.closeDisplay();
	}

}
