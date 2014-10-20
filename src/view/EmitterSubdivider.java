package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

import com.jogamp.common.os.Platform.ABIType;

import model.Event;
import processing.core.PApplet;

public class EmitterSubdivider {

    public Emitter em; // the parent Emitter

    // It is exactly like HashMap, but presents the items in the insertion order.
    // Key : the unique value (ex: process name) used to separate emitter regions,
    // Value : the number associated (# of syscall) with that region (its size)
    public LinkedHashMap<String, EmitterSubdivision> currentDivisions;
    public long divisionsTotalSize;

    public LinkedList<LinkedHashMap<String, EmitterSubdivision>> divisionsHistory;

    public EmitterSubdivider(Emitter em) {
        this.em = em;

        currentDivisions = new LinkedHashMap<String, EmitterSubdivision>();
        divisionsTotalSize = 0;


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
        timer.schedule(new SubdivisonSaver(this), 0,
        		1000 * em.getHud().params.emitterSubDivisionsTimeoutSec);

    }

    /**
     * This method is saving the events divisions at every second into the
     * divisionsHistory list, and check if the history is not over the limit,
     * if that is the case, the data currently displayed is reaching its time
     * limit (ex 30 sec) and in that case the divisions need to be recalculated.
     */
    public void saveDivisions() {

//    	int historyMaxSize = em.getHud().params.emitterSubDivisionsTimeoutSec;
//
//    	LinkedHashMap<String, EmitterSubdivision> currentDiv =
//    				new LinkedHashMap<String, EmitterSubdivision>();
//
//    	currentDiv.putAll(currentDivisions);
//
//    	divisionsHistory.push(currentDiv);
//
//    	if (divisionsHistory.size() > historyMaxSize) {
//
//    		LinkedHashMap<String, EmitterSubdivision> timedOutData =
//    				divisionsHistory.removeLast();
//
//    		System.out.println("removed last element : " + divisionsHistory.size() );
//
//    		Iterator<String> it = currentDivisions.keySet().iterator();
//    		Iterator<EmitterSubdivision> it2 = currentDivisions.values().iterator();
//
//    		float i = 0;
//
//    		while(it.hasNext()) {
//    			String divID = it.next();
//    			EmitterSubdivision div = it2.next();
//
//    			if(timedOutData.containsKey(divID)) {
//    				float outdated = timedOutData.get(divID).size;
//    				div.size = div.size - outdated;
//
//    				if (div.size < 1) {
//    					System.out.print(" removed " + divID);
//    					currentDivisions.remove(div);
//    				} else{
//    					System.out.print(divID + ": " + div.size );
//    				}
//    				i = i + outdated;
//    			}
//    			System.out.println(" ");
//    		}
//    		this.divisionsTotalSize = (long) (this.divisionsTotalSize - i);
//    	}
////
//    	if ((divisionsHistory.size() / 100) == historyMaxSize) {
//
//    		divisionsHistory.removeLast();
//
//    	}

    	currentDivisions.clear();
    	divisionsTotalSize = 0;

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

    // divide the circle according to the event distribution
    public void adjustDivisionsSizes() {

        Iterator<EmitterSubdivision> divisionsValues = currentDivisions.values().iterator();

        float lastAngle = 0;
        em.labelsList = new ArrayList<>();

        while(divisionsValues.hasNext()) {

            EmitterSubdivision div = divisionsValues.next();
            float relativeSize = PApplet.map(div.size, 0, divisionsTotalSize, 0, 360);

            div.startAngleDeg = lastAngle;
            div.endAngleDeg = lastAngle + relativeSize;
            lastAngle = lastAngle + relativeSize;
        }

    }

    public float getDivisionStartAngle(String divisionID) {
        return currentDivisions.get(divisionID).startAngleDeg;
    }

    public float getDivisonEndAngle(String divisionID) {
        return currentDivisions.get(divisionID).endAngleDeg;
    }


}
