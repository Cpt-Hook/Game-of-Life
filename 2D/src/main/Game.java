package main;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Game {
	
	@SuppressWarnings("unused")
	private Main main;
	private Grid grid;
	private boolean[] ruleSet;
	private boolean[][] rows;
	private Canvas canvas;
	private GraphicsContext g;
	public Random rnd;
	private int genCount = 0, columns, ruleNum;
	private AnimationTimer scroll;
	
	public Game(Main main, int columns) {
		this.main=main;
		this.columns=columns;
		grid = new Grid(this, columns);
		canvas = main.getCanvas();
		g = canvas.getGraphicsContext2D();
		rnd = new Random();
		rows= new boolean[columns][columns];
		
		scroll = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				nextGeneration();
			}
		};
		
		randomiseRuleSet();
		firstPointGenInit();
		System.out.println(ruleNum);
	}
	
	public void setRule(int ruleNum) {
		String binary = Integer.toBinaryString(ruleNum);
		char[] array = binary.toCharArray();
		boolean[] rules = new boolean[8];
		
		for(int i = 0; i < 8-array.length;i++) {
			rules[i] = false;
		}
		for(int i = 8-array.length; i < 8;i++) {
			rules[i] = (array[i-(8-array.length)]=='1')?true:false;
		}
		
		ruleSet=rules;
		this.ruleNum=ruleNum;
	}

	public void randomiseRuleSet() {
		int ruleNum=0;
		ruleSet = new boolean[8];
		for (int i = 0; i < ruleSet.length; i++) {
			boolean temp = rnd.nextBoolean();
			ruleSet[i] = temp;
			if(temp) {
				ruleNum += Math.pow(2, Math.abs(7-i));
			}
		}
		this.ruleNum=ruleNum;
	}

	public void allGenerations() {
		for (int i = 0; i < columns; i++) {
			newGeneration();
		}
		render();
	}
	
	public void nextGeneration() {
		newGeneration();
		render();
	}
	
	public void clearGrid() {
		rows= new boolean[columns][columns];
		genCount=1;
		render();
	}
	
	public void firstRandomGenInit() {
		clearGrid();
		rows[0] = new boolean[columns];
		for (int i = 0; i < columns; i++) {
			rows[0][i] = rnd.nextBoolean();
		}
		render();
	}
	
	public void firstPointGenInit() {
		clearGrid();
		rows[0] = new boolean[columns];
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < columns; j++) {
				rows[i][j]=false;
			}
		}
		rows[0][columns/2]=true;
		render();
	}
	
	public String getRuleDecimalString() {
		
		 return Integer.toString(ruleNum);
	}
	
	public void render() {
		grid.render(g, rows);	
	}
	
	private void newGeneration() {
		if(genCount<columns) {
			rows[genCount] = new boolean[columns];
		}else {
			moveArray(rows);
			rows[columns-1] = new boolean[columns];
		}
		
		for (int i = 0; i < columns; i++) {
			if(genCount<columns) {
				rows[genCount][i] = rules(
						(i-1 == -1)? false : rows[genCount-1][i-1],
						rows[genCount-1][i],
						(i+1==columns)?false : rows[genCount-1][i+1]);
			}else {
				rows[columns-1][i] = rules(
						(i-1 == -1)? false : rows[columns-2][i-1],
						rows[columns-2][i],
						(i+1 == columns)? false : rows[columns-2][i+1]);
			}
		}
		
		genCount++;
	}
	
	
	private boolean rules(boolean prev, boolean me, boolean next) {
		
		if(prev == true && me == true && next == true)	 		return ruleSet[0];
		else if(prev == true && me == true && next == false)	return ruleSet[1];
		else if(prev == true && me == false && next == true) 	return ruleSet[2];
		else if(prev == true && me == false && next == false) 	return ruleSet[3];
		else if(prev == false && me == true && next == true)	return ruleSet[4];
		else if(prev == false && me == true && next == false) 	return ruleSet[5];
		else if(prev == false && me == false && next == true) 	return ruleSet[6];
		else if(prev == false && me == false && next == false) 	return ruleSet[7];
		else {
			System.out.println("error");
			return false;
		}
	}
	
	private static void moveArray(boolean[][] array) {
		for (int i = 1; i < array.length; i++) {
				array[i-1]=array[i];
			}
		}
	
	public void startScroll() {
		scroll.start();
	}
	
	public void stopScroll() {
		scroll.stop();
	}
	
	/**
	 * @return the canvas
	 */
	public Canvas getCanvas() {
		return canvas;
	}
	
	public Grid getGrid() {
		return grid;
	}
}
