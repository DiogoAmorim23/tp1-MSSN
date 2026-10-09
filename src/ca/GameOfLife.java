package ca;

import processing.core.PApplet;

public class GameOfLife extends CellularAutomata {

	private int[][] aliveAge;

	public GameOfLife(PApplet p, int nRows, int nCols, int nStates, int radiusNeigh) {
		super(p, nRows, nCols, nStates, radiusNeigh);
		aliveAge = new int[nRows][nCols];
	}
	
	/**
	 * Sets a random color for the agents and a black background
	 */
	public void setStateColors(PApplet p) {
		colors[0] = p.color(0); // black background
		colors[1] = p.color(0, 80, 255); // starting color for young cells
	}

	@Override
	public void initRandom() {
		super.initRandom();
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				aliveAge[i][j] = cells[i][j].getState() == 1 ? 1 : 0;
			}
		}
	}

	/** Toggle a cell and reset its age when its state is changed by the user. */
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
	
	/**
	 * Method called in each frame to update game's grid
	 */
	public void updateGOL() {
		
		Cell c          = null;
		int[][] nextGen = new int[nRows][nCols]; // next GOL generation
		int[][] nextAge = new int[nRows][nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				c              = cells[i][j];
				nextGen[i][j]  = c.getState();
				int neighAlive = countNeighAlive(c);
				if (c.getState() == 1) {
					if (neighAlive < 1) nextGen[i][j] = 0; // cell dies of loneliness
					if (neighAlive > 5)	nextGen[i][j] = 0; // cell dies of overcrowding
				} else {
					
					if (neighAlive == 3) nextGen[i][j] = 1; // cell comes to life
				}
				if (nextGen[i][j] == 1) {
					nextAge[i][j] = c.getState() == 1 ? aliveAge[i][j] + 1 : 1;
				}
			}
		}
		// cells matrix = nextGen matrix
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				cells[i][j].setState(nextGen[i][j]);
			}
		}
		aliveAge = nextAge;
				
	}

	@Override
	public void display(PApplet p) {
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
	
	/**
	 * Count number of neighbors alive
	 * @param cell instance of class Cell
	 */
	public int countNeighAlive(Cell cell) {
		
		int count = 0; 
		int x     = cell.getCol();
		int y     = cell.getRow();
		
		// count neighbors with radiusNeigh = 1
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
