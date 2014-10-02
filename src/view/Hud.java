package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

import model.DataAggregator;
import model.Event;
import model.Params;
import processing.core.PApplet;

/**
 * The hud handle the update and drawing of one or multiples emitters,
 * and keeps state variables that need to be shared by each emitter, 
 * like the smallest event latency accross all data source for example.
 */
public class Hud {
	
	public PApplet p;
 	public ArrayList<Emitter> emitters;
 	
 	public Params params; 	
	public DataAggregator dataAgg;
	public HudRegionManager regionManager;
	
	// parameters shared by all emitters
	public float smallestEvtLatency = 1;
	public float biggestEvtLatency = 1;
	
	public Hud(PApplet p, Params params, DataAggregator da) {		
		this.p = p;		
		this.dataAgg = da;
		this.params = params;
		
		this.emitters = new ArrayList<Emitter>();
		this.regionManager = new HudRegionManager(this);
		
	}

	public void updateDisplayedData(Params params) {
		
		// first collect the list of hosts sending data
		ArrayList<String> hosts = new ArrayList<>();
		for (Emitter em : emitters) {
			hosts.add(em.host);
		}
		
		for (String host : hosts) {
			
			// then for each host check if there is new data available			
			Queue<ArrayList<Event>> dataList = dataAgg.getDataForHost(host);			
			if (dataList != null) {
				
				// and if this is the case update all the emitters
				// attached to that source
				ArrayList<Event> newData = dataList.poll();			
				while(newData != null) {					
					for(Emitter em : emitters) {
						if (em.host.equals(host))
							em.addParticles(newData, params);
					}					
					newData = dataList.poll();
				}
			}	
		}
	}
	
	// Draw the hud (the particles emitters)
	public void draw(Params params) {
		
		if (params.displayGrid == true)
			drawGrid();
		
		// to avoid ConcurrentModificationException 
		Object[] emittersList = emitters.toArray();			
		for (Object o : emittersList) { 
			Emitter em = (Emitter) o;
			em.update(params);
			em.draw(params);
		}
	}
	
	public void drawGrid() {
		
		int gridSquareSize = 45;		
		int cols = p.width / gridSquareSize;
		int rows = p.height / gridSquareSize;
		
		p.pushMatrix();
		p.translate(0, 0, -10);
		
		for (int i = 0; i < cols; i++) {
		  for (int j = 0; j < rows; j++) {
			  int x = i * gridSquareSize;
			  int y = j * gridSquareSize;
			  
			  p.noFill();
		      p.stroke(40);
		      p.rect(x,y,gridSquareSize,gridSquareSize);		      
		      p.fill(120);
		      p.rect(x - 1, y - 1, 3, 3);
		  }
		}
		
		p.popMatrix();
		
	}

}
