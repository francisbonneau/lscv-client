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
		PImage slide1 = p.loadImage("help1.jpg");
		slides.add(slide1);

		PImage slide2 = p.loadImage("help2.jpg");
		slides.add(slide2);

		PImage slide3 = p.loadImage("help3.jpg");
		slides.add(slide3);

		PImage slide4 = p.loadImage("help4.jpg");
		slides.add(slide4);

		currentSlide = slide1;
	}

	public PImage getCurrentSlide() {

		return currentSlide;
	}

	public void SwitchToNextSlide() {

		int index = slides.indexOf(currentSlide);

		if (index != slides.size() - 1) {
			index++;
		} else {
			index = 0;
		}
		currentSlide = slides.get(index);
	}

	public void SwitchToPreviousSlide() {

		int index = slides.indexOf(currentSlide);

		if (index != 0) {
			index--;
		} else {
			index = slides.size() - 1;
		}
		currentSlide = slides.get(index);
	}

	public void draw() {
		p.image(currentSlide, 0, 0);
	}

}
