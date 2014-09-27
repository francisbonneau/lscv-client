package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
	
	private PApplet p;
 	private ArrayList<Emitter> emitters;
 	
	public DataAggregator da;
	
	// parameters shared by all emitters
	public float smallestEvtLatency = 1;
	public float biggestEvtLatency = 1;
	
	public Hud(PApplet p, DataAggregator da) {		
		this.p = p;		
		this.da = da;

		emitters = new ArrayList<Emitter>();
	}

	public void updateDisplayedData(Params params) {
		
		// for each emitter, check if there is new data	
		for (Emitter em : emitters) {
			
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
	
	public void addEmitter(Params params, String host) {		
		int emittersX = params.numberOfEmittersX;
		int emittersY = params.numberOfEmittersY;
		
		int currentEmittersNb = emitters.size();
		
		if (currentEmittersNb == 0) { // if there is only one emitter, center it						
			int centerX = this.p.width/2;
			int centerY = this.p.height/2;
			Emitter em = new Emitter(this.p, this, host, centerX, centerY);
			emitters.add(em);
		} else { 
			
			// add the new emitter with a random posistion
			Emitter em = new Emitter(this.p, this, host, 10, 10);
			emitters.add(em);
			
			// else recalculate the position of each emitter			
			// but only if the number of emitters match
			if (emitters.size() == (emittersX * emittersY)) {
								
				currentEmittersNb++;				
				Iterator<Emitter> it = emitters.iterator();
				
				float Xincr =  p.width / (emittersX+1);
				float Yincr =  p.height / (emittersY+1);
	 				 
				for (int i = 1; i <= emittersX; i++) {
					  for (int j = 1; j <= emittersY; j++) {
						  
						  Emitter currentEmitter = it.next();
						  currentEmitter.centerX = i * Xincr;
						  currentEmitter.centerY = j * Yincr;
					  }
				}
				
			}
						
		
			//}
//			catch (Exception e ) { 
//				// Catch the exception that occurs when the number of emitters added
//				// via the Y axis (multiples added in a single shot) is not yet updated here
//				// causing an exception due to the emitters.size() not matching the # of emitters X/Y
//			}
						
		}
		
	}
	
	// Draw the hud (the particles emitters)
	public void draw(Params params) {
		if (params.displayGrid == true)
			drawGrid();
		
		
		Iterator<Emitter> it = emitters.iterator();
		
		while(it.hasNext()) { 
			Emitter em = it.next();
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
