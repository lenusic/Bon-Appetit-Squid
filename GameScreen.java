import javax.swing.*;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Color;

public class GameScreen extends JPanel {
	// default reference ID
	private static final long serialVersionUID = 1L;

	// global variables
	private int screenWidth, screenHeight;
	private boolean isSplash = true;
	private int successfulJumps = 0;
	private String message = "Limber Squid";
	private Font primaryFont = new Font("Futura", Font.BOLD, 56), failFont = new Font("Calibri", Font.BOLD, 56);
	private int messageWidth = 0, scoreWidth = 0;
	private BottomCoral bc1, bc2;
	private TopCoral tc1, tc2;
	private Squid squid;

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

		g.setColor(new Color(89, 81, 247)); // color for the blue ocean
		g.fillRect(0, 0, screenWidth, screenHeight * 7 / 8); // create the ocean rectangle
		g.setColor(new Color(147, 136, 9)); // brown color for ocean floor
		g.fillRect(0, screenHeight * 7 / 8, screenWidth, screenHeight / 8); // create the ocean floor rectangle
		g.setColor(Color.BLACK); // dividing line color
		g.drawLine(0, screenHeight * 7 / 8, screenWidth, screenHeight * 7 / 8); // draw the dividing line

		// objects must be instantiated before they're drawn!
		if (bc1 != null && bc2 != null && tc1 != null && tc2 != null) {
			g.drawImage(bc1.getCoral(), bc1.getX(), bc1.getY(), null);
			g.drawImage(bc2.getCoral(), bc2.getX(), bc2.getY(), null);
			g.drawImage(tc1.getCoral(), tc1.getX(), tc1.getY(), null);
			g.drawImage(tc2.getCoral(), tc2.getX(), tc2.getY(), null);
		}

		if (!isSplash && squid != null) {
			g.drawImage(squid.getSquid(), squid.getX(), squid.getY(), null);
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
			g.drawString(String.format("%d", successfulJumps), screenWidth / 2 - scoreWidth / 2, 50);
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
	 * Parsing method for GameScreen's global Squid variable
	 * 
	 * @param squid The Squid object
	 */
	public void setSquid(Squid squid) {
		this.squid = squid;
	}

	/**
	 * Method called to invoke an increase in the variable tracking the current
	 * jump score
	 */
	public void incrementJump() {
		successfulJumps++;
	}

	/**
	 * Method called to return the current jump score
	 * 
	 * @return
	 */
	public int getScore() {
		return successfulJumps;
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