package view;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import processing.core.PApplet;
import processing.core.PConstants;

public class EmitterHalo {

	// https://www.processing.org/reference/arc_.html

    private PApplet p;
    private Emitter em;

    public EmitterHalo(PApplet p, Emitter em) {
		this.p = p;
		this.em = em;
	}

	public void draw(LinkedHashMap<String, EmitterSubdivision> divisions,
			float distance, float brightness) {

		try {

			Iterator<String> it = divisions.keySet().iterator();
			Iterator<EmitterSubdivision> it2 = divisions.values().iterator();

			while (it.hasNext()) {

				String divisionID = it.next();
				EmitterSubdivision div = it2.next();

				float color = em.getHud().colorPalette.get(divisionID);

				p.colorMode(PConstants.HSB, 360, 100, 100);
				p.stroke(color, 100, brightness);
				p.smooth();
				p.noFill();

				// float: x-coordinate of the arc's ellipse
				// float: y-coordinate of the arc's ellipse
				// float: width of the arc's ellipse by default
				// float: height of the arc's ellipse by default
				// float: angle to start the arc, specified in radians
				// float: angle to stop the arc, specified in radians

				float dist = em.getHud().params.emitterRadius + distance;
				p.arc(em.centerX, em.centerY, dist, dist,
						PApplet.radians(div.startAngleDeg + 45),
						PApplet.radians(div.endAngleDeg + 45));
			}

		} catch (ConcurrentModificationException e) {
			// no modification of the hashmap occurs here, but a simple
			// get is enough to trigger a conccurrent exception
		}


    }

}
