package processing;

import processing.core.PApplet;
import processing.core.PFont;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Textlabel;

//the ControlFrame class extends PApplet, so we
// are creating a new processing applet inside a
// new frame with a controlP5 object loaded
public class ControlPanel extends PApplet {
 
	private static final long serialVersionUID = 1L;

	ControlP5 cp5;
	private RenderLoop rl;
	
	int width, height;
	int abc = 100;
	
	public ControlPanel(RenderLoop rl, int width, int height) {
		this.rl = rl;
		this.width = width;
		this.height = height;
	}

	public void setup() {
		size(width, height);
		frameRate(25);
		cp5 = new ControlP5(this);
		
 		PFont openSans14 = loadFont("OpenSans-14.vlw");
		PFont openSans15 = loadFont("OpenSans-15.vlw");
		cp5.setFont(openSans14);
		
		cp5.addTextlabel("label1", "GLOBAL SETTINGS", 20, 20);

		// http://www.sojamo.de/libraries/controlP5/reference/controlP5/ControlP5.html
  		// addSlider(String theName, float theMin, float theMax, float theDefaultValue,
        //          int theX, int theY, int theW,int theH)
		
		cp5.addSlider("Background brightness", 0, 100, 30, 50, 500, 15)
				.setId(1).setValue(rl.params.backgroundBrightness);

		cp5.addSlider("Emitter radius", 100, 2000, 30, 80, 500, 15).setId(2)
				.setValue(rl.params.emitterRadius);

		cp5.addSlider("Radius brightness", 0, 100, 30, 110, 500, 15).setId(3)
				.setValue(rl.params.emitterRadiusBrightness);

		cp5.addSlider("Particle size", 1, 400, 30, 140, 500, 15).setId(4)
				.setValue(rl.params.particleSize);

		cp5.addRange("Particle velocity", rl.params.particleMinVelocity,
				rl.params.particleMaxVelocity,
				rl.params.particleCurrentMinVelocity,
				rl.params.particleCurrentMaxVelocity, 30, 170, 500, 15)
				.setId(5);

		cp5.addSlider("Particle acceleration", 0, 1, 30, 200, 500, 15).setId(6)
				.setValue(rl.params.particleAcceleration);

		cp5.addSlider("Latency rounding", 1, 8, 30, 230, 500, 15).setId(7)
				.setValue(rl.params.latencyRoundup).setNumberOfTickMarks(8)
				.showTickMarks(false);
		
		cp5.addTextlabel("label2", "( "
						+ rl.params.latencyRoundupLegend[rl.params.latencyRoundup - 1]
						+ " )", 680, 230).setFont(openSans15);
		
		cp5.addTextlabel("label3", "DATA SOURCES", 20, 275);
		
		
		cp5.addSlider("Number of Emitters", 1, 8, 1, 80, 305, 450, 10).setId(8)
				.setNumberOfTickMarks(8).showTickMarks(true)
				.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);
		
		
		// trick to place the slider at the correct val ( on top )				
		cp5.addSlider(" ", 1, 4, 4, 40, 345, 10, 150).setId(9)
				.setNumberOfTickMarks(4).showTickMarks(true)
				.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);
		
		
		
	}
	
	public void controlEvent(ControlEvent theEvent) {
		
		float newValue = theEvent.getController().getValue();
		
		switch (theEvent.getController().getId()) {
		case (1):
			rl.params.backgroundBrightness = newValue;			
			break;
		case (2):
			rl.params.emitterRadius = newValue;
			break;
		case (3):
			rl.params.emitterRadiusBrightness = newValue;
			break;
		case (4):
			rl.params.particleSize = newValue;
			break;
		case (5):
			rl.params.particleCurrentMinVelocity = theEvent.getController().getArrayValue(0);
			rl.params.particleCurrentMaxVelocity = theEvent.getController().getArrayValue(1);			
			break;
		case (6):
			rl.params.particleAcceleration = newValue;
			break;
		case (7):
			rl.params.latencyRoundup = (int) newValue;
			cp5.get("label2").setStringValue( "( " + rl.params.latencyRoundupLegend[rl.params.latencyRoundup - 1] + " )");
		case (8):
			rl.params.numberOfEmittersX = (int) newValue;
			break;			
		case (9):
			// trick to place the slider at the correct val ( on top )
			float correctedVal = Math.abs(5 - newValue);			
			rl.params.numberOfEmittersY = (int) correctedVal;
			break;
		}
	}
	
	public void draw() {
		
		colorMode(HSB,360,100,100);
		background(0,0,0);
		
		// draw emitters circles in the data source section
		float circlesDistanceX = 62f;
		float circlesDistanceY = 45f;
		for (int i = rl.params.numberOfEmittersX; i > 0; i--) {		
			// ellipse(30 + ( j * circlesDistanceX), 355, 20, 20);		
			for (int j = rl.params.numberOfEmittersY; j > 0; j--) {		
				ellipse(23 + ( i * circlesDistanceX), 305 + (j * circlesDistanceY), 20, 20);
			}
		}
		
	}

	public ControlP5 control() {
		return cp5;
	}

}