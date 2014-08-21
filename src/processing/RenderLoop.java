package processing;
import data.*;
import processing.core.*;

public class RenderLoop extends PApplet {
	
	private static final long serialVersionUID = 1L;
	
	public Params params;
	
	public Hud hud;
	
	public static void main(String args[]) {
		
		// fullscreen on 
		//PApplet.main(new String[] { "--present", "processing.RenderLoop" });
		
		// fullscreen off
		PApplet.main(new String[] { "processing.RenderLoop" });
	}

	public void setup() {
		
		params = new Params(); // default parameters			
		
		if (params.fullscreen) // screen settings
			size(displayWidth, displayHeight);
		else
			size(params.defaultWidth, params.defaultHeight);
					
		frameRate(params.framerate);
		
		if (this.params.resizable)
			frame.setResizable(true);
									
		background(0);
		smooth();
		
		// Data source		
		DataAggregator da = new DataAggregator(this);
		// Data display
		hud = new Hud(this);
		hud.setDataSource(da);
		hud.addEmitter();
		
	}

	public void draw() {
	 
		background(0);
		smooth();

		stroke(255);
		
		if (params.fpsCounter)
			displayFPSCounter();
		
		if (mousePressed)
			line(mouseX, mouseY, pmouseX, pmouseY);
		
		hud.draw(params);
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