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
		
		// first collect the list of hosts sending data
		ArrayList<String> hosts = new ArrayList<>();
		for (Emitter em : emitters) {
			hosts.add(em.host);
		}
		
		for (String host : hosts) {
			
			// then for each host check if there is new data available			
			Queue<ArrayList<Event>> dataList = da.getDataForHost(host);			
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
	
	/**
	 * Add a new emitter to the hud, and recaculate the position of 
	 * all emitters if necessary
	 * @param params
	 * @param host
	 */	
	public void addEmitter(Params params, String host) {
		int emittersX = params.numberOfEmittersX;
		int emittersY = params.numberOfEmittersY;

		int currentEmittersNb = emitters.size();

		if (currentEmittersNb == 0) { 
			// if there is only one emitter, center it
			int centerX = this.p.width / 2;
			int centerY = this.p.height / 2;
			Emitter em = new Emitter(this.p, this, host, centerX, centerY);
			emitters.add(em);
		} else {			
			// if there is multiples emitters, start by adding the new emitter
			// with a random posistion
			Emitter em = new Emitter(this.p, this, host, 10, 10);
			emitters.add(em);

			// and recalculate the position of each emitter
			// but only if the number of emitters match the expected value 
			int expectedNbEmitters = emittersX * emittersY;			

			if (emitters.size() == expectedNbEmitters) {
				currentEmittersNb++;
				Iterator<Emitter> it = emitters.iterator();

				float Xincr = p.width / (emittersX + 1);
				float Yincr = p.height / (emittersY + 1);

				for (int i = 1; i <= emittersX; i++) {
					for (int j = 1; j <= emittersY; j++) {
						Emitter currentEmitter = it.next();
						currentEmitter.centerX = i * Xincr;
						currentEmitter.centerY = j * Yincr;						
					}
				}
				
				// also change the emitters radius to avoid collisions
				if (params.emitterRadius > Xincr) 
					params.emitterRadius = Xincr - 10;
			
			} // if (emitters.size() ...
		} // if (currentEmittersNb == 0 ...
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
