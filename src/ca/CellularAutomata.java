package ca;

import processing.core.PApplet;

public class CellularAutomata {

	protected int nRows;
	protected int nCols;
	private int nStates;
	private int radiusNeigh;
	protected Cell[][] cells;
	protected int[] colors;
	private int cellWidth, cellHeight;
	
	public CellularAutomata(PApplet p, int nRows, int nCols, int nStates, int radiusNeigh) {
		this.nRows       = nRows;
		this.nCols       = nCols;
		this.nStates     = nStates;
		this.radiusNeigh = radiusNeigh;
		cells            = new Cell[nRows][nCols];
		colors           = new int[nStates];
		cellWidth        = p.width/nCols;
		cellHeight       = p.height/nRows;
		createCells();
		setStateColors(p);
	}
	
	public int getCellWidth() {
		return this.cellWidth;
	}
	
	public int getCellHeight() {
		return this.cellHeight;
	}
	
	public void setStateColors(PApplet p) {
		for (int i = 0; i < nStates; i++) {
			colors[i] = p.color(p.random(255), p.random(255), p.random(255));
		}
	}
	
	public int[] getStateColors() {
		return colors;
	}

	private void createCells() {
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				cells[i][j] = new Cell(this, i, j);
			}
		}
		setMooreNeighbors();
	}
	
	/**
	 * Initialize cells in a random state
	 */
	public void initRandom() {
		int rndState = 0;
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				// cells[i][j].setState((int)((nStates-1) * Math.random()));
				rndState = (int)((nStates)*Math.random());
				cells[i][j].setState(rndState);
			}
		}
	}
	
	public Cell pixel2Cell(int x, int y) {
		int row = y / cellHeight;
		int col = x / cellWidth;
		if (row >= nRows) row = nRows - 1;
		if (col >= nCols) col = nCols - 1;
		return cells[row][col];
	}
	
	/**
	 * Moore Neighbors
	 * Number of neighbors NN = (2 * r + 1) ^ 2
	 */
	private void setMooreNeighbors() {
		int NN = (int) Math.pow(2*radiusNeigh+1, 2); // nº de vizinhos
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				Cell[] neigh = new Cell[NN];
				int n = 0;
				for (int ii = -radiusNeigh; ii <= radiusNeigh; ii++) {
					int row = (i + ii + nRows) % nRows;
					for (int jj = -radiusNeigh; jj <= radiusNeigh; jj++) {
						int col = (j + jj + nCols) % nCols;
						neigh[n++] = cells[row][col];
					}
				}
				cells[i][j].setNeighbors(neigh);
			}
		}
	}
	
	public void display(PApplet p) {
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				cells[i][j].display(p);
			}
		}
	}
	
}