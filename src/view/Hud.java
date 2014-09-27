package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import model.DataAggregator;
import model.Event;
import model.Params;
import processing.core.PApplet;

public class Hud {
	
	private PApplet p;
 	private HashMap<String, Emitter> emitters;
 	
	public DataAggregator da;
	
	// parameters shared by all emitters
	public float smallestEvtLatency = 1;
	public float biggestEvtLatency = 1;
	
	public Hud(PApplet p, DataAggregator da) {		
		this.p = p;		
		this.da = da;

		emitters = new HashMap<String, Emitter>();
	}

	public void updateDisplayedData(Params params) {
		
		// for each emitter, check if there is new data	
		for (Emitter em : emitters.values()) {
			
			Queue<ArrayList<Event>> dataList = da.getDataForHost(em.host);
			
			// if the data list for the emitter exist, and data source is set 
			if (dataList != null) {
				// we process all the new incoming events not yet processed
				ArrayList<Event> newData = dataList.poll();			
				while(newData != null) {
					em.addParticles(newData, params);
					newData = dataList.poll();
				}
			}			
		}
		
	}
	
	public void addEmitter(String host) {
		
		if (emitters.size() == 0) {			
			// if there is only one emitter, center it			
			int centerX = this.p.width/2;
			int centerY = this.p.height/2;
			Emitter em = new Emitter(this.p, this, host, centerX, centerY);
			emitters.put(host, em);			
		} else {
			// TODO support multiples emitters via panels
		}
		
	}
	
	// Draw the hud (the particles emitters)
	public void draw(Params params) {		
		if (params.displayGrid == true)
			drawGrid();
		
		for (Emitter em : emitters.values()) {
			em.update(params);
			em.draw(params);
		}				
	}
	
	public void drawGrid() {	
		
		int gridSquareSize = 45;		
		int cols = p.width / gridSquareSize;
		int rows = p.height / gridSquareSize;
		
		for (int i = 0; i < cols; i++) {
		  for (int j = 0; j < rows; j++) {
			  int x = i * gridSquareSize;
			  int y = j * gridSquareSize;
			  
			  p.noFill();
		      p.stroke(40);
		      p.rect(x,y,gridSquareSize,gridSquareSize);
		      
		      p.fill(120);
		      p.rect(x - 1, y - 1, 2, 2); 		      
		  }
		}
		
	}

}
