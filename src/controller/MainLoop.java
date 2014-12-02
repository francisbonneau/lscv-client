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

public class MainLoop extends PApplet {

    private DataAggregator da;
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

        if (key == 'h' || key == 'H') { // H or h key pressed
        	if (getParams().displayHelpMenu == false) {
                getParams().displayHelpMenu = true;
            } else {
                getParams().displayHelpMenu = false;
            }
        }

        // Arrows keys pressed
        if (key == 'n') {
        	helpMenu.SwitchToNextSlide();
        }

        if (key == 'p') {
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
