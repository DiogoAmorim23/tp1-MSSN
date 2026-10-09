package ca;

import processing.core.PApplet;
import setup.IProcessingApp;

public class TestGOL implements IProcessingApp {
	
	private int ncols       = 40;
	private int nrows       = 30;
	private int nStates     = 2;
	private int radiusNeigh = 1;
	private int generation  = 0;
	private boolean running = false; // Programa começa parado
	private GameOfLife gol;
	
	@Override
	public void setup(PApplet p) {
		gol = new GameOfLife(p, nrows, ncols, nStates, radiusNeigh);
		gol.initRandom();
	}
	
	/**
	 * To create, click on the cells
	 * To start/stop simulation press 'SPACE'
	 * To clear window press 'C'
	 */
	@Override
	public void draw(PApplet p, float dt) {
		if (running) {
			gol.updateGOL(); 
			System.out.println(generation++); // number of generations
		}
		gol.display(p);
		// p.delay(1000); // waits 1 second before the next generation
		
	}

	@Override
	public void mousePressed(PApplet p) {
		
		Cell cell = gol.pixel2Cell(p.mouseX, p.mouseY);
		if (cell.getState() == 0) cell.setState(nStates-1); // add cell
		else cell.setState(0); // erase cell 
		
		
		// debug Moore neighbors
//		Cell[] neigh = cell.getNeighbors();
//		for (int i = 0; i < neigh.length; i++) {
//			neigh[i].setState(nStates-1);
//		}
		
	}

	@Override
	public void keyPressed(PApplet p) {
		if (p.key == ' ') {
			running = !running;
		} else if (p.key == 'c' || p.key == 'C') {
			gol = new GameOfLife(p, nrows, ncols, nStates, radiusNeigh);
			generation = 0;
			running = false;
		}
	}

}
