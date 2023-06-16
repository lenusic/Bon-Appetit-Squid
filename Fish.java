import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class Fish {
	private Image fish;
	private int xLoc = 0, yLoc = 0;
	public boolean isVisible;
	
	public Fish(int initialWidth, int initialHeight, String image) {
		fish = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(image));
		scaleFish(initialWidth, initialHeight);
		isVisible = true;
	}
	
	public void scaleFish(int width, int height) {
		fish = fish.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	
	public Image getFish() {
		return fish;
	}

	public int getWidth() {
		return fish.getWidth(null);
	}

	public int getHeight() {
		return fish.getHeight(null);
	}

	public void setX(int x) {
		xLoc = x;
	}

	public int getX() {
		return xLoc;
	}

	public void setY(int y) {
		yLoc = y;
	}

	public int getY() {
		return yLoc;
	}

	public Rectangle getRectangle() {
		return (new Rectangle(xLoc, yLoc, fish.getWidth(null), fish.getHeight(null)));
	}

	public BufferedImage getBI() {
		BufferedImage bi = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        if(fish.getWidth(null) > 0 && fish.getHeight(null) > 0) {
            bi = new BufferedImage(fish.getWidth(null), fish.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
        }
		Graphics g = bi.getGraphics();
		g.drawImage(fish, 0, 0, null);
		g.dispose();
		return bi;
	}
}
