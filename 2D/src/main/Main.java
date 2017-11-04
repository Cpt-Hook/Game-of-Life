package main;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//TODO set number of columns
// nice rules: 90, 165, 111 60,

public class Main extends Application {
	
	private Stage mainStage, handlerStage;
	private Scene mainScene, handlerScene;
	private Canvas canvas;
	private Game game;
	private TextField ruleField;
	private TextField columnsField;
	private ToggleButton scrollBtn;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		mainStage = primaryStage;
		
		mainStage.setTitle("Game of Life");
		mainStage.setScene(mainScene);
		mainStage.setResizable(true);
		mainStage.setWidth(500);
		mainStage.setHeight(500);
		mainStage.centerOnScreen();
		mainStage.setOnCloseRequest( e-> handlerStage.close() );
		mainStage.show();
		handlerStage = new Stage();
		handlerStage.setTitle("Game of Life controls");
		handlerStage.setResizable(false);
		handlerStage.centerOnScreen();
		handlerStage.setScene(handlerScene);
		handlerStage.setOnCloseRequest( e-> mainStage.close() );
		handlerStage.show();
		handlerStage.setX(mainStage.getX()-handlerStage.getWidth());
		
		canvas.widthProperty().bind(mainScene.widthProperty());
		canvas.heightProperty().bind(mainScene.heightProperty());
		canvas.widthProperty().addListener( (e, o, n) -> game.render() );
		canvas.heightProperty().addListener( (e, o, n) -> game.render() );
		
		game = new Game(this, 150);
	}
	
	@Override
	public void init() {
		mainStageinit();
		handlerStageinit();
	}
	

	private void mainStageinit() {
		canvas = new Canvas(500, 500);
		Group group = new Group(canvas);
		mainScene = new Scene(group);
	}
	
	private void handlerStageinit() {
		//control buttons
		Button clearBtn = new Button("Clear");
		clearBtn.setOnAction( e-> game.clearGrid() );
		
		Button pointBtn = new Button("Point first layer");
		pointBtn.setOnAction( e-> game.firstPointGenInit() );
		
		Button randomBtn = new Button("Random first layer");
		randomBtn.setOnAction( e-> game.firstRandomGenInit() );
		
		HBox controlButtons = new HBox(10);
		controlButtons.setAlignment(Pos.CENTER);
		controlButtons.getChildren().addAll(pointBtn, randomBtn, clearBtn);
		
		//rules
		Label ruleLabel = new Label("Rule:");
		
		ruleField = new TextField();
		ruleField.setPromptText("from 0 to 255");
		
		Button saveRulesBtn = new Button("Set rules");
		saveRulesBtn.setOnAction( e-> game.setRule(getRules()) );
		
		Button randomRulesBtn = new Button("Randomise rules");
		randomRulesBtn.setOnAction( e->{
			game.randomiseRuleSet();
			ruleField.setText(game.getRuleDecimalString());
		});
		
		HBox ruleBox = new HBox(3);
		ruleBox.setAlignment(Pos.CENTER);
		ruleBox.getChildren().addAll(ruleLabel, ruleField, saveRulesBtn, randomRulesBtn);
		
		//renderButtons
		Button renderAllBtn = new Button("Render everything");
		renderAllBtn.setOnAction( e-> game.allGenerations() );
		
		scrollBtn = new ToggleButton("Scroll");
		scrollBtn.selectedProperty().addListener( (obser, oldVal, newVal) -> {
			if(newVal) {
				game.startScroll();
			}else {
				game.stopScroll();
			}
		});
		
		HBox renderButtons = new HBox(10);
		renderButtons.setAlignment(Pos.CENTER);
		renderButtons.getChildren().addAll(renderAllBtn, scrollBtn);
		
		//column settings
		Label columnLabel = new Label("Number of columns: ");
		
		columnsField = new TextField("150");
		
		Button savecolumnsBtn = new Button("Save");
		savecolumnsBtn.setOnAction( e-> newGame(columnsField.getText()) );
		
		HBox columnsBox = new HBox(10);
		columnsBox.setAlignment(Pos.CENTER);
		columnsBox.getChildren().addAll(columnLabel, columnsField, savecolumnsBtn);
		
		//root
		VBox root = new VBox(15);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(7));
		root.getChildren().addAll(controlButtons, ruleBox, renderButtons, columnsBox);
		
		handlerScene = new Scene(root);
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * @return the canvas
	 */
	public Canvas getCanvas() {
		return canvas;
	}
	
	private int getRules() {
		if(ruleField.getText().equals("")) {
			ruleField.setText(game.getRuleDecimalString());
		}
		try {
			int num = Integer.parseInt(ruleField.getText());
			if(num<0 || num>255) {
				ruleField.setText("0");
				num = 0;
			}
			return num;
			
			}catch(NumberFormatException exc) {
			ruleField.setText("0");
			return 0;
		}
	}
	
	private void newGame(String text) {
		try {
			game.stopScroll();
			scrollBtn.selectedProperty().set(false);
			game = new Game(this, Integer.parseInt(text));
			
		}catch(NumberFormatException e) {
			columnsField.setText("");
			return;
		}
	}
	
//	private int getColumns() {
//		
//	}
}
