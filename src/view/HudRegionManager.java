package view;

import java.util.Iterator;

/**
 * This object handle the subdivision of the hud space into multiples regions,
 * by adding rows of emitters, horizontally or vertically
 * @author Francis Bonneau
 */
public class HudRegionManager {

    private Hud hud;

    // Constructor, create the first emitter
    public HudRegionManager(Hud hud) {
        this.hud = hud;

        // add the first emitter, there is always at least one emitter on the screen
        Emitter em = new Emitter(hud.p, hud, 1, 10, 10);
        hud.emitters.add(em);
        handleOnlyOneEmitterCase();
    }

    // Add a horizontal row of emitters on the screen, the number of emitters
    // added depends on the number of vertical emitters (Y row)
    public final void addRowOfEmittersAxisX() {

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

    // Add a vertical row of emitters on the screen, the number of emitters
    // added depends on the number of horizontal emitters (X row)
    public final void addRowOfEmittersAxisY() {

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

    // Remove a horizontal row of emitters on the screen, the number of emitters
    // removed depends on the number of vertical emitters (Y row)
    public final void removeRowOfEmittersAxisX() {

        for (int i = 0; i < hud.params.emittersRowsY; i++) {
            hud.emitters.remove(hud.emitters.size() - 1);
        }

        boolean onlyOneEmitter = handleOnlyOneEmitterCase();
        if (!onlyOneEmitter) {
            calculateEmittersPositions();
        }
    }

	// Remove a vertical row of emitters on the screen, the number of emitters
	// removed depends on the number of horizontal emitters (X row)
    public final void removeRowOfEmittersAxisY() {

        for (int i = 0; i < hud.params.emittersRowsX; i++) {
            hud.emitters.remove(hud.emitters.size() - 1);
        }

        boolean onlyOneEmitter = handleOnlyOneEmitterCase();
        if (!onlyOneEmitter) {
            calculateEmittersPositions();
        }
    }

    // Recalculate the emitter positions, check if there is only one emitter
    public final void refresh() {
        boolean onlyOneEmitter = handleOnlyOneEmitterCase();
        if (!onlyOneEmitter) {
            calculateEmittersPositions();
        }

    }

    // When there is only one X,Y row there is only one emitter, and in that
    // special case the only emitter is centered on the screen
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

    // Divide the screen according to the number of emitters, so that they are
    // positionned at even distance from each other
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
            if (Yincr < Xincr) {
				smallestDist = Yincr;
			}

            if (hud.params.emitterRadius > smallestDist) {
				hud.params.emitterRadius = smallestDist - 10;
			}
        }
    }

}
