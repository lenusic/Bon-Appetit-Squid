import javax.swing.*;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Color;

public class GameScreen extends JPanel {
	// default reference ID
	private static final long serialVersionUID = 1L;

	// global variables
	private int screenWidth, screenHeight;
	private boolean isSplash = true;
	private int successfulJumps = 0;
	private int food = 0;
	private String message = "Limber Squid";
	private Font primaryFont = new Font("Futura", Font.BOLD, 40), failFont = new Font("Calibri", Font.BOLD, 56);
	private int messageWidth = 0, scoreWidth = 0;
	private BottomCoral bc1, bc2;
	private TopCoral tc1, tc2;
	private Squid squid;
	private Fish fish1, fish2, fish3;
	private Enemy enemy;
	private Shield shield;

	/**
	 * Default constructor for the GameScreen class
	 */
	public GameScreen(int screenWidth, int screenHeight, boolean isSplash) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.isSplash = isSplash;
	}

	/**
	 * Manually control what's drawn on this JPanel by calling the paintComponent
	 * method with a graphics object and painting using that object
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Image sea = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/ocean.gif"));
		g.drawImage(sea, 0, 0, screenWidth, screenHeight, null);
		g.setColor(Color.WHITE); // dividing line color

		// objects must be instantiated before they're drawn!
		if (bc1 != null && bc2 != null && tc1 != null && tc2 != null) {
			g.drawImage(bc1.getCoral(), bc1.getX(), bc1.getY(), null);
			g.drawImage(bc2.getCoral(), bc2.getX(), bc2.getY(), null);
			g.drawImage(tc1.getCoral(), tc1.getX(), tc1.getY(), null);
			g.drawImage(tc2.getCoral(), tc2.getX(), tc2.getY(), null);
			if (fish1.isVisible()) {
				g.drawImage(fish1.getFish(), fish1.getX(), fish1.getY(), null);
			}
			if (fish2.isVisible()) {
				g.drawImage(fish2.getFish(), fish2.getX(), fish2.getY(), null);
			}
			if (fish3.isVisible()) {
				g.drawImage(fish3.getFish(), fish3.getX(), fish3.getY(), null);
			}
			if (enemy.isVisible()) {
				g.drawImage(enemy.getEnemy(), enemy.getX(), enemy.getY(), null);
			}

		}

		if (!isSplash && squid != null) {
			g.drawImage(squid.getSquid(), squid.getX(), squid.getY(), null);
			if (shield.isVisible()) {
				g.drawImage(shield.getShield(), shield.getX(), shield.getY(), null);
			}
		}

		// needed in case the primary font does not exist
		try {
			g.setFont(primaryFont);
			FontMetrics metric = g.getFontMetrics(primaryFont);
			messageWidth = metric.stringWidth(message);
			scoreWidth = metric.stringWidth(String.format("%d", successfulJumps));
		} catch (Exception e) {
			g.setFont(failFont);
			FontMetrics metric = g.getFontMetrics(failFont);
			messageWidth = metric.stringWidth(message);
			scoreWidth = metric.stringWidth(String.format("%d", successfulJumps));
		}

		g.drawString(message, screenWidth / 2 - messageWidth / 2, screenHeight / 4);

		if (!isSplash) {
			g.drawString(String.format("Jumps: %d", successfulJumps), screenWidth / 3 - scoreWidth / 2, 50);
			g.drawString(String.format("Food: %d", food), screenWidth / 2 - scoreWidth / 5, 50);
		}
	}

	/**
	 * Parsing method for GameScreen's global BottomCoral variables
	 * 
	 * @param bc1 The first BottomCoral
	 * @param bc2 The second BottomCoral
	 */
	public void setBottomCoral(BottomCoral bc1, BottomCoral bc2) {
		this.bc1 = bc1;
		this.bc2 = bc2;
	}

	/**
	 * Parsing method for GameScreen's global TopCoral variables
	 * 
	 * @param tc1 The first TopCoral
	 * @param tc2 The second TopCoral
	 */
	public void setTopCoral(TopCoral tc1, TopCoral tc2) {
		this.tc1 = tc1;
		this.tc2 = tc2;
	}

	/**
	 * Parsing method for GameScreen's global variables
	 * 
	 * @param squid The Squid object
	 */
	public void setSquid(Squid squid) {
		this.squid = squid;
	}

	public void setFish(Fish fish1, Fish fish2, Fish fish3) {
		this.fish1 = fish1;
		this.fish2 = fish2;
		this.fish3 = fish3;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

	public void setShield(Shield shield) {
		this.shield = shield;
	}

	/**
	 * Method called to invoke an increase in the variable tracking the current jump
	 * score
	 */
	public void incrementJump() {
		successfulJumps++;
	}

	public void incrementFood() {
		food++;
		System.out.println("Food" + food);
	}

	public boolean speedUp() {
		if (successfulJumps != 0 && successfulJumps % 10 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method called to return the current jump score
	 * 
	 * @return
	 */
	public int getScore() {
		return successfulJumps;
	}

	public int getFoodScore() {
		return food;
	}

	/**
	 * Method called to parse a message onto the screen
	 * 
	 * @param message The message to parse
	 */
	public void sendText(String message) {
		this.message = message;
	}
}