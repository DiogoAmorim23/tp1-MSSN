package dla;

import java.util.ArrayList;
import java.util.List;

import dla.Walker.State;
import processing.core.PApplet;
import processing.core.PVector;
import setup.IProcessingApp;

public class DLA implements IProcessingApp {

	private List<Walker> walkers; // Lista de todas as partículas (caminhantes) do DLA
	private int NUM_WALKERS         = 100; // Número de partículas que caminham aleatoriamente
	private int NUM_STEPS_PER_FRAME = 100; // Número de passos que cada partícula dá a cada frame (quanto maior, mais rápido o DLA cresce)
	private int red = 0; // red component iterator
	private int green = 0; // green component iterator
	private int blue = 0; // blue component iterator
	
	@Override 
	public void setup(PApplet p) { // Inicializa a simulação DLA
		walkers  = new ArrayList<Walker>(); // Inicializar a lista de partículas (construtor)
		// A seed particle stays in the middle; all other particles wander.
		Walker w1 = new Walker(p, new PVector(p.width/2, p.height/2)); // Partícula inicial (semente) que fica no centro da tela
		w1.setColor(p.color(255, 0, 255)); // Cor da partícula inicial (semente) do DLA
		walkers.add(w1); // Adiciona a partícula inicial (semente) à lista de partículas do DLA
		for (int i = 0; i < NUM_WALKERS; i++) {
			walkers.add(new Walker(p, 7)); // Adiciona uma partícula que caminha aleatoriamente à lista de partículas do DLA
		}
	}

	@Override
	public void draw(PApplet p, float dt) {
		p.background(0); // Limpa a tela a cada frame (preto)
		
		for (int i = 0; i < NUM_STEPS_PER_FRAME; i++) // Cada partícula dá NUM_STEPS_PER_FRAME passos a cada frame
			for (Walker w : walkers) 
				if (w.getState() == State.WANDER) { // Se a partícula está no estado de "wander" (caminhar aleatoriamente)
					w.wander(p); // A partícula dá um passo aleatório
					w.updateState(p, walkers); // Atualiza o estado da partícula (se ela colidiu com outra partícula, ela para de caminhar)
					if (w.getState() == State.STOPPED) colorPattern(p, w); // Se a partícula parou de caminhar, ela recebe uma cor
				}

		// Replace stopped particles so the number of moving particles stays fixed.
		int moving = 0; // Contador de partículas que estão no estado de "wander" (caminhar aleatoriamente)
		for (Walker w : walkers) {
			if (w.getState() == State.WANDER) moving++; // Incrementa o contador de partículas que estão no estado de "wander" (caminhar aleatoriamente)
		}
		while (moving < NUM_WALKERS) { 
			walkers.add(new Walker(p, 7)); // Adiciona uma nova partícula
			moving++;
		}

		for (Walker w : walkers) w.display(p); // Desenha todas as partículas na tela
		
//		System.out.println("Stopped = " + Walker.num_stopped +
//						   " Wander = " + Walker.num_wanders + 
//						   " Radius = " + w.getRadius());
	}
	
	/**
	 * Assigns a color pattern to the DLA
	 */
	private void colorPattern(PApplet p, Walker w) {
		
		if (red < 255 && green == 0 && blue == 0) { // Red to 0, Green - 255, Blue - 255
			w.setColor(p.color(255-red, 255, 255)); 
			red++;
		} else if (red == 255 && green < 255 && blue == 0) { // Red - 0, Green to 0, Blue - 255
			w.setColor(p.color(0, 255-green, 255));
			green++;
		} else if (red > 0 && green == 255 && blue < 255) {  // Red to 255, Green - 0, Blue to 0
			if (red == 255) red = 0;
			w.setColor(p.color(0+red, 0, 255-blue));
			blue++;
			red++;
		} else if (red == 255 && green > 0 && blue == 255) { // Red - 255, Green to 255, Blue - 0
			if (green == 255) green = 0;
			w.setColor(p.color(255, 0+green, 0));
			green++;
			if (green == 255) green = -1;
		} else if (red == 255 && green == -1 && blue > 0) {  // Red - 255, Green - 255, Blue to 255
			if (blue == 255) blue = 0;
			w.setColor(p.color(255, 255, 0+blue));
			blue++;
			if (blue == 255) blue = -1;
		}
		
	}

	@Override
	public void mousePressed(PApplet p) {
		// No mouse interaction in the basic DLA simulation.
	}

	@Override
	public void keyPressed(PApplet p) {
		// No keyboard interaction in the basic DLA simulation.
	}

}
