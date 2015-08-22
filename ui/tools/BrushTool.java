// File: BrushTool.java
// Summary: A tool for painting objects on a canvas.

package ui.tools;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import ui.panels.*;
import ui.panels.canvas.*;

public class BrushTool extends Tool {
	private Graphic brush;
	
	// Dimensions and coordinates of the last drawn graphic
	private int lastW;
	private int lastH;
	private int lastX;
	private int lastY;
	
	private LibraryPanel libraryPanel;
	
	public BrushTool(CanvasPanel cp, LibraryPanel lp) {
		super(cp);
		setBrush(null);
		setLibraryPanel(lp);
		
		lastW = 0;
		lastH = 0;
		lastX = 0;
		lastY = 0;
	}
	
	public Graphic getBrush() {
		return brush;
	}
	
	public void setBrush(Graphic g) {
		brush = g;
	}
	
	public LibraryPanel getLibraryPanel() {
		return libraryPanel;
	}
	
	public void setLibraryPanel(LibraryPanel lp) {
		libraryPanel = lp;
	}
	
	public void addGraphicToCanvas(int x, int y) {
		GraphicMetadata selectedItem = libraryPanel.getSelectedItem();
		
		if (selectedItem != null) {
			brush = GraphicFactory.createGraphic(selectedItem);
			
			lastW = brush.getWidth();
			lastH = brush.getHeight();
			lastX = x - lastW/2;
			lastY = y - lastH/2;
			x     = lastX;
			y     = lastY;
			
			CanvasPanel cp = getCanvas();
			
			if (cp.isSnapToGrid()) {
				x = x - x % cp.getGridSize().width;
				y = y - y % cp.getGridSize().height;
			}
			
			brush.setX(x);
			brush.setY(y);
			
			cp.addGraphic(brush);
		}
	}
	
	public void mousePressed(MouseEvent e) {
		addGraphicToCanvas(e.getX(), e.getY());
	}
	
	public void mouseDragged(MouseEvent e) {
		// Add the graphics next to each other as the user
		// drags the mouse
		
		// Return if the pointer is inside the last drawn graphic...
		if (e.getX() >= lastX && e.getX() <= lastX + lastW &&
			e.getY() >= lastY && e.getY() <= lastY + lastH)
		{
			return;
		}
		
		// The graphics are added by their middle point
		// so to determine the new point you have to add
		// half the width and and half the height to the
		// calculations
		
		int newX = lastX + lastW/2;
		int newY = lastY + lastH/2;
		
		if (e.getX() < lastX) {
			newX = lastX - lastW - 1 + lastW/2;
		} else if (e.getX() > lastX + lastW) {
			newX = lastX + lastW + 1 + lastW/2;
		}
		
		if (e.getY() < lastY) {
			newY = lastY - lastH - 1 + lastH/2;
		} else if (e.getY() > lastY + lastH) {
			newY = lastY + lastH + 1 + lastH/2;
		}
		
		addGraphicToCanvas(newX, newY);
	}
}