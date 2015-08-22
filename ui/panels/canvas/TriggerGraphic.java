// File: TriggerGraphic.java
// Summary: Class that represents a trigger object.

package ui.panels.canvas;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import base.*;

public class TriggerGraphic extends NoImageGraphic {
	public final Color BOUNDING_BOX_COLOR = new Color(255, 0, 0);
	
	public TriggerGraphic(
		ObjectData objectData, String cls, int width, int height) {
		
		super(objectData, cls, width, height);
	}
	
	public void setX(int x) {
		super.setX(x);
		
		CompositeProperty geometryProperty = 
			(CompositeProperty)getObjectData().getProperty("geometry");
		Property xProperty = 
			geometryProperty.getProperty("x");
		
		xProperty.setValue(String.format("%s", x));
	}
	
	public void setY(int y) {
		super.setY(y);
		
		CompositeProperty geometryProperty = 
			(CompositeProperty)getObjectData().getProperty("geometry");
		Property yProperty = 
			geometryProperty.getProperty("y");
		
		yProperty.setValue(String.format("%s", y));
	}
	
	public void setWidth(int width) {
		super.setWidth(width);
		
		CompositeProperty geometryProperty = 
			(CompositeProperty)getObjectData().getProperty("geometry");
		Property widthProperty = 
			geometryProperty.getProperty("width");
		
		widthProperty.setValue(String.format("%s", width));
	}
	
	public void setHeight(int height) {
		super.setHeight(height);
		
		CompositeProperty geometryProperty = 
			(CompositeProperty)getObjectData().getProperty("geometry");
		Property heightProperty = 
			geometryProperty.getProperty("height");
		
		heightProperty.setValue(String.format("%s", height));
	}
	
	public Color getBoundingBoxColor() {
		return BOUNDING_BOX_COLOR;
	}
	
	public void setBoundingBoxColor(Color color) {}
}