package processing;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import org.msgpack.type.RawValue;


public class Emitter {
	
	private PApplet p;
	public int centerX;
	public int centerY;	
	
	public List<Particle> particlesList;

	public Emitter(PApplet p, int x, int y) {
		this.p = p;
		this.centerX = x;
		this.centerY = y;
		particlesList = new ArrayList<>();
	}
	
	public void addParticle() {
		
	}
	
	public void updateParticles() {		
		for (Particle particle : particlesList) {
			particle.update();
		}
	}
	
	public void drawParticles() {		
		for (Particle particle : particlesList) {
			particle.draw();
		}		
	}
	
	public void drawRadius(int color, float radius) {
		// draw emitters radius
		p.stroke(color);
		p.fill(0);
		p.ellipse(centerX, centerY, radius, radius);
	}
	
	public void draw(Params params) {
		
		if (params.drawEmitterRadius)
			drawRadius(params.emitterRadiusColor, 
				params.emitterRadius);
		
		drawParticles();
	}
	
}
