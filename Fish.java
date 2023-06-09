import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class Fish {
	private Image fish;
	private int xLoc = 0, yLoc = 0;
	
	public Fish(int initialWidth, int initialHeight) {
		fish = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/fish.png"));
		scaleFish(initialWidth, initialHeight);
	}
	
	public void scaleFish(int width, int height) {
		fish = fish.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
	
	public Image getCoral() {
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
		BufferedImage bi = new BufferedImage(fish.getWidth(null), fish.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.drawImage(fish, 0, 0, null);
		g.dispose();
		return bi;
	}
}
