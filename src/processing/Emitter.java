package processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import data.Event;


public class Emitter {
	
	private PApplet p;	
	public int centerX;
	public int centerY;	
	
	public List<Particle> particlesList;
	
	public List<EmitterLabel> labelsList;
	
	// It is exactly like HashMap, except that when you iterate over it,
	// it presents the items in the insertion order.
	public LinkedHashMap<String, Integer> eventDistribution;
	public long eventsTotalCount;
	public HashMap<String, Float> eventDistributionColor;
	
	int lastSectionsCount;
	HashMap<String, Float> eventLowerAngle;
	HashMap<String, Float> eventHigherAngle;

	public Emitter(PApplet p, int x, int y) {
		this.p = p;
		this.centerX = x;
		this.centerY = y;
		particlesList = new ArrayList<>();
		
		eventDistribution = new LinkedHashMap<String, Integer>();
		eventsTotalCount = 0;
		eventDistributionColor = new HashMap<String, Float>();
		
		lastSectionsCount = 0;
		eventLowerAngle = new HashMap<String, Float>();
		eventHigherAngle = new HashMap<String, Float>();
		
		labelsList = new ArrayList<>();
	}
	
	public void addParticles(ArrayList<Event> newData, Params params) {
		
		int elementsCount = newData.size();
 
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
										
				//Random randomno = new Random();				
				//float hue = randomno.nextFloat() * 200;
				float hue = 0 + (int)(Math.random() * ((255 - 0) + 1));
				eventDistributionColor.put(e.processName, hue);
			}
			eventsTotalCount = eventsTotalCount + 1;
		}
		
		// if a new process name appeared, recalculate sections
		//if (eventDistribution.size() != lastSectionsCount) {
								
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
				
				// also add labels
//				float Min = lastAngle + 10;
//				float Max = lastAngle + relativeSize - 10;
				//float angle = p.radians((Max - Min) /2);				
				float angle = lastAngle + relativeSize;				
				float radius = 100;
				float textXposition = (float) Math.cos(angle) * (radius + centerX);
				float textYposition = (float) Math.sin(angle) * (radius + centerY);				
				float color = this.eventDistributionColor.get(procName); 				
				EmitterLabel label = new EmitterLabel(p, procName, 15, color, textXposition, textYposition);
				labelsList.add(label);
			}
										
			lastSectionsCount = eventDistribution.size();			
		//}
	
 
		// for each process in the list
		while (events.hasNext()) {
			
			Event event = events.next();			
		
			Iterator<Integer> syscallName = event.latencyBreakdown.keySet().iterator();
			Iterator<Integer> syscallData = event.latencyBreakdown.values().iterator();
			
			//float angle = PApplet.map(i, 1, elementsCount, 1, 360);
			
			float Min = eventLowerAngle.get(event.processName) + 10 ;
			float Max = eventHigherAngle.get(event.processName) - 10;
			
			//p.println("processName :" + event.processName);
			//p.println("Min : " + Min + " Max : " + Max);
			
			float angle = Min + (int)(Math.random() * ((Max - Min) + 1));			
			//float angle = (Max - Min) /2 ;
			//float angle = Max;
			
			//p.println("angle :" + angle);
			//float angleIncr = 360 / elementsCount;
			
//			p.println(" l : " + eventLowerAngle.get("redis-server").toString());
//			p.println(" h : " + eventHigherAngle.get("redis-server").toString());
//			//p.println(" c : " + eventDistributionColor.toString());
		
//			p.line((float) Math.cos(angle) * params.emitterRadius, 
//					(float) Math.sin(angle) * params.emitterRadius, centerX, centerY);			
			
			//float randomIncr = p.random(0, 0.5f);		
			//angle = angle + randomIncr;
						
			while(syscallName.hasNext()) { 
				int latency = syscallName.next();
				int eventCount = syscallData.next();
				
				Particle newP = new Particle(p, event);
				newP.setup(new PVector(centerX, centerY), params);
				newP.color = (float) eventDistributionColor.get(event.processName);
				
				float newAcceleration = PApplet.map(latency, 1, 1000000, 0.1f, 100);				
				newP.acceleration = new PVector(newAcceleration, newAcceleration);
				
				newP.velocity.rotate(PApplet.radians(angle));
				newP.acceleration.rotate(PApplet.radians(angle));
																			
				newP.size = (float) Math.sqrt(newP.size * eventCount);
				
				particlesList.add(newP);
			}
		}
		
		//p.map(value, start1, stop1, start2, stop2)		
	}
	
	public void updateParticles(Params params) {
		
		Iterator<Particle> it = particlesList.iterator();		
		while(it.hasNext()) { 
			Particle particle = it.next();
			particle.update();
			
			double distance = Math.sqrt(Math.pow(centerX - particle.location.x, 2) +  
					Math.pow(centerY - particle.location.y, 2));
									
			// check if the particle is outside of the emitter radius
			if ( distance > params.emitterRadius/2 ) {
				// if this is the case the particle is removed				
				it.remove();
			}
		}
		
	}
	
	public void drawLabels() { 
		for (EmitterLabel label : labelsList) {
			label.drawLabel();
		}
	}
	
	public void drawParticles() {		
		for (Particle particle : particlesList) {
			particle.draw();
		}
	}
	
	public void drawRadius(int color, float radius) {
		// draw emitters radius
		p.stroke(0,0,color);
		p.fill(0,0,10);
		p.ellipse(centerX, centerY, radius, radius);
	}	
	
	public void update(Params params) {
		updateParticles(params);
	}
	
	public void draw(Params params) {
		
		if (params.drawEmitterRadius)
			drawRadius(params.emitterRadiusColor, 
				params.emitterRadius);
					
		drawParticles();
		//drawLabels();
	}
	
}
