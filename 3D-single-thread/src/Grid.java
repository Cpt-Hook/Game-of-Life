import javafx.scene.canvas.GraphicsContext;

public class Grid {
	private int width, height;
	private double tileWidth, tileHeight;
	
	public Grid(int width, int height) {
		this.width=width;
		this.height=height;
		
		calcTileHeight();
		calcTileWidth();
	}
	
	public void render(GraphicsContext g, boolean[][] generation) {
		g.clearRect(0, 0, Main.getWidth(), Main.getHeight());
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if(generation[i][j]==true)
					g.fillRect(Math.ceil(toXCoord(i)), 
							   Math.ceil(toYCoord(j)),
							   Math.ceil(tileWidth()),
							   Math.ceil(tileHeight()));
			}
		}
	}
	
	//coords
	public double toXCoord(int x) {
		return tileWidth()*x;
	}
	
	public double toYCoord(int y) {
		return (tileHeight()*y); 
	}
	
	public int toXPos(double x) {
		return (int)(x/tileWidth());
	}
	
	public int toYPos(double y) {
		return (int) (y/tileHeight());
	}
	
	public double tileWidth() {
		return tileWidth;
	}
	
	public double tileHeight() {
		return tileHeight;
	}
	
	public int gridXCoord(int x) {
		if(x == -1)
			return width-1;
		else if(x == width)
			return 0;
		else
			return x;
	}
	
	public int gridYCoord(int y) {
		if(y == -1)
			return height-1;
		else if(y == height)
			return 0;
		else
			return y;
	}
	
	public void calcTileWidth() {
		tileWidth = Main.getWidth()/width;
	}
	
	public void calcTileHeight() {
		tileHeight = Main.getHeight()/height;
	}
}