package view;

import processing.core.PApplet;
import processing.core.PConstants;

public class EmitterLabel {

    private PApplet p;

    public String text;
    public Integer textSize;
    public float color;
    public float positionX;
    public float positionY;

    public EmitterLabel(PApplet p, String text, Integer textSize,
            float color,float positionX, float positionY) {

        this.p = p;
        this.text = text;
        this.textSize = textSize;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
    }


    public void draw() {
        p.colorMode(PConstants.HSB, 360, 100, 100);
        p.textSize(textSize);
        p.fill(this.color, 100, 100);
        p.text(text, positionX, positionY);

    }

}
