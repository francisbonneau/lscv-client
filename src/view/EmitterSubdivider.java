package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import model.Event;
import processing.core.PApplet;

public class EmitterSubdivider {

    public Emitter em; // the parent Emitter

    // It is exactly like HashMap, but presents the items in the insertion order.
    // Key : the unique value (ex: process name) used to separate emitter regions,
    // Value : the number associated (# of syscall) with that region (its size)

//    public LinkedHashMap<String, Integer> divisionsSize;
//    public HashMap<String, Float> divisionsLowerAngles;
//    public HashMap<String, Float> divisionsHigherAngle;

    public LinkedHashMap<String, EmitterSubdivision> divisions;

    public long divisionsMaxSize;


    public EmitterSubdivider(Emitter em, float timeoutSeconds) {
        this.em = em;

//        divisionsSize = new LinkedHashMap<String, Integer>();
//        divisionsLowerAngles = new HashMap<String, Float>();
//        divisionsHigherAngle = new HashMap<String, Float>();

        divisions = new LinkedHashMap<String, EmitterSubdivision>();

        divisionsMaxSize = 0;


        class SubdivSaver extends TimerTask {

        	EmitterSubdivider emSubdiv;
        	public SubdivSaver(EmitterSubdivider emSubdiv) {
        		this.emSubdiv = emSubdiv;
        	}

            public void run() {
            	emSubdiv.saveDivisions();
            }
         }

        Timer timer = new Timer();
        timer.schedule(new SubdivSaver(this), 0, 1000);

    }


    public void saveDivisions() {

//    	Iterator<Integer> it = divisionsSize.values().iterator();
//    	Iterator<Float> it2 = divisionsHigherAngle.values().iterator();
//    	Iterator<Float> it3 = divisionsLowerAngles.values().iterator();
//
//    	while(it.hasNext()) {
//    		int size = it.next();
//    		size = size - 10;
//    	}

//    	this.divisionsSize.clear();
//    	this.divisionsMaxSize = 0;
////    	this.divisionsLowerAngles.clear();
//    	this.divisionsHigherAngle.clear();

	}

	public void addDivisions(ArrayList<Event> newData, String divisionAttribute) {

        for (Event e : newData) {

            String divisionID = e.attributes.get(divisionAttribute);

            if (divisions.containsKey(divisionID)) {

            	EmitterSubdivision div = divisions.get(divisionID);
            	div.size = div.size + e.syscallNumber;

//                int lastVal = divisionsSize.get(divisionID);
//                divisionsSize.put(divisionID, lastVal + e.syscallNumber);
            }
            else {
                // new division
            	EmitterSubdivision div = new EmitterSubdivision(e.syscallNumber);

            	divisions.put(divisionID, div);
                //divisionsSize.put(divisionID, e.syscallNumber);

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

        Iterator<String> divisionsIDs = divisions.keySet().iterator();
        Iterator<EmitterSubdivision> divisionsValues = divisions.values().iterator();

        //Iterator<Integer> divisionsSizes = divisionsSize.values().iterator();

//        divisionsLowerAngles = new HashMap<String, Float>();
//        divisionsHigherAngle = new HashMap<String, Float>();

        float lastAngle = 0;
        em.labelsList = new ArrayList<>();

        while(divisionsIDs.hasNext()) {

            String divisionID = divisionsIDs.next();
            //int size = divisionsSizes.next();
            EmitterSubdivision div = divisionsValues.next();


            float relativeSize = PApplet.map(div.size, 0, divisionsMaxSize, 0, 360);

            div.startAngleDeg = lastAngle;

            //divisionsLowerAngles.put(divisionID, lastAngle);
            //divisionsHigherAngle.put(divisionID, lastAngle + relativeSize);
            div.endAngleDeg = lastAngle + relativeSize;
            lastAngle = lastAngle + relativeSize;
        }

    }

    public float getDivisionLowerAngle(String divisionID) {
        return divisions.get(divisionID).startAngleDeg;
    }

    public float getDivisonHigherAngle(String divisionID) {
        return divisions.get(divisionID).endAngleDeg;
    }


}
