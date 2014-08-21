package processing;
import processing.core.PApplet;
import processing.core.PVector;


public class Particle {
	
	private PApplet p;	
	public PVector location;
	public PVector velocity;
	public PVector acceleration;	
	
	public boolean alive;
	
	
	public Particle(PApplet p) {
		this.p = p;		
	}
	
	public void setup(PVector location) {				
		this.location = location.get();
		velocity = new PVector(0,0);
		acceleration = new PVector(0,0);
	}
	
	public void update() { 
		velocity.add(acceleration);
		location.add(velocity);
	}
	
	public void draw() {
		p.stroke(0);
		p.fill(255);
		p.ellipse(location.x, location.y, 3,3);
	}
	
}
