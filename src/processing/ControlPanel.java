package processing;

import processing.core.PApplet;
import processing.core.PFont;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Textlabel;

/**
 * See http://www.sojamo.de/libraries/controlP5/reference/controlP5/ControlP5.html 
 *
 */
public class ControlPanel extends PApplet {
 
	private static final long serialVersionUID = 1L;

	ControlP5 cp5;
	private RenderLoop rl;
	
	int width, height;
	Boolean currentTabIsDataDisplay = false;
	
	PFont openSans12;
	PFont openSans13;
	PFont openSans14;
	PFont openSans15;	
	PFont eurostile15;
	
	
	public ControlPanel(RenderLoop rl, int width, int height) {
		this.rl = rl;
		this.width = width;
		this.height = height;
	}

	@SuppressWarnings("deprecation")
	public void setup() {
		size(width, height);
		frameRate(25);
		cp5 = new ControlP5(this);
		
		cp5.window().setPositionOfTabs(20,20);
		
		// setup fonts
		eurostile15 = loadFont("EurostileLTStd-15.vlw");		
		openSans12 = loadFont("OpenSans-12.vlw");
		openSans13 = loadFont("OpenSans-13.vlw");
 		openSans14 = loadFont("OpenSans-14.vlw");
		openSans15 = loadFont("OpenSans-15.vlw");		
		cp5.setFont(openSans15);
		
		// Setup the tabs
		cp5.getTab("default").setColorLabel(color(255));		
		cp5.addTab("Data display settings").setColorLabel(color(255));		
		cp5.addTab("Global settings").setColorLabel(color(255));		
		cp5.addTab("Statistics").setColorLabel(color(255));
		cp5.addTab("About").setColorLabel(color(255));
				     		  
		cp5.getTab("default").activateEvent(true).setLabel("Data source settings").setId(101);
		cp5.getTab("Data display settings").activateEvent(true).setId(102);
		cp5.getTab("Global settings").activateEvent(true).setId(103);
		cp5.getTab("Statistics").activateEvent(true).setId(104);
		cp5.getTab("About").activateEvent(true).setId(105);
		
		// first tab content
		
		cp5.addTextlabel("tab1Label", "DATA SOURCES", 20, 60).moveTo(cp5.getTab("default"));
		
		// second tab content
		
		cp5.addTextlabel("tab2Label", "DATA DISPLAY", 20, 60).moveTo(cp5.getTab("Data display settings"));
		
		cp5.addSlider("Number of Emitters", 1, 8, 1, 80, 305, 450, 10).setId(8).moveTo(cp5.getTab("Data display settings")).setNumberOfTickMarks(8).showTickMarks(false).setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);
				
		// trick to place the slider at the correct val ( on top )				
		cp5.addSlider(" ", 1, 4, 4, 40, 345, 10, 150).setId(9).moveTo(cp5.getTab("Data display settings"))
				.setNumberOfTickMarks(4).showTickMarks(false)
				.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);
		
		// third tab content
		
		cp5.addTextlabel("tab3Label", "GLOBAL SETTINGS", 20, 60).moveTo(cp5.getTab("Global settings"));
		
		cp5.addSlider("Background brightness", 0, 100, 30, 90, 400, 15)
				.setId(1).setValue(rl.params.backgroundBrightness).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Emitter radius", 100, 2000, 30, 120, 400, 15).setId(2)
				.setValue(rl.params.emitterRadius).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Radius brightness", 0, 100, 30, 150, 400, 15).setId(3)
				.setValue(rl.params.emitterRadiusBrightness).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Particle size", 1, 400, 30, 180, 400, 15).setId(4)
				.setValue(rl.params.particleSize).moveTo(cp5.getTab("Global settings"));

		cp5.addRange("Particle velocity", rl.params.particleMinVelocity,
				rl.params.particleMaxVelocity,
				rl.params.particleCurrentMinVelocity,
				rl.params.particleCurrentMaxVelocity, 30, 210, 400, 15)
				.setId(5).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Particle acceleration", 0, 1, 30, 240, 400, 15).setId(6)
				.setValue(rl.params.particleAcceleration).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Latency rounding", 1, 8, 30, 270, 400, 15).setId(7)
				.setValue(rl.params.latencyRoundup).setNumberOfTickMarks(8)
				.showTickMarks(false).moveTo(cp5.getTab("Global settings"));
		
		cp5.addTextlabel("label2", "( "
						+ rl.params.latencyRoundupLegend[rl.params.latencyRoundup - 1]
						+ " )", 580, 270).setFont(openSans15).moveTo(cp5.getTab("Global settings"));
		
		// fourth tab content
		
		cp5.addTextlabel("tab4Label", "STATISTICS", 20, 60).moveTo(cp5.getTab("Statistics"));
		
		// fifth tab content
		
		cp5.addTextlabel("tab5Label", "ABOUT", 20, 60).moveTo(cp5.getTab("About"));
		
		
	}
	
	public void controlEvent(ControlEvent theEvent) {
		

		if (theEvent.getId() > 100 ) {
			// if the event is a tab switch event, just clear the screen			
			if (theEvent.getId() == 102) {
				currentTabIsDataDisplay = true;
			} else {
				currentTabIsDataDisplay = false;
			}
			
		} else {
			
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
				Textlabel label = (Textlabel) cp5.get("label2");
				if (label != null) {
					label.setStringValue( "( " +
						rl.params.latencyRoundupLegend[rl.params.latencyRoundup - 1] + " )");
				}			
				break;
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
		
		
	}
	
	public void draw() {
		
		colorMode(HSB,360,100,100);
		background(0,0,0);
		
		if (currentTabIsDataDisplay) {			
			noFill();
			stroke(255);
			// draw emitters circles in the data source section
			int circlesDistanceX = 62;
			int circlesDistanceY = 45;
			int numberOfEmitters = 1;
			for (int i = rl.params.numberOfEmittersX; i > 0; i--) {	
				for (int j = rl.params.numberOfEmittersY; j > 0; j--) {		
					ellipse(23 + ( i * circlesDistanceX), 305 + (j * circlesDistanceY), 25, 25);				
					textFont(openSans12);
					
					int ajustedXPos = 20; // ajust the X position of the text to center double digits
					if (numberOfEmitters > 9 ) { 
						ajustedXPos = 16;
					}				
					text("" + numberOfEmitters, ajustedXPos + ( i * circlesDistanceX), 310 + (j * circlesDistanceY));				
					numberOfEmitters++;
				}
			}			
		}
				
	}

	public ControlP5 control() {
		return cp5;
	}

}