package view;
import model.Event;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Particle {
	
	// the PApplet where the particle is drew
	private PApplet p;
	
	// the data that this particle represent
	public Event event;
	
	// the position X,Y
	public PVector location;
	
	// the particle angle (in degrees) from the Emitter center
	private float angle;
	
	// the particle speed
	public PVector velocity;
	
	// the particle acceleration
	public PVector acceleration;
	
	// the particle hue (H) in the HSB model
	private float hue;
	
	// the particle brightness (B) in the HSB model
	public float brightness;
	
	// the particle size in pixels
	public float size;
	
	/**
	 * Create a new particle to be displayed in the PApplet p
	 * @param p	the PApplet where the particle is displayed
	 * @param event the data associated to the particle
	 */
	public Particle(PApplet p, Event event) {
		this.p = p;		
		this.event = event;	
	}
	
	// Configure the particle according to the parameters in argument
	public void setup(PVector location, float size, float angle, 
			float baseVelocity, float baseAcceleration, float hue, 
			float brightness) {
		
		this.size = size;
		this.location = location.get();
		this.angle = angle;
		
		this.velocity = new PVector(baseVelocity, baseVelocity);		
		this.acceleration = new PVector(baseAcceleration, baseAcceleration);
		
		this.velocity.rotate(PApplet.radians(this.angle));
		this.acceleration.rotate(PApplet.radians(this.angle));		
		
		this.hue = hue;
		this.brightness = brightness;
	}

	// Update the particle position and speed (by one unit of time)
	public void update() { 
		velocity.add(acceleration);
		location.add(velocity);		
	}
	
	// Draw the particle on the PApplet
	public void draw(float backgroundBrightness) {
	
		p.colorMode(PConstants.HSB, 360, 100, 100);		
		//p.noStroke();				
		p.stroke(hue, 100, backgroundBrightness);
		p.fill(hue, 100, brightness);
		p.ellipse(location.x, location.y, size, size);
	}
	
}
