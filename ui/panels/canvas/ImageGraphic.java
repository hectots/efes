// File: ImageGraphic.java
// Summary: Class that represents an image object.

package ui.panels.canvas;

import java.io.File;
import java.io.IOException;

import java.awt.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import base.*;

public class ImageGraphic extends Graphic {
	private Image image;
	private final Color BOUNDING_BOX_COLOR = new Color(0, 0, 255);
	private final Color OVERLAY_COLOR = new Color(0, 152, 255, 64);
	
	public ImageGraphic(ObjectData objectData, String imagePath) {
		super(objectData);
		setImage(imagePath);
		setWidth(image.getWidth(null));
		setHeight(image.getHeight(null));
	}
	
	// Overrided to set the image->x property as well.
	public void setX(int x) {
		super.setX(x);
		
		CompositeProperty imageProperty = (CompositeProperty)getObjectData().getProperty("image");
		Property xProperty = imageProperty.getProperty("x");
		
		xProperty.setValue(String.format("%d", x));
	}
	
	// Overrided to set the image->y property as well.
	public void setY(int y) {
		super.setY(y);
		
		CompositeProperty imageProperty = (CompositeProperty)getObjectData().getProperty("image");
		Property yProperty = imageProperty.getProperty("y");
		
		yProperty.setValue(String.format("%d", y));
	}
	
	public Image getImage() {
		return image;
	}
	
	private void setImage(String imagePath) {
		File imageFile = new File(imagePath);
		
		if (imageFile.exists()) {
			try {
				this.image = ImageIO.read(imageFile);
			} catch (IOException ioException) {
				System.err.printf("Error reading image %s",
					imageFile.getAbsolutePath());
				System.exit(1);
			}
		}
	}
	
	public void drawBoundingBox(Graphics2D g2d) {
		g2d.setColor(BOUNDING_BOX_COLOR);
		g2d.drawRect(getX(), getY(), getWidth(), getHeight());
		g2d.setColor(OVERLAY_COLOR);
		g2d.fillRect(getX(), getY(), getWidth(), getHeight());
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(getImage(), getX(), getY(), null);
		
		if (isSelected()) {
			drawBoundingBox(g2d);
		}
	}
}