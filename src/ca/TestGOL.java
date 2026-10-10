package ca;

import processing.core.PApplet;
import setup.IProcessingApp;

public class TestGOL implements IProcessingApp {
	
	private int ncols       = 40;
	private int nrows       = 30;
	private int nStates     = 2;
	private int neighborRadius = 1;
	private int generation  = 0;
	private boolean running = false; // Programa começa parado
	private GameOfLife gol;
	private float tempoAcumulado = 0; // tempo acumulado desde a última atualização da simulação
	private float intervaloGeracao = 0.2f; // intervalo de tempo entre gerações (em segundos)
	
	@Override
	public void setup(PApplet p) { // Inicializa o Game of Life com as dimensões e parâmetros especificados	
		gol = new GameOfLife(p, nrows, ncols, nStates, neighborRadius);
		gol.initRandom();
	}
	
	@Override
	public void draw(PApplet p, float dt) {
		if (running) {
			tempoAcumulado += dt; // acumula o tempo desde a última atualização
			if (tempoAcumulado >= intervaloGeracao) {
				gol.UpdateGame(); 
				System.out.println(generation++); // incrementa a geração e imprime no consola
				tempoAcumulado -= intervaloGeracao; // reseta o tempo acumulado
			}
		}
		gol.display(p); // desenha o estado atual do Game of Life na tela
	}

	@Override
	public void mousePressed(PApplet p) {
		
		Cell cell = gol.pixel2Cell(p.mouseX, p.mouseY);
		gol.toggleCell(cell); // CARREGAR NO RATO PARA ALTERAR O ESTADO DE UMA CÉLULA
		
	}

	@Override
	public void keyPressed(PApplet p) {
		if (p.key == ' ') { // CARREGAR NO ESPAÇO PARA INICIAR/PAUSAR A SIMULAÇÃO
			running = !running;
		} else if (p.key == 'c' || p.key == 'C') { // CARREGAR NO C PARA REINICIAR O JOGO
			gol = new GameOfLife(p, nrows, ncols, nStates, neighborRadius);
			generation = 0;
			running = false;
		}
	}

}
