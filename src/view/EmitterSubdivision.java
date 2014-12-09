package view;

/**
 * Simple class to store the parameters of an emitter section
 * @author Francis Bonneau
 */
public class EmitterSubdivision implements Cloneable {

	public float size;
	public float startAngleDeg;
	public float endAngleDeg;

	// TODO : figure out if this class should take more responsibilities
	public EmitterSubdivision(float size) {
		super();
		this.size = size;
	}

}
