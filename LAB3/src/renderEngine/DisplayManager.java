package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class DisplayManager {
	
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 576;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;
	
	public static void createDisplay(String title) {
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
			Display.setTitle(title);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
		
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f; 
		lastFrameTime = currentFrameTime;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
}
