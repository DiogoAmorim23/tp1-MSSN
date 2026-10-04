package dla;

import java.util.ArrayList;
import java.util.List;

import dla.Walker.State;
import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;

public class DLA implements IProcessingApp {

	private List<Walker> walkers;
	private int NUM_WALKERS         = 100;
	private int NUM_STEPS_PER_FRAME = 100;
	private int rIt = 0; // red component iterator
	private int gIt = 0; // green component iterator
	private int bIt = 0; // blue component iterator
	
	@Override
	public void setup(PApplet p) {
		walkers  = new ArrayList<Walker>();
		// A seed particle stays in the middle; all other particles wander.
		Walker w1 = new Walker(p, new PVector(p.width/2, p.height/2));
		w1.setColor(p.color(255, 0, 255));
		walkers.add(w1);
		for (int i = 0; i < NUM_WALKERS; i++) {
			walkers.add(new Walker(p, 7));
		}
	}

	@Override
	public void draw(PApplet p, float dt) {
		p.background(0);
		
		for (int i = 0; i < NUM_STEPS_PER_FRAME; i++)
			for (Walker w : walkers) 
				if (w.getState() == State.WANDER) {
					w.wander(p);
					w.updateState(p, walkers);
					if (w.getState() == State.STOPPED) colorPattern(p, w);
				}

		// Replace stopped particles so the number of moving particles stays fixed.
		int moving = 0;
		for (Walker w : walkers) {
			if (w.getState() == State.WANDER) moving++;
		}
		while (moving < NUM_WALKERS) {
			walkers.add(new Walker(p, 7));
			moving++;
		}

		for (Walker w : walkers) w.display(p);
		
//		System.out.println("Stopped = " + Walker.num_stopped +
//						   " Wander = " + Walker.num_wanders + 
//						   " Radius = " + w.getRadius());
	}
	
	/**
	 * Assigns a color pattern to the DLA
	 */
	private void colorPattern(PApplet p, Walker w) {
		
		if (rIt < 255 && gIt == 0 && bIt == 0) {          // Red to 0, Green - 255, Blue - 255
			w.setColor(p.color(255-rIt, 255, 255));
			rIt++;
		} else if (rIt == 255 && gIt < 255 && bIt == 0) { // Red - 0, Green to 0, Blue - 255
			w.setColor(p.color(0, 255-gIt, 255));
			gIt++;
		} else if (rIt > 0 && gIt == 255 && bIt < 255) {  // Red to 255, Green - 0, Blue to 0
			if (rIt == 255) rIt = 0;
			w.setColor(p.color(0+rIt, 0, 255-bIt));
			bIt++;
			rIt++;
		} else if (rIt == 255 && gIt > 0 && bIt == 255) { // Red - 255, Green to 255, Blue - 0
			if (gIt == 255) gIt = 0;
			w.setColor(p.color(255, 0+gIt, 0));
			gIt++;
			if (gIt == 255) gIt = -1;
		} else if (rIt == 255 && gIt == -1 && bIt > 0) {  // Red - 255, Green - 255, Blue to 255
			if (bIt == 255) bIt = 0;
			w.setColor(p.color(255, 255, 0+bIt));
			bIt++;
			if (bIt == 255) bIt = -1;
		}
		
	}

	@Override
	public void mousePressed(PApplet p) {
		// TODO Auto-generated method stub
		
	}

}
