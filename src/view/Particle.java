package view;
import java.util.Iterator;

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

    public float saturation;

    // the particle brightness (B) in the HSB model
    public float brightness;

    // the particle size in pixels
    public float size;

    /**
     * Create a new particle to be displayed in the PApplet p
     * @param p	the PApplet where the particle is displayed
     * @param event the data associated to the particle
     */
    public Particle(final PApplet p, final Event event) {
        this.p = p;
        this.event = event;
    }

    // Configure the particle according to the parameters in argument
    public final void setup(final PVector location, final float size, final float angle,
            final float baseVelocity, final float baseAcceleration, final float hue,
            final float brightness) {

        this.size = size;
        this.location = location.get();
        this.angle = angle;

        this.velocity = new PVector(baseVelocity, baseVelocity);
        this.acceleration = new PVector(baseAcceleration, baseAcceleration);

        this.velocity.rotate(PApplet.radians(this.angle));
        this.acceleration.rotate(PApplet.radians(this.angle));

        this.hue = hue;
        this.saturation = 90;
        this.brightness = brightness;
    }

    // Update the particle position and speed (by one unit of time)
    public final void update() {
        velocity.add(acceleration);
        location.add(velocity);
    }

    // Draw the particle on the PApplet
    public final void draw(final float backgroundBrightness, final boolean drawStroke) {

        p.colorMode(PConstants.HSB, 360, 100, 100);

        // if the user mouse is hovering the particle
        if (p.mouseX > location.x - size && p.mouseX < location.x + size
            && p.mouseY > location.y - size && p.mouseY < location.y + size) {

            p.fill(hue, 0, brightness);
            p.ellipse(location.x, location.y, size, size);
            p.fill(hue, saturation, brightness);

            String args = event.toString() + "\n\n\n";
            Iterator<String> it = event.arguments.values().iterator();
            while(it.hasNext()) {
                String s = it.next();
                args += s + "\n";
            }
            p.text(args, 50, 50);

        } else {

            if (drawStroke) {
                p.stroke(hue, saturation, backgroundBrightness);
            } else {
            	p.noStroke();
            }

            p.fill(hue, saturation, brightness);
            p.ellipse(location.x, location.y, size, size);

        }

    }

}
