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
 * See ControlP5 documentation at  
 * http://www.sojamo.de/libraries/controlP5/reference/controlP5/ControlP5.html 
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
		//cp5.addTab("Filters").setColorLabel(color(255));
		cp5.addTab("Global settings").setColorLabel(color(255));		
		//cp5.addTab("Statistics").setColorLabel(color(255));
		cp5.addTab("About").setColorLabel(color(255));
				     		  
		cp5.getTab("default").activateEvent(true)
			.setLabel("  Data sources settings  ").setId(201);
		
		cp5.getTab("Data display settings").activateEvent(true)
			.setLabel("  Data display settings  ").setId(202);
		
		//cp5.getTab("Filters").activateEvent(true).setId(106);
		
		cp5.getTab("Global settings").activateEvent(true)
			.setLabel("    Global settings    ").setId(203);
		
		//cp5.getTab("Statistics").activateEvent(true).setId(104);
		
		cp5.getTab("About").activateEvent(true)
			.setLabel("       About       ").setId(204);
		
		// first tab content
		
		cp5.addTextlabel("tab1Label", "DATA SOURCES", 50, 60).moveTo(cp5.getTab("default"));
		cp5.addTextlabel("tab1Legend", "Enter here the network addresses of the servers to fetch data from.", 50, 90);
		// Name, X, Y, W, H		
		cp5.addTextfield("newConnexionField", 85, 135, 360, 25)
			.setValue(rl.params.defaultDataSource)
			.setLabel("Server address - IP:PORT").getCaptionLabel().setFont(openSans13);
			
		// Name, Value, X, Y, W, H
		cp5.addButton("newConnexionButton", 1, 470, 135, 165, 25).setLabel("Connect to server");
		
		Textarea myTextarea = cp5.addTextarea("txt", "", 85, 205, 550, 150)                  
                  .setFont(openSans14).setColor(color(255))
                  .setColorBackground(color(20))
                  .setColorForeground(color(255, 100));	
		cp5.addConsole(myTextarea);
		println("Ready to establish new connexions...");
		
		cp5.addTextlabel("tab1Labe2", "EVENTS FILTERS", 50, 380)
			.moveTo(cp5.getTab("default"));
		cp5.addTextlabel("tab1Legend2", "(Optionnal) Filters can be applied here to the "
				+ "data sources to restrict the incoming events.", 50, 410);		
		cp5.addTextlabel("tab1Legend3", "See https://github.com/draios/sysdig"
				+ "/wiki/Sysdig-User-Guide", 50, 435);
				
		cp5.addTextlabel("tab1Legend4", "Select data source :", 50, 480);
		
		cp5.addDropdownList("dataSourcesList2", 200, 498, 200, 105)
			.setBarHeight(17)
			.setLabel("Data sources list")				
			.setId(103);
		
		cp5.addTextfield("newfilterField", 85, 530, 360, 25)			
			.setValue("")
			.setLabel("example : proc.name=top").getCaptionLabel().setFont(openSans13);
		
		cp5.addButton("newFilterButton", 1, 470, 530, 165, 25)
			.setLabel("       Apply filter");
				
		cp5.addButton("Load Config", 1, 510, 615, 87, 20).captionLabel().setFont(openSans12);
		cp5.addButton("Save Config", 1, 605, 615, 87, 20).captionLabel().setFont(openSans12);		
		
		// second tab content
		
		cp5.addTextlabel("tab2Label", "DATA DISPLAY", 50, 60).moveTo(cp5.getTab("Data display settings"));
		cp5.addTextlabel("tab2Legend", "Ajust the sliders to subdivide "
				+ " the screen in multiples sections, each containing a circle.", 50, 90)
				.moveTo(cp5.getTab("Data display settings"));
		
		//addSlider(String theIndex, Sring theName, float theMin, float theMax, 
		//		float theDefaultValue, int theX, int theY, int theW, int theH)
		cp5.addSlider("  ", 1, rl.params.maxNumberOfEmittersX, 1, 130, 135,
				450, 10).setId(8).moveTo(cp5.getTab("Data display settings"))
				.setNumberOfTickMarks(8).showTickMarks(false)				
				.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);
				
		// trick to place the slider at the correct val ( on top ), we 
		// set the min and max val to the same val
		cp5.addSlider(" ", 1, rl.params.maxNumberOfEmittersY, 
				rl.params.maxNumberOfEmittersY, 90, 175, 10, 120)
				.setId(9).moveTo(cp5.getTab("Data display settings"))
				.setNumberOfTickMarks(4).showTickMarks(false)
				.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);
		
		cp5.addTextlabel("tab2Legend2", "Then select the data source that"
				+ " each circle should display :", 50, 350)
				.moveTo(cp5.getTab("Data display settings"));
		
		// (String theName, int theX, int theY, int theW, int theH)
		DropdownList d1 = cp5.addDropdownList("circlesList", 90, 400, 200, 125)				
				.setBarHeight(17)
				.setLabel("Circle number")
				.moveTo(cp5.getTab("Data display settings"))
				.setId(101);
		
		// (String theName, int theValue
		d1.addItem("Circle #1", 1);
		d1.setIndex(0);
		
		cp5.addTextlabel("tab2Legend3", "==", 305, 382)
				.setFont(openSans15)
				.moveTo(cp5.getTab("Data display settings"));
		
		cp5.addDropdownList("dataSourcesList", 345, 400, 200, 155)
			.setBarHeight(17)
			.setLabel("No data source yet")
			.moveTo(cp5.getTab("Data display settings"))
			.setId(102);
		
		cp5.addButton("selectDataSrcButton", 1, 570, 380, 70, 20)
			.setLabel("  Select  ")
			.moveTo(cp5.getTab("Data display settings"));
		
		cp5.getTab("Data display settings").add(cp5.getController("Load Config"));
		cp5.getTab("Data display settings").add(cp5.getController("Save Config"));
		
		// third tab content
		
		cp5.addTextlabel("tab4Label", "GLOBAL SETTINGS", 50, 60).moveTo(cp5.getTab("Global settings"));
		
		//addSlider(String theIndex, Sring theName, float theMin, float theMax, 
		//		float theDefaultValue, int theX, int theY, int theW, int theH)
		cp5.addSlider("Background brightness", 0, 100, 60, 90, 400, 15)
				.setId(1).setValue(rl.params.backgroundBrightness).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Circle radius", 100, 2000, 60, 120, 400, 15).setId(2)
				.setValue(rl.params.emitterRadius).moveTo(cp5.getTab("Global settings"));
		
		cp5.addSlider("Radius brightness", 0, 100, 60, 150, 400, 15).setId(3)
				.setValue(rl.params.emitterRadiusBrightness).moveTo(cp5.getTab("Global settings"));
		
		cp5.addSlider("Distance between circles", 0, 1000, 60, 180, 400, 15).setId(14)
		.setValue(rl.params.distanceBetweenEmitters).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Particle size", 1, 400, 60, 210, 400, 15).setId(4)
				.setValue(rl.params.particleSize).moveTo(cp5.getTab("Global settings"));

		cp5.addRange("Particle velocity", rl.params.particleVelocityRangeMin,
				rl.params.particleVelocityRangeMax,
				rl.params.particleMinVelocity,
				rl.params.particleMaxVelocity, 60, 240, 400, 15)
				.setId(5).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Particle acceleration", 0, 1, 60, 270, 400, 15).setId(6)
				.setValue(rl.params.particleAcceleration).moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Latency rounding", 1, 8, 60, 300, 400, 15).setId(7)
				.setValue(rl.params.latencyRoundup).setNumberOfTickMarks(8)
				.showTickMarks(false).moveTo(cp5.getTab("Global settings"));
		
		cp5.addTextlabel("latencyLabel", "( "
						+ rl.params.latencyRoundupLegend[rl.params.latencyRoundup - 1]
						+ " )", 620, 300).setFont(openSans15).moveTo(cp5.getTab("Global settings"));
		
		cp5.addToggle("Display Grid").setPosition(60,360).setSize(40,20)
			.setValue(rl.params.displayGrid).setId(10).moveTo(cp5.getTab("Global settings"));
		cp5.addToggle("Display Circle Radius").setPosition(260,360).setSize(40,20)
			.setValue(rl.params.displayEmitterRadius).setId(11).moveTo(cp5.getTab("Global settings"));
		cp5.addToggle("Display Labels").setPosition(60,410).setSize(40,20)
			.setValue(rl.params.displayEmitterRadius).setId(12).moveTo(cp5.getTab("Global settings"));
		cp5.addToggle("Display FPS counter").setPosition(260,410).setSize(40,20)
			.setValue(rl.params.displayFPSCounter).setId(13).moveTo(cp5.getTab("Global settings"));
		
		cp5.getTab("Global settings").add(cp5.getController("Load Config"));
		cp5.getTab("Global settings").add(cp5.getController("Save Config"));		
		
		// fifth tab content
			
		cp5.addTextlabel("ta56Label", "ABOUT", 50, 60).moveTo(
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
		
		if (theEvent.getId() > 200 ) {
			// if the event is a tab switch event, just clear the screen			
			if (theEvent.getId() == 202)
				currentTabIsDataDisplay = true;
			else
				currentTabIsDataDisplay = false;			
		} else if (theEvent.getId() > 100 ) {
			// if the event is a dropdown menu, do nothing
			
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
				
			// slider to control the number of rows in X
			case (8):
				
				if (rl.params.emittersRowsX < (int) newValue) {
					
					int newVal = (int) newValue;					
					int oldVal = rl.params.emittersRowsX;
					rl.params.emittersRowsX = newVal;
					
					for (int i = oldVal; i < newVal; i++ ) {
						rl.hud.regionManager.addRowOfEmittersAxisX();
					}
					updateCirclesDropdown();
					
				} else if (rl.params.emittersRowsX > (int) newValue) {
					
					int newVal = (int) newValue;					
					int oldVal = rl.params.emittersRowsX;
					rl.params.emittersRowsX = newVal;
					
					for (int i = oldVal; i > newVal; i-- ) {
						rl.hud.regionManager.removeRowOfEmittersAxisX();
					}
					updateCirclesDropdown();
					
				}			
				
				break;
			
			// slider to control the number of rows in Y
			case (9):

				// trick to place the slider at the correct val ( on top )
				int ajustedValue = (int) Math.abs(rl.params.maxNumberOfEmittersY
						+ 1 - newValue);
				
				if (rl.params.emittersRowsY < ajustedValue) {
					
					int newVal = ajustedValue;				
					int oldVal = rl.params.emittersRowsY;
					rl.params.emittersRowsY = newVal;
					
					for (int i = oldVal; i < newVal; i++ ) {
						rl.hud.regionManager.addRowOfEmittersAxisY();
					}
					updateCirclesDropdown();
					
				} else if (rl.params.emittersRowsY > ajustedValue) {
					
					int newVal = ajustedValue;
					int oldVal = rl.params.emittersRowsY;
					rl.params.emittersRowsY = newVal;
					
					for (int i = oldVal; i > newVal; i-- ) {
						rl.hud.regionManager.removeRowOfEmittersAxisY();
					}
					updateCirclesDropdown();
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
					rl.params.distanceBetweenEmitters = newValue;
					rl.hud.regionManager.refresh();
					
				break;
			}			
		}	
	}
	
	// update the dropdown to select the circles ids
	private void updateCirclesDropdown() {
		DropdownList d = (DropdownList) cp5.get("circlesList");		
		d.clear();		
		int circlesNb = rl.params.emittersRowsX * rl.params.emittersRowsY;		
		for (int i = 1; i <= circlesNb; i++ ) {
			d.addItem("Circle #" + i, i);
		}
	}

	// Called the the user click on the select button in the data src tab
	public void selectDataSrcButton(int theValue) {
		DropdownList d1 = (DropdownList) cp5.get("circlesList");
		DropdownList d2 = (DropdownList) cp5.get("dataSourcesList");
		
		int selectedCircle = (int) d1.getValue() - 1;
		String selectedDataSource = rl.hud.dataAgg.getDataSources().get((int) d2.getValue() -1);
		
		rl.hud.emitters.get(selectedCircle).host = selectedDataSource;		
	}

	// Called when the user clicks the button "Connect to server"
	public void newConnexionButton(int theValue) {
		// get the new server address from the adjacent textField
		String connexionAddress = cp5.get(Textfield.class, "newConnexionField")
				.getValueLabel().getText();
		
		println("Trying to reach " + connexionAddress);
		
		rl.hud.dataAgg.addDataSource(connexionAddress);
		DropdownList d = (DropdownList) cp5.get("dataSourcesList");
		int i = d.getListBoxItems().length;
		d.addItem(connexionAddress, i + 1);		
		d.setIndex(0);
		
		DropdownList d2 = (DropdownList) cp5.get("dataSourcesList2");
		i = d.getListBoxItems().length;
		d2.addItem(connexionAddress, i + 1);
		d2.setIndex(0);
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