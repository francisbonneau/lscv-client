package view;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * This class is used to calculate the labels positions, and draw them around
 * the emitter.
 * @author Francis Bonneau
 */
public class EmitterLabel {

    private PApplet p;
    public String divisionID;
    public Integer textSize;
    public float color;
    public float positionX;
    public float positionY;

    // Constructor
    public EmitterLabel(PApplet p, String text, Integer textSize, float color) {
        this.p = p;
        this.divisionID = text;
        this.textSize = textSize;
        this.color = color;
    }

    /**
     * Calculate the label position around the emitter circle, the label should
     * be positionned in the middle of the circle arc (mean of the min angle
     * and max angle)
     * @param minAngle 		the start angle of the section of that category
     * @param maxAngle		the end angle of the section of that category
     * @param emitterRadius		the emitter circle radius
     * @param emitterCenterX	the emitter center pos in X
     * @param emitterCenterY	the emitter center pos in Y
     * @param labelsDistance	the distance between the labels and the emitter center
     */
    public void calculateLabelPosition(float minAngle, float maxAngle,
    		float emitterRadius, float emitterCenterX, float emitterCenterY,
    		float labelsDistance) {

        float angle = PApplet.radians((minAngle + maxAngle)/2 + 45);
        float radius = emitterRadius/2 + labelsDistance;

        positionX = (float) Math.cos(angle) * radius + emitterCenterX;
        positionY = (float) Math.sin(angle) * radius + emitterCenterY;
    }

    /**
     * Draw the label
     * @param selected true if the user muose is over a particle of that category
     * @return true if the user mouse is over the label, or over a part. of that cat.
     */
    public boolean draw(boolean selected) {

        p.colorMode(PConstants.HSB, 360, 100, 100);

        // Check if the user mouse is on the label
        boolean mouseOnLabel = p.mouseX > positionX &&
        		p.mouseX < positionX + textSize * divisionID.length() &&
                p.mouseY > positionY - textSize &&
                p.mouseY < positionY;

        // If the label category is selected, either by a mouse over one of its
        // its particle or the label itself, we draw the label bigger
        if (selected || mouseOnLabel) {
        	p.textSize(textSize + 5);
        	p.fill(this.color, 100, 100);
            p.text(divisionID, positionX, positionY);
        	return true;

        } else {
        	p.textSize(textSize);
        	p.fill(this.color, 100, 100);
            p.text(divisionID, positionX, positionY);
        	return false;
        }
    }

}
