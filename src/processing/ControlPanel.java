package processing;

import processing.core.PApplet;
import processing.core.PFont;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Textarea;
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
		cp5.addTab("Filters").setColorLabel(color(255));
		cp5.addTab("Global settings").setColorLabel(color(255));		
		cp5.addTab("Statistics").setColorLabel(color(255));
		cp5.addTab("About").setColorLabel(color(255));
				     		  
		cp5.getTab("default").activateEvent(true).setLabel("Data sources settings").setId(101);
		cp5.getTab("Data display settings").activateEvent(true).setId(102);
		cp5.getTab("Filters").activateEvent(true).setId(106);
		cp5.getTab("Global settings").activateEvent(true).setId(103);
		cp5.getTab("Statistics").activateEvent(true).setId(104);
		cp5.getTab("About").activateEvent(true).setId(105);
		
		// first tab content
		
		cp5.addTextlabel("tab1Label", "DATA SOURCES", 50, 60).moveTo(cp5.getTab("default"));
		cp5.addTextlabel("tab1Legend", "Enter here the network addresses of the servers to fetch data from.", 50, 90);
		// Name, X, Y, W, H		
		cp5.addTextfield("Server address - IP:PORT", 85, 135, 360, 25).setText("127.0.0.1:6379");
		// Name, Value, X, Y, W, H
		cp5.addButton("Connect to server", 1, 460, 135, 165, 25);
		
		Textarea myTextarea = cp5.addTextarea("txt", "", 85, 205, 550, 250)                  
                  .setFont(openSans14)                  
                  .setColor(color(255))
                  .setColorBackground(color(20))
                  .setColorForeground(color(255, 100));	
		cp5.addConsole(myTextarea);
		println("Ready to establish new connexions...");
				
		cp5.addButton("Load Config", 1, 520, 525, 87, 20).captionLabel().setFont(openSans12);
		cp5.addButton("Save Config", 1, 615, 525, 87, 20).captionLabel().setFont(openSans12);		
		
		// second tab content
		
		cp5.addTextlabel("tab2Label", "DATA DISPLAY", 20, 60).moveTo(cp5.getTab("Data display settings"));
		cp5.addTextlabel("tab2Legend", "Ajust the sliders to subdivide "
				+ " the screen in multiples sections, each containing a circle.", 20, 90)
				.moveTo(cp5.getTab("Data display settings"));
		
		cp5.addSlider("  ", 1, 8, 1, 80, 125, 450, 10).setId(8)
				.moveTo(cp5.getTab("Data display settings"))
				.setNumberOfTickMarks(8).showTickMarks(false)				
				.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);
				
		// trick to place the slider at the correct val ( on top )				
		cp5.addSlider(" ", 1, 4, 4, 40, 165, 10, 150).setId(9).moveTo(cp5.getTab("Data display settings"))
				.setNumberOfTickMarks(4).showTickMarks(false)
				.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);
		
		// third tab content
		
		cp5.addTextlabel("tab3Label", "GLOBAL SETTINGS", 20, 60).moveTo(cp5.getTab("Global settings"));
		
		cp5.addSlider("Background brightness", 0, 100, 30, 90, 400, 15)
				.setId(1).setValue(rl.params.backgroundBrightness).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Circle radius", 100, 2000, 30, 120, 400, 15).setId(2)
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
		
		cp5.addToggle("Display Grid").setPosition(30,310).setSize(40,20).setValue(rl.params.displayGrid).setId(10).moveTo(cp5.getTab("Global settings"));
		cp5.addToggle("Display Circle Radius").setPosition(230,310).setSize(40,20).setValue(rl.params.displayEmitterRadius).setId(11).moveTo(cp5.getTab("Global settings"));
		cp5.addToggle("Display Labels").setPosition(30,360).setSize(40,20).setValue(rl.params.displayEmitterRadius).setId(12).moveTo(cp5.getTab("Global settings"));
		cp5.addToggle("Display FPS counter").setPosition(230,360).setSize(40,20).setValue(rl.params.displayFPSCounter).setId(13).moveTo(cp5.getTab("Global settings"));
		
		// fourth tab content
		
		cp5.addTextlabel("tab4Label", "STATISTICS", 20, 60).moveTo(cp5.getTab("Statistics"));
		
		// fifth tab content
		
		cp5.addTextlabel("tab5Label", "ABOUT", 20, 60).moveTo(
				cp5.getTab("About"));
		cp5.addTextlabel("about1",
				"Project created for the course GTI719 at l'École de technologie supérieure (ETS) in ",
				20, 90).moveTo(cp5.getTab("About"));
		cp5.addTextlabel("about2",
				"Montréal, by Francis Bonneau under the supervision of Professor Michael J. McGuffin, Ph.D.",
				20, 115).moveTo(cp5.getTab("About"));
		cp5.addTextlabel("about3",
				"using the following open source projects : Sysdig ( http://www.sysdig.org/ ), ",
				20, 140).moveTo(cp5.getTab("About"));
		cp5.addTextlabel("about4",
				"Redis ( http://redis.io/ ), Processing ( http://www.processing.org/ )",
				20, 165).moveTo(cp5.getTab("About"));
		cp5.addTextlabel("about5",
				"and ControlP5 ( http://www.sojamo.de/libraries/controlP5/ )",
				20, 190).moveTo(cp5.getTab("About"));
		
		cp5.addTextlabel("license", rl.params.softwareLicense, 20, 250)
				.setFont(openSans14).moveTo(cp5.getTab("About"));
		
	}
	
	public void controlEvent(ControlEvent theEvent) {
		
		if (theEvent.getId() > 100 ) {
			// if the event is a tab switch event, just clear the screen			
			if (theEvent.getId() == 102)
				currentTabIsDataDisplay = true;
			else
				currentTabIsDataDisplay = false;			
		} else {
			// if the event is associated with a controller ex. slider
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
				if (label != null)
					label.setStringValue( "( " + rl.params.latencyRoundupLegend[rl.params.latencyRoundup - 1] + " )");			
				break;
			case (8):
				rl.params.numberOfEmittersX = (int) newValue;
				break;			
			case (9):
				// trick to place the slider at the correct val ( on top )
				rl.params.numberOfEmittersY = (int) Math.abs(5 - newValue);
				break;
			case (10):
				if (newValue == 0.0)
					rl.params.displayGrid = false;
				else
					rl.params.displayGrid = true;
				break;
			case (11):
				if (newValue == 0.0)
					rl.params.displayEmitterRadius = false;
				else
					rl.params.displayEmitterRadius = true;
				break;
			case (12):
				if (newValue == 0.0)
					rl.params.displayEmitterLabels = false;
				else
					rl.params.displayEmitterLabels = true;
				break;
			case (13):
				if (newValue == 0.0)
					rl.params.displayFPSCounter = false;
				else
					rl.params.displayFPSCounter = true;
				break;
			}			
		}	
	}
	
	public void draw() {		
		colorMode(HSB,360,100,100);
		background(0,0,0);
		
		if (currentTabIsDataDisplay) {	// draw emitters circles in the data source section
			
			noFill();
			stroke(255);
			int circlesDistanceX = 62;
			int circlesDistanceY = 45;
			int numberOfEmitters = 1;
			
			for (int i = rl.params.numberOfEmittersX; i > 0; i--) {	
				for (int j = rl.params.numberOfEmittersY; j > 0; j--) {
					ellipse(23 + ( i * circlesDistanceX),
							125 + (j * circlesDistanceY), 25, 25);				
					textFont(openSans12);
					int ajustedXPos = 20; // adjust the X position of the text to center double digits
					if (numberOfEmitters > 9 )
						ajustedXPos = 16;					
					text("" + numberOfEmitters, ajustedXPos + ( i * circlesDistanceX),
						130 + (j * circlesDistanceY));				
					numberOfEmitters++;
				}
			}
		}
				
	}

	public ControlP5 control() {
		return cp5;
	}

}