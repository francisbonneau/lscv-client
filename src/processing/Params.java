package processing;


public class Params {
	
	// default parameters of the application
	
	// display parameters
	public boolean fullscreen = false;
	public int defaultHeight = 900;
	public int defaultWidth = 900;
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
	public int latencyRoundup = 6;
	
	public String[] latencyRoundupLegend = {
		"100 ms", "10 ms", "1 ms",
		"100 µs", "10 µs", "1 µs",
		"100 ns", "10 ns", "1 ns"
	};
	
	public float backgroundBrightness = 10;	
	public float emitterRadius = 500;
	public float emitterRadiusBrightness = 15;
	public boolean drawEmitterRadius = true;

	public float particleSize = 10;		
	public float particleMinVelocity = 1;
	public float particleCurrentMinVelocity = 5;	
	public float particleCurrentMaxVelocity = 80;
	public float particleMaxVelocity = 100;	
	public float particleAcceleration = 0;	
	
	
}
