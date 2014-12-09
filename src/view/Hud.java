package view;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Queue;

import model.Event;
import model.Params;
import processing.core.PApplet;
import controller.MainLoop;

/**
 * The hud handle the update and drawing of one or multiples emitters,
 * and keeps state variables that need to be shared by each emitter,
 * like the smallest event latency accross all data source for example.
 * @author Francis Bonneau
 */
public class Hud {

	private MainLoop rl;
	public PApplet p;
	public Params params;

    public ArrayList<Emitter> emitters;  	// the list of all the emitters
    public HudRegionManager regionManager;  // object to divide the hud regions

    // parameters shared by all emitters
    public float smallestEvtLatency = 1; // all the events speed is relative
    public float biggestEvtLatency = 1;	 // to the min and max particle speed

    public HashMap<String, Float> colorPalette;  // the color palette, one color
    public ColorGenerator colorGenerator;	// for each category is also shared

    public long eventsDisplayedCount;	// Stats computed across all the
    public long syscallDisplayedCount;	// currently visible emitters
    public long eventsTotalCount;
    public long syscallTotalCount;

    // Constructor
    public Hud(MainLoop rl) {

        this.rl = rl;
        this.params = rl.getParams();
        this.p = rl;

        this.emitters = new ArrayList<Emitter>();
        this.regionManager = new HudRegionManager(this);

        this.colorPalette =  new HashMap<String, Float>();
        this.colorGenerator = new ColorGenerator();
    }

    // Update the stats that will be displayed on the bottom left of the screen
    private void updateDisplayedData(Params params) {

        if (!params.displayPaused) {

            // first collect the list of hosts sending data
            ArrayList<String> hosts = new ArrayList<>();
            for (Emitter em : emitters) {
                hosts.add(em.host);
            }

            for (String host : hosts) {

                // then for each host check if there is new data available
                Queue<ArrayList<Event>> dataList = rl.getDataAgg().getDataForHost(host);
                if (dataList != null) {

                    // and if this is the case update all the emitters
                    // attached to that source
                    ArrayList<Event> newData = dataList.poll();
                    while(newData != null) {
                        for(Emitter em : emitters) {
                            if (em.host.equals(host))
                                em.addParticles(newData);
                        }
                        newData = dataList.poll();
                    }
                }
            }
        }
    }

    // Get the stats from all emitters and combine them
    private void getStats() {

        eventsDisplayedCount = 0;
        syscallDisplayedCount = 0;
        eventsTotalCount = 0;
        syscallTotalCount = 0;

        Object[] emittersList = emitters.toArray();
        for (Object o : emittersList) {
            Emitter em = (Emitter) o;

            eventsDisplayedCount += em.eventsDisplayedCount;
            syscallDisplayedCount += em.syscallDisplayedCount;
            eventsTotalCount += em.eventsTotalCount;
            syscallTotalCount += em.syscallTotalCount;
        }

    }

    // Update the hud
    public void update(Params params) {
        updateDisplayedData(params);
        getStats();
    }

    // Update the labels positions
    public void updateLabelsPositions() {
        for (Emitter em : emitters) {
        	em.updateLabelsPositions();
        }
    }

    // Draw the statistics on the bottom left of the screen
    private void drawStats() {
        p.fill(225); // gray color
        int x, y;

        if (rl.getParams().windowMaximized) {
            x = 50;
            y = p.displayHeight - 150;
        } else {
            x = 50;
            y = params.mainWindowHeight - 125;
        }

        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
        p.textSize(12);
        String visibleParticles = formatter.format(eventsDisplayedCount);
        p.text("Particles : " + visibleParticles, x, y);
        String visibleSyscalls = formatter.format(syscallDisplayedCount);
        p.text("System calls : " + visibleSyscalls , x, y + 25);
        String totalParticles = formatter.format(eventsTotalCount);
        p.text("Particles processed total : " + totalParticles, x, y + 50);
        String totalSyscalls = formatter.format(syscallTotalCount);
        p.text("System calls processed total : " + totalSyscalls, x, y + 75);
    }

    // Draw a grid on the background
    public void drawGrid() {

        int gridSquareSize = 45;
        int cols = p.width / gridSquareSize;
        int rows = p.height / gridSquareSize;

        p.pushMatrix();
        p.translate(0, 0, -10);

        for (int i = 0; i < cols; i++) {
          for (int j = 0; j < rows; j++) {
              int x = i * gridSquareSize;
              int y = j * gridSquareSize;

              p.noFill();
              p.stroke(40);
              p.rect(x,y,gridSquareSize,gridSquareSize);
              p.fill(120);
              p.rect(x - 1, y - 1, 3, 3);
          }
        }
        p.popMatrix();
    }

    // Draw the hud, the grid if enabled, and the stats if enabled
    public void draw(Params params) {

        if (params.displayGrid == true)
            drawGrid();

        // to avoid ConcurrentModificationException
        Object[] emittersList = emitters.toArray();
        for (Object o : emittersList) {
            Emitter em = (Emitter) o;
            em.update();
            em.draw();
        }

        if(params.displayStats)
            drawStats();
    }

}
