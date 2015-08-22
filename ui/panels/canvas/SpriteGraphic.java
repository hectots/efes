// File: SpriteGraphic.java
// Summary: Class that represents a trigger object.

package ui.panels.canvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import base.*;

public class SpriteGraphic extends NoImageGraphic {
	public final Color BOUNDING_BOX_COLOR = new Color(0, 255, 0);
	
	public SpriteGraphic(
		ObjectData objectData, String cls, int width, int height) {
		
		super(objectData, cls, width, height);
	}
	
	public void setX(int x) {
		super.setX(x);
		
		Property xProperty = getObjectData().getProperty("x");
		xProperty.setValue(String.format("%d", x));
	}
	
	public void setY(int y) {
		super.setY(y);
		
		Property yProperty = getObjectData().getProperty("y");
		yProperty.setValue(String.format("%d", y));
	}
	
	public Color getBoundingBoxColor() {
		return BOUNDING_BOX_COLOR;
	}
	
	public void setBoundingBoxColor(Color color) {}
}