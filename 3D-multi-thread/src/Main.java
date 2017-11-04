import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.Random;

//TODO changable ruleset
//TODO optimize

public class Main extends Application {

    public static Random rnd;
    private static Canvas canvas;
    private static Scene scene;

    static {
        rnd = new Random();
    }

    private Map map;

    public static void main(String[] args) {
        launch(args);
    }

    public static double getWidth() {
        return canvas.getWidth();
    }

    public static double getHeight() {
        return canvas.getHeight();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.show();

        map = new Map(canvas.getGraphicsContext2D(), 500, 500, 1);

        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        canvas.widthProperty().addListener((obs, o, n) -> {
            map.getGrid().calcTileWidth();
            map.render();
        });
        canvas.heightProperty().addListener((obs, o, n) -> {
            map.getGrid().calcTileHeight();
            map.render();
        });
        canvas.setOnMouseClicked(map::clickEvent1Cell);
        canvas.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                map.nextGen();
            else if (e.getCode() == KeyCode.R) {
                map.initRandomGrid();
                map.render();
            } else if (e.getCode() == KeyCode.C) {
                map.clearGrid();
                map.render();
            } else if (e.getCode() == KeyCode.SPACE) {
                map.startStop();
            }
            e.consume();
        });
        canvas.requestFocus();

        map.render();

    }

    @Override
    public void init() {
        canvas = new Canvas(500, 500);
        Group root = new Group(canvas);
        scene = new Scene(root, 500, 500);

    }

    public void newMap(int width, int height) {

    }
}
