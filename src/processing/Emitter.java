package processing;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.msgpack.type.RawValue;

import data.Event;


public class Emitter {
	
	private PApplet p;	
	public int centerX;
	public int centerY;	
	
	public List<Particle> particlesList;
	
	HashMap<String, Integer> particlesCount;

	public Emitter(PApplet p, int x, int y) {
		this.p = p;
		this.centerX = x;
		this.centerY = y;
		particlesList = new ArrayList<>();
	}
	
	public void addParticles(ArrayList<Event> newData, Params params) {
		
		int elementsCount = newData.size();
		
		
		
//		// example of a key : compiz
//		Iterator<String> keys = newData.keySet().iterator();
//		// value ex : 5100 => 1, 12400 => 3, 30000 => 2,
//		Iterator<HashMap<Integer, Integer>> values = newData.values().iterator();
		
		Iterator<Event> events = newData.iterator();
		
		System.out.println(newData.size());
		
		int i = 1;
		// for each process in the list
		while (events.hasNext()) {
			
			Event event = events.next();			
		
			Iterator<Integer> syscallName = event.latencyBreakdown.keySet().iterator();
			Iterator<Integer> syscallData = event.latencyBreakdown.values().iterator();
			
			float angle = PApplet.map(i, 1, elementsCount, 1, 360);			
			float angleIncr = 360 / elementsCount;
			
			//p.line((float) Math.cos(angle) * params.emitterRadius, 
			//(float) Math.sin(angle) * params.emitterRadius, centerX, centerY);			
			
			//float randomIncr = p.random(0, 0.5f);
			
//			p.println("elementsCount" + elementsCount);
//			p.println("angle" + angle);
//			p.println("angleincr" + angleIncr);
						
			while(syscallName.hasNext()) { 
				int latency = syscallName.next();
				int eventCount = syscallData.next();
				
				Particle newP = new Particle(p, event);
				newP.setup(new PVector(centerX, centerY), params);											
								
				newP.color = angle;
				
				//newP.velocity.rotate(angle);
				//newP.acceleration.rotate(angle);
				
				float newAcceleration = PApplet.map(latency, 1, 1000000, 0.1f, 100);				
				newP.acceleration = new PVector(newAcceleration,newAcceleration);
				
				newP.velocity.rotate(angle);
				newP.acceleration.rotate(angle);
																			
				newP.size = (float) Math.sqrt(newP.size * eventCount);
				
				particlesList.add(newP);
			}
			i++;
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
	}
	
}
