// File: Graphic.java
// Summary: Abstract base class for all viewable object in the Canvas.

package ui.panels.canvas;

import java.awt.Graphics2D;

import base.*;

public abstract class Graphic {
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean selected;
	private ObjectData objectData;
	
	public Graphic(ObjectData objectData) {
		setObjectData(objectData);
	}
	
	public ObjectData getObjectData() {
		return objectData;
	}
	
	public void setObjectData(ObjectData objectData) {
		this.objectData = objectData;
	}
	
	public int getX() {
		return x;
	}
	
	public void	setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean onBounds(int x, int y) {
		boolean between_x_and_width = x >= this.getX() && x <=  this.getX() + this.getWidth();
		boolean between_y_and_height = y >= this.getY() && y <=  this.getY() + this.getHeight();
		
		return between_x_and_width && between_y_and_height;
	}
	
	public abstract void draw(Graphics2D g2d);
}