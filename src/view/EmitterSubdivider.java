package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import model.Event;
import processing.core.PApplet;

public class EmitterSubdivider {

    public Emitter em; // the parent Emitter

    // It is exactly like HashMap, but presents the items in the insertion order.
    // Key : the unique value (ex: process name) used to separate emitter regions,
    // Value : the number associated (# of syscall) with that region (its size)
    public LinkedHashMap<String, EmitterSubdivision> currentDivisions;
    public long divisionsTotalSize;
    private int divisionsTimeout;

    public LinkedList<LinkedHashMap<String, EmitterSubdivision>> halosDivisions;
    public Long[] halosDivisionsTotalSize;
    public Integer[] halosTimeout;

    public EmitterSubdivider(Emitter em) {
        this.em = em;

        // initialise the emitter divisions and timers
        currentDivisions = new LinkedHashMap<String, EmitterSubdivision>();
        divisionsTotalSize = 0;
        divisionsTimeout = 0;

        // initialise the halos divisions and timers
        int halosNb = em.getHud().params.emitterHalosIntervalsSec.length;
        halosDivisions = new LinkedList<>();
        halosDivisionsTotalSize = new Long[halosNb];
        halosTimeout = new Integer[halosNb];
        for (int i = 0; i < halosNb; i++) {
        	halosDivisions.add(new LinkedHashMap<String, EmitterSubdivision>());
        	halosDivisionsTotalSize[i] = 0l;
        	halosTimeout[i] = 0;
        }

        // This thread will reset the subdivisions sizes at each interval
        class SubdivisonResetter extends TimerTask {

            EmitterSubdivider emSubdiv;
            public SubdivisonResetter(EmitterSubdivider emSubdiv) {
                this.emSubdiv = emSubdiv;
            }

            public void run() {
                emSubdiv.resetDivisions();
            }
         }

        Timer timer = new Timer();
        timer.schedule(new SubdivisonResetter(this), 0, 1000);
    }

    /**
     * This method is saving the events divisions at every second into the
     * divisionsHistory list, and check if the history is not over the limit,
     * if that is the case, the data currently displayed is reaching its time
     * limit (ex 30 sec) and in that case the divisions need to be recalculated.
     */
    public void resetDivisions() {

        if (divisionsTimeout == em.getHud().params.emitterDivisionsIntervalSec) {
            currentDivisions.clear();
            divisionsTotalSize = 0;
            divisionsTimeout = 0;
        }
        divisionsTimeout++;

        int i = 0;
        for (int haloInterval : em.getHud().params.emitterHalosIntervalsSec) {
        	if (halosTimeout[i] == haloInterval) {
        		halosDivisions.get(i).clear();
        		halosDivisionsTotalSize[i] = 0l;
        		halosTimeout[i] = 0;
        	}
        	halosTimeout[i] = halosTimeout[i] + 1;
        	i++;
        }

     }

    /**
     * Prepare the division of the circle (emitter) into sections based on the
     * data and the division attribute (ex: divide by user, program name).
     * The sections sizes represent the distribution (# of system calls) of that
     * specific category.
     *
     * @param newData
     * @param divisionAttribute
     */
    public void addDivisions(ArrayList<Event> newData, String divisionAttribute) {

        for (Event e : newData) {

            String divisionID = e.attributes.get(divisionAttribute);

            if (currentDivisions.containsKey(divisionID)) {
                EmitterSubdivision div = currentDivisions.get(divisionID);
                div.size = div.size + e.syscallNumber;

            }
            else {
                // new division
                EmitterSubdivision div = new EmitterSubdivision(e.syscallNumber);
                currentDivisions.put(divisionID, div);

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
            divisionsTotalSize += e.syscallNumber;

        }

    }

	public void addHalos(ArrayList<Event> newData, String divisionAttribute) {

		int halosNb = em.getHud().params.emitterHalosIntervalsSec.length;

		for (Event e : newData) {

			String divisionID = e.attributes.get(divisionAttribute);

			for (int i = 0; i < halosNb; i++) {

				if (halosDivisions.get(i).containsKey(divisionID)) {
					EmitterSubdivision div = halosDivisions.get(i).get(
																	divisionID);
					div.size = div.size + e.syscallNumber;
				} else {
					// new halo division
					halosDivisions.get(i).put(divisionID,
										new EmitterSubdivision(e.syscallNumber));
				}
				halosDivisionsTotalSize[i] += e.syscallNumber;
			}

		}
	}

    // divide the circle according to the event distribution
    public void adjustDivisionsSizes() {

        Iterator<EmitterSubdivision> divisionsValues = currentDivisions.values().iterator();

        float lastAngle = 0;
        while(divisionsValues.hasNext()) {

            EmitterSubdivision div = divisionsValues.next();
            float relativeSize = PApplet.map(div.size, 0,
            							divisionsTotalSize, 0, 360);

            div.startAngleDeg = lastAngle;
            div.endAngleDeg = lastAngle + relativeSize;
            lastAngle = lastAngle + relativeSize;
        }

    }


    // divide the circle according to the event distribution
    public void adjustHalosSizes() {

    	int halosNb = em.getHud().params.emitterHalosIntervalsSec.length;
    	for (int i = 0; i < halosNb; i++) {

    		Iterator<EmitterSubdivision> divisionsValues =
    				halosDivisions.get(i).values().iterator();

            float lastAngle = 0;
            while(divisionsValues.hasNext()) {

                EmitterSubdivision div = divisionsValues.next();
                float relativeSize = PApplet.map(div.size, 0,
                						halosDivisionsTotalSize[i], 0, 360);

                div.startAngleDeg = lastAngle;
                div.endAngleDeg = lastAngle + relativeSize;
                lastAngle = lastAngle + relativeSize;
            }
    	}

    }

    public float getDivisionStartAngle(String divisionID) {
        return currentDivisions.get(divisionID).startAngleDeg;
    }

    public float getDivisonEndAngle(String divisionID) {
        return currentDivisions.get(divisionID).endAngleDeg;
    }


}
