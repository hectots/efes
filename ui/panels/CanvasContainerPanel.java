// File: CanvasContainerPanel.java
// Summary: A panel that contains the canvas panel.
//          It implements the Scrollable interface in order to scroll
//          according to the canvas dimensions.

package ui.panels;

import java.awt.*;
import javax.swing.*;

public class CanvasContainerPanel extends JPanel implements Scrollable {
	private int canvasWidth;
	private int canvasHeight;
	
	public Component add(Component c) {
		super.add(c);
		
		setCanvasSize(c.getWidth(), c.getHeight());
		
		return c;
	}
	
	public void setCanvasSize(int width, int height) {
		canvasWidth = width;
		canvasHeight = height;
	}
	
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(canvasWidth, canvasHeight);
	}
	
	public int getScrollableUnitIncrement(
		Rectangle visibleRect, int orientation, int direction) {
		return 1;
	}
	
	public int getScrollableBlockIncrement(
		Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}
	
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
}