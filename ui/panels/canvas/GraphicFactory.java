// File: GraphicFactory.java
// Summary: Manages the instantiation of Graphic instances.

package ui.panels.canvas;

import java.io.File;

import base.*;
import ui.panels.canvas.*;

public abstract class GraphicFactory {
	public static Graphic createGraphic(GraphicMetadata item) {
		String type = item.getGraphicType();
		
		if (type.equals("Image")) {
			return createImageGraphic(item);
		}
		else if (type.equals("Trigger")) {
			return createTriggerGraphic(item);
		}
		else if (type.equals("Sprite")) {
			return createSpriteGraphic(item);
		}
		
		return null;
	}
	
	private static Graphic createImageGraphic(GraphicMetadata item) {
		String itemClass = item.getGraphicClass();
		String itemImage = item.getImage();
		ObjectData objectData = new ObjectData(itemClass);
		
		// Populate object data with the corresponding properties.
		CompositeProperty imageProperty = new CompositeProperty("image");
		
		imageProperty.addProperty(new Property("filename", itemImage));
		imageProperty.addProperty(new Property("x", "0"));
		imageProperty.addProperty(new Property("y", "0"));
		
		objectData.addProperty(imageProperty);
		
		return new ImageGraphic(objectData, item.getImage());
	}
	
	private static Graphic createTriggerGraphic(GraphicMetadata item) {
		String itemClass = item.getGraphicClass();
		ObjectData objectData = new ObjectData(itemClass);
		
		// Populate object data with the corresponding properties.
		CompositeProperty geometryProperty = new CompositeProperty("geometry");
		
		geometryProperty.addProperty(new Property("x", "0"));
		geometryProperty.addProperty(new Property("y", "0"));
		
		geometryProperty.addProperty(
			new Property("width", String.format("%d", item.getWidth())));
		geometryProperty.addProperty(
			new Property("height", String.format("%d", item.getHeight())));
		
		objectData.addProperty(geometryProperty);
		
		return new TriggerGraphic(
			objectData, item.getGraphicClass(), item.getWidth(), item.getHeight());
	}
	
	private static Graphic createSpriteGraphic(GraphicMetadata item) {
		String itemClass = item.getGraphicClass();
		ObjectData objectData = new ObjectData(itemClass);
		
		objectData.addProperty(new Property("x", "0"));
		objectData.addProperty(new Property("y", "0"));
		
		return new SpriteGraphic(
			objectData, item.getGraphicClass(), item.getWidth(), item.getHeight());
	}
}