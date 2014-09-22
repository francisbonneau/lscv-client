package processing;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import data.Event;
import data.DataAggregator;

public class Hud {
	
	private PApplet p;	
	private DataAggregator sa;
		
	private ArrayList<HashMap<String, HashMap<Integer, Integer>>> displayedData;
	
	private ArrayList<Emitter> emitters;
	
	public Hud(PApplet p) {		
		this.p = p;		
		
		displayedData = new ArrayList<>();
		emitters = new ArrayList<>();
	}

	public void setDataSource(DataAggregator da) {
		this.sa = da;			
	}
	
	public void updateDisplayedData(Params params) {
		
		ArrayList<Event> newData = sa.data.poll();
		while(newData != null) {	
					
			//displayedData.add(newData);
			
			// TODO figure out to which emitter the data is supposed to go
			Emitter em = emitters.get(0);
			em.addParticles(newData, params);
			
			newData = sa.data.poll();
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
		
		if (params.displayGrid == true) {
			drawGrid();
		}
		
		for (Emitter em : emitters) {
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
