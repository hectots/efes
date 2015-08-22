// File: SelectTool.java
// Summary: A tool for selecting and moving objects on a canvas.

package ui.tools;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import ui.panels.*;
import ui.panels.canvas.*;

public class SelectTool extends Tool {
	private int selectedGraphicXWhenClicked;
	private int selectedGraphicYWhenClicked;
	private int mouseXOnSelectedWhenClicked;
	private int mouseYOnSelectedWhenClicked;
	
	public SelectTool(CanvasPanel cp) {
		super(cp);
	}
	
	public void mousePressed(MouseEvent e) {
		CanvasPanel cp = getCanvas();
		
		boolean found = false;
		String[] layers = cp.getLayers();
		ListIterator<String> layerIterator = 
			Arrays.asList(layers).listIterator(layers.length);
		while (layerIterator.hasPrevious()) {
			String layer = layerIterator.previous();
			
			Graphic[] graphics = cp.getGraphicsByLayer(layer);
			ListIterator<Graphic> graphicsIterator =
				Arrays.asList(graphics).listIterator(graphics.length);
			
			while (graphicsIterator.hasPrevious()) {
				Graphic graphic = graphicsIterator.previous();
				int mouseX = e.getX();
				int mouseY = e.getY();
				
				if (graphic.onBounds(mouseX, mouseY)) {
					if (cp.getSelectedGraphic() != null) {
						cp.getSelectedGraphic().setSelected(false);
					}
					
					cp.setSelectedGraphic(graphic);
					cp.getSelectedGraphic().setSelected(true);
					
					found = true;
					
					selectedGraphicXWhenClicked = cp.getSelectedGraphic().getX();
					selectedGraphicYWhenClicked = cp.getSelectedGraphic().getY(); 
					mouseXOnSelectedWhenClicked = mouseX;
					mouseYOnSelectedWhenClicked = mouseY;
					
					cp.setSelectedGraphicLayer(layer);
					
					break;
				}
			}
			
			if (found) {
				break;
			}
		}
		
		if (!found) {
			if (cp.getSelectedGraphic() != null) {
				cp.getSelectedGraphic().setSelected(false);
			}
			
			cp.setSelectedGraphic(null);
		}
		
		cp.repaint();
	}
	
	public void mouseDragged(MouseEvent e) {
		CanvasPanel cp = getCanvas();
		
		if (cp.getSelectedGraphic() != null) {
			int newMouseX = selectedGraphicXWhenClicked + e.getX() - mouseXOnSelectedWhenClicked;
			int newMouseY = selectedGraphicYWhenClicked + e.getY() - mouseYOnSelectedWhenClicked;
			
			if (cp.isSnapToGrid()) {
				newMouseX = newMouseX - newMouseX % cp.getGridSize().width;
				newMouseY = newMouseY - newMouseY % cp.getGridSize().height;
			}
			
			cp.getSelectedGraphic().setX(newMouseX);
			cp.getSelectedGraphic().setY(newMouseY);

			cp.repaint();
		}
	}
}