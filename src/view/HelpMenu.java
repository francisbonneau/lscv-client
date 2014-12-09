package view;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Simple class in charge of the help menu, a group of images displayed at
 * the application startup to help the user understand the concepts of the
 * visualization. The help menu is like powerpoint, the user can change images
 * (or slides) that the press of a key.
 * @author Francis Bonneau
 */
public class HelpMenu {

    private PApplet p;

    private ArrayList<PImage> slides;	// The list of slides
    private PImage currentSlide;		// The currently displayed slide

    // Initialization
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

	// Return the currently active slide image
	public PImage getCurrentSlide() {
		return currentSlide;
	}

	// Change the currently active slide to the next (if there is a next)
	public void SwitchToNextSlide() {

		int index = slides.indexOf(currentSlide);

		if (index != slides.size() - 1) {
			index++;
		}
		currentSlide = slides.get(index);
	}

	// Change the currently active slide to the previous (if there is a previous)
	public void SwitchToPreviousSlide() {

		int index = slides.indexOf(currentSlide);

		if (index != 0) {
			index--;
		}
		currentSlide = slides.get(index);
	}

	// Draw the currently active slide (image)
	public void draw() {
		p.image(currentSlide, 0, 0);
	}

}
