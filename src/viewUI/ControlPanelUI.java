package viewUI;

import processing.core.PApplet;
import processing.core.PFont;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.DropdownList;
import controlP5.Slider;
import controlP5.Textarea;
import controlP5.Textfield;
import controlP5.Textlabel;
import controller.RenderLoop;

/**
 * See http://www.sojamo.de/libraries/controlP5/reference/controlP5/ControlP5.html 
 */
public class ControlPanelUI extends PApplet {
 
	private static final long serialVersionUID = 1L;

	ControlP5 cp5;
	private RenderLoop rl;
	
	public int width;
	public int height;
	Boolean currentTabIsDataDisplay = false;
	
	PFont openSans12;
	PFont openSans13;
	PFont openSans14;
	PFont openSans15;	
	PFont eurostile15;
	
	
	public ControlPanelUI(RenderLoop rl, int width, int height) {
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
		cp5.addTextfield("newConnexionField", 85, 135, 360, 25)
			.setValue(rl.params.defaultDataSource).setLabel("Server address - IP:PORT");
		// Name, Value, X, Y, W, H
		cp5.addButton("newConnexionButton", 1, 470, 135, 165, 25).setLabel("Connect to server");
		
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
		
		cp5.addTextlabel("tab2Label", "DATA DISPLAY", 50, 60).moveTo(cp5.getTab("Data display settings"));
		cp5.addTextlabel("tab2Legend", "Ajust the sliders to subdivide "
				+ " the screen in multiples sections, each containing a circle.", 50, 90)
				.moveTo(cp5.getTab("Data display settings"));
		
		cp5.addSlider("  ", 1, 8, 1, 130, 135, 450, 10).setId(8)
				.moveTo(cp5.getTab("Data display settings"))
				.setNumberOfTickMarks(8).showTickMarks(false)				
				.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);
				
