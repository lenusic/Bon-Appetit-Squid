import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Image;

public class Shield {
	// global variables
	private Image bubbleShield;
	private int xLoc = 0, yLoc = 0;
	private boolean isVisible = false;
	private int lastShieldEnd = 0;

	/**
	 * Default constructor
	 */
	public Shield(int initialWidth, int initialHeight) {
		bubbleShield = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/bubbleshield.png"));
		scaleShield(initialWidth, initialHeight);
	}

	/**
	 * Method to scale the shield sprite into the desired dimensions
	 * 
	 * @param width  The desired width of the bubble shield
	 * @param height The desired height of the bubble shield
	 */
	public void scaleShield(int width, int height) {
		bubbleShield = bubbleShield.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

	/**
	 * Getter method for the bubbleShield object.
	 * 
	 * @return Image
	 */
	public Image getShield() {
		return bubbleShield;
	}

	/**
	 * Method to obtain the width of the Shield object
	 * 
	 * @return int
	 */
	public int getWidth() {
		try {
			return bubbleShield.getWidth(null);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Method to obtain the height of the Bird object
	 * 
	 * @return int
	 */
	public int getHeight() {
		try {
			return bubbleShield.getHeight(null);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Method to set the x location of the Shield object
	 * 
	 * @param x
	 */
	public void setX(int x) {
		xLoc = x;
	}

	/**
	 * Method to get the x location of the Shield object
	 * 
	 * @return int
	 */
	public int getX() {
		return xLoc;
	}

	/**
	 * Method to set the y location of the Shield object
	 * 
	 * @param y
	 */
	public void setY(int y) {
		yLoc = y;
	}

	/**
	 * Method to get the y location of the Shield object
	 * 
	 * @return int
	 */
	public int getY() {
		return yLoc;
	}

	/**
	 * Method used to acquire a Rectangle that outlines the Shield's image
	 * 
	 * @return Rectangle outlining the Shield's position on screen
	 */
	public Rectangle getRectangle() {
		return (new Rectangle(xLoc, yLoc, bubbleShield.getWidth(null), bubbleShield.getHeight(null)));
	}

	/**
	 * Method to get the value of the isVisible boolean for the Shield
	 * @return boolean
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Method to set the value of the isVisible boolean for the Shield
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.isVisible = visible;
	}

	/**
	 * Method to get the value of the last Shiled End (food value when shiled was last deactivated)
	 * @return int
	 */
	public int getLastShieldEnd(){
		return lastShieldEnd;
	}

	/**
	 * Method to set the value of lastShieldEnd
	 * @param newEnd
	 */
	public void setLastShieldEnd(int newEnd){
		this.lastShieldEnd = newEnd;
	}
}