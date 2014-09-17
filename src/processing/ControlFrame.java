package processing;

import processing.core.PApplet;
import controlP5.ControlEvent;
import controlP5.ControlP5;

//the ControlFrame class extends PApplet, so we
// are creating a new processing applet inside a
// new frame with a controlP5 object loaded
public class ControlFrame extends PApplet {
 
	private static final long serialVersionUID = 1L;

	ControlP5 cp5;
	private RenderLoop rl;
	
	int width, height;
	int abc = 100;
	
	public ControlFrame(RenderLoop rl, int width, int height) {
		this.rl = rl;
		this.width = width;
		this.height = height;
	}

	public void setup() {
		size(width, height);
		frameRate(25);
		cp5 = new ControlP5(this);
		
//		java.lang.Object theObject,
//        java.lang.String theIndex,
//        java.lang.String theName,
//        float theMin,
//        float theMax,
//        int theX,
//        int theY,
//        int theW,
//        int theH)
 		cp5.addSlider("Emitter Radius", 100, 2000, 10, 50, 300, 20).setDefaultValue(500).setId(1);
 		
 		//cp5.addSlider("emitterRadius2", 100, 1000, 10, 90, 300, 20).setDefaultValue(500).setId(2);
 		
		cp5.addSlider("abc").setRange(0, 255).setPosition(10, 10);
		//cp5.addSlider("def").plugTo(parent, "def").setRange(0, 255).setPosition(10, 30);
	}
	
	public void controlEvent(ControlEvent theEvent) {
		println("got a control event from controller with id " + theEvent.getController().getId());

		switch (theEvent.getController().getId()) {
		case (1):
			rl.params.emitterRadius = theEvent.getController().getValue();
			break;
		case (2):
			break;
		case (3):
			break;
		}
	}

	public void draw() {
		colorMode(HSB,360,100,100);
		background(0,0,10);		
	}


	public ControlP5 control() {
		return cp5;
	}



}