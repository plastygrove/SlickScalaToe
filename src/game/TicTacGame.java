package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class TicTacGame extends BasicGame {

	public static void main(String[] args) {
		try{
		AppGameContainer app = new AppGameContainer(new TicTacGame());
		app.setDisplayMode(800, 600, false);
		app.setShowFPS(false);
		app.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private TicTac ticTac = null;
	private Image xImage = null;
	private Image oImage = null;
	private Image frame = null;
	private Image[] pieces = null;
	private boolean hasPlayerPlayed = false;
	private int playerMove = -1;
	private boolean isGameOver = false;
	private int winner = -1;
	private boolean restartGame = false;
	private Sound clickSound = null;
	private Sound controlSound = null;
	private Sound loseSound = null;
	private Sound drawSound = null;
	
	private boolean isGameOverTunePlayed = false;

	private int humanPlayer;
	private int compPlayer;

	public TicTacGame() {
		super("Tic Tac game using Scala");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ticTac = new TicTac();
		Image whole = new Image("res/pieces.png");
		xImage = whole.getSubImage(40, 30, 160, 170);
		oImage = whole.getSubImage(240, 40, 150, 150);
		frame = whole.getSubImage(80, 270, 540, 530);
		pieces = new Image[9];
		humanPlayer = 1;
		compPlayer = 2;
		clickSound = new Sound("res/click.wav");
		controlSound = new Sound("res/drop.wav");
		loseSound = new Sound("res/sadwhistle.wav");
		drawSound = new Sound("res/whip.wav");
		playCompIfFirst();
		
	}
	
	private void playCompIfFirst(){
		if(compPlayer  == 1){
			ticTac.setCompPlayer(1);
			int compMove = ticTac.getNextMove(compPlayer);
			ticTac.play(compPlayer, compMove);
			updatePieces();
		} else {
			ticTac.setCompPlayer(2);
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		super.mouseClicked(button, x, y, clickCount);
//		System.out.println("X: "+x+" Y: "+y);
		int colNum = -1;
		int rowNum = -1;

		if (x > 210 && x < 335) {
			colNum = 0;
		} else if (x > 335 && x < 490) {
			colNum = 1;
		} else if (x > 490 && x < 620) {
			colNum = 2;
		}

		if (y > 120 && y < 220) {
			rowNum = 0;
		} else if (y > 220 && y < 365) {
			rowNum = 1;
		} else if (y > 365 && y < 505) {
			rowNum = 2;
		}

		if (rowNum != -1 && colNum != -1 && !isGameOver) {
			clickSound.play();
			playerMove = rowNum * 3 + colNum;
			hasPlayerPlayed = true;
		}
		
		if(x>10 && y>30 && x<154 && y<43)
			keyPressed(Input.KEY_F2, 'a');
		if(x>10 && y>50 && x<250 && y<60)
			keyPressed(Input.KEY_F3, 'a');
		if(x>10 && y>70 && x<230 && y<85)
			keyPressed(Input.KEY_F4, 'a');

	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if (restartGame) {
			ticTac.restart();
			pieces = new Image[9];
			restartGame = false;
			isGameOver = false;
			isGameOverTunePlayed = false;
			playCompIfFirst();
		}
		winner = ticTac.getGameStatus();
		if (winner != -1) {
			isGameOver = true;
			if(!isGameOverTunePlayed)
				playGameOverTune(winner);
		}

		if (hasPlayerPlayed && !isGameOver) {
			if (ticTac.play(humanPlayer, playerMove)) {
				int compMove = ticTac.getNextMove(compPlayer);
				if (compMove != -1)
					ticTac.play(compPlayer, compMove);
				updatePieces();
			}

			hasPlayerPlayed = false;
		}
	}
	
	private void playGameOverTune(int winner){
		isGameOverTunePlayed = true;
		if(winner == compPlayer)
			loseSound.play();
		else
			drawSound.play();
	}
	
	private void updatePieces(){
		int[] matrix = ticTac.getGameMatrix();
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[i] != 0) {
				if (pieces[i] == null)
					pieces[i] = matrix[i] == 1 ? oImage.copy() : xImage.copy();
			}
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		frame.draw(200, 100, .8f);
		int sRow=7;
		int sCol=10;
		g.drawString("Controls", sCol, sRow);
		g.drawString("F2: Restart Game", sCol, sRow+20);
		g.drawString("F3: Puny Human plays first", sCol, sRow+40);
		g.drawString("F4: Computer plays first", sCol, sRow+60);

		for (int i = 0; i < pieces.length; i++) {
			if (pieces[i] != null) {
				int col = i / 3;
				int row = i - col * 3;
				pieces[i].draw(220 + row * 150, 100 + col * 150, .6f);
			}
		}

		if (isGameOver) {
			String str;
			if (winner == humanPlayer)
				str = "You win!";
			else if (winner == compPlayer)
				str = "Computer wins! Puny Human loses!";
			else
				str = "It's a draw!";

			g.drawString(str + " Press F2 to play again", 200, 550);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if (key == Input.KEY_F2) {
			restartGame = true;
			controlSound.play();
		}
		
		if(key == Input.KEY_F3){
			restartGame = true;
			humanPlayer = 1;
			compPlayer = 2;
			controlSound.play();
		}
		
		if(key == Input.KEY_F4){
			restartGame = true;
			humanPlayer = 2;
			compPlayer = 1;
			controlSound.play();
		}
	}
}
