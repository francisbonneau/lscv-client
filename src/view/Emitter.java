package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import model.Event;

/**
 * This class represent the circle where the events (particles) are displayed.
 * The name is from particle *emitter* because it handle the position of the
 * particles and the asssociated labels.
 * @author Francis Bonneau
 */
public class Emitter {

	private PApplet p;		// Where to draw the particles
    private Hud hud;		// Abstraction of multiples emitters
    private int id;			// The Emitter ID
    public String host;		// The data source hostname
    public float centerX;	// The X center position of the emmitter
    public float centerY;	// The Y center position of the emmitter

    public List<Particle> particlesList;	// The list of displayed particles
    public List<EmitterLabel> labelsList;	// The list of displayed labels
    public List<EmitterHalo> halosList;		// The list of displayed halos

	// The circle subdivisons - the circle is subdivided into section according
    // to the events distributions, and this class take care of that
    public EmitterSubdivider subdivisions;

    // True if the user mouse is over a displayed particle
    public boolean selectionActive = false;
    // The name of category of the currently selected particle
    public String selectionID;

    // Various counters to keep stats about the Emitter
    public long eventsDisplayedCount;
    public long syscallDisplayedCount;
    public long eventsTotalCount;
    public long syscallTotalCount;

    // Constructor
    public Emitter(PApplet p, Hud hud, int id, int x, int y) {

    	this.p = p;
        this.hud = hud;
        this.id = id;
        this.centerX = x;
        this.centerY = y;

        this.host = ""; // empty hostname on initialisation

        // create a new subdivider for this object
        subdivisions = new EmitterSubdivider(this);

        // initialise the lists
        particlesList = new ArrayList<Particle>();
        labelsList = new ArrayList<EmitterLabel>();
        halosList = new ArrayList<EmitterHalo>();

        // Populate the halos list according to the settings
        int halosNb = hud.params.emitterHalosIntervalsSec.length;
        for (int i = 0; i < halosNb; i++) {
            halosList.add(new EmitterHalo(this.p, this));
        }

        // initialise the stats counters
        eventsDisplayedCount = 0;
        syscallDisplayedCount = 0;
        eventsTotalCount = 0;
        syscallTotalCount = 0;
    }

    // Setup the emitter data source hostname
    public void setHost(String host) {
        this.host = host;
    }

    // Return the emitter ID
    public int getID() {
        return this.id;
    }

    // Return the hud associated to the emitter
    public Hud getHud() {
        return this.hud;
    }

   /**
    * Add particles to the emitter, based on the new data received. The new
    * particles will be added to the list, and updated and drawn every time
    * at every update() and draw() of the emitter
    */
    public void addParticles(ArrayList<Event> newData) {

        String divisionAttribute = hud.params.divisionAttribute;

        subdivisions.addDivisions(newData, divisionAttribute);
        subdivisions.addHalos(newData, divisionAttribute);

        subdivisions.adjustDivisionsSizes();
        subdivisions.adjustHalosSizes();

        // Add the labels to each emitter subdivision
        labelsList = new ArrayList<>();
        Iterator<String> it = subdivisions.currentDivisions.keySet().iterator();
        while (it.hasNext()) {

            String divisionID = it.next();

            float minAngle = subdivisions.getDivisionStartAngle(divisionID);
            float maxAngle = subdivisions.getDivisonEndAngle(divisionID);
            float emitterRadius = hud.params.emitterRadius;
            float labelColor = hud.colorPalette.get(divisionID);
            float labelsDistance = hud.params.emitterLabelsDistance;

            EmitterLabel label = new EmitterLabel(p, divisionID, 15, labelColor);
            label.calculateLabelPosition(minAngle, maxAngle, emitterRadius,
                    centerX, centerY, labelsDistance);
            labelsList.add(label);
        }

        // for each process in the list
        Iterator<Event> events = newData.iterator();
        while (events.hasNext()) {
            Event event = events.next();

            String divisionID = event.attributes.get(divisionAttribute);

            Iterator<Integer> syscallName = event.latencyBreakdown.keySet().iterator();

            float Min = subdivisions.getDivisionStartAngle(divisionID);
            float Max = subdivisions.getDivisonEndAngle(divisionID);

            float angle = Min + (float) Math.random() * ((Max - Min) + 1);

            while(syscallName.hasNext()) {

                // update the hud latencies displayed
                int latency = syscallName.next();
                if (hud.smallestEvtLatency > latency)
                    hud.smallestEvtLatency = latency;
                if (hud.biggestEvtLatency < latency)
                    hud.biggestEvtLatency = latency;

                Particle newParticle = new Particle(p, event);

                // determine the new paticle parameters
                float hue = this.hud.colorPalette.get(divisionID);
                float brightness = 100;

                float size = (float) Math.sqrt(hud.params.particleSize * event.syscallNumber);

                float velocity = PApplet.map(latency, hud.smallestEvtLatency,
                        hud.biggestEvtLatency, hud.params.particleMinVelocity,
                        hud.params.particleMaxVelocity);

                float acceleration = hud.params.particleAcceleration;

                newParticle.setup(new PVector(centerX, centerY), size,  angle,
                            velocity, acceleration, hue, brightness);

                particlesList.add(newParticle);

                eventsDisplayedCount += 1;
                syscallDisplayedCount += event.syscallNumber;
                eventsTotalCount += 1;
                syscallTotalCount += event.syscallNumber;
            }
        }
    }

