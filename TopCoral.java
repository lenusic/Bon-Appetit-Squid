import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class TopCoral {
	// global variables
	private Image topCoral;
	private int xLoc = 0, yLoc = 0;

	/**
	 * Default constructor
	 */
	public TopCoral(int initialWidth, int initialHeight) {
		topCoral = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/corals_Top.png"));
		scaleTopCoral(initialWidth, initialHeight);
	}

	/**
	 * Method to scale the topPipe sprite into the desired dimensions
	 * 
	 * @param width  The desired width of the topCoral
	 * @param height The desired height of the topCoral
	 */
	public void scaleTopCoral(int width, int height) {
		topCoral = topCoral.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

	/**
	 * Getter method for the TopCoral object.
	 * 
	 * @return Image
	 */
	public Image getCoral() {
		return topCoral;
	}

	/**
	 * Method to obtain the width of the TopCoral object
	 * 
	 * @return int
	 */
	public int getWidth() {
		return topCoral.getWidth(null);
	}

	/**
	 * Method to obtain the height of the TopCoral object
	 * 
	 * @return int
	 */
	public int getHeight() {
		return topCoral.getHeight(null);
	}

	/**
	 * Method to set the x location of the TopCoral object
	 * 
	 * @param x
	 */
	public void setX(int x) {
		xLoc = x;
	}

	/**
	 * Method to get the x location of the TopCoral object
	 * 
	 * @return int
	 */
	public int getX() {
		return xLoc;
	}

	/**
	 * Method to set the y location of the TopCoral object
	 * 
	 * @param y
	 */
	public void setY(int y) {
		yLoc = y;
	}

	/**
	 * Method to get the y location of the TopCoral object
	 * 
	 * @return int
	 */
	public int getY() {
		return yLoc;
	}

	/**
	 * Method used to acquire a Rectangle that outlines the TopCoral's image
	 * 
	 * @return Rectangle outlining the TopCoral's position on screen
	 */
	public Rectangle getRectangle() {
		return (new Rectangle(xLoc, yLoc, topCoral.getWidth(null), topCoral.getHeight(null)));
	}

	/**
	 * Method to acquire a BufferedImage that represents the TopCoral's image object
	 * 
	 * @return TopPipe's BufferedImage object
	 */
	public BufferedImage getBI() {
		BufferedImage bi = new BufferedImage(topCoral.getWidth(null), topCoral.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.drawImage(topCoral, 0, 0, null);
		g.dispose();
		return bi;
	}
}