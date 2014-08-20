import processing.core.*;

import Data.*;

public class Main extends PApplet {
	
	private static final long serialVersionUID = 1L;
	
	private Params params;
	
	public static void main(String args[]) {
		
		// fullscreen on 
		//PApplet.main(new String[] { "--present", "Main" });
		
		// fullscreen off
		PApplet.main(new String[] { "Main" });
	}

	public void setup() {
		
		params = new Params();
		
		DataAggregator da = new DataAggregator(params.hosts);
		
		if (params.fullscreen) { 
			size(displayWidth, displayHeight);
		} else {
			size(params.defaultWidth, params.defaultHeight);			
		}
					
		frameRate(params.framerate);

						
		if (this.params.resizable) {
		    frame.setResizable(true);
		}
		
				
		background(0);
		smooth();
	}

	public void draw() {
	 
		background(0);
		smooth();

		stroke(255);
		
		if (params.fpsCounter) {
			displayFPSCounter();
		}
		
		if (mousePressed) {
			line(mouseX, mouseY, pmouseX, pmouseY);
		}		

	}
	
	public void keyPressed() { 
		if (keyCode == TAB) {		
			if (params.fullscreen) {
				frame.setResizable(true);
				frame.setBounds(400, 400, params.defaultWidth, params.defaultHeight);				
				params.fullscreen = false;
			} else {				
				frame.setResizable(true);
				frame.setBounds(0, 0, displayWidth, displayWidth);
				params.fullscreen = true;
			}			
		}		
	}
	
	// Display the FPS counter
	public void displayFPSCounter() {		
		fill(200); // gray color	
		int x, y;
		
		if (params.fullscreen) {			
			x = displayWidth - 75;
			y = displayHeight - 25; 
		} else {
			x = params.defaultWidth - 75;
			y = params.defaultHeight - 25;			
		}
		text(frameRate, x, y);
	}

}