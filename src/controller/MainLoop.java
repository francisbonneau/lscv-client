package controller;

import java.awt.Frame;
import model.DataAggregator;
import model.Params;
import processing.core.PApplet;
import view.Hud;
import viewUI.ControlPanelUI;
import controlP5.ControlP5;
import processing.opengl.*;

@SuppressWarnings({ "unused", "serial" })

public class MainLoop extends PApplet {

	private Params params;
	private DataAggregator da;
	private Hud hud;

	// Single parameter outside of the Params class for the static context
	public static boolean fullscreenMode = true;

	// Main method for starting the PApplet
	public static void main(String args[]) {
		if (fullscreenMode) {
			PApplet.main(new String[] { "--present", "controller.MainLoop" });
		} else {
			PApplet.main(new String[] { "controller.MainLoop" });
		}

	}

	// Setup the application
	public final void setup() {

		frame.setTitle("LSCV");

		setParams(new Params()); // default parameters initialisation
		getParams().windowMaximized = fullscreenMode;

		if (getParams().windowMaximized) {
			size(displayWidth, displayHeight, P3D);
		} else {
			size(getParams().mainWindowWidth, getParams().mainWindowHeight, P3D);
		}

		System.out.println("rendering with : " + g.getClass());

		frameRate(getParams().maxFramerate); // frames per second limit

		if (this.getParams().resizable) {
			frame.setResizable(true); // resizable window (non-fullscreen)
		}

		colorMode(HSB, 360, 100, 100); // default color mode

		// Data source setup
		setDataAgg(new DataAggregator(this));

		// Data visualisation setup
		setHud(new Hud(this));
		// hud.addEmitter(params, params.defaultDataSource);

		// UI controls are in separate window, configured here
		new ControlP5(this);
		Frame f = new Frame("Settings");
		ControlPanelUI cf = new ControlPanelUI(this);
		f.add(cf);
		cf.init();
		f.setTitle("Settings");
		f.setSize(cf.windowWidth, cf.windowHeight);
		f.setLocation(100, 100);
		f.setResizable(false);
		f.setVisible(true);
	}

	// Main screen refresh function
	public final void draw() {

		background(0, 0, getParams().backgroundBrightness);

		smooth();
		noStroke();

		if (getParams().displayFPSCounter) {
			displayFPSCounter();
		}

		getHud().update(getParams());
		getHud().draw(getParams());

	}

	public final void keyPressed() {
		if (keyCode == TAB) {
			if (getParams().windowMaximized) {
				frame.setResizable(true);
				frame.setBounds(400, 400, getParams().mainWindowWidth,
						getParams().mainWindowHeight);
				getParams().windowMaximized = false;
			} else {
				frame.setResizable(true);
				frame.setBounds(0, 0, displayWidth, displayWidth);
				getParams().windowMaximized = true;

			}
			getHud().regionManager.refresh();
		}

		if (key == ' ') { // Spacebar pressed
			if (getParams().displayPaused == false) {
				getParams().displayPaused = true;
			} else {
				getParams().displayPaused = false;
			}
		}

	}

	// Display the FPS counter
	public final void displayFPSCounter() {
		fill(200); // gray color
		int x, y;

		if (getParams().windowMaximized) {
			x = displayWidth - 150;
			y = displayHeight - 125;
		} else {
			x = getParams().mainWindowWidth - 75;
			y = getParams().mainWindowHeight - 45;
		}
		text("FPS:", x - 25, y);
		text(frameRate, x, y);
	}

	/**
	 * @return the params
	 */
	public Params getParams() {
		return params;
	}

	/**
	 * @param params
	 */
	public void setParams(Params params) {
		this.params = params;
	}

	/**
	 * @return the da
	 */
	public DataAggregator getDataAgg() {
		return da;
	}

	/**
	 * @param da
	 */
	public void setDataAgg(DataAggregator da) {
		this.da = da;
	}

	/**
	 * @return the hud
	 */
	public Hud getHud() {
		return hud;
	}

	/**
	 * @param hud
	 */
	public void setHud(Hud hud) {
		this.hud = hud;
	}

}
