package processing;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import data.DataAggregator;

public class Hud {
	
	private PApplet p;	
	private DataAggregator da;
		
	private ArrayList<HashMap<String, HashMap<Integer, Integer>>> displayedData;
	
	private ArrayList<Emitter> emitters;
	
	public Hud(PApplet p) {		
		this.p = p;		
		
		displayedData = new ArrayList<>();
		emitters = new ArrayList<>();
	}

	public void setDataSource(DataAggregator da) {
		this.da = da;			
	}
	
	public void updateDisplayedData(Params params) {
		
		HashMap<String, HashMap<Integer, Integer>> newData = da.data.poll();
		while(newData != null) {	
					
			displayedData.add(newData);
			
			// TODO figure out to which emitter the data is supposed to go
			Emitter em = emitters.get(0);
			em.addParticles(newData, params);
			
			newData = da.data.poll();			
		}
		
	}
	
	public void addEmitter() {
		
		if (emitters.size() == 0) {			
			// if there is only one emitter, center it			
			int centerX = this.p.width/2;
			int centerY = this.p.height/2;
			Emitter em = new Emitter(this.p, centerX, centerY);
			emitters.add(em);
		} else {
			// TODO support multiples emitters via panels
		}
		
	}
	
	/**
	 * Draw the hud (the particles emitters)
	 */
	public void draw(Params params) {		
		for (Emitter em : emitters) {
			em.update(params);
			em.draw(params);
		}
	}

}
