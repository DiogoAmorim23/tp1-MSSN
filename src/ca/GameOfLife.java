package ca;

import processing.core.PApplet;

public class GameOfLife extends CellularAutomata {

	private int[][] aliveAge;

	// Construtor da classe GameOfLife
	public GameOfLife(PApplet p, int nRows, int nCols, int nStates, int neighborRadius) {
		super(p, nRows, nCols, nStates, neighborRadius);
		aliveAge = new int[nRows][nCols];
	}
	
	// Define as cores para o estado das células vivas e mortas
	public void setStateColors(PApplet p) {
		colors[0] = p.color(0); // celula morta: preta
		colors[1] = p.color(0, 80, 255); // celula viva: azul
	}

	@Override
	public void initRandom() { // Inicializa a matriz aliveAge com base no estado inicial das células
		super.initRandom(); // Chama o método initRandom() da classe pai para inicializar as células aleatoriamente
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				aliveAge[i][j] = cells[i][j].getState() == 1 ? 1 : 0; // Se a célula estiver viva, define a idade como 1, caso contrário, define como 0
			}
		}
	}

	// Trocar o estado de uma célula e redefinir a sua idade quando o seu estado é alterado pelo usuário
	public void toggleCell(Cell cell) {
		int row = cell.getRow();
		int col = cell.getCol();
		if (cell.getState() == 0) {
			cell.setState(1);
			aliveAge[row][col] = 1;
		} else {
			cell.setState(0);
			aliveAge[row][col] = 0;
		}
	}
	
	// Metodo chamado a cada frame que atualiza o estado das células com base nas regras do Jogo da Vida
	public void UpdateGame() {
		
		Cell c = null;
		int[][] nextGen = new int[nRows][nCols]; // proxima geração de células
		int[][] nextAge = new int[nRows][nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				c              = cells[i][j];
				nextGen[i][j]  = c.getState();
				int NeighborsAlive = countNeighborsAlive(c);
				if (c.getState() == 1) {
					/*
					 * VARIANTE IMPLEMENTADA (sobrevive com 1 a 5 vizinhos; nasce com 3).
					 * Para usar a versão clássica, comentar estas duas condições e descomentar as duas condições identificadas como CLASSIC 23/3 abaixo.
					 */
					// if (NeighborsAlive < 1) nextGen[i][j] = 0; // nossa variante: morre com 0 vizinhos
					// if (NeighborsAlive > 5) nextGen[i][j] = 0; // nossa variante: morre com mais de 5 vizinhos

					/*
					 * CLASSIC 23/3: a célula viva sobrevive apenas com 2 ou 3 vizinhos. Para desativar, 
					 * comentar estas duas linhas e descomentar as duas condições acima.
					 */
					if (NeighborsAlive < 2) nextGen[i][j] = 0; // morre com 0 ou 1 vizinho
					if (NeighborsAlive > 3) nextGen[i][j] = 0; // morre com 4 ou mais vizinhos
				} else {
					// Regra de nascimento comum às duas variantes: nasce com exatamente 3 vizinhos.
					if (NeighborsAlive == 3) nextGen[i][j] = 1;
				}
				if (nextGen[i][j] == 1) {
					nextAge[i][j] = c.getState() == 1 ? aliveAge[i][j] + 1 : 1; // Incrementa a idade da célula viva ou define como 1 se a célula acabou de nascer
				}
			}
		}
		// Atualiza o estado das células e a idade das células vivas com base na próxima geração calculada
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				cells[i][j].setState(nextGen[i][j]);
			}
		}
		aliveAge = nextAge;
				
	}

	@Override
	public void display(PApplet p) { // Desenha as células na tela com base no seu estado e idade
		int[] ageColors = {
			p.color(0, 80, 255),   // young: blue
			p.color(0, 230, 255),  // cyan
			p.color(30, 220, 70),  // green
			p.color(255, 220, 0),  // yellow
			p.color(255, 35, 0)    // old: red
		};
		p.stroke(0);
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				int state = cells[i][j].getState();
				if (state == 0) {
					p.fill(colors[0]);
				} else {
					float ageProgress = Math.min((Math.max(1, aliveAge[i][j]) - 1) / 20f, 1f);
					float palettePosition = ageProgress * (ageColors.length - 1);
					int segment = Math.min((int) palettePosition, ageColors.length - 2);
					float segmentProgress = palettePosition - segment;
					p.fill(p.lerpColor(ageColors[segment], ageColors[segment + 1], segmentProgress));
				}
				p.rect(j * getCellWidth(), i * getCellHeight(), getCellWidth(), getCellHeight());
			}
		}
	}
	
	// Conta o número de vizinhos vivos de uma célula usando a vizinhança de Moore
	public int countNeighborsAlive(Cell cell) {
		
		int count = 0; 
		int x = cell.getCol();
		int y = cell.getRow();
		
		// contar os vizinhos com neighborRadius = 1
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				
				// Ignora a própria célula, incluindo quando o índice dá a volta à grelha.
				if (i == 0 && j == 0) continue;
				int c = (x + j + nCols) % nCols;
				int l = (y + i + nRows) % nRows;
				count += cells[l][c].getState();
			}
		}
		return count;
		
	}

}
