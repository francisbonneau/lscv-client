import processing.core.*;

import Data.*;

public class Main extends PApplet {
	
	private static final long serialVersionUID = 1L;
	 
	
	public static void main(String args[]) {
		
		// fullscreen on 
		//PApplet.main(new String[] { "--present", "Main" });
		
		// fullscreen off
		PApplet.main(new String[] { "Main" });
	}

	public void setup() {
		
		Params params = new Params();
		
		DataAggregator da = new DataAggregator(params.hosts);
		
		size(900, 900);	 	
		frameRate(30);
		smooth();
	 
		
		if (frame != null) {
		    frame.setResizable(true);
		}
		
		background(0);
	}

	public void draw() {
		
		background(0);
		
		stroke(255);
	 
			
		
		// fps counter
		fill(200);
		text(frameRate,825,875);
		
		if (mousePressed) {
			line(mouseX, mouseY, pmouseX, pmouseY);
		}
					
	}

}