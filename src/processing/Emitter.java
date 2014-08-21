package processing;

import processing.core.PApplet;
import java.util.List;


public class Emitter {
	
	private PApplet p;
	public int x;
	public int y;
	
	public List<Particle> particlesList;

	public Emitter(PApplet p, int x, int y) {		
		this.x = x;
		this.y = y;		
		this.p = p;
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
	
}
