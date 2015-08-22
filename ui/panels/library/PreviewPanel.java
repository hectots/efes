// File: PreviewPanel.java
// Summary: A label that shows a preview of an image item in the library.

package ui.panels.library;

import java.awt.*;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PreviewPanel extends JPanel {
	private Image imagePreview;
	
	public PreviewPanel(String imagePath) {
		setImage(imagePath);
	}
	
	public PreviewPanel() {
		imagePreview = null;
	}
	
	public Image getImage() {
		return imagePreview;
	}
	
	public void setImage(String imagePath) {
		File imageFile = new File(imagePath);
		
		if (imageFile.exists()) {
			try {
				imagePreview = ImageIO.read(imageFile);
			} catch (IOException ioException) {
				System.err.printf("Error reading image %s",
					imageFile.getAbsolutePath());
				System.exit(1);
			}
		}
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (getImage() != null) {
			Graphics2D g2d = (Graphics2D)g;
		
			int width = getPreferredSize().width;
			int height = getPreferredSize().height;
			int imageWidth = getImage().getWidth(this);
			int imageHeight = getImage().getHeight(this);
			int centerX = width/2;
			int centerY = height/2;
			double percent = 0;
			
			if (imageWidth > width || imageHeight > height){
				if (imageHeight >= imageWidth){
					percent = (double)height/imageHeight;
				}
				else{
					percent = (double)width/imageWidth;
				}
				imageWidth *= percent;
				imageHeight *= percent;
			}
			
			g2d.drawImage(getImage(), centerX - imageWidth/2, centerY - imageHeight/2, imageWidth, imageHeight, null);
		}
	}
}