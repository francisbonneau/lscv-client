package view;

import processing.core.PApplet;
import processing.core.PConstants;

public class EmitterLabel {

    private PApplet p;
    public String divisionID;
    public Integer textSize;
    public float color;
    public float positionX;
    public float positionY;

    public EmitterLabel(PApplet p, String text, Integer textSize, float color) {

        this.p = p;
        this.divisionID = text;
        this.textSize = textSize;
        this.color = color;
    }

    public void calculateLabelPosition(float minAngle, float maxAngle,
    		float emitterRadius, float emitterCenterX, float emitterCenterY,
    		float labelsDistance) {

        float angle = PApplet.radians((minAngle + maxAngle)/2 + 45);
        float radius = emitterRadius/2 + labelsDistance;

        positionX = (float) Math.cos(angle) * radius + emitterCenterX;
        positionY = (float) Math.sin(angle) * radius + emitterCenterY;
    }

    public boolean draw(boolean selected) {

        p.colorMode(PConstants.HSB, 360, 100, 100);

        boolean mouseOnLabel = p.mouseX > positionX &&
        		p.mouseX < positionX + textSize * divisionID.length() &&
                p.mouseY > positionY - textSize &&
                p.mouseY < positionY;

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
