import java.awt.Dimension;
import java.awt.Font;
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

import javax.swing.*;
import javax.swing.border.Border;

public class Constants implements ActionListener, KeyListener {
	// global constant variables
	private static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private static final int CORALS_GAP = SCREEN_HEIGHT / 5; // distance in pixels between corals
	private static final int CORAL_WIDTH = SCREEN_WIDTH / 8, CORAL_HEIGHT = 4 * CORAL_WIDTH;
	private static final int SQUID_WIDTH = 120, SQUID_HEIGHT = 75;
	private static final int UPDATE_DIFFERENCE = 25; // time in ms between updates
	private static final int X_MOVEMENT_DIFFERENCE = 5; // distance the corals move every update
	private static final int SCREEN_DELAY = 300; // needed because of long load times forcing corals to pop up mid-screen
	private static final int SQUID_X_LOCATION = SCREEN_WIDTH / 7;
	private static final int SQUID_JUMP_DIFF = 10, SQUID_FALL_DIFF = SQUID_JUMP_DIFF / 2,
			SQUID_JUMP_HEIGHT = CORALS_GAP - SQUID_HEIGHT - SQUID_JUMP_DIFF * 2;

	// global variables
	private boolean loopVar = true; // false -> don't run loop; true -> run loop for corals
	private boolean gamePlay = false; // false -> game not being played
	private boolean squidThrust = false; // false -> key has not been pressed to move the squid vertically
	private boolean squidFired = false; // true -> button pressed before jump completes
	private boolean released = true; // space bar released; starts as true so first press registers
	private int squidYTracker = SCREEN_HEIGHT / 2 - SQUID_HEIGHT;
	private Object buildComplete = new Object();

	// global swing objects
	private JFrame f = new JFrame("Limber Squid");
	private JButton startGame;
	private JPanel topPanel; // declared globally to accommodate the repaint operation and allow for
								// removeAll(), etc.

	// other global objects
	private static Constants tc = new Constants();
	private static GameScreen pgs; // panel that has the moving background at the start of the game

