package model;


public class Params {

    // Default parameters of the application, those variables can be changed
	// in runtime, eg. by the user in the settings window or possibly via an
	// API in the future.

    // ---- Display parameters

	// If the main window should take all the screen (different then fulllscreen)
    public boolean windowMaximized = false;

    // Non-fullscreen default window height
    public int mainWindowHeight = 800;

    // Non-fullscreen default window width
    public int mainWindowWidth = 1200;

    // Default height for the settings window
    public int settingsWindowHeight = 700;

    // Default width for the settings window
    public int settingsWindowWidth = 725;

    // Default framerate maximum limit
    public int maxFramerate = 30;

    // True if the user can resize the app window (in non fullscreen mode)
    public boolean resizable = false;

    // Display the frames-per-second counter on the bottom right
    public boolean displayFPSCounter = true;

    // Display a grid on the background
    public boolean displayGrid = false;

    // Display stats on the # of events/particles in the bottom left corner
    public boolean displayStats = true;

    // Draw a visible border around each particle (cercle) displayed
    // It is way easier to distinguish multiples circles on top of each
    // other when it is on
    public boolean drawCirclesStrokes = false;

    // Default data source
    public String defaultDataSource = "127.0.0.1:6379";

    // Used to stop the dat viz, eg. by pressing the space bar key
    public boolean displayPaused = false;

    // ---- Visualisation parameters

    // between 1 and 8, used to control the number of particles
    // displayed by rounding up the events lat. in buckets (heat map)
    // 10^−1 s	100 ms millisecond
    // 10^−2 s	10 	ms millisecond
    // 10^−3 s	1   ms millisecond
    // 10^−4 s	100 µs microsecond
    // 10^−5 s	10  µs microsecond
    // 10^−6 s	1   µs microsecond
    // 10^−7 s	100 ns nanosecond
    // 10^−8 s	10  ns nanosecond
    // 10^−9 s	1   ns nanosecond
    public int latencyRoundup = 3;

    public String[] latencyRoundupLegend = {
        "100 ms", "10 ms", "1 ms",
        "100 µs", "10 µs", "1 µs",
        "100 ns", "10 ns", "1 ns"
    };

    // Background color of the entire window
    public float backgroundBrightness = 9.0f;

    // An emitter (as in particle emitter) represent the circle where all the
    // events are displayed as particles
    public float emitterRadius = 500;          // the radius the circle
    public float emitterRadiusBrightness = 15; // the brightness of the circle stroke

    // If the emitter radius (a single gray circle) should be visible
    public boolean displayEmitterRadius = true;

    // If the labels for the categories should be displayed around the emitter
    public boolean displayEmitterLabels = true;

    // Every 10 seconds the emitter divisions are resetted to display only
    // the data from the last 10 seconds
    public int emitterDivisionsIntervalSec = 10;

    // However the emitter can also represent the movement of the different
    // subdivisons over time intervals, by adding others circles on top of
    // the particle emitter, each new circle (halo) represent a different time

    // If the halos should be visible
    public boolean displayEmitterHalos = true;

    // in this case 3 halos are displayed, for the 1 min, 5 min, and 15 min data
    public int[] emitterHalosIntervalsSec = {10, 60, 300, 900};

    // The maximum number of emitter columns
    public int maxNumberOfEmittersX = 8;

    // The maximum number of emitter rows
    public int maxNumberOfEmittersY = 4;

    // The default number of emitter rows
    public int emittersRowsX = 1;

    // The default number of emitter columns
    public int emittersRowsY = 1;

    // The distance between all the emitters
    public float distanceBetweenEmitters = 35;

    // The size of each particle (circle) displayed
    public float particleSize = 10;

    // Each particle speed is calculated by comparing its latency to the overall
    // events latency. So the speed is relative, but the ranges can be adjusted
    // to speed up or slow down all particles.
    public float particleMinVelocity = 5;
    public float particleMaxVelocity = 50;

    // Particles acceleration, not used in the data viz by default
    // but can be applied to all particles if the user wish
    public float particleAcceleration = 0;

    // The text displayed in the *about* panel of the settings window
    public String softwareLicense = "Copyright (c) 2014 Francis Bonneau\n" +
        "\n" +
        "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
        "of this software and associated documentation files (the \"Software\"), to deal\n" +
        "in the Software without restriction, including without limitation the rights\n" +
        "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
        "copies of the Software, and to permit persons to whom the Software is\n" +
        "furnished to do so, subject to the following conditions:\n" +
        "\n" +
        "The above copyright notice and this permission notice shall be included in\n" +
        "all copies or substantial portions of the Software.\n" +
        "\n" +
        "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
        "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
        "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
        "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
        "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
        "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n" +
        "THE SOFTWARE.";
}
