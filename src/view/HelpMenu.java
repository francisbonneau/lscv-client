package view;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class HelpMenu {

    private PApplet p;

    private ArrayList<PImage> slides;

    private PImage currentSlide;

	public HelpMenu(PApplet p) {

		this.p = p;

		slides = new ArrayList<>();

		// Load the help menu slides images
		PImage slide1 = p.loadImage("slide1.png");
		slides.add(slide1);

		PImage slide2 = p.loadImage("slide2.png");
		slides.add(slide2);

		PImage slide3 = p.loadImage("slide3.png");
		slides.add(slide3);

		PImage slide4 = p.loadImage("slide4.png");
		slides.add(slide4);

		PImage slide5 = p.loadImage("slide5.png");
		slides.add(slide5);

		currentSlide = slide1;
	}

	public PImage getCurrentSlide() {

		return currentSlide;
	}

	public void SwitchToNextSlide() {

		int index = slides.indexOf(currentSlide);

		if (index != slides.size() - 1) {
			index++;
		}
		currentSlide = slides.get(index);
	}

	public void SwitchToPreviousSlide() {

		int index = slides.indexOf(currentSlide);

		if (index != 0) {
			index--;
		}
		currentSlide = slides.get(index);
	}

	public void draw() {
		p.image(currentSlide, 0, 0);
	}

}
