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

    public void draw() {
        p.colorMode(PConstants.HSB, 360, 100, 100);
        p.textSize(textSize);
        p.fill(this.color, 100, 100);
        p.text(divisionID, positionX, positionY);

    }

}
