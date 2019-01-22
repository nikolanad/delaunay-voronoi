package particles;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;

public class ParticleSystem {
	
	private Random rand;
	
	private Vector3f position;
	
	private Vector3f direction;
	private float directionDeviation;
	
	private float pps;
	private float initialSpeed;
	private float gravityFactor;
	private float lifetime;
	
	private ParticleTexture texture;
	
	private float sizeMin = 1;
	private float sizeMax = 1;
	private float rotMin = 0;
	private float rotMax = 0;
	private float speedDeviation = 0;
	
	private float particlesToEmit = 0;
	
	public ParticleSystem(Vector3f position, Random rand, float pps, float initialSpeed,
			float gravityFactor, float lifetime, ParticleTexture texture) {
		this.position = position;
		this.rand = rand;
		this.pps = pps;
		this.initialSpeed = initialSpeed;
		this.gravityFactor = gravityFactor;
		this.lifetime = lifetime;
		this.texture = texture;
	}
	
	public void generateParticles() {
		float delta = DisplayManager.getFrameTimeSeconds();
		particlesToEmit += pps * delta;
		
		int count = (int) Math.floor(particlesToEmit);
		for(int i = 0; i <count; i++) {
			emitParticle();
		}
		particlesToEmit -= count;
	}
	
	public void setScaleRange(float sizeMin, float sizeMax) {
		this.sizeMin = sizeMin;
		this.sizeMax = sizeMax;
	}
	
	public void setRotationRange(float rotMin, float rotMax) {
		this.rotMin = rotMin;
		this.rotMax = rotMax;
	}
	
	public void setSpeedDeviation(float speedDeviation) {
		this.speedDeviation = speedDeviation;
	}
	
	public void setCone(Vector3f direction, float angle) {
		this.direction = new Vector3f(direction);
		this.direction.normalise();
		this.directionDeviation = (float) Math.toRadians(angle);
	}
	
	private void emitParticle() {
		Vector3f velocity = null;
		if(direction != null) {
			velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
		} else {
			velocity = generateRandomUnitVector();
		}
		velocity.scale(initialSpeed + (rand.nextFloat() * 2 * speedDeviation - speedDeviation));
		float rotation = randomValueFromInterval(rotMin, rotMax);
		float size = randomValueFromInterval(sizeMin, sizeMax);
		new Particle(texture, new Vector3f(position), velocity, gravityFactor, lifetime, rotation, size);
	}
	
	private float randomValueFromInterval(float min, float max) {
		return rand.nextFloat() * (max - min) + min;
	}
	
	private Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle) {
		float cosAngle = (float) Math.cos(angle);
		float theta = (float) (rand.nextFloat() * 2f * Math.PI);
		float z = cosAngle + (rand.nextFloat() * (1 - cosAngle));
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));
		
		Vector4f direction = new Vector4f(x, y, z, 1);
		if (coneDirection.x != 0 || coneDirection.y != 0 || (coneDirection.z != 1 && coneDirection.z != -1)) {
			Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0, 0, 1), null);
			rotateAxis.normalise();
			float rotateAngle = (float) Math.acos(Vector3f.dot(coneDirection, new Vector3f(0, 0, 1)));
			Matrix4f rotationMatrix = new Matrix4f();
			rotationMatrix.setIdentity();
			rotationMatrix.rotate(-rotateAngle, rotateAxis);
			Matrix4f.transform(rotationMatrix, direction, direction);
		} else if (coneDirection.z == -1) {
			direction.z *= -1;
		}
		Vector3f result = new Vector3f(direction);
		result.normalise();
		return result;
	}
	
	private Vector3f generateRandomUnitVector() {
		float theta = (float) (rand.nextFloat() * 2f * Math.PI);
		float z = (rand.nextFloat() * 2) - 1;
		float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
		float x = (float) (rootOneMinusZSquared * Math.cos(theta));
		float y = (float) (rootOneMinusZSquared * Math.sin(theta));
		
		Vector3f result = new Vector3f(x, y, z);
		result.normalise();
		return result;
	}
}
