package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import jogamp.opengl.glu.nurbs.Subdivider;
import model.Event;
import processing.core.PApplet;

public class EmitterSubdivider {

    public Emitter em; // the parent Emitter

    // It is exactly like HashMap, but presents the items in the insertion order.
    // Key : the unique value (ex: process name) used to separate emitter regions,
    // Value : the number associated (# of syscall) with that region (its size)
    public LinkedHashMap<String, EmitterSubdivision> divisions;
    public long divisionsMaxSize;

    public LinkedList<LinkedHashMap<String, EmitterSubdivision>> divisionsHistory;

    public EmitterSubdivider(Emitter em) {
        this.em = em;

        divisions = new LinkedHashMap<String, EmitterSubdivision>();
        divisionsMaxSize = 0;

        divisionsHistory = new LinkedList<>();

        // This thread will save the subdivisions sizes at each secdon
        class SubdivisonSaver extends TimerTask {

        	EmitterSubdivider emSubdiv;
        	public SubdivisonSaver(EmitterSubdivider emSubdiv) {
        		this.emSubdiv = emSubdiv;
        	}

            public void run() {
            	emSubdiv.saveDivisions();
            }
         }

        Timer timer = new Timer();
        timer.schedule(new SubdivisonSaver(this), 0, 1000);

    }

    public void saveDivisions() {

    	int historyMaxSize = em.getHud().params.emitterSubDivisionsTimeoutSec;

    	LinkedHashMap<String, EmitterSubdivision> currentDiv =
    				new LinkedHashMap<String, EmitterSubdivision>();

    	currentDiv.putAll(divisions);

    	divisionsHistory.push(currentDiv);

    	if (divisionsHistory.size() > historyMaxSize) {

    		LinkedHashMap<String, EmitterSubdivision> timedOutData =
    				divisionsHistory.removeLast();

    		Iterator<String> it = divisions.keySet().iterator();
    		Iterator<EmitterSubdivision> it2 = divisions.values().iterator();

    		int i = 0;

    		while(it.hasNext()) {
    			String divID = it.next();
    			EmitterSubdivision div = it2.next();

    			if(timedOutData.containsKey(divID)) {
    				int outdated = timedOutData.get(divID).size;
    				div.size = div.size - outdated;
    				i = i + outdated;
    			}
    		}
    		this.divisionsMaxSize = this.divisionsMaxSize - i;

    	}

	}

	public void addDivisions(ArrayList<Event> newData, String divisionAttribute) {

        for (Event e : newData) {

            String divisionID = e.attributes.get(divisionAttribute);

            if (divisions.containsKey(divisionID)) {
            	EmitterSubdivision div = divisions.get(divisionID);
            	div.size = div.size + e.syscallNumber;
            }
            else {
                // new division
            	EmitterSubdivision div = new EmitterSubdivision(e.syscallNumber);
            	divisions.put(divisionID, div);

                // Get a new color for the new division or get the pre assigned
                // color to that division ID
                float newColor;
                if (em.getHud().colorPalette.containsKey(divisionID)) {
                	newColor =em.getHud().colorPalette.get(divisionID);
                } else {
                	newColor = em.getHud().colorGenerator.getNewColorHue();
                }
                em.getHud().colorPalette.put(divisionID, newColor);
            }
            divisionsMaxSize += e.syscallNumber;
        }

    }

    // divide the circle according to the event distribution
    public void adjustDivisionsSizes() {

        Iterator<EmitterSubdivision> divisionsValues = divisions.values().iterator();

        float lastAngle = 0;
        em.labelsList = new ArrayList<>();

        while(divisionsValues.hasNext()) {

            EmitterSubdivision div = divisionsValues.next();
            float relativeSize = PApplet.map(div.size, 0, divisionsMaxSize, 0, 360);

            div.startAngleDeg = lastAngle;
            div.endAngleDeg = lastAngle + relativeSize;
            lastAngle = lastAngle + relativeSize;
        }

    }

    public float getDivisionStartAngle(String divisionID) {
        return divisions.get(divisionID).startAngleDeg;
    }

    public float getDivisonEndAngle(String divisionID) {
        return divisions.get(divisionID).endAngleDeg;
    }


}
