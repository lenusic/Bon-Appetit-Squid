import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class Constants implements ActionListener, KeyListener {
	// global constant variables
	private static int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private static final int CORALS_GAP = SCREEN_HEIGHT / 5; // distance in pixels between corals
	private static final int CORAL_WIDTH = SCREEN_WIDTH / 8, CORAL_HEIGHT = 4 * CORAL_WIDTH;
	private static final int FISH_WIDTH = SCREEN_WIDTH / 2;
	private static final int SQUID_WIDTH = 120, SQUID_HEIGHT = 75;
	private static final int ENEMY_WIDTH = SCREEN_WIDTH / 2;
	private static final int SHIELD_WIDTH = SQUID_WIDTH + 100, SHIELD_HEIGHT = SQUID_HEIGHT + 100;

	private static final int UPDATE_DIFFERENCE = 25; // time in ms between updates
	private static int X_MOVEMENT_DIFFERENCE = 5; // distance the corals move every update
	private static final int SCREEN_DELAY = 300; // needed because of long load times forcing corals to pop up
													// mid-screen
	private static final int SQUID_X_LOCATION = SCREEN_WIDTH / 7;
	private static final int SQUID_JUMP_DIFF = 10, SQUID_FALL_DIFF = SQUID_JUMP_DIFF / 2,
			SQUID_JUMP_HEIGHT = CORALS_GAP - SQUID_HEIGHT - SQUID_JUMP_DIFF * 2;

	// global variables
	private boolean loopVar = true; // false -> don't run loop; true -> run loop for corals
	private boolean gamePlay = false; // false -> game not being played or game is over
	private boolean squidThrust = false; // false -> key has not been pressed to move the squid vertically
	private boolean squidFired = false; // true -> button pressed before jump completes
	private boolean released = true; // space bar released; starts as true so first press registers
	private int squidYTracker = SCREEN_HEIGHT / 2 - SQUID_HEIGHT;
	private Object buildComplete = new Object();
	private boolean eatenFish1 = false;
	private boolean eatenFish2 = false;
	private boolean eatenFish3 = false;

	private boolean gameOver = false;

	// global swing objects
	private JFrame frame = new JFrame("Limber Squid");
	private static JButton startGame, restartGame;
	private JButton gameoverLabel;
	private JPanel topPanel; // declared globally to accommodate the repaint operation and allow for
								// removeAll(), etc.

	// other global objects
	private static Constants tc = new Constants();
	private static GameScreen pgs = new GameScreen(SCREEN_WIDTH, SCREEN_HEIGHT, true); // panel that has the moving
																						// background at the start of
																						// the game

	/**
	 * Default constructor
	 */
	public Constants() {
		// for (DisplayMode mode :
		// GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
		// .getDisplayModes()) {
		// if (SCREEN_WIDTH != mode.getWidth())
		// SCREEN_WIDTH = mode.getWidth();
		// if (SCREEN_HEIGHT != mode.getHeight())
		// SCREEN_HEIGHT = mode.getHeight();
		// }
	}

	public static void main(String[] args) {

		        Audio audioPlayer = new Audio("resources/the-squid-song.wav");

		// Create and start the audio thread
        Thread audioThread = new Thread(audioPlayer);
        audioThread.start();
        audioPlayer.play();

		// build the GUI on a new thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tc.buildFrame();

				// create a new thread to keep the GUI responsive while the game runs
				Thread t = new Thread() {
					public void run() {
						tc.gameScreen(true);
					}
				};
				t.start();
			}
		});
	}

	/**
	 * Sets squid location to the right place, should be called on start and on
	 * restart
	 */
	public void squidRespawn() {
		squidYTracker = SCREEN_HEIGHT / 2 - SQUID_HEIGHT;
		System.out.println("Squid respawned");
	}

	// add Start Game JButton
	private void addStartButton() {
		startGame = new JButton("Start");
		startGame.setForeground(new Color(250, 60, 80));
		startGame.setFocusable(false); // rather than just setFocusabled(false)
		startGame.setFont(new Font("Tahoma", Font.BOLD, 42));
		startGame.setBorderPainted(false);
		startGame.setContentAreaFilled(false);
		startGame.setFocusPainted(false);
		startGame.setOpaque(false);
		startGame.setVisible(true);
		startGame.setAlignmentX(0.5f); // center horizontally on-screen
		startGame.setAlignmentY(0.5f); // center vertically on-screen
		startGame.addActionListener(this); // register listener to this object actions
		topPanel.add(startGame);
	}

	// add Restart game JButton
	private void addRestartButton() {
		restartGame = new JButton("Restart");
		restartGame.setForeground(new Color(0, 255, 127));
		restartGame.setFocusable(false);
		restartGame.setFont(new Font("Tahoma", Font.BOLD, 42));
		restartGame.setBorderPainted(false);
		restartGame.setContentAreaFilled(false);
		restartGame.setFocusPainted(false);
		restartGame.setOpaque(false);
		restartGame.setAlignmentX(0.5f); // center horizontally on-screen
		restartGame.setAlignmentY(0.09f); // center vertically on-screen
		restartGame.addActionListener(this);
		restartGame.setVisible(true);
		topPanel.add(restartGame);
	}

	private void addGamoverText() {
		gameoverLabel = new JButton();
		gameoverLabel.setText("GAME OVER");

		gameoverLabel.setForeground(new Color(200, 40, 60));
		gameoverLabel.setFocusable(false);
		gameoverLabel.setFont(new Font("Tahoma", Font.BOLD, 42));
		gameoverLabel.setBorderPainted(false);
		gameoverLabel.setContentAreaFilled(false);
		gameoverLabel.setFocusPainted(false);
		gameoverLabel.setOpaque(false);
		gameoverLabel.setAlignmentX(0.5f); // center horizontally on-screen
		gameoverLabel.setAlignmentY(1.0f); // center vertically on-screen
		gameoverLabel.addActionListener(this);
		gameoverLabel.setVisible(true);

		topPanel.add(gameoverLabel);
	}

	private JPanel createContentPane() {
		topPanel = new JPanel(); // top-most JPanel in layout hierarchy
		// allow us to layer the panels
		LayoutManager overlay = new OverlayLayout(topPanel);
		topPanel.setLayout(overlay);

		if (gameOver == true) {
			addGamoverText();
			addRestartButton();
		} else {
			addStartButton();
		}

		// must add last to ensure button's visibility.
		// UPD: not button, it appends the moving background(pgs) to the topPanel
		pgs = new GameScreen(SCREEN_WIDTH, SCREEN_HEIGHT, true); // true --> we want pgs to be the splash screen
		topPanel.add(pgs);

		return topPanel;
	}

	/**
	 * Method to construct the JFrame and add the program content
	 */
	private void buildFrame() {
		Image squidAvatar = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/squiddy.png"));

		frame.setContentPane(createContentPane());
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(false);
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(SCREEN_WIDTH * 1 / 4, SCREEN_HEIGHT * 1 / 4));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setIconImage(squidAvatar);
		frame.addKeyListener(this);
	}

	/**
	 * Implementation for action events
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startGame) {
			System.out.println(">>> start btn pressed, starting the game");
			// stop the splash screen
			loopVar = false;
			gamePlay = true;
			
			fadeOperation();
		} else if (e.getSource() == buildComplete) {
			Thread t = new Thread() {
				public void run() {
					loopVar = true;
					gamePlay = true;
					tc.gameScreen(false);
				}
			};
			t.start();
		} else if (e.getSource() == restartGame) { // listen to restart button press
			System.out.println(">>> restart btn pressed");
			// stop the splash screen
			loopVar = false;
			gamePlay = true;
			squidRespawn();
			fadeOperation();
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && gamePlay == true && released == true) {
			// update a boolean that's tested in game loop to move the squid
			if (squidThrust) { // need this to register the button press and reset the squidYTracker before the
								// jump operation completes
				squidFired = true;
			}
			squidThrust = true;
			released = false;
		} else if (e.getKeyCode() == KeyEvent.VK_B && gamePlay == false) {
			squidYTracker = SCREEN_HEIGHT / 2 - SQUID_HEIGHT; // need to reset the squid's starting height
			squidThrust = false; // if user presses SPACE before collision and a collision occurs before reaching
									// max height, you get residual jump, so this is preventative
			actionPerformed(new ActionEvent(startGame, -1, ""));
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			released = true;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Perform the fade operation that take place before the start of rounds
	 */
	private void fadeOperation() {
		Thread t = new Thread() {
			public void run() {
				topPanel.remove(startGame);
				topPanel.remove(pgs);
				topPanel.revalidate();
				topPanel.repaint();

				// panel to fade
				JPanel temp = new JPanel();
				int alpha = 0; // alpha channel variable
				temp.setBackground(new Color(0, 0, 0, alpha)); // transparent, black JPanel
				topPanel.add(temp);
				topPanel.add(pgs);
				topPanel.revalidate();
				topPanel.repaint();

				long currentTime = System.currentTimeMillis();

				while (temp.getBackground().getAlpha() != 255) {
					if ((System.currentTimeMillis() - currentTime) > UPDATE_DIFFERENCE / 2) {
						if (alpha < 255 - 10) {
							alpha += 10;
						} else {
							alpha = 255;
						}

						temp.setBackground(new Color(0, 0, 0, alpha));

						topPanel.revalidate();
						topPanel.repaint();
						currentTime = System.currentTimeMillis();
					}
				}

				topPanel.removeAll();
				topPanel.add(temp);
				pgs = new GameScreen(SCREEN_WIDTH, SCREEN_HEIGHT, false);
				pgs.sendText(""); // remove title text
				topPanel.add(pgs);

				while (temp.getBackground().getAlpha() != 0) {
					if ((System.currentTimeMillis() - currentTime) > UPDATE_DIFFERENCE / 2) {
						if (alpha > 10) {
							alpha -= 10;
						} else {
							alpha = 0;
						}

						temp.setBackground(new Color(0, 0, 0, alpha));

						topPanel.revalidate();
						topPanel.repaint();
						currentTime = System.currentTimeMillis();
					}
				}

				actionPerformed(new ActionEvent(buildComplete, -1, "Build Finished"));
			}
		};
		t.start();
	}

	/**
	 * Method that performs the splash screen graphics movements
	 */
	private void gameScreen(boolean isSplash) {
		BottomCoral bc1 = new BottomCoral(CORAL_WIDTH, CORAL_HEIGHT);
		BottomCoral bc2 = new BottomCoral(CORAL_WIDTH, CORAL_HEIGHT);
		TopCoral tc1 = new TopCoral(CORAL_WIDTH, CORAL_HEIGHT);
		TopCoral tc2 = new TopCoral(CORAL_WIDTH, CORAL_HEIGHT);
		Squid squid = new Squid(SQUID_WIDTH, SQUID_HEIGHT);
		Fish fish1 = new Fish(80, 60, "resources/green-fish.png");
		Fish fish2 = new Fish(60, 40, "resources/fish2.png");
		Fish fish3 = new Fish(60, 40, "resources/yellow-fish.png");

		Enemy enemy = new Enemy(180, 90, "resources/seal.png");

		Shield shield = new Shield(SHIELD_WIDTH, SHIELD_HEIGHT);

		// variables to track x and y image locations
		int squidX = SQUID_X_LOCATION, squidY = squidYTracker;
		int xLoc1 = SCREEN_WIDTH + SCREEN_DELAY,
				xLoc2 = (int) ((double) 3.0 / 2.0 * SCREEN_WIDTH + CORAL_WIDTH / 2.0) + SCREEN_DELAY;
		int yLoc1 = generateBottomCoralLocation(), yLoc2 = generateBottomCoralLocation();
		int xLocFish1 = SCREEN_WIDTH / 5 + SCREEN_DELAY,
				xLocFish2 = (int) ((double) 3.0 / 2.0 * (SCREEN_WIDTH) + FISH_WIDTH / 2.0) + SCREEN_DELAY,
				xLocFish3 = (int) ((double) 3.0 / 2.0 * (SCREEN_WIDTH) + FISH_WIDTH / 2.0) + SCREEN_DELAY;
		int fishY1 = fishLoc(), fishY2 = fishLoc(), fishY3 = fishLoc();
		int enemyX1 = (int) ((double) 4.0 / 2.0 * (SCREEN_WIDTH) + FISH_WIDTH / 1.5) + SCREEN_DELAY;
		// int enemyX1 = SCREEN_WIDTH / 9 + SCREEN_DELAY;
		int enemyY1 = enemyLoc();

		// variable to hold the loop start time
		long startTime = System.currentTimeMillis();

		while (loopVar) {
			if ((System.currentTimeMillis() - startTime) > UPDATE_DIFFERENCE) {
				// check if a set of corals has left the screen
				// if so, reset the coral's X location and assign a new Y location
				if (xLoc1 < (0 - CORAL_WIDTH)) {
					xLoc1 = SCREEN_WIDTH;
					yLoc1 = generateBottomCoralLocation();
					bc1.setVisible(true);
					tc1.setVisible(true);
				} else if (xLoc2 < (0 - CORAL_WIDTH)) {
					xLoc2 = SCREEN_WIDTH;
					yLoc2 = generateBottomCoralLocation();
					bc2.setVisible(true);
					tc2.setVisible(true);
				}
				if (xLocFish1 < (0 - FISH_WIDTH)) {
					xLocFish1 = SCREEN_WIDTH / 5 + SCREEN_DELAY;
					fishY1 = fishLoc();
					fish1.setVisible(true);
				} else if (xLocFish2 < (0 - FISH_WIDTH)) {
					xLocFish2 = SCREEN_WIDTH;
					fishY2 = fishLoc();
					fish2.setVisible(true);
				} else if (xLocFish3 < (0 - FISH_WIDTH)) {
					xLocFish3 = SCREEN_WIDTH;
					fishY3 = fishLoc();
					fish3.setVisible(true);
				}
				if (enemyX1 < (0 - ENEMY_WIDTH)) {
					enemyX1 = SCREEN_WIDTH + SCREEN_DELAY;
					enemyY1 = enemyLoc();
					enemy.setVisible(true);
				}
				// decrement locations by the predetermined amount
				xLoc1 -= X_MOVEMENT_DIFFERENCE;
				xLoc2 -= X_MOVEMENT_DIFFERENCE;

				xLocFish1 -= X_MOVEMENT_DIFFERENCE * 1.1;
				xLocFish2 -= X_MOVEMENT_DIFFERENCE * 1.3;
				xLocFish3 -= X_MOVEMENT_DIFFERENCE * 1.5;

				enemyX1 -= X_MOVEMENT_DIFFERENCE * 1.4;

				if (squidFired && !isSplash) {
					squidYTracker = squidY;
					squidFired = false;
				}

				if (squidThrust && !isSplash) {
					// move squid vertically
					if (squidYTracker - squidY - SQUID_JUMP_DIFF < SQUID_JUMP_HEIGHT) {
						if (squidY - SQUID_JUMP_DIFF > 0) {
							squidY -= SQUID_JUMP_DIFF;
						} else {
							squidY = 0;
							squidYTracker = squidY;
							squidThrust = false;
						}
					} else {
						squidYTracker = squidY;
						squidThrust = false;
					}
				} else if (!isSplash) {
					squidY += SQUID_FALL_DIFF;
					squidYTracker = squidY;
				}

				// update the locations
				bc1.setX(xLoc1);
				bc1.setY(yLoc1);
				bc2.setX(xLoc2);
				bc2.setY(yLoc2);
				tc1.setX(xLoc1);
				tc1.setY(yLoc1 - CORALS_GAP - CORAL_HEIGHT); // ensure tc1 placed in proper location
				tc2.setX(xLoc2);
				tc2.setY(yLoc2 - CORALS_GAP - CORAL_HEIGHT); // ensure tc2 placed in proper location
				fish1.setX(xLocFish1);
				fish1.setY(fishY1);
				fish2.setX(xLocFish2);
				fish2.setY(fishY2);
				fish3.setX(xLocFish3);
				fish3.setY(fishY3);
				enemy.setX(enemyX1);
				enemy.setY(enemyY1);

				if (!isSplash) {
					squid.setX(squidX);
					squid.setY(squidY);
					pgs.setSquid(squid);
					shield.setX(squidX - (SQUID_WIDTH / 2));
					shield.setY(squidY - (SQUID_HEIGHT / 2));
					pgs.setShield(shield);
				}

				// set the BottomCoral and TopCoral local variables in GameScreen by parsing
				// the local variables
				pgs.setBottomCoral(bc1, bc2);
				pgs.setTopCoral(tc1, tc2);
				pgs.setFish(fish1, fish2, fish3);
				pgs.setEnemy(enemy);

				if (!isSplash && squid.getWidth() != -1) { // need the second part because if squid not on-screen,
															// cannot
															// get image width and have cascading error in collision
					collisionDetection(bc1, bc2, tc1, tc2, squid, shield);
					updateScore(bc1, bc2, squid);
					updateSpeed(bc1, bc2, squid);
					collisionFood(fish1, fish2, fish3, squid);
					collisionEnemy(enemy, squid, shield);
				}

				// update pgs's JPanel
				topPanel.revalidate();
				topPanel.repaint();

				// update the time-tracking variable after all operations completed
				startTime = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Calculates a random int for the bottom coral's placement
	 * 
	 * @return int
	 */
	private int generateBottomCoralLocation() {
		int temp = 0;
		// iterate until temp is a value that allows both corals to be onscreen
		while (temp <= CORALS_GAP + 50 || temp >= SCREEN_HEIGHT - CORALS_GAP) {
			temp = (int) ((double) Math.random() * ((double) SCREEN_HEIGHT));
		}
		return temp;
	}

	private int fishLoc() {
		int temp = 0;
		while (temp <= CORALS_GAP + 30 || temp >= SCREEN_HEIGHT - CORALS_GAP) {
			temp = (int) ((double) Math.random() * ((double) SCREEN_HEIGHT));
		}
		return temp;
	}

	private int enemyLoc() {
		int temp = 0;
		while (temp <= CORALS_GAP + 40 || temp >= SCREEN_HEIGHT - CORALS_GAP) {
			temp = (int) ((double) Math.random() * ((double) SCREEN_HEIGHT));
		}
		return temp;
	}

	/**
	 * Method that checks whether the score needs to be updated
	 * 
	 * @param bc1   First BottomCoral object
	 * @param bc2   Second BottomCoral object
	 * @param squid Squid object
	 */
	private void updateScore(BottomCoral bc1, BottomCoral bc2, Squid squid) {
		if (bc1.getX() + CORAL_WIDTH < squid.getX()
				&& bc1.getX() + CORAL_WIDTH > squid.getX() - X_MOVEMENT_DIFFERENCE) {
			pgs.incrementJump();
		} else if (bc2.getX() + CORAL_WIDTH < squid.getX()
				&& bc2.getX() + CORAL_WIDTH > squid.getX() - X_MOVEMENT_DIFFERENCE) {
			pgs.incrementJump();
		}
	}

	private void updateFoodScore(Fish f1, Fish f2, Fish f3, Squid squid) {
		if (f1.getX() < squid.getX()
				&& f1.getX() > squid.getX() - X_MOVEMENT_DIFFERENCE * 1.1
				&& eatenFish1) {
			pgs.incrementFood();
			f1.setVisible(false);
		}
		if (f2.getX() < squid.getX()
				&& f2.getX() > squid.getX() - X_MOVEMENT_DIFFERENCE * 1.3 && eatenFish2) {
			pgs.incrementFood();
			f2.setVisible(false);
		}
		if (f3.getX() < squid.getX()
				&& f3.getX() > squid.getX() - X_MOVEMENT_DIFFERENCE * 1.5 && eatenFish3) {
			pgs.incrementFood();
			f3.setVisible(false);
		}
	}

	private void updateSpeed(BottomCoral bc1, BottomCoral bc2, Squid squid) {
		if (pgs.speedUp()) {
			X_MOVEMENT_DIFFERENCE += 5;
		}
	}

	/**
	 * Method to test whether a collision has occurred
	 * 
	 * @param bc1   First BottomCoral object
	 * @param bc2   Second BottomCoral object
	 * @param tc1   First TopCoral object
	 * @param tc2   Second TopCoral object
	 * @param squid Squid object
	 */
	private void collisionDetection(BottomCoral bc1, BottomCoral bc2, TopCoral tc1, TopCoral tc2, Squid squid,
			Shield shield) {
		collisionCoral(squid, tc1, bc1, shield);
		collisionCoral(squid, tc2, bc2, shield);
		if (squid.getY() + SQUID_HEIGHT > SCREEN_HEIGHT * 7 / 8) { // ground detection
			gameOver = true;
			loopVar = false;
			gamePlay = false; // game has ended
			tc.buildFrame(); // fixes the adding restart button but spoils pgs.sendText()
		} else if (squid.getY() + SQUID_HEIGHT < 100) { // ceiling detection
			loopVar = false;
			gamePlay = false; // game has ended
			gameOver = true;
			tc.buildFrame(); // fixes the adding restart button but spoils pgs.sendText()
		}

	}

	private void collisionCoral(Squid squid, TopCoral tc, BottomCoral bc, Shield shield) {
		boolean isCollide;
		if (bc.isVisible()) {
			isCollide = collisionHelper(squid.getRectangle(), bc.getRectangle(), squid.getBI(), bc.getBI(), shield);
			if (isCollide && shield.isVisible()) {
				bc.setVisible(false);
				shield.setVisible(false);
			}
		}
		if (tc.isVisible()) {
			isCollide = collisionHelper(squid.getRectangle(), tc.getRectangle(), squid.getBI(), tc.getBI(), shield);
			if (isCollide && shield.isVisible()) {
				tc.setVisible(false);
				shield.setVisible(false);
			}
		}
	}

	private void collisionFood(Fish f1, Fish f2, Fish f3, Squid squid) {
		if (collisionHelperFood(squid.getRectangle(), f1.getRectangle(), squid.getBI(), f1.getBI())) {
			eatenFish1 = true;
			updateFoodScore(f1, f2, f3, squid);
		}
		if (collisionHelperFood(squid.getRectangle(), f2.getRectangle(), squid.getBI(), f2.getBI())) {
			eatenFish2 = true;
			updateFoodScore(f1, f2, f3, squid);
		}
		if (collisionHelperFood(squid.getRectangle(), f3.getRectangle(), squid.getBI(), f3.getBI())) {
			eatenFish3 = true;
			updateFoodScore(f1, f2, f3, squid);
		}
	}

	private void collisionEnemy(Enemy enemy, Squid squid, Shield shield) {
		if (enemy.isVisible) {
			boolean isCollide = collisionHelper(squid.getRectangle(), enemy.getRectangle(), squid.getBI(),
					enemy.getBI(), shield);
			if (isCollide && shield.isVisible()) {
				enemy.setVisible(false);
				shield.setVisible(false);
			}
		}
	}

	/**
	 * Helper method to test the Squid object's potential collision with objects
	 * 
	 * @param r1 The Squid's rectangle component
	 * @param r2 Collision component rectangle
	 * @param b1 The Squid's BufferedImage component
	 * @param b2 Collision component BufferedImage
	 */
	private boolean collisionHelper(Rectangle r1, Rectangle r2, BufferedImage b1, BufferedImage b2, Shield shield) {
		if (r1.intersects(r2)) {
			Rectangle r = r1.intersection(r2);

			int firstI = (int) (r.getMinX() - r1.getMinX()); // firstI is the first x-pixel to iterate from
			int firstJ = (int) (r.getMinY() - r1.getMinY()); // firstJ is the first y-pixel to iterate from
			int bp1XHelper = (int) (r1.getMinX() - r2.getMinX()); // helper variables to use when referring to collision
																	// object
			int bp1YHelper = (int) (r1.getMinY() - r2.getMinY());

			for (int i = firstI; i < r.getWidth() + firstI; i++) { //
				for (int j = firstJ; j < r.getHeight() + firstJ; j++) {
					if ((b1.getRGB(i, j) & 0xFF000000) != 0x00
							&& (b2.getRGB(i + bp1XHelper, j + bp1YHelper) & 0xFF000000) != 0x00) {
						if (!shield.isVisible()) {
							// pgs.sendText("Game Over");
							gameOver = true;
							loopVar = false; // stop the game loop
							gamePlay = false; // game has ended
							tc.buildFrame();
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean collisionHelperFood(Rectangle r1, Rectangle r2, BufferedImage b1, BufferedImage b2) {
		if (r1.intersects(r2)) {
			return true;
		} else {
			return false;
		}
	}

}