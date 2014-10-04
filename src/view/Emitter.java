package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import model.Event;
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
	
	// It is exactly like HashMap, but presents the items in the insertion order.	
	// Key : the unique value (ex: process name) used to separate emitter regions, 
	// Value : the number associated (# of syscall) with that region
	public LinkedHashMap<String, Integer> regionsSize;
	
	HashMap<String, Float> divisionsLowerAngles;
	
	HashMap<String, Float> divisionsHigherAngle;
	
	private long regionsTotalSize;
	
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
		
		regionsSize = new LinkedHashMap<String, Integer>();
		divisionsLowerAngles = new HashMap<String, Float>();
		divisionsHigherAngle = new HashMap<String, Float>();
		
		eventsDisplayedCount = 0;
		syscallDisplayedCount = 0;
		eventsTotalCount = 0;
		syscallTotalCount = 0;
		regionsTotalSize = 0;
		
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getID() {
		return this.id;
	}
	
	private void calculateDivisionsDistribution(ArrayList<Event> newData) {
		
		// the filtering ordering should be by username, process name, then syscall
		for (Event e : newData) {
			
			if (regionsSize.containsKey(e.processName)) {
				int lastVal = regionsSize.get(e.processName);
				regionsSize.put(e.processName, lastVal + e.syscallNumber);
			}
			else {				
				// new process
				regionsSize.put(e.processName, e.syscallNumber);
										
				// Get a new color for the new division
				float newColor = hud.colorGenerator.getNewColorHue();
				hud.colorPalette.put(e.processName, newColor);
				
			}		
			regionsTotalSize += e.syscallNumber;			
		}
		
	}
	
	private void divideEmitter() {
		
		// divide the circle according to the event distribution
		Iterator<String> eventDistNames = regionsSize.keySet().iterator();
		Iterator<Integer> eventsDistCount = regionsSize.values().iterator();
		
		divisionsLowerAngles = new HashMap<String, Float>();
		divisionsHigherAngle = new HashMap<String, Float>();
		
		float lastAngle = 0;
		labelsList = new ArrayList<>();	
		
		while(eventDistNames.hasNext()) {
			String procName = eventDistNames.next();
			int size = eventsDistCount.next();
			
			float relativeSize = PApplet.map(size, 0, regionsTotalSize, 0, 360);
						
			divisionsLowerAngles.put(procName, lastAngle);
			divisionsHigherAngle.put(procName, lastAngle + relativeSize);
			lastAngle = lastAngle + relativeSize;
			
			float Min = divisionsLowerAngles.get(procName);
			float Max = divisionsHigherAngle.get(procName);
			float angle = (Min + Max)/2 + 45;
			float radius = hud.params.emitterRadius/2 + 35;
			
			float labelX = (float) Math.cos(PApplet.radians(angle)) * radius + centerX;
			float labelY = (float) Math.sin(PApplet.radians(angle)) * radius + centerY;
			
			labelsList.add(new EmitterLabel(p, procName, 15, 
				hud.colorPalette.get(procName), labelX, labelY));
		}
		
	}
		
	public void addParticles(ArrayList<Event> newData) {
					
		calculateDivisionsDistribution(newData);
		
		divideEmitter();
			
		// for each process in the list
		Iterator<Event> events = newData.iterator();
		while (events.hasNext()) {
			Event event = events.next();		
			
			Iterator<Integer> syscallName = event.latencyBreakdown.keySet().iterator();			
			
			float Min = divisionsLowerAngles.get(event.processName);
			float Max = divisionsHigherAngle.get(event.processName);			
			float angle = Min + (int)(Math.random() * ((Max - Min) + 1));			
		
			while(syscallName.hasNext()) {
				
				// update the hud latencies displayed
				int latency = syscallName.next();
				if (hud.smallestEvtLatency > latency)
					hud.smallestEvtLatency = latency;
				if (hud.biggestEvtLatency < latency)
					hud.biggestEvtLatency = latency;
			 				
				Particle newParticle = new Particle(p, event);
								
				float hue = this.hud.colorPalette.get(event.processName);				
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
		
		//boolean particleArgsDisplayed = false;
				
		for (Particle particle : particlesList) {
			 particle.draw(backgroundBrightness);
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