		// trick to place the slider at the correct val ( on top )				
		cp5.addSlider(" ", 1, 4, 4, 90, 175, 10, 150).setId(9).moveTo(cp5.getTab("Data display settings"))
				.setNumberOfTickMarks(4).showTickMarks(false)
				.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);
		
		cp5.addTextlabel("tab2Legend2", "Then select the data source that each circle should display :", 50, 350)
				.moveTo(cp5.getTab("Data display settings"));
		
		DropdownList d1, d2;		
		d1 = cp5.addDropdownList("myList-d1", 90, 390, 200, 35).moveTo(cp5.getTab("Data display settings"));
		cp5.addTextlabel("tab2Legend3", " => ", 310, 380).moveTo(cp5.getTab("Data display settings"));		
		d2 = cp5.addDropdownList("myList-d2", 350, 390, 200, 35).moveTo(cp5.getTab("Data display settings"));
		cp5.addButton("Select", 1, 570, 375, 60, 20).moveTo(cp5.getTab("Data display settings"));
		
		cp5.getTab("Data display settings").add(cp5.getController("Load Config"));
		cp5.getTab("Data display settings").add(cp5.getController("Save Config"));
		
		// third tab content
		
		cp5.addTextlabel("tab3Label", "FILTERS", 50, 60).moveTo(cp5.getTab("Filters"));
		
		cp5.getTab("Filters").add(cp5.getController("Load Config"));
		cp5.getTab("Filters").add(cp5.getController("Save Config"));
		
		// fourth tab content
		
		cp5.addTextlabel("tab4Label", "GLOBAL SETTINGS", 50, 60).moveTo(cp5.getTab("Global settings"));
		
		cp5.addSlider("Background brightness", 0, 100, 60, 90, 400, 15)
				.setId(1).setValue(rl.params.backgroundBrightness).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Circle radius", 100, 2000, 60, 120, 400, 15).setId(2)
				.setValue(rl.params.emitterRadius).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Radius brightness", 0, 100, 60, 150, 400, 15).setId(3)
				.setValue(rl.params.emitterRadiusBrightness).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Particle size", 1, 400, 60, 180, 400, 15).setId(4)
				.setValue(rl.params.particleSize).moveTo(cp5.getTab("Global settings"));

		cp5.addRange("Particle velocity", rl.params.particleVelocityRangeMin,
				rl.params.particleVelocityRangeMax,
				rl.params.particleMinVelocity,
				rl.params.particleMaxVelocity, 60, 210, 400, 15)
				.setId(5).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Particle acceleration", 0, 1, 60, 240, 400, 15).setId(6)
				.setValue(rl.params.particleAcceleration).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Latency rounding", 1, 8, 60, 270, 400, 15).setId(7)
				.setValue(rl.params.latencyRoundup).setNumberOfTickMarks(8)
				.showTickMarks(false).moveTo(cp5.getTab("Global settings"));
		
		cp5.addTextlabel("latencyLabel", "( "
						+ rl.params.latencyRoundupLegend[rl.params.latencyRoundup - 1]
						+ " )", 620, 270).setFont(openSans15).moveTo(cp5.getTab("Global settings"));
		
		cp5.addToggle("Display Grid").setPosition(60,310).setSize(40,20).setValue(rl.params.displayGrid).setId(10).moveTo(cp5.getTab("Global settings"));
		cp5.addToggle("Display Circle Radius").setPosition(260,310).setSize(40,20).setValue(rl.params.displayEmitterRadius).setId(11).moveTo(cp5.getTab("Global settings"));
		cp5.addToggle("Display Labels").setPosition(60,360).setSize(40,20).setValue(rl.params.displayEmitterRadius).setId(12).moveTo(cp5.getTab("Global settings"));
		cp5.addToggle("Display FPS counter").setPosition(260,360).setSize(40,20).setValue(rl.params.displayFPSCounter).setId(13).moveTo(cp5.getTab("Global settings"));
		
		cp5.getTab("Global settings").add(cp5.getController("Load Config"));
		cp5.getTab("Global settings").add(cp5.getController("Save Config"));		
		
		// fifth tab content
		
		cp5.addTextlabel("tab5Label", "STATISTICS", 50, 60).moveTo(cp5.getTab("Statistics"));
		
		// sixth tab content
		
		cp5.addTextlabel("tab6Label", "ABOUT", 50, 60).moveTo(
				cp5.getTab("About"));
		cp5.addTextlabel("about1",
				"Project created for the course GTI719 at l'École de technologie supérieure (ETS) in ",
				50, 90).moveTo(cp5.getTab("About"));
		cp5.addTextlabel("about2",
				"Montréal, by Francis Bonneau under the supervision of Professor Michael J. McGuffin, Ph.D.",
				50, 115).moveTo(cp5.getTab("About"));
		cp5.addTextlabel("about3",
				"using the following open source projects : Sysdig ( http://www.sysdig.org/ ), ",
				50, 140).moveTo(cp5.getTab("About"));
		cp5.addTextlabel("about4",
				"Redis ( http://redis.io/ ), Processing ( http://www.processing.org/ )",
				50, 165).moveTo(cp5.getTab("About"));
		cp5.addTextlabel("about5",
				"and ControlP5 ( http://www.sojamo.de/libraries/controlP5/ )",
				50, 190).moveTo(cp5.getTab("About"));
				
		cp5.addTextlabel("license", rl.params.softwareLicense, 50, 250)
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
				rl.params.particleMinVelocity = theEvent.getController().getArrayValue(0);
				rl.params.particleVelocityRangeMax = theEvent.getController().getArrayValue(1);			
				break;
			case (6):
				rl.params.particleAcceleration = newValue;
				break;
			case (7):
				rl.params.latencyRoundup = (int) newValue;
				Textlabel label = (Textlabel) cp5.get("latencyLabel");
				if (label != null)
					label.setStringValue( "( " + rl.params.latencyRoundupLegend[rl.params.latencyRoundup - 1] + " )");			
				break;
			case (8):
				
				if (rl.params.emittersRowsX < (int) newValue) {
					
					int newVal = (int) newValue;					
					int oldVal = rl.params.emittersRowsX;
					rl.params.emittersRowsX = newVal;
					
					for (int i = oldVal; i < newVal; i++ ) {
						rl.hud.regionManager.addRowOfEmittersAxisX();
					}
					
				} else if (rl.params.emittersRowsX > (int) newValue) {
					
					int newVal = (int) newValue;					
					int oldVal = rl.params.emittersRowsX;
					rl.params.emittersRowsX = newVal;
					
					for (int i = oldVal; i > newVal; i-- ) {
						rl.hud.regionManager.removeRowOfEmittersAxisX();
					}
					
				}
				break;
				
			case (9):

				// trick to place the slider at the correct val ( on top )
				if (rl.params.emittersRowsY < (int) Math.abs(5 - newValue)) {
					
					int newVal = (int) Math.abs(5 - newValue);				
					int oldVal = rl.params.emittersRowsY;
					rl.params.emittersRowsY = newVal;
					
					for (int i = oldVal; i < newVal; i++ ) {
						rl.hud.regionManager.addRowOfEmittersAxisY();
					}
					
				} else if (rl.params.emittersRowsY > (int) Math.abs(5 - newValue)) {
					
					int newVal = (int) Math.abs(5 - newValue);
					int oldVal = rl.params.emittersRowsY;
					rl.params.emittersRowsY = newVal;
					
					for (int i = oldVal; i > newVal; i-- ) {
						rl.hud.regionManager.removeRowOfEmittersAxisY();
					}
				}
				
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
			case (14):
					//p5.get("Server address - IP:PORT");
					println("button pressed");
					
				break;
			}			
		}	
	}

	// Called when the user clicks the button "Connect to server"
	public void newConnexionButton(int theValue) {
		// get the new server address from the adjacent textField
		String connexionAddress = cp5.get(Textfield.class, "newConnexionField").getValueLabel().getText();
		println("Trying to reach " + connexionAddress);
		rl.hud.da.addDataSource(connexionAddress);
	}

	public void draw() {		
		colorMode(HSB,360,100,100);
		background(0,0,0);
		
		if (currentTabIsDataDisplay) {	// draw emitters circles in the data source section
			
			noFill();
			stroke(255);
			int circlesDistanceX = 62;
			int circlesDistanceY = 45;
			int numberOfEmitters = rl.params.emittersRowsX * rl.params.emittersRowsY;
			
			for (int i = rl.params.emittersRowsX; i > 0; i--) {	
				for (int j = rl.params.emittersRowsY; j > 0; j--) {
					ellipse(73 + ( i * circlesDistanceX),
							135 + (j * circlesDistanceY), 25, 25);				
					textFont(openSans12);
					int ajustedXPos = 70; // adjust the X position of the text to center double digits
					if (numberOfEmitters > 9 )
						ajustedXPos = 66;
					text("" + numberOfEmitters, ajustedXPos + ( i * circlesDistanceX),
						140 + (j * circlesDistanceY));
					numberOfEmitters--;
				}
			}
		}
				
	}

	public ControlP5 control() {
		return cp5;
	}

}