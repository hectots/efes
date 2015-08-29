// File: Tool.java
// Summary: Abstract class of a tool that works on the canvas.

package ui.tools;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import ui.panels.*;
import ui.panels.canvas.*;

public abstract class Tool implements 
	MouseListener, MouseMotionListener, KeyListener {
		
	private CanvasPanel canvasPanel;
		
	public Tool(CanvasPanel cp) {
		setCanvas(cp);
	}
	
	public Tool() {
		this(null);
	}
	
	public CanvasPanel getCanvas() {
		return canvasPanel;
	}
	
	public void setCanvas(CanvasPanel cp) {
		canvasPanel = cp;
				
		if (canvasPanel != null) {
			canvasPanel.addMouseListener(this);
			canvasPanel.addMouseMotionListener(this);
			canvasPanel.addKeyListener(this);
		}
	}
	
	public void unsetCanvas() {
		if (canvasPanel != null) {
			canvasPanel.removeMouseListener(this);
			canvasPanel.removeMouseMotionListener(this);
			canvasPanel.removeKeyListener(this);
		}
	}
	
	// Subclasses should implement at least one of the methods below
	
	// MouseListener Interface
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	// MouseMotionListener
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	
	// KeyListener
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	// Draw
	public void draw(Graphics2D g2d) {}
}