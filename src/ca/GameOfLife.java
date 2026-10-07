package ca;

import processing.core.PApplet;

public class GameOfLife extends CellularAutomata {

	public GameOfLife(PApplet p, int nRows, int nCols, int nStates, int radiusNeigh) {
		super(p, nRows, nCols, nStates, radiusNeigh);
	}
	
	/**
	 * Sets a random color for the agents and a black background
	 */
	public void setStateColors(PApplet p) {
		colors[0] = p.color(0); // black background 
		colors[1] = p.color(p.random(255), p.random(255), p.random(255));
//		colors[1] = p.color(255, 0, 0);	
	}
	
	/**
	 * Method called in each frame to update game's grid
	 */
	public void updateGOL() {
		
		Cell c          = null;
		int[][] nextGen = new int[nRows][nCols]; // next GOL generation 
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				c              = cells[i][j];
				nextGen[i][j]  = c.getState();
				int neighAlive = countNeighAlive(c);
				if (c.getState() == 1) {
					if (neighAlive < 2) nextGen[i][j] = 0; // cell dies of loneliness
					if (neighAlive > 3)	nextGen[i][j] = 0; // cell dies of overcrowding
				} else {
					if (neighAlive == 3) nextGen[i][j] = 1; // cell comes to life
				}
			}
		}
		// cells matrix = nextGen matrix
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				cells[i][j].setState(nextGen[i][j]);
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
