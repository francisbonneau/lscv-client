package controller;

import java.awt.Frame;

import model.DataAggregator;
import model.Params;
import processing.core.PApplet;
import processing.core.PImage;
import view.HelpMenu;
import view.Hud;
import viewUI.ControlPanelUI;
import controlP5.ControlP5;
import processing.opengl.*;

@SuppressWarnings({ "unused", "serial" })

/**
 * Main render loop of the application
 * @author Francis Bonneau
 */
public class MainLoop extends PApplet {

    private DataAggregator dataAgg;
    private Hud hud;
    private HelpMenu helpMenu;
    private Params params;

    // Single parameter outside of the Params class for the static context
    public static boolean fullscreenMode = false;

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

        frame.setTitle("LSCV"); // set the window name

        this.params = Params.getInstance(); // default parameters initialisation
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

        // Help menu setup
        helpMenu = new HelpMenu(this);

        // Data source setup
        dataAgg = new DataAggregator(this);

        // Data visualisation setup
        hud = new Hud(this);

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

        // if the help menu is on dont draw anything else on the screen
        if (getParams().displayHelpMenu) {
        	helpMenu.draw();

        } else {

        	if (getParams().displayFPSCounter) {
                displayFPSCounter();
            }
            getHud().update(getParams());
            getHud().draw(getParams());
        }

    }

    // Display the FPS counter
    public final void displayFPSCounter() {
        textSize(12);
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

    // Handle the user key pressses
    public final void keyPressed() {

        // Pause the animation if the spacebar key is pressed
        if (key == ' ') {
            if (getParams().displayPaused == false) {
                getParams().displayPaused = true;
            } else {
                getParams().displayPaused = false;
            }
        }

        // Hide or show the help menu if the h or H key is pressed
        if (key == 'h' || key == 'H') {

        	if (getParams().displayHelpMenu == false) {
                getParams().displayHelpMenu = true;
            } else {
                getParams().displayHelpMenu = false;
            }
        }

        // Switch to the next slide in the help menu
        if (key == 'n' || key == 'N') {
        	helpMenu.SwitchToNextSlide();
        }

        // Switch to the previous slide in the help menu
        if (key == 'p' || key == 'P') {
        	helpMenu.SwitchToPreviousSlide();
        }

    }

    /**
     * @return the params
     */
    public Params getParams() {
        return params;
    }

    /**
     * @return the dataAggregator
     */
    public DataAggregator getDataAgg() {
        return dataAgg;
    }

    /**
     * @return the hud
     */
    public Hud getHud() {
        return hud;
    }

}
