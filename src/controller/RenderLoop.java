package controller;
import java.awt.Frame;

import model.DataAggregator;
import model.Params;
import processing.core.PApplet;
import view.Hud;
import viewUI.ControlPanelUI;
import controlP5.ControlP5;

public class RenderLoop extends PApplet {
	
	private static final long serialVersionUID = 1L;	
	public Params params;	
	public Hud hud;
	
	// Single parameter outside of the Params class for the static context
	public static boolean fullscreenMode = false;
	
	// Main method for starting the PApplet
	public static void main(String args[]) {		
		if (fullscreenMode) {
			PApplet.main(new String[] { "--present", "controller.RenderLoop" });
		} else {
			PApplet.main(new String[] { "controller.RenderLoop" });
		}			
	}

	// Setup the application
	public void setup() {
		
		params = new Params(); // default parameters initialisation	
		params.fullscreen = fullscreenMode;
		
		if (params.fullscreen) // screen settings
			size(displayWidth, displayHeight);
		else
			size(params.mainWindowWidth, params.mainWindowHeight);
		
		frameRate(params.framerate);  // frames per second limit
		
		if (this.params.resizable)
			frame.setResizable(true); // resizable window (non-fullscreen)
		
		colorMode(HSB,360,100,100);   // default color mode 
	 
		// Data source setup
		DataAggregator dataAggregator = new DataAggregator(this);
		
		// Data visualisation setup
		hud = new Hud(this, params, dataAggregator);		
		//hud.addEmitter(params, params.defaultDataSource);		 
		
		// UI controls are in separate window, configured here
		new ControlP5(this);			
		Frame f = new Frame("Settings");
		ControlPanelUI cf = new ControlPanelUI(this, params.controlsWindowWidth, 
				params.controlsWindowHeight);
		f.add(cf);
		cf.init();
		f.setTitle("Settings");
		f.setSize(cf.width, cf.height);
		f.setLocation(100, 100);
		f.setResizable(false);
		f.setVisible(true);
	}
	
	// Main screen refresh function
	public void draw() {		
		
		background(0, 0, params.backgroundBrightness);
		smooth();
		noStroke();
		
		if (params.displayFPSCounter)
			displayFPSCounter();
		
		hud.updateDisplayedData(params);
		hud.draw(params);
	}
	
	public void keyPressed() { 
		if (keyCode == TAB) {		
			if (params.fullscreen) {
				frame.setResizable(true);
				frame.setBounds(400, 400, params.mainWindowWidth, 
						params.mainWindowHeight);				
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
			x = params.mainWindowWidth - 75;
			y = params.mainWindowHeight - 25;			
		}
		text(frameRate, x, y);
	}

}