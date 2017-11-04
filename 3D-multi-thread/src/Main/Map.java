package Main;

import Threads.CalcRunnable;
import Threads.RenderRunnable;
import animation.Animatable;
import animation.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class Map implements Animatable {

    private final Runnable comRunnable;
    private final Runnable renRunnable;
    private GraphicsContext g;
    private int width, height;
    private boolean[][] cells, nextGen;
    private int[] surviveRules, bornRules;
    private Grid grid;
    private Animation<Map> anim;
    public int doneCheck;

    Map(GraphicsContext g, int width, int height, int slowness) {
        this.width = width;
        this.height = height;
        this.g = g;

        anim = new Animation<>(this, slowness);
        grid = new Grid(width, height);

        cells = new boolean[width][height];
        nextGen = new boolean[width][height];

        int threadCount = 2;
        comRunnable = new CalcRunnable(this, threadCount);
        renRunnable = new RenderRunnable(this);
        Thread comThread = new Thread(comRunnable);
        Thread renThread = new Thread(renRunnable);

        comThread.start();
        renThread.start();
        doneCheck = 0;

        initRules();
        initRandomGrid();
    }

    @Override
    public void handle() {
        nextGen = new boolean[width][height];
        synchronized (comRunnable) {
            comRunnable.notifyAll();
        }
        synchronized (renRunnable) {
            renRunnable.notifyAll();
        }
        synchronized (this) {
            while (doneCheck != 2) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
        doneCheck = 0;
        cells = nextGen;
    }

    void clickEvent6Cells(MouseEvent e) {
        int x = grid.toXPos(e.getX());
        int y = grid.toYPos(e.getY());

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                cells[grid.gridXCoord(x + i)][grid.gridYCoord(y + j)] = true;
            }
        }
        render();
    }

    public void clickEvent1Cell(MouseEvent e) {
        int x = grid.toXPos(e.getX());
        int y = grid.toYPos(e.getY());

        cells[grid.gridXCoord(x)][grid.gridYCoord(y)] = true;

        render();
    }


    public void render() {
        grid.render(g, cells);
    }

    public void computeNextGen(int offset, int threadNum) {
        for (int i = offset; i < width; i += threadNum) {
            for (int j = 0; j < height; j++) {
                rules(i, j);
            }
        }
    }

    public void computeNextGen() {
        nextGen = new boolean[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rules(i, j);
            }
        }
    }

    void startStop() {
        if (anim.getRunning())
            anim.stop();
        else
            anim.start();
    }


    void initRandomGrid() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = Main.rnd.nextBoolean();
            }
        }
    }

    void clearGrid() {
        cells = new boolean[width][height];
    }

    public void setRules(int[] aliveRules, int[] deadRules) {
        this.surviveRules = aliveRules;
        this.bornRules = deadRules;
    }

    private int rules(int x, int y) {
        int neighbors = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    if (cells[grid.gridXCoord(x + i)][grid.gridYCoord(y + j)])
                        neighbors++;
                }
            }
        }

        if (cells[x][y] && surviveRules(neighbors)) {
            nextGen[x][y] = true;

        } else if (!cells[x][y] && bornRules(neighbors))
            nextGen[x][y] = true;
        else
            nextGen[x][y] = false;
        return neighbors;
    }

    private boolean surviveRules(int neighbors) {
        for (int surviveRule : surviveRules) {
            if (surviveRule == neighbors)
                return true;
        }
        return false;
    }

    private boolean bornRules(int neighbors) {
        for (int bornRule : bornRules) {
            if (bornRule == neighbors)
                return true;
        }
        return false;
    }

    private void initRules() {

        surviveRules = new int[]{2, 3}; //game of life
        bornRules = new int[]{3};

//		surviveRules = new int[] {}; //seeds
//		bornRules = new int[] {2};

//        surviveRules = new int[]{1, 2}; //triangle fractal
//        bornRules = new int[]{1};

//		surviveRules = new int[] {2,3}; //high life
//		bornRules = new int[] {3,6};
    }

    /**
     * @return the grid
     */
    Grid getGrid() {
        return grid;
    }

}
