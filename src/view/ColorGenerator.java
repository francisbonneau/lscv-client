package view;

import java.util.Random;
import processing.core.PApplet;

/**
 * Color generator, capable of generating colors on demand using an algorithm
 *	to ensure the colors are spaced evently across the color space - eg. the
 *	colors are easy to distinguish from each other.
 * @author Francis Bonneau
 */
public class ColorGenerator {

	// Algorithm based on the following websites :
	// http://devmag.org.za/2012/07/29/how-to-choose-colours-procedurally-algorithms/
	// http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/

    private static float goldenRatio = 0.618033988749895f;
    private Random rnd;
    private float currentHue;

    // Initialisation of the random generator
    public ColorGenerator() {
        this.rnd = new Random();
        this.currentHue = (float) rnd.nextDouble();
    }

    // Return a new color Hue different from the last colors returned
    public float getNewColorHue() {

        float hue, adjustedHue;
        do {
            hue = currentHue;
            currentHue += goldenRatio;
            currentHue %= 1.0f;
            // switch from 0..1 hue to 0..360
            adjustedHue = PApplet.map(hue, 0, 1, 0, 360);

        // avoid dark blue colors - hard to read on a black background
        } while (adjustedHue >= 200 && adjustedHue <= 270);

        return adjustedHue;
    }

}
