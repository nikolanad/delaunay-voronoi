package demo;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.FloatingCamera;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;

public class ParticleSystemDemo {

	public static void main(String[] args) {
		DisplayManager.createDisplay("Particle system demo");

		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);		
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		Camera camera = new FloatingCamera();
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"), 4);
		ParticleTexture particleTexture2 = new ParticleTexture(loader.loadTexture("smoke"), 8);
		
		Random rand = new Random();
		ParticleSystem system = new ParticleSystem(new Vector3f(0, 0, -5), rand, 100f, 0.7f, 0.0f, 2, particleTexture);
		system.setCone(new Vector3f(0, 1, 0), 30);
		system.setRotationRange(-45, 45);
		system.setScaleRange(0.25f, 0.5f);
		system.setSpeedDeviation(0f);
		
		ParticleSystem system2 = new ParticleSystem(new Vector3f(0, 1.4f, -5), rand, 20f, 0.2f, 0.0f, 10, particleTexture2);
		//system2.setCone(new Vector3f(0, 1, 0), 60);
		system2.setRotationRange(-45, 45);
		system2.setScaleRange(1.0f, 2.0f);
		system2.setSpeedDeviation(0f);
		
		while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			system.generateParticles();
			system2.generateParticles();
			
			ParticleMaster.update(camera);
			camera.update();
			renderer.prepare();
			ParticleMaster.renderParticles(camera);
			
			DisplayManager.updateDisplay();
		}
		
		ParticleMaster.cleanUp();
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
