package Main;

import javafx.scene.canvas.GraphicsContext;

public class Grid {
	private int width, height;
	private double tileWidth, tileHeight;
	
	Grid(int width, int height) {
		this.width=width;
		this.height=height;
		
		calcTileHeight();
		calcTileWidth();
	}
	
	void render(GraphicsContext g, boolean[][] generation) {
		g.clearRect(0, 0, Main.getWidth(), Main.getHeight());

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if(generation[i][j])
					g.fillRect(Math.ceil(toXCoord(i)),
							Math.ceil(toYCoord(j)),
							Math.ceil(tileWidth()),
							Math.ceil(tileHeight()));
			}
		}
	}
	
	//coords
    private double toXCoord(int x) {
		return tileWidth()*x;
	}
	
	private double toYCoord(int y) {
		return (tileHeight()*y); 
	}
	
	int toXPos(double x) {
		return (int)(x/tileWidth());
	}
	
	int toYPos(double y) {
		return (int) (y/tileHeight());
	}
	
	private double tileWidth() {
		return tileWidth;
	}
	
	private double tileHeight() {
		return tileHeight;
	}
	
	int gridXCoord(int x) {
		if(x == -1)
			return width-1;
		else if(x == width)
			return 0;
		else
			return x;
	}
	
	int gridYCoord(int y) {
		if(y == -1)
			return height-1;
		else if(y == height)
			return 0;
		else
			return y;
	}
	
	void calcTileWidth() {
		tileWidth = Main.getWidth()/width;
	}
	
	void calcTileHeight() {
		tileHeight = Main.getHeight()/height;
	}
}