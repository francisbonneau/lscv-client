package view;

import java.util.Iterator;

import model.Params;


/**
 * This object should handle the subdivision of the hud space into multiples 
 * regions
 */
public class HudRegionManager {
	
	private Hud hud;
	
	public HudRegionManager(Hud hud) {
		this.hud = hud;
		
		// add the first emitter
		Emitter em = new Emitter(hud.p, hud, 1, 10, 10);		
		hud.emitters.add(em);
		handleOnlyOneEmitterCase();
	}
	
	public void refresh() {		
		boolean onlyOneEmitter = handleOnlyOneEmitterCase();
		System.out.println("onlyOneEmitter : " + onlyOneEmitter);
		if (!onlyOneEmitter) {
			calculateEmittersPositions();
		}
		
	}
	
	public void addRowOfEmittersAxisX() {
		
		boolean onlyOneEmitter = handleOnlyOneEmitterCase();
		if (!onlyOneEmitter) {
								
			for (int i = 0; i < hud.params.emittersRowsY; i++) {
				
				int id = hud.emitters.size() + 1; 
				Emitter em = new Emitter(hud.p, hud, id,  10,10);				
				hud.emitters.add(em);
			}
			calculateEmittersPositions();
		}

	}
	
	public void addRowOfEmittersAxisY() {
		
		boolean onlyOneEmitter = handleOnlyOneEmitterCase();
		if (!onlyOneEmitter) {
			
			for (int i = 0; i < hud.params.emittersRowsX; i++) { 
				int id = hud.emitters.size() + 1; 
				Emitter em = new Emitter(hud.p, hud, id,  10,10);
				hud.emitters.add(em);
			}
			calculateEmittersPositions();
		}
	}
	
	public void removeRowOfEmittersAxisX() {
				
		for (int i = 0; i < hud.params.emittersRowsY; i++) { 
			hud.emitters.remove(hud.emitters.size() - 1);
		}
		
		boolean onlyOneEmitter = handleOnlyOneEmitterCase();
		if (!onlyOneEmitter) {
			calculateEmittersPositions();
		}
	}
	
	public void removeRowOfEmittersAxisY() {
		
		for (int i = 0; i < hud.params.emittersRowsX; i++) { 
			hud.emitters.remove(hud.emitters.size() - 1);
		}
		
		boolean onlyOneEmitter = handleOnlyOneEmitterCase();
		if (!onlyOneEmitter) {
			calculateEmittersPositions();
		}		
	}
	
	private void calculateEmittersPositions() {
		
		int emittersX = hud.params.emittersRowsX;
		int emittersY = hud.params.emittersRowsY;
		
		// and recalculate the position of each emitter
		// but only if the number of emitters match the expected value 
		int expectedNbEmitters = emittersX * emittersY;

		if (hud.emitters.size() == expectedNbEmitters && 
				hud.emitters.size() > 1) {
			
			Iterator<Emitter> it = hud.emitters.iterator();

			float Xincr = (hud.p.width + hud.params.distanceBetweenEmitters) 
					/ (emittersX + 1);			
			float Yincr = (hud.p.height + hud.params.distanceBetweenEmitters) 
					/ (emittersY + 1);

			for (int i = 1; i <= emittersX; i++) {
				for (int j = 1; j <= emittersY; j++) {
					Emitter currentEmitter = it.next();
					currentEmitter.centerX = i * Xincr -
							hud.params.distanceBetweenEmitters/2;
					currentEmitter.centerY = j * Yincr - 
							hud.params.distanceBetweenEmitters/2;
				}
			}
			
			// also change the emitters radius to avoid collisions
			float smallestDist = Xincr;
			if (Yincr < Xincr)
				smallestDist = Yincr;
			
			if (hud.params.emitterRadius > smallestDist) 
				hud.params.emitterRadius = smallestDist - 10;
		}
		
	}
	
	private boolean handleOnlyOneEmitterCase() {
		
		if (hud.params.emittersRowsX == 1 && hud.params.emittersRowsY == 1) { 
			// if there is only one emitter, center it
			int centerX = hud.p.width / 2;
			int centerY = hud.p.height / 2;

			Emitter em = hud.emitters.get(0);
			em.centerX = centerX;
			em.centerY = centerY;
			return true;
		} else {
			return false;
		}
	}
	
}
