package processing;
import processing.core.PApplet;
import processing.core.PVector;


public class Particle {
	
	private PApplet p;	
	public PVector location;
	public PVector velocity;
	public PVector acceleration;	
	
	public float color;
	public float size;	
	
	public Particle(PApplet p) {
		this.p = p;		
	}
	
	public void setup(PVector location, Params params) {				
		this.location = location.get();
		velocity = new PVector(params.particleVelocity, params.particleVelocity);
		acceleration = new PVector(params.particleAcceleration, params.particleAcceleration);
		size = params.particleSize;
	}
	
	public void update() { 
		velocity.add(acceleration);
		location.add(velocity);		
	}
	
	public void draw() {
		p.stroke(0);
		
		p.colorMode(p.HSB,360,100,100);
		p.fill(color, 100, 100);
		p.ellipse(location.x, location.y, size, size);
	}
	
}
