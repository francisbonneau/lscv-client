package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import model.Event;
import processing.core.PApplet;

public class EmitterSubdivision {

    public Emitter em; // the parent Emitter

    // It is exactly like HashMap, but presents the items in the insertion order.
    // Key : the unique value (ex: process name) used to separate emitter regions,
    // Value : the number associated (# of syscall) with that region (its size)
    public LinkedHashMap<String, Integer> divisionsSize;
    public long divisionsMaxSize;

    public HashMap<String, Float> divisionsLowerAngles;
    public HashMap<String, Float> divisionsHigherAngle;


    public EmitterSubdivision(Emitter em) {
        this.em = em;
        divisionsSize = new LinkedHashMap<String, Integer>();

        divisionsLowerAngles = new HashMap<String, Float>();
        divisionsHigherAngle = new HashMap<String, Float>();
        divisionsMaxSize = 0;
    }


    public void createDivisions(ArrayList<Event> newData, String divisionAttribute) {

        for (Event e : newData) {

            String divisionID = e.attributes.get(divisionAttribute);

            if (divisionsSize.containsKey(divisionID)) {
                int lastVal = divisionsSize.get(divisionID);
                divisionsSize.put(divisionID, lastVal + e.syscallNumber);
            }
            else {
                // new division
                divisionsSize.put(divisionID, e.syscallNumber);

                // Get a new color for the new division
                float newColor = em.getHud().colorGenerator.getNewColorHue();
                em.getHud().colorPalette.put(divisionID, newColor);

            }
            divisionsMaxSize += e.syscallNumber;
        }

    }

    // divide the circle according to the event distribution
    public void calculateDivisionsSizes() {

        Iterator<String> divisionsIDs = divisionsSize.keySet().iterator();
        Iterator<Integer> divisionsSizes = divisionsSize.values().iterator();

        divisionsLowerAngles = new HashMap<String, Float>();
        divisionsHigherAngle = new HashMap<String, Float>();

        float lastAngle = 0;
        em.labelsList = new ArrayList<>();

        while(divisionsIDs.hasNext()) {

            String divisionID = divisionsIDs.next();
            int size = divisionsSizes.next();
            float relativeSize = PApplet.map(size, 0, divisionsMaxSize, 0, 360);

            divisionsLowerAngles.put(divisionID, lastAngle);
            divisionsHigherAngle.put(divisionID, lastAngle + relativeSize);
            lastAngle = lastAngle + relativeSize;
        }

    }

    public float getDivisionLowerAngle(String divisionID) {
        return divisionsLowerAngles.get(divisionID);
    }

    public float getDivisonHigherAngle(String divisionID) {
        return divisionsHigherAngle.get(divisionID);
    }


}