	/**
	 * Default constructor
	 */
	public Constants() {

	}

	
	public static void main(String[] args) {

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
	 * Method to construct the JFrame and add the program content
	 */
	private void buildFrame() {
		Image icon = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/squiddy.png"));

		f.setContentPane(createContentPane());
		f.setResizable(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setAlwaysOnTop(false);
		f.setVisible(true);
		f.setMinimumSize(new Dimension(SCREEN_WIDTH * 1 / 4, SCREEN_HEIGHT * 1 / 4));
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setIconImage(icon);
		f.addKeyListener(this);
	}

	private JPanel createContentPane() {
		topPanel = new JPanel(); // top-most JPanel in layout hierarchy
		topPanel.setBackground(Color.BLACK);
		// allow us to layer the panels
		LayoutManager overlay = new OverlayLayout(topPanel);
		topPanel.setLayout(overlay);

		// Start Game JButton
		startGame = new JButton("Start");
		startGame.setForeground(new Color(0, 255, 0));
		startGame.setFocusable(false); // rather than just setFocusabled(false)
		startGame.setFont(new Font("Tahoma", Font.BOLD, 42));
		startGame.setBorderPainted(false);
		startGame.setContentAreaFilled(false);
		startGame.setFocusPainted(false);
		startGame.setOpaque(false);
		startGame.setAlignmentX(0.5f); // center horizontally on-screen
		startGame.setAlignmentY(0.5f); // center vertically on-screen
		startGame.addActionListener(this);
		topPanel.add(startGame);

		// must add last to ensure button's visibility
		pgs = new GameScreen(SCREEN_WIDTH, SCREEN_HEIGHT, true); // true --> we want pgs to be the splash screen
		topPanel.add(pgs);

		return topPanel;
	}

	/**
	 * Implementation for action events
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startGame) {
			// stop the splash screen
			loopVar = false;

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

		// variables to track x and y image locations for the bottom coral
		int xLoc1 = SCREEN_WIDTH + SCREEN_DELAY,
				xLoc2 = (int) ((double) 3.0 / 2.0 * SCREEN_WIDTH + CORAL_WIDTH / 2.0) + SCREEN_DELAY;
		int yLoc1 = bottomCoralLoc(), yLoc2 = bottomCoralLoc();
		int squidX = SQUID_X_LOCATION, squidY = squidYTracker;

		// variable to hold the loop start time
		long startTime = System.currentTimeMillis();

		while (loopVar) {
			if ((System.currentTimeMillis() - startTime) > UPDATE_DIFFERENCE) {
				// check if a set of corals has left the screen
				// if so, reset the coral's X location and assign a new Y location
				if (xLoc1 < (0 - CORAL_WIDTH)) {
					xLoc1 = SCREEN_WIDTH;
					yLoc1 = bottomCoralLoc();
				} else if (xLoc2 < (0 - CORAL_WIDTH)) {
					xLoc2 = SCREEN_WIDTH;
					yLoc2 = bottomCoralLoc();
				}

				// decrement the coral locations by the predetermined amount
				xLoc1 -= X_MOVEMENT_DIFFERENCE;
				xLoc2 -= X_MOVEMENT_DIFFERENCE;

				if (squidFired && !isSplash) {
					squidYTracker = squidY;
					squidFired = false;
				}

				if (squidThrust && !isSplash) {
					// move squid vertically
					if (squidYTracker - squidY - SQUID_JUMP_DIFF < SQUID_JUMP_HEIGHT) {
						if (squidY - SQUID_JUMP_DIFF > 0) {
							squidY -= SQUID_JUMP_DIFF; // coordinates different
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

				// update the BottomCoral and TopCoral locations
				bc1.setX(xLoc1);
				bc1.setY(yLoc1);
				bc2.setX(xLoc2);
				bc2.setY(yLoc2);
				tc1.setX(xLoc1);
				tc1.setY(yLoc1 - CORALS_GAP - CORAL_HEIGHT); // ensure tc1 placed in proper location
				tc2.setX(xLoc2);
				tc2.setY(yLoc2 - CORALS_GAP - CORAL_HEIGHT); // ensure tc2 placed in proper location

				if (!isSplash) {
					squid.setX(squidX);
					squid.setY(squidY);
					pgs.setSquid(squid);
				}

				// set the BottomCoral and TopCoral local variables in GameScreen by parsing
				// the local variables
				pgs.setBottomCoral(bc1, bc2);
				pgs.setTopCoral(tc1, tc2);

				if (!isSplash && squid.getWidth() != -1) { // need the second part because if squid not on-screen, cannot
															// get image width and have cascading error in collision
					collisionDetection(bc1, bc2, tc1, tc2, squid);
					updateScore(bc1, bc2, squid);
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
	private int bottomCoralLoc() {
		int temp = 0;
		// iterate until temp is a value that allows both corals to be onscreen
		while (temp <= CORALS_GAP + 50 || temp >= SCREEN_HEIGHT - CORALS_GAP) {
			temp = (int) ((double) Math.random() * ((double) SCREEN_HEIGHT));
		}
		return temp;
	}

	/**
	 * Method that checks whether the score needs to be updated
	 * 
	 * @param bc1  First BottomCoral object
	 * @param bc2  Second BottomCoral object
	 * @param squid Squid object
	 */
	private void updateScore(BottomCoral bc1, BottomCoral bc2, Squid squid) {
		if (bc1.getX() + CORAL_WIDTH < squid.getX() && bc1.getX() + CORAL_WIDTH > squid.getX() - X_MOVEMENT_DIFFERENCE) {
			pgs.incrementJump();
		} else if (bc2.getX() + CORAL_WIDTH < squid.getX()
				&& bc2.getX() + CORAL_WIDTH > squid.getX() - X_MOVEMENT_DIFFERENCE) {
			pgs.incrementJump();
		}
	}

	/**
	 * Method to test whether a collision has occurred
	 * 
	 * @param bc1  First BottomCoral object
	 * @param bc2  Second BottomCoral object
	 * @param tc1  First TopCoral object
	 * @param tc2  Second TopCoral object
	 * @param squid Squid object
	 */
	private void collisionDetection(BottomCoral bc1, BottomCoral bc2, TopCoral tc1, TopCoral tc2, Squid squid) {
		collisionHelper(squid.getRectangle(), bc1.getRectangle(), squid.getBI(), bc1.getBI());
		collisionHelper(squid.getRectangle(), bc2.getRectangle(), squid.getBI(), bc2.getBI());
		collisionHelper(squid.getRectangle(), tc1.getRectangle(), squid.getBI(), tc1.getBI());
		collisionHelper(squid.getRectangle(), tc2.getRectangle(), squid.getBI(), tc2.getBI());

		if (squid.getY() + SQUID_HEIGHT > SCREEN_HEIGHT * 7 / 8) { // ground detection
			pgs.sendText("Game Over");
			loopVar = false;
			gamePlay = false; // game has ended
		}
	}

	/**
	 * Helper method to test the Squid object's potential collision with a coral
	 * object.
	 * 
	 * @param r1 The Squid's rectangle component
	 * @param r2 Collision component rectangle
	 * @param b1 The Squid's BufferedImage component
	 * @param b2 Collision component BufferedImage
	 */
	private void collisionHelper(Rectangle r1, Rectangle r2, BufferedImage b1, BufferedImage b2) {
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
						pgs.sendText("Game Over");
						loopVar = false; // stop the game loop
						gamePlay = false; // game has ended
						break;
					}
				}
			}
		}
	}
}