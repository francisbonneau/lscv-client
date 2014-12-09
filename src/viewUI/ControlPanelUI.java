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
import controller.MainLoop;

/**
 * See ControlP5 documentation at
 * http://www.sojamo.de/libraries/controlP5/reference/controlP5/ControlP5.html
 */
public class ControlPanelUI extends PApplet {

	private static final long serialVersionUID = 1L;

	ControlP5 cp5;
	private MainLoop rl;

	public int windowWidth;
	public int windowHeight;
	Boolean currentTabIsDataDisplay = false;

	PFont openSans12;
	PFont openSans13;
	PFont openSans14;
	PFont openSans15;
	PFont eurostile15;

	public ControlPanelUI(MainLoop rl) {
		this.rl = rl;
		this.windowWidth = rl.getParams().settingsWindowWidth;
		this.windowHeight =  rl.getParams().settingsWindowHeight;
	}

	@SuppressWarnings("deprecation")
	public void setup() {

		size(windowWidth, windowHeight);
		frameRate(25);
		cp5 = new ControlP5(this);
		cp5.window().setPositionOfTabs(20,20);

		// Load fonts
		openSans12 = loadFont("OpenSans-12.vlw");
		openSans13 = loadFont("OpenSans-13.vlw");
 		openSans14 = loadFont("OpenSans-14.vlw");
		openSans15 = loadFont("OpenSans-15.vlw");
		cp5.setFont(openSans15);

		// ---------- Setup the tabs

		cp5.getTab("default").setColorLabel(color(255));
		cp5.addTab("Data display settings").setColorLabel(color(255));
		cp5.addTab("Global settings").setColorLabel(color(255));
		cp5.addTab("About").setColorLabel(color(255));

		cp5.getTab("default").activateEvent(true)
			.setLabel("  Data sources settings  ").setId(201);

		cp5.getTab("Data display settings").activateEvent(true)
			.setLabel("  Data display settings  ").setId(202);

		cp5.getTab("Global settings").activateEvent(true)
			.setLabel("    Global settings    ").setId(203);

		cp5.getTab("About").activateEvent(true)
			.setLabel("       About       ").setId(204);

		// ---------- first tab content

		cp5.addTextlabel("tab1Label", "DATA SOURCES", 50, 60)
			.moveTo(cp5.getTab("default"));

		cp5.addTextlabel("tab1Legend", "Enter here the network addresses of" +
			" the servers to fetch data from.", 50, 90);

		// Name, X, Y, W, H
		cp5.addTextfield("newConnexionField", 85, 135, 360, 25)
			.setValue(rl.getParams().defaultDataSource)
			.setLabel("Server address - IP:PORT")
			.getCaptionLabel().setFont(openSans13);

		// Name, Value, X, Y, W, H
		cp5.addButton("newConnexionButton", 1, 470, 135, 165, 25)
			.setLabel("Connect to server");

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
			.setBarHeight(17).setLabel("Data sources list").setId(103);

		cp5.addTextfield("newfilterField", 85, 530, 360, 25)
			.setValue("").setLabel("example : proc.name=top")
			.getCaptionLabel().setFont(openSans13);

		cp5.addButton("newFilterButton", 1, 470, 530, 165, 25)
			.setLabel("       Apply filter");

		cp5.addButton("Load Config", 1, 510, 615, 87, 20).captionLabel().setFont(openSans12);
		cp5.addButton("Save Config", 1, 605, 615, 87, 20).captionLabel().setFont(openSans12);

		// ---------- second tab content

		cp5.addTextlabel("tab2Label", "DATA DISPLAY", 50, 60)
		.moveTo(cp5.getTab("Data display settings"));

		cp5.addTextlabel("tab2Legend", "Ajust the sliders to subdivide "
			+ " the screen in multiples sections, each containing a circle.", 50, 90)
			.moveTo(cp5.getTab("Data display settings"));

		//addSlider(String theIndex, Sring theName, float theMin, float theMax,
		//		float theDefaultValue, int theX, int theY, int theW, int theH)
		cp5.addSlider("  ", 1, rl.getParams().maxNumberOfEmittersX, 1, 130, 135,
			450, 10).setId(8).moveTo(cp5.getTab("Data display settings"))
			.setNumberOfTickMarks(8).showTickMarks(false)
			.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);

		// trick to place the slider at the correct val ( on top ), we
		// set the min and max val to the same val
		cp5.addSlider(" ", 1, rl.getParams().maxNumberOfEmittersY,
			rl.getParams().maxNumberOfEmittersY, 90, 175, 10, 120)
			.setId(9).moveTo(cp5.getTab("Data display settings"))
			.setNumberOfTickMarks(4).showTickMarks(false)
			.setSliderMode(Slider.FLEXIBLE).valueLabel().setVisible(false);

		cp5.addTextlabel("tab2Legend2", "Then select the data source that"
			+ " each circle should display :", 50, 350)
			.moveTo(cp5.getTab("Data display settings"));

		// (String theName, int theX, int theY, int theW, int theH)
		DropdownList d1 = cp5.addDropdownList("circlesList", 90, 400, 200, 125)
			.setBarHeight(17).setLabel("Circle number")
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

		// ---------- third tab content

		cp5.addTextlabel("tab4Label", "GLOBAL SETTINGS", 50, 60)
			.moveTo(cp5.getTab("Global settings"));

		//addSlider(String theIndex, Sring theName, float theMin, float theMax,
		//		float theDefaultValue, int theX, int theY, int theW, int theH)
		cp5.addSlider("Background brightness", 0, 100, 60, 90, 400, 15)
			.setId(1).setValue(rl.getParams().backgroundBrightness)
			.moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Circle radius", 100, 2000, 60, 120, 400, 15).setId(2)
			.setValue(rl.getParams().emitterRadius)
			.moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Radius brightness", 0, 100, 60, 150, 400, 15)
			.setId(3).setValue(rl.getParams().emitterRadiusBrightness)
			.moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Distance between circles", 0, 1000, 60, 180, 400, 15)
			.setId(15).setValue(rl.getParams().distanceBetweenEmitters)
			.moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Particle size", 1, 400, 60, 210, 400, 15)
			.setId(4).setValue(rl.getParams().particleSize)
			.moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Particle min speed", 1, 10,
			rl.getParams().particleMaxVelocity, 60, 240, 400, 15)
			.setId(16).setValue(rl.getParams().particleMinVelocity)
			.moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Particle max speed", 10, 100,
			rl.getParams().particleMinVelocity, 60, 270, 400, 15)
			.setId(17).setValue(rl.getParams().particleMaxVelocity)
			.moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Particle acceleration", 0, 1, 60, 300, 400, 15).setId(6)
			.setValue(rl.getParams().particleAcceleration)
			.moveTo(cp5.getTab("Global settings"));

		cp5.addSlider("Latency rounding", 1, 8, 60, 330, 400, 15).setId(7)
			.setValue(rl.getParams().latencyRoundup).setNumberOfTickMarks(8)
			.showTickMarks(false).moveTo(cp5.getTab("Global settings"));

		cp5.addTextlabel("latencyLabel", "( "
			+ rl.getParams().latencyRoundupLegend[rl.getParams().latencyRoundup - 1]
			+ " )", 620, 330).setFont(openSans15)
			.moveTo(cp5.getTab("Global settings"));

		// switches for on/off settings
		// first row
		cp5.addToggle("Display Grid").setPosition(60,370).setSize(40,20)
			.setValue(rl.getParams().displayGrid)
			.setId(10).moveTo(cp5.getTab("Global settings"));

		cp5.addToggle("Display Circle Radius").setPosition(260,370)
			.setSize(40,20).setValue(rl.getParams().displayEmitterRadius)
			.setId(11).moveTo(cp5.getTab("Global settings"));

		cp5.addToggle("Draw circles strokes").setPosition(460,370)
			.setSize(40,20).setValue(rl.getParams().drawCirclesStrokes)
			.setId(14).moveTo(cp5.getTab("Global settings"));

		// second row
		cp5.addToggle("Display Labels").setPosition(60,420).setSize(40,20)
			.setValue(rl.getParams().displayEmitterRadius)
			.setId(12).moveTo(cp5.getTab("Global settings"));

		cp5.addToggle("Display FPS counter").setPosition(260,420)
			.setSize(40,20).setValue(rl.getParams().displayFPSCounter)
			.setId(13).moveTo(cp5.getTab("Global settings"));

		cp5.addToggle("Display Stats").setPosition(460,420)
			.setSize(40,20).setValue(rl.getParams().displayStats)
			.setId(18).moveTo(cp5.getTab("Global settings"));

		// third row

		cp5.addToggle("Display halos").setPosition(60,470)
			.setSize(40,20).setValue(rl.getParams().displayEmitterHalos)
			.setId(19).moveTo(cp5.getTab("Global settings"));


		cp5.getTab("Global settings").add(cp5.getController("Load Config"));
		cp5.getTab("Global settings").add(cp5.getController("Save Config"));

		// ---------- fifth tab content

		cp5.addTextlabel("ta56Label", "ABOUT", 50, 60).moveTo(
				cp5.getTab("About"));

		cp5.addTextlabel("about1", "Project created for the course GTI719 at "+
				" l'École de technologie supérieure (ETS) in ",
				50, 90).moveTo(cp5.getTab("About"));

		cp5.addTextlabel("about2",
				"Montréal, by Francis Bonneau under the supervision of " +
				"Professor Michael J. McGuffin, Ph.D.",
				50, 115).moveTo(cp5.getTab("About"));

		cp5.addTextlabel("about3",
				"using the following open source projects : Sysdig " +
				"( http://www.sysdig.org/ ), ",
				50, 140).moveTo(cp5.getTab("About"));

		cp5.addTextlabel("about4",
				"Redis ( http://redis.io/ ), Processing " +
				"( http://www.processing.org/ )",
				50, 165).moveTo(cp5.getTab("About"));

		cp5.addTextlabel("about5",
				"and ControlP5 ( http://www.sojamo.de/libraries/controlP5/ )",
				50, 190).moveTo(cp5.getTab("About"));

		cp5.addTextlabel("license", rl.getParams().softwareLicense, 50, 250)
				.setFont(openSans14).moveTo(cp5.getTab("About"));
	}


	/**
	 *  TODO Clean up this terrible mess
	 */
	public void controlEvent(ControlEvent theEvent) {

		if (theEvent.getId() > 200 ) {
			// if the event is a tab switch event, just clear the screen
			if (theEvent.getId() == 202)
				currentTabIsDataDisplay = true;
			else
				currentTabIsDataDisplay = false;
		} else if (theEvent.getId() > 100 ) {
			// if the event is a dropdown menu, do nothing

			switch (theEvent.getId()) {
			case (103): // filter data source

				DropdownList d =  (DropdownList) cp5.get("dataSourcesList2");
				String host = rl.getDataAgg().getDataSources().get((int) d.getValue() -2);
				String filter = rl.getDataAgg().getDataSourceFilter(host);
				if (filter != null) {
					Textfield tf = (Textfield) cp5.get("newfilterField");
					tf.setValue(filter);
				}

				break;
			}

		} else {
			// if the event is associated with a controller ex. slider
			float newValue = theEvent.getController().getValue();
			switch (theEvent.getController().getId()) {
			case (1):
				rl.getParams().backgroundBrightness = newValue;
				break;

			case (2):
				rl.getParams().emitterRadius = newValue;
				break;

			case (3):
				rl.getParams().emitterRadiusBrightness = newValue;
				break;

			case (4):
				rl.getParams().particleSize = newValue;
				break;

			case (6):
				rl.getParams().particleAcceleration = newValue;
				break;
			case (7):
				rl.getParams().latencyRoundup = (int) newValue;
				Textlabel label = (Textlabel) cp5.get("latencyLabel");
				if (label != null)
					label.setStringValue( "( " +
						rl.getParams().latencyRoundupLegend[rl.getParams().latencyRoundup - 1] + " )");
				break;

			// slider to control the number of rows in X
			case (8):

				if (rl.getParams().emittersRowsX < (int) newValue) {

					int newVal = (int) newValue;
					int oldVal = rl.getParams().emittersRowsX;
					rl.getParams().emittersRowsX = newVal;

					for (int i = oldVal; i < newVal; i++ ) {
						rl.getHud().regionManager.addRowOfEmittersAxisX();
					}
					updateCirclesDropdown();

				} else if (rl.getParams().emittersRowsX > (int) newValue) {

					int newVal = (int) newValue;
					int oldVal = rl.getParams().emittersRowsX;
					rl.getParams().emittersRowsX = newVal;

					for (int i = oldVal; i > newVal; i-- ) {
						rl.getHud().regionManager.removeRowOfEmittersAxisX();
					}
					updateCirclesDropdown();

				}

				break;

			// slider to control the number of rows in Y
			case (9):

				// trick to place the slider at the correct val ( on top )
				int ajustedValue = (int) Math.abs(rl.getParams().maxNumberOfEmittersY
						+ 1 - newValue);

				if (rl.getParams().emittersRowsY < ajustedValue) {

					int newVal = ajustedValue;
					int oldVal = rl.getParams().emittersRowsY;
					rl.getParams().emittersRowsY = newVal;

					for (int i = oldVal; i < newVal; i++ ) {
						rl.getHud().regionManager.addRowOfEmittersAxisY();
					}
					updateCirclesDropdown();

				} else if (rl.getParams().emittersRowsY > ajustedValue) {

					int newVal = ajustedValue;
					int oldVal = rl.getParams().emittersRowsY;
					rl.getParams().emittersRowsY = newVal;

					for (int i = oldVal; i > newVal; i-- ) {
						rl.getHud().regionManager.removeRowOfEmittersAxisY();
					}
					updateCirclesDropdown();
				}

				break;

			case (10):
				if (newValue == 0.0)
					rl.getParams().displayGrid = false;
				else
					rl.getParams().displayGrid = true;
				break;

			case (11):
				if (newValue == 0.0)
					rl.getParams().displayEmitterRadius = false;
				else
					rl.getParams().displayEmitterRadius = true;
				break;

			case (12):
				if (newValue == 0.0)
					rl.getParams().displayEmitterLabels = false;
				else
					rl.getParams().displayEmitterLabels = true;
				break;

			case (13):
				if (newValue == 0.0)
					rl.getParams().displayFPSCounter = false;
				else
					rl.getParams().displayFPSCounter = true;
				break;

			case (14):
				if (newValue == 0.0)
					rl.getParams().drawCirclesStrokes = false;
				else
					rl.getParams().drawCirclesStrokes = true;
				break;

			case (15):
				rl.getParams().distanceBetweenEmitters = newValue;
				rl.getHud().regionManager.refresh();
				break;

			case (16):
				rl.getParams().particleMinVelocity = newValue;
				break;

			case(17):
				rl.getParams().particleMaxVelocity = newValue;
				break;

			case(18):
				if (newValue == 0.0)
					rl.getParams().displayStats = false;
				else
					rl.getParams().displayStats = true;
				break;

			case(19):
				if (newValue == 0.0) {
					rl.getParams().displayEmitterHalos = false;
					rl.getParams().emitterLabelsDistance = 35;
					rl.getHud().updateLabelsPositions();
				}
				else {
					rl.getParams().displayEmitterHalos = true;
					rl.getParams().emitterLabelsDistance = 105;
					rl.getHud().updateLabelsPositions();
				}

				break;

			}
		}
	}

	// update the dropdown to select the circles ids
	private void updateCirclesDropdown() {
		DropdownList d = (DropdownList) cp5.get("circlesList");
		d.clear();
		int circlesNb = rl.getParams().emittersRowsX * rl.getParams().emittersRowsY;
		for (int i = 1; i <= circlesNb; i++ ) {
			d.addItem("Circle #" + i, i);
		}
	}


	// add a new filter
	public void newFilterButton(int theValue) {

		DropdownList d = (DropdownList) cp5.get("dataSourcesList2");
		String host = rl.getDataAgg().getDataSources().get((int) d.getValue() -2);
		String filter = cp5.get(Textfield.class, "newfilterField")
								.getValueLabel().getText();

		rl.getDataAgg().applyFilterToDataSource(host, filter);
	}

	// Called the the user click on the select button in the data src tab
	public void selectDataSrcButton(int theValue) {
		DropdownList d1 = (DropdownList) cp5.get("circlesList");
		DropdownList d2 = (DropdownList) cp5.get("dataSourcesList");

		int selectedCircle = (int) d1.getValue() - 1;
		String selectedDataSource = rl.getDataAgg().getDataSources().get((int) d2.getValue() -1);

		rl.getHud().emitters.get(selectedCircle).host = selectedDataSource;
	}

	// Called when the user clicks the button "Connect to server"
	public void newConnexionButton(int theValue) {
		// get the new server address from the adjacent textField
		String connexionAddress = cp5.get(Textfield.class, "newConnexionField")
				.getValueLabel().getText();

		println("Trying to reach " + connexionAddress);

		rl.getDataAgg().addDataSource(connexionAddress);
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
			int numberOfEmitters = rl.getParams().emittersRowsX * rl.getParams().emittersRowsY;

			for (int i = rl.getParams().emittersRowsX; i > 0; i--) {
				for (int j = rl.getParams().emittersRowsY; j > 0; j--) {
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