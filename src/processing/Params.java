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
	
	// 10^−1 s	ds	decisecond
	// 10^−2 s	cs	centisecond
	// 10^−3 s	ms	millisecond
	// 10^−6 s	µs	microsecond
	// 10^−9 s	ns	nanosecond
	public float latencyRoundup = 2; 
	
	
	
	
}
