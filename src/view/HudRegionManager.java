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
		Emitter em = new Emitter(hud.p, hud, 10, 10);
		em.id = 1;
		hud.emitters.add(em);
		handleOnlyOneEmitterCase();
	}
	
	public void addRowOfEmittersAxisX() {
		
		boolean onlyOneEmitter = handleOnlyOneEmitterCase();
		if (!onlyOneEmitter) {
								
			for (int i = 0; i < hud.params.emittersRowsY; i++) { 
				Emitter em = new Emitter(hud.p, hud, 10,10);
				em.id = hud.emitters.size() + 1;
				hud.emitters.add(em);
			}
			calculateEmittersPositions();
		}

	}
	
	public void addRowOfEmittersAxisY() {
		
		boolean onlyOneEmitter = handleOnlyOneEmitterCase();
		if (!onlyOneEmitter) {
			
			for (int i = 0; i < hud.params.emittersRowsX; i++) { 
				Emitter em = new Emitter(hud.p, hud, 10,10);
				em.id = hud.emitters.size() + 1;
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
		
	}
	
	private void calculateEmittersPositions() {
		
		int emittersX = hud.params.emittersRowsX;
		int emittersY = hud.params.emittersRowsY;
		
		// and recalculate the position of each emitter
		// but only if the number of emitters match the expected value 
		int expectedNbEmitters = emittersX * emittersY;

		if (hud.emitters.size() == expectedNbEmitters) {			
			Iterator<Emitter> it = hud.emitters.iterator();

			float Xincr = hud.p.width / (emittersX + 1);
			float Yincr = hud.p.height / (emittersY + 1);

			for (int i = 1; i <= emittersX; i++) {
				for (int j = 1; j <= emittersY; j++) {
					Emitter currentEmitter = it.next();
					currentEmitter.centerX = i * Xincr;
					currentEmitter.centerY = j * Yincr;
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
