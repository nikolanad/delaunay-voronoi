package input;

import org.lwjgl.input.Keyboard;

public class VisibilityControls {
	
	private boolean delaunay = true;
	private boolean voronoi = false;
	private boolean circumcircles = false;
	
	private boolean delaunayControl = false;
	private boolean voronoiControl = false;
	private boolean circumcirclesControl = false;
		
	public VisibilityControls() {
	}
	
	public VisibilityControls(boolean delaunay, boolean voronoi, boolean circumcircles) {
		this.delaunay = delaunay;
		this.voronoi = voronoi;
		this.circumcircles = circumcircles;
	}
	
	public void update() {
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if(!delaunayControl) {
				delaunay = !delaunay;
			}
			delaunayControl = true;
		} else {
			delaunayControl = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_V)) {
			if(!voronoiControl) {
				voronoi = !voronoi;
			}
			voronoiControl = true;
		} else {
			voronoiControl = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_C)) {
			if(!circumcirclesControl) {
				circumcircles = !circumcircles;
			}
			circumcirclesControl = true;
		} else {
			circumcirclesControl = false;
		}
	}

	public boolean isDelaunayVisible() {
		return delaunay;
	}
	
	public boolean isVoronoiVisible() {
		return voronoi;
	}
	
	public boolean areCircumcirclesVisible() {
		return circumcircles;
	}
}
