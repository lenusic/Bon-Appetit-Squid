import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Squid {
	// global variables
	private Image limberSquid;
	private int xLoc = 0, yLoc = 0;

	/**
	 * Default constructor
	 */
	public Squid(int initialWidth, int initialHeight) {
		limberSquid = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/squiddy.png"));
		scaleSquid(initialWidth, initialHeight);
	}

	/**
	 * Method to scale the squid sprite into the desired dimensions
	 * 
	 * @param width  The desired width of the limber squid
	 * @param height The desired height of the limber squid
	 */
	public void scaleSquid(int width, int height) {
		limberSquid = limberSquid.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

	/**
	 * Getter method for the limberSquid object.
	 * 
	 * @return Image
	 */
	public Image getSquid() {
		return limberSquid;
	}

	/**
	 * Method to obtain the width of the Squid object
	 * 
	 * @return int
	 */
	public int getWidth() {
		try {
			return limberSquid.getWidth(null);
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
			return limberSquid.getHeight(null);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Method to set the x location of the Squid object
	 * 
	 * @param x
	 */
	public void setX(int x) {
		xLoc = x;
	}

	/**
	 * Method to get the x location of the Squid object
	 * 
	 * @return int
	 */
	public int getX() {
		return xLoc;
	}

	/**
	 * Method to set the y location of the Squid object
	 * 
	 * @param y
	 */
	public void setY(int y) {
		yLoc = y;
	}

	/**
	 * Method to get the y location of the Squid object
	 * 
	 * @return int
	 */
	public int getY() {
		return yLoc;
	}

	/**
	 * Method used to acquire a Rectangle that outlines the Squid's image
	 * 
	 * @return Rectangle outlining the Squid's position on screen
	 */
	public Rectangle getRectangle() {
		return (new Rectangle(xLoc, yLoc, limberSquid.getWidth(null), limberSquid.getHeight(null)));
	}

	/**
	 * Method to acquire a BufferedImage that represents the Squid's image object
	 * 
	 * @return Squid's BufferedImage object
	 */
	public BufferedImage getBI() {
		BufferedImage bi = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        if(limberSquid.getWidth(null) > 0 && limberSquid.getHeight(null) > 0) {
            bi = new BufferedImage(limberSquid.getWidth(null), limberSquid.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
        }
		Graphics g = bi.getGraphics();
		g.drawImage(limberSquid, 0, 0, null);
		g.dispose();
		return bi;
	}
}