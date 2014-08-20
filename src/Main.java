import processing.core.*;

import Data.*;

public class Main extends PApplet {
	
	private static final long serialVersionUID = 1L;
	
	public static void main(String args[]) {
		
		String[] hosts = { "10.0.0.60:6379" };
		
		DataAggregator da = new DataAggregator(hosts);
				
		PApplet.main(new String[] { "--present", "Main" });
	}

	public void setup() {
		size(1200, 1200);
		background(0);
	}

	public void draw() {
		stroke(255);
		if (mousePressed) {
			line(mouseX, mouseY, pmouseX, pmouseY);
		}
	}



}