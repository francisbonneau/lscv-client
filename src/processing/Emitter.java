package processing;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.msgpack.type.RawValue;


public class Emitter {
	
	private PApplet p;	
	public int centerX;
	public int centerY;	
	
	public List<Particle> particlesList;

	public Emitter(PApplet p, int x, int y) {
		this.p = p;
		this.centerX = x;
		this.centerY = y;
		particlesList = new ArrayList<>();
	}
	
	public void addParticles(HashMap<String, HashMap<Integer, Integer>> newData, Params params) {
		
		int elementsCount = newData.size();
		
		// example of a key : compiz
		Iterator<String> keys = newData.keySet().iterator();
		// value ex : 5100 => 1, 12400 => 3, 30000 => 2,
		Iterator<HashMap<Integer, Integer>> values = newData.values().iterator();
		
		int i = 1;
		while (keys.hasNext()) {
			String key = keys.next();
			HashMap<Integer, Integer> value = values.next();
			
			Iterator<Integer> k = value.keySet().iterator();
			Iterator<Integer> v = value.values().iterator();
			
			float angle = PApplet.map(i, 1, elementsCount, 1, 360);
			
			while(k.hasNext()) { 
				int latency = k.next();
				int eventCount = v.next();
				
				Particle newP = new Particle(p);
				newP.setup(new PVector(centerX, centerY), params);
											
				newP.velocity.rotate(angle);
				newP.size = newP.size + eventCount;
				
				particlesList.add(newP);
			}
			i++;
		}
		
		//p.map(value, start1, stop1, start2, stop2)		
	}
	
	public void updateParticles() {		
		for (Particle particle : particlesList) {
			particle.update();
		}
	}
	
	public void drawParticles() {		
		for (Particle particle : particlesList) {
			particle.draw();
		}		
	}
	
	public void drawRadius(int color, float radius) {
		// draw emitters radius
		p.stroke(color);
		p.fill(0);
		p.ellipse(centerX, centerY, radius, radius);
	}
	
	
	public void update(Params params) {
		updateParticles();
	}
	
	public void draw(Params params) {
		
		if (params.drawEmitterRadius)
			drawRadius(params.emitterRadiusColor, 
				params.emitterRadius);
					
		drawParticles();
	}
	
}
