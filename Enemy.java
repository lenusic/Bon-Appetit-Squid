import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
public class Enemy {
    
	private Image enemy;
	private int xLoc = 0, yLoc = 0;
	public boolean isVisible;
	
	public Enemy(int initialWidth, int initialHeight, String image) {
		enemy = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(image));
		scaleEnemy(initialWidth, initialHeight);
		isVisible = true;
	}
	
	public void scaleEnemy(int width, int height) {
	    enemy = enemy.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	
	public Image getEnemy() {
		return enemy;
	}

	public int getWidth() {
		return enemy.getWidth(null);
	}

	public int getHeight() {
		return enemy.getHeight(null);
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
		return (new Rectangle(xLoc, yLoc, enemy.getWidth(null), enemy.getHeight(null)));
	}

	public BufferedImage getBI() {
		BufferedImage bi = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        if(enemy.getWidth(null) > 0 && enemy.getHeight(null) > 0) {
            bi = new BufferedImage(enemy.getWidth(null), enemy.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
        }
		Graphics g = bi.getGraphics();
		g.drawImage(enemy, 0, 0, null);
		g.dispose();
		return bi;
	}

}

