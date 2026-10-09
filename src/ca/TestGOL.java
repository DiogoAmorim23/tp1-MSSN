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
	private float tempoAcumulado = 0; // tempo acumulado desde a última atualização da simulação
	private float intervaloGeracao = 0.2f; // intervalo de tempo entre gerações (em segundos)
	
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
			tempoAcumulado += dt; // acumula o tempo desde a última atualização
			if (tempoAcumulado >= intervaloGeracao) {
				gol.updateGOL(); 
				System.out.println(generation++); // number of generations
				tempoAcumulado -= intervaloGeracao; // reseta o tempo acumulado
			}
		}
		gol.display(p);
	}

	@Override
	public void mousePressed(PApplet p) {
		
		Cell cell = gol.pixel2Cell(p.mouseX, p.mouseY);
		gol.toggleCell(cell);
		
		
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