    // Update all the particles position and speed
    public void updateParticles() {

        Iterator<Particle> it = particlesList.iterator();
        while(it.hasNext()) {

            Particle particle = it.next();
            particle.update();

            // check if the particle is outside of the emitter radius
            double distance = Math.sqrt(Math.pow(centerX - particle.location.x, 2)
                    + Math.pow(centerY - particle.location.y, 2));

            if ( distance > hud.params.emitterRadius/2 ) {
                // if this is the case the particle is removed
                it.remove();
                // and the stats are updated
                eventsDisplayedCount -= eventsDisplayedCount - 1;
                syscallDisplayedCount -= particle.event.syscallNumber;

            // if the particle is approching the radius, fade it out
            } else if (hud.params.emitterRadius/2 - distance < 60) {
                if (particle.brightness -8 >= hud.params.backgroundBrightness)
                    particle.brightness = particle.brightness - 8;
            }
        }

    }

    // Draw all the particles, and update the current selection if a particle
    // is selected by the user
    public void drawParticles(float backgroundBrightness) {

    	boolean selectionDetected = false;

        for (Particle particle : particlesList) {

            boolean particleSelected = particle.draw(backgroundBrightness,
            	hud.params.drawCirclesStrokes, selectionActive, selectionID);

            if (particleSelected) {
            	selectionActive = true;
                selectionID = particle.getDivisionID(hud.params.divisionAttribute);
                selectionDetected = true;
            }
        }

        if (!selectionDetected) {
        	selectionActive = false;
        }

    }

    // Draw the labels - around the circle
    public void drawLabels() {

    	boolean selectionDetected = false;

        for (EmitterLabel label : labelsList) {
        	boolean selectedLabel = false;

        	if (selectionActive && selectionID.equals(label.divisionID)) {
        		selectedLabel = true;
        	}
        	boolean labelSelected = label.draw(selectedLabel);

        	if (labelSelected) {
            	selectionActive = true;
                selectionID = label.divisionID;
                selectionDetected = true;
            }
        }

        if (!selectionDetected) {
        	selectionActive = false;
        }

    }

    // Draw others circles (halos) around the main circle to represent different
    // (longer) timeframes of events distribution
    public void drawHalos() {

        LinkedList<LinkedHashMap<String, EmitterSubdivision>> halosDivs =
                subdivisions.halosDivisions;

        Iterator<LinkedHashMap<String, EmitterSubdivision>> it =
                halosDivs.iterator();

        float brightness = 100;
        float distance = 40;
        for (EmitterHalo halo : halosList) {
            brightness -= 15;
            halo.draw(it.next(), distance, brightness);
            distance += 45;
        }

    }

    // Draw the Emitter radius circle
    public void drawRadius(float backgroundBrightness, float emitterRadiusColor,
            float radius) {
        p.colorMode(PConstants.HSB,360,100,100);
        p.stroke(0,0,emitterRadiusColor);
        p.noFill();
        p.ellipse(centerX, centerY, radius, radius);
    }

    // Update all the components of the emitter
    public void update() {
        if (!hud.params.displayPaused) {
            updateParticles();
        }
    }

    // Update the position of all the labels, used to realign the labels to
    // the middle of each section of the emitter (or section of the pie chart)
    // also used to readjust the labels distance to the center
    // TODO : Should maybe be merged with the update() method, the only reason
    // its not is to avoid the labels change position too frequently
    public void updateLabelsPositions() {
        for (EmitterLabel label : labelsList) {
            float minAngle = subdivisions.getDivisionStartAngle(label.divisionID);
            float maxAngle = subdivisions.getDivisonEndAngle(label.divisionID);
            float emitterRadius = hud.params.emitterRadius;
            float labelsDistance = hud.params.emitterLabelsDistance;

            label.calculateLabelPosition(minAngle, maxAngle,
                    emitterRadius, centerX, centerY, labelsDistance);
        }
    }

    // Draw all the components of the emitter
    public void draw() {

        drawParticles(hud.params.backgroundBrightness);

        if (hud.params.displayEmitterLabels)
            drawLabels();

        if (hud.params.displayEmitterRadius)
            drawRadius(hud.params.backgroundBrightness,
                hud.params.emitterRadiusBrightness, hud.params.emitterRadius);

        if (hud.params.displayEmitterHalos)
            drawHalos();
    }

}
