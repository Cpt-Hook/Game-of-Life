package main;

import javafx.scene.canvas.GraphicsContext;

public class Grid {
	private Game game;
	private int columns;
	
	public Grid(Game game, int columns) {
		this.game=game;
		this.columns=columns;
	}
	
	public void render(GraphicsContext g, boolean[][] generation) {
		g.clearRect(0, 0, game.getCanvas().getWidth(), game.getCanvas().getHeight());
		
		for (int i = 0; i < generation.length; i++) {
			for (int j = 0; j < generation.length; j++) {
				if(generation[i][j]==true)
					g.fillRect(toXCoord(j), toYCoord(i), tileWidth(), tileHeight());
			}
		}
	}
	
	//coords
	public double toXCoord(int x) {
		return tileWidth()*x;
	}
	
	public double toYCoord(int y) {
		return tileHeight()*y; 
	}
	
	public double tileWidth() {
		return game.getCanvas().getWidth()/columns;
	}
	
	public double tileHeight() {
		return game.getCanvas().getHeight()/columns;
	}
}