package view;

import java.util.ArrayList;

import model.DataAggregator;
import model.Event;
import model.Params;
import processing.core.PApplet;

public class Hud {
	
	private PApplet p;
 	private ArrayList<Emitter> emitters;
	public DataAggregator da;
	
	public Hud(PApplet p, DataAggregator da) {		
		this.p = p;		
		this.da = da;

		emitters = new ArrayList<>();
	}

	public void updateDisplayedData(Params params) {
		
		ArrayList<Event> newData = da.data.poll();
		while(newData != null) {	
					
			//displayedData.add(newData);
			
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
	
	// Draw the hud (the particles emitters)
	public void draw(Params params) {		
		if (params.displayGrid == true)
			drawGrid();
		
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
