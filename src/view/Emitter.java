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
	public int id;
	public String host;	
	public float centerX;
	public float centerY;	
	
	public List<Particle> particlesList;	
	public List<EmitterLabel> labelsList;
	
	// It is exactly like HashMap, except that when you iterate over it,
	// it presents the items in the insertion order.
	public LinkedHashMap<String, Integer> eventDistribution;
	public long eventsTotalCount;
	public HashMap<String, Float> eventDistributionColor;

	HashMap<String, Float> eventLowerAngle;
	HashMap<String, Float> eventHigherAngle;
	
	Integer lastEventDistSize;

	public Emitter(PApplet p, Hud hud, int x, int y) {
		this.p = p;
		this.hud = hud;		
		this.centerX = x;
		this.centerY = y;
		particlesList = new ArrayList<>();
		
		eventDistribution = new LinkedHashMap<String, Integer>();
		eventsTotalCount = 0;
		eventDistributionColor = new HashMap<String, Float>();

		eventLowerAngle = new HashMap<String, Float>();
		eventHigherAngle = new HashMap<String, Float>();
		
		labelsList = new ArrayList<>();
		lastEventDistSize = 0;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void addParticles(ArrayList<Event> newData, Params params) {
		
		Iterator<Event> events = newData.iterator();
		
		// the filtering ordering should be by username, process name, then syscall
		// but pocess name is a good default
		for (Event e : newData) {
			if (eventDistribution.containsKey(e.processName)) { 
				int lastVal = eventDistribution.get(e.processName);
				eventDistribution.put(e.processName, lastVal + 1);
			}					
			else {				
				// new process
				eventDistribution.put(e.processName, 1);
										
				// Generate a new color randomly
				float hue = 0 + (int)(Math.random() * ((360 - 0) + 1));				
				eventDistributionColor.put(e.processName, hue);
			}
			eventsTotalCount = eventsTotalCount + 1;
		}
		
							
		// divide the circle according to the event distribution
		Iterator<String> eventDistNames = eventDistribution.keySet().iterator();
		Iterator<Integer> eventsDistCount = eventDistribution.values().iterator();
		
		eventLowerAngle = new HashMap<String, Float>();
		eventHigherAngle = new HashMap<String, Float>();
		
		float lastAngle = 0;
		labelsList = new ArrayList<>();	
		
		while(eventDistNames.hasNext()) {
			String procName = eventDistNames.next();
			int size = eventsDistCount.next();
			
			float relativeSize = PApplet.map(size, 0, eventsTotalCount, 0, 360);
						
			eventLowerAngle.put(procName, lastAngle);
			eventHigherAngle.put(procName, lastAngle + relativeSize);
			lastAngle = lastAngle + relativeSize;
			
			float Min = eventLowerAngle.get(procName);
			float Max = eventHigherAngle.get(procName);
			float angle = (Min + Max)/2 + 45;
			float radius = params.emitterRadius/2 + 35;
			float textXposition = (float) Math.cos(PApplet.radians(angle)) * radius + centerX;
			float textYposition = (float) Math.sin(PApplet.radians(angle)) * radius + centerY;
			
			float color = this.eventDistributionColor.get(procName); 				
			EmitterLabel label = new EmitterLabel(p, procName, 15, color, textXposition, textYposition);
			labelsList.add(label);
		}
			
		// for each process in the list
		while (events.hasNext()) {
			Event event = events.next();		
			
			Iterator<Integer> syscallName = event.latencyBreakdown.keySet().iterator();
			Iterator<Integer> syscallData = event.latencyBreakdown.values().iterator();
			
			float Min = eventLowerAngle.get(event.processName);
			float Max = eventHigherAngle.get(event.processName);			
			float angle = Min + (int)(Math.random() * ((Max - Min) + 1));			
		
			while(syscallName.hasNext()) { 
				
				// update the hud latencies displayed
				int latency = syscallName.next();
				if (hud.smallestEvtLatency > latency)
					hud.smallestEvtLatency = latency;
				if (hud.biggestEvtLatency < latency)
					hud.biggestEvtLatency = latency;
				
				int eventCount = syscallData.next();
				
				Particle newParticle = new Particle(p, event);
								
				float hue = (float) eventDistributionColor.get(event.processName);				
				float brightness = 100;
				
				float size = (float) Math.sqrt(params.particleSize * eventCount);
								
				float velocity = PApplet.map(latency, hud.smallestEvtLatency, 
						hud.biggestEvtLatency, params.particleMinVelocity,
						params.particleMaxVelocity);
				
				float acceleration = params.particleAcceleration;
				
				newParticle.setup(new PVector(centerX, centerY), size,  angle,
							velocity, acceleration, hue, brightness);
				
				particlesList.add(newParticle);
			}
		}
	}
	
	/**
	 * Update all the particles position and speed 
	 * @param params
	 */
	public void updateParticles(Params params) {
		
		Iterator<Particle> it = particlesList.iterator();		
		while(it.hasNext()) { 
			
			Particle particle = it.next();
			particle.update();
			
			// check if the particle is outside of the emitter radius
			double distance = Math.sqrt(Math.pow(centerX - particle.location.x, 2) 
					+ Math.pow(centerY - particle.location.y, 2));
												
			if ( distance > params.emitterRadius/2 ) {
				// if this is the case the particle is removed				
				it.remove();
			}
		}
		
	}
		
	// Draw all the particles
	public void drawParticles() {		
		for (Particle particle : particlesList) {
			particle.draw();
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
	public void update(Params params) {
		updateParticles(params);
	}

	// Draw all the components of the emitter 
	public void draw(Params params) {
				
		p.text(id, centerX, centerY);
		
		drawParticles();
		
		if (params.displayEmitterLabels)
			drawLabels();
		
		if (params.displayEmitterRadius)
			drawRadius(params.backgroundBrightness, 
					params.emitterRadiusBrightness, params.emitterRadius);
	}
	
}
