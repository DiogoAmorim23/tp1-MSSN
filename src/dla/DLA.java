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
	private float nextWalkerRadius = 7f;
	private static final float MIN_WALKER_RADIUS = 1.5f;
	private boolean stopSpawning = false;
	private boolean finished = false;
	private boolean paused = false;
	
	@Override 
	public void setup(PApplet p) { // Inicializa a simulação DLA
		resetSimulation(p);
	}

	private void resetSimulation(PApplet p) {
		walkers  = new ArrayList<Walker>(); // Inicializar a lista de partículas (construtor)
		nextWalkerRadius = 7f;
		stopSpawning = false;
		finished = false;
		paused = false;
		Walker.num_wanders = 0;
		Walker.num_stopped = 0;

		/*
		 * Escolhe UMA forma para a semente inicial do agregado:
		 * deixa ativa apenas uma opção (comenta a atual e descomenta outra).
		 * Os caminhantes móveis continuam a ser lançados numa circunferência,
		 * como já acontecia; estas opções mudam a forma inicial do agregado.
		 */
		// addPointSeed(p);       // Padrão atual: uma partícula no centro.
		// addLineSeed(p);     // Linha horizontal centrada.
		// addCircleSeed(p);   // Circunferência de partículas paradas.
		// addSquareSeed(p);   // Contorno de um quadrado.
		addCustomSeed(p);   // Exemplo de forma arbitrária (um L).

		for (int i = 0; i < NUM_WALKERS; i++) {
			walkers.add(new Walker(p, nextWalkerRadius)); // Adiciona uma partícula que caminha aleatoriamente à lista de partículas do DLA
		}
	}

	private void addSeed(PApplet p, float x, float y) {
		Walker seed = new Walker(p, new PVector(x, y));
		seed.setColor(p.color(255, 0, 255));
		walkers.add(seed);
	}

	private void addPointSeed(PApplet p) {
		addSeed(p, p.width / 2f, p.height / 2f);
	}

	private void addLineSeed(PApplet p) {
		float centerX = p.width / 2f;
		float centerY = p.height / 2f;
		for (float x = centerX - 70; x <= centerX + 70; x += 14) {
			addSeed(p, x, centerY);
		}
	}

	private void addCircleSeed(PApplet p) {
		float centerX = p.width / 2f;
		float centerY = p.height / 2f;
		float radius = 70;
		int particles = 32;
		for (int i = 0; i < particles; i++) {
			double angle = 2 * Math.PI * i / particles;
			float x = centerX + radius * (float) Math.cos(angle);
			float y = centerY + radius * (float) Math.sin(angle);
			addSeed(p, x, y);
		}
	}

	private void addSquareSeed(PApplet p) {
		float centerX = p.width / 2f;
		float centerY = p.height / 2f;
		float halfSide = 70;
		float spacing = 14;
		for (float offset = -halfSide; offset <= halfSide; offset += spacing) {
			addSeed(p, centerX + offset, centerY - halfSide); // lado superior
			addSeed(p, centerX + offset, centerY + halfSide); // lado inferior
			if (offset > -halfSide && offset < halfSide) {
				addSeed(p, centerX - halfSide, centerY + offset); // lado esquerdo
				addSeed(p, centerX + halfSide, centerY + offset); // lado direito
			}
		}
	}

	private void addCustomSeed(PApplet p) {
		/*
		 * Forma arbitrária de exemplo: a letra L.
		 * Acrescenta ou altera coordenadas (x, y) relativas ao centro.
		 * Mantém cerca de 14 píxeis entre pontos para partículas de raio 7.
		 */
		float centerX = p.width / 2f;
		float centerY = p.height / 2f;
		for (float y = -100; y <= 100; y += 14) {
			addSeed(p, centerX - 0, centerY + y);
		}
		for (float x = -100; x <= 100 ; x += 14) {
			addSeed(p, centerX + x, centerY + 0);
		}
	}

	@Override
	public void draw(PApplet p, float dt) {
		p.background(0); // Limpa a tela a cada frame (preto)
		if (!finished && !paused) {
			for (int i = 0; i < NUM_STEPS_PER_FRAME; i++) {
				for (Walker w : walkers) {
					if (w.getState() == State.WANDER) {
						w.wander(p);
						w.updateState(p, walkers);
						if (w.getState() == State.STOPPED) {
							colorPattern(p, w);
							nextWalkerRadius = Math.max(MIN_WALKER_RADIUS, nextWalkerRadius * 0.999f);
							if (nextWalkerRadius <= MIN_WALKER_RADIUS) stopSpawning = true;
						}
					}
				}
			}

			int moving = 0;
			for (Walker w : walkers) {
				if (w.getState() == State.WANDER) moving++;
			}

			if (stopSpawning) {
				// Let all remaining walkers attach before freezing the simulation.
				if (moving == 0) finished = true;
			} else {
				// Replace stopped particles to keep the moving population constant.
				while (moving < NUM_WALKERS) {
					walkers.add(new Walker(p, nextWalkerRadius));
					moving++;
				}
			}
		}

		for (Walker w : walkers) w.display(p); // Desenha todas as partículas na tela
		
//		System.out.println("Stopped = " + Walker.num_stopped +
//						   " Wander = " + Walker.num_wanders + 
//						   " Radius = " + w.getRadius());
	}
	
	/**
	 * Colors stopped particles according to their distance from the center.
	 */
	private void colorPattern(PApplet p, Walker w) {
		float dx = w.getX() - p.width / 2f;
		float dy = w.getY() - p.height / 2f;
		float distance = (float) Math.sqrt(dx * dx + dy * dy);
		float maxDistance = Math.min(p.width, p.height) * 0.45f;
		float amount = Math.max(0f, Math.min(1f, distance / maxDistance));

		int[] colors = {
			p.color(255, 0, 0),     // red
			p.color(255, 128, 0),   // orange
			p.color(255, 255, 0),   // yellow
			p.color(0, 255, 0),     // green
			p.color(0, 0, 255),     // blue
			p.color(255, 255, 255)  // white
		};

		float position = amount * (colors.length - 1);
		int segment = Math.min((int) position, colors.length - 2);
		float segmentAmount = position - segment;
		w.setColor(p.lerpColor(colors[segment], colors[segment + 1], segmentAmount));
	}

	@Override
	public void mousePressed(PApplet p) {
		// No mouse interaction in the basic DLA simulation.
	}

	@Override
	public void keyPressed(PApplet p) {
		if (p.key == ' ') {
			paused = !paused;
		} else if (p.key == 'c' || p.key == 'C') {
			resetSimulation(p);
		}
	}

}
