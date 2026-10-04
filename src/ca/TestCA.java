package ca;

import processing.core.PApplet;
import setup.IProcessingApp;

public class TestCA implements IProcessingApp {

	private int nRows       = 15;
	private int nCols       = 20;
	private int nStates     = 4;
	private int radiusNeigh = 1;
	private CellularAutomata ca;
	
	@Override
	public void setup(PApplet p) {
		ca = new CellularAutomata(p, nRows, nCols, nStates, radiusNeigh);
		ca.initRandom();
	}

	@Override
	public void draw(PApplet p, float dt) {
		ca.display(p);		
	}

	@Override
	public void mousePressed(PApplet p) {
		Cell cell = ca.pixel2Cell(p.mouseX, p.mouseY);
		// cell.setState(nStates - 1);
		
		Cell[] neigh = cell.getNeighbors();
		for (int i = 0; i < neigh.length; i++) {
			neigh[i].setState(nStates-1);
		}
	}

}