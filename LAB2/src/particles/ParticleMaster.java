package particles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import renderEngine.Loader;

public class ParticleMaster {

	private static Map<ParticleTexture, List<Particle>> particles = new HashMap<>();
	private static ParticleRenderer renderer;
	
	public static void init(Loader loader, Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(loader, projectionMatrix);
	}
	
	public static void update(Camera camera) {
		if(particles.size() == 0) {
			return;
		}
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while(mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			for(int i = list.size() - 1; i >= 0; i--) {
				Particle particle = list.get(i);
				boolean stillAlive = particle.update(camera);
				if(!stillAlive) {
					list.remove(i);
				}
			}
			if(list.isEmpty()) {
				mapIterator.remove();
			}
			Collections.sort(list, (p1, p2) -> Double.compare(p2.getDistanceToCameraSquared(), p1.getDistanceToCameraSquared()));
		}
	}
	
	public static void renderParticles(Camera camera) {
		renderer.render(particles, camera);
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
	
	public static void addParticle(Particle particle) {
		List<Particle> list = particles.get(particle.getTexture());
		if(list == null) {
			list = new ArrayList<>();
			particles.put(particle.getTexture(), list);
		}
		list.add(particle);
	}
}
