package processing;

import processing.core.PVector;

public class Params {
	
	// default parameters of the application
	
	// display parameters
	public boolean fullscreen = false;
	public int defaultHeight = 800;
	public int defaultWidth = 800;
	public int framerate = 30;
	public boolean fpsCounter = true;
	public boolean resizable = false; 
	
	// data source
	public String[] hosts = { "10.0.0.60:6379" };
	
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
	public float latencyRoundup = 6;
	
	public String[] latencyRoundupLegend = {
		"100 ms", "10 ms", "1 ms",
		"100 µs", "10 µs", "1 µs",
		"100 ns", "10 ns", "1 ns"
	};
		
	public float emitterRadius = 500;
	public int emitterRadiusColor = 25;
	public boolean drawEmitterRadius = true;

	public float particleVelocity = 1;
	public float particleAcceleration = 0.25f;
	public float particleSize = 10;
	
	
	
}
