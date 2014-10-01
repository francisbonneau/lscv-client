package model;


public class Params {
	
	// default parameters of the application
	
	// display parameters
	public boolean fullscreen = false;
	
	public int mainWindowHeight = 800;
	public int mainWindowWidth = 1200;
		
	public int controlsWindowHeight = 700;
	public int controlsWindowWidth = 725;
	
	public int framerate = 30;	
	public boolean resizable = false;
	
	public boolean displayFPSCounter = true;	
	public boolean displayGrid = true;
		
	// data source
	public String defaultDataSource = "10.0.0.60:6379";
	
	// visualisation parameters
		
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
	public int latencyRoundup = 6;
	
	public String[] latencyRoundupLegend = {
		"100 ms", "10 ms", "1 ms",
		"100 µs", "10 µs", "1 µs",
		"100 ns", "10 ns", "1 ns"
	};
	
	public float backgroundBrightness = 10;	
	
	public float emitterRadius = 500;
	public float emitterRadiusBrightness = 15;		
	public boolean displayEmitterRadius = true;
	public boolean displayEmitterLabels = true;	
	public int maxNumberOfEmittersX = 8;
	public int maxNumberOfEmittersY = 4;
	public float distanceBetweenEmitters = 35;

	public float particleSize = 10;		
	public float particleVelocityRangeMin = 1;
	public float particleMinVelocity = 5;
	public float particleMaxVelocity = 100;
	public float particleVelocityRangeMax = 1000;	
	public float particleAcceleration = 0;	
		
	public int emittersRowsX = 1;
	public int emittersRowsY = 1;	
	
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
