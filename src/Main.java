import processing.core.*;

import Data.*;

public class Main extends PApplet {
	
	private static final long serialVersionUID = 1L;

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

	public static void main(String args[]) {
					
		System.out.print("starting redis thread");
		Thread t = new Thread(new RedisSubscribeThread());
        t.start();
		
		PApplet.main(new String[] { "--present", "Main" });
	}

}