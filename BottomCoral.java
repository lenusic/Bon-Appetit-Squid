import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class BottomCoral {
	// global variables
	private Image bottomCoral;
	private int xLoc = 0, yLoc = 0;

	/**
	 * Default constructor
	 */
	public BottomCoral(int initialWidth, int initialHeight) {
		bottomCoral = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/corals_Bottom.png"));
		scaleBottomCoral(initialWidth, initialHeight);
	}

	/**
	 * Method to scale the BottomCoral sprite into the desired dimensions
	 * 
	 * @param width  The desired width of the BottomCoral
	 * @param height The desired height of the BottomCoral
	 */
	public void scaleBottomCoral(int width, int height) {
		bottomCoral = bottomCoral.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

	/**
	 * Getter method for the BottomCoral object.
	 * 
	 * @return Image
	 */
	public Image getCoral() {
		return bottomCoral;
	}

	/**
	 * Method to obtain the width of the BottomCoral object
	 * 
	 * @return int
	 */
	public int getWidth() {
		return bottomCoral.getWidth(null);
	}

	/**
	 * Method to obtain the height of the BottomPipe object
	 * 
	 * @return int
	 */
	public int getHeight() {
		return bottomCoral.getHeight(null);
	}

	/**
	 * Method to set the x location of the BottomCoral object
	 * 
	 * @param x
	 */
	public void setX(int x) {
		xLoc = x;
	}

	/**
	 * Method to get the x location of the BottomCoral object
	 * 
	 * @return int
	 */
	public int getX() {
		return xLoc;
	}

	/**
	 * Method to set the y location of the BottomCoral object
	 * 
	 * @param y
	 */
	public void setY(int y) {
		yLoc = y;
	}

	/**
	 * Method to get the y location of the BottomCoral object
	 * 
	 * @return int
	 */
	public int getY() {
		return yLoc;
	}

	/**
	 * Method used to acquire a Rectangle that outlines the BottomCoral's image
	 * 
	 * @return Rectangle outlining the BottomCoral's position on screen
	 */
	public Rectangle getRectangle() {
		return (new Rectangle(xLoc, yLoc, bottomCoral.getWidth(null), bottomCoral.getHeight(null)));
	}

	/**
	 * Method to acquire a BufferedImage that represents the TopCoral's image object
	 * 
	 * @return TopCoral's BufferedImage object
	 */
	public BufferedImage getBI() {
        BufferedImage bi = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        if(bottomCoral.getWidth(null) > 0 && bottomCoral.getHeight(null) > 0) {
            bi = new BufferedImage(bottomCoral.getWidth(null), bottomCoral.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
        }
		Graphics g = bi.getGraphics();
		g.drawImage(bottomCoral, 0, 0, null);
		g.dispose();
		return bi;
	}
}