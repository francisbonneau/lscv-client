package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import model.Event;
import model.Params;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Emitter {

    private PApplet p;
    private Hud hud;
    private int id;
    public String host;
    public float centerX;
    public float centerY;

    public List<Particle> particlesList;
    public List<EmitterLabel> labelsList;

    public EmitterSubdivider subdivisions;

    // stats
    public long eventsDisplayedCount;
    public long syscallDisplayedCount;
    public long eventsTotalCount;
    public long syscallTotalCount;

    public Emitter(PApplet p, Hud hud, int id, int x, int y) {
        this.p = p;
        this.hud = hud;
        this.id = id;
        this.centerX = x;
        this.centerY = y;

        this.host = "";

        particlesList = new ArrayList<>();
        labelsList = new ArrayList<>();

        eventsDisplayedCount = 0;
        syscallDisplayedCount = 0;
        eventsTotalCount = 0;
        syscallTotalCount = 0;

        subdivisions = new EmitterSubdivider(this);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getID() {
        return this.id;
    }

    public Hud getHud() {
        return this.hud;
    }


    public void addParticles(ArrayList<Event> newData) {

        String divisionsAttribute = "process";

        subdivisions.addDivisions(newData, divisionsAttribute);
        subdivisions.adjustDivisionsSizes();

        // Add the labels to each emitter subdivision
        Iterator<String> it = subdivisions.divisions.keySet().iterator();
        while (it.hasNext()) {
            String divisionID = it.next();
            float Min = subdivisions.getDivisionStartAngle(divisionID);
            float Max = subdivisions.getDivisonEndAngle(divisionID);
            float angle = (Min + Max)/2 + 45;

            float radius = hud.params.emitterRadius/2 + 35;
            float labelX = (float) Math.cos(PApplet.radians(angle)) * radius + centerX;
            float labelY = (float) Math.sin(PApplet.radians(angle)) * radius + centerY;

            labelsList.add(new EmitterLabel(p, divisionID, 15,
                    hud.colorPalette.get(divisionID), labelX, labelY));
        }

        // for each process in the list
        Iterator<Event> events = newData.iterator();
        while (events.hasNext()) {
            Event event = events.next();

            String divisionID = event.attributes.get(divisionsAttribute);

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
            } else if (hud.params.emitterRadius/2 - distance < 40) {
                particle.brightness =  particle.brightness - 10;
            }
        }

    }

    // Draw all the particles
    public void drawParticles(float backgroundBrightness) {
        for (Particle particle : particlesList) {
             particle.draw(backgroundBrightness, hud.params.drawCirclesStrokes);
        }
    }

    // Draw the emitter labels
    public void drawLabels() {
        for (EmitterLabel label : labelsList) {
            label.drawLabel();
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

    // Draw all the components of the emitter
    public void draw() {

        drawParticles(hud.params.backgroundBrightness);

        if (hud.params.displayEmitterLabels)
            drawLabels();

        if (hud.params.displayEmitterRadius)
            drawRadius(hud.params.backgroundBrightness,
                hud.params.emitterRadiusBrightness, hud.params.emitterRadius);
    }



}
