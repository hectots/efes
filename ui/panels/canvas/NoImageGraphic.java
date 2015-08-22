// File: NoImageGraphic.java
// Summary: Class that represents a trigger object.

package ui.panels.canvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import base.*;

public abstract class NoImageGraphic extends Graphic {
	private String objectClass;
		private final Color BOUNDING_BOX_COLOR = new Color(0, 0, 255);
	
	public NoImageGraphic(
		ObjectData objectData, String cls, int width, int height) {
		
		super(objectData);
		
		setObjectClass(cls);
		setWidth(width);
		setHeight(height);
	}
	
	public String getObjectClass() {
		return objectClass;
	}
	
	public void setObjectClass(String cls) {
		objectClass = cls;
	}
	
	public void drawBoundingBox(Graphics2D g2d) {
		g2d.setColor(BOUNDING_BOX_COLOR);
		g2d.drawRect(getX(), getY(), getWidth(), getHeight());
	}
	
	public void draw(Graphics2D g2d) {
		g2d.setColor(getBoundingBoxColor());
		g2d.drawRect(getX(), getY(), getWidth(), getHeight());
		
		Font font = g2d.getFont();
		Rectangle2D bounds = font.getStringBounds(
			getObjectClass(), g2d.getFontRenderContext());
		
		int stringX = (int)(getWidth()/2 - bounds.getWidth()/2) + getX();
		int stringY = (int)(getHeight()/2 - bounds.getHeight()/2) + getY();
		
		g2d.drawString(getObjectClass(), stringX, stringY);
		
		if (isSelected()) {
			drawBoundingBox(g2d);
			g2d.drawString(getObjectClass(), stringX, stringY);
		}
	}
	
	public abstract Color getBoundingBoxColor();
	public abstract void setBoundingBoxColor(Color color);
}