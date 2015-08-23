// File: JsonLevelFormat.java
// Summary: Formats level files in JSON.

package io.formats;

import base.*;
import ui.panels.CanvasPanel;
import ui.panels.canvas.Graphic;

public class JsonLevelFormat implements LevelFormat {
	public JsonLevelFormat() {}
	
	public String encode(LevelData levelData, CanvasPanel canvasPanel) {
		return writeLevel(levelData, canvasPanel);
	}
	
	protected String writeLevel(
		LevelData levelData, CanvasPanel canvasPanel) {
		
		String level;
		
		level  = this.startLevel(levelData);
		level += this.writeBody(canvasPanel);
		level += this.endLevel();
		
		return level;
	}
	
	protected String startLevel(LevelData levelData) {
		String levelHeader;
		
		String additionalLevelProperties = " ";
		for (Property property : levelData.getProperties()) {
			additionalLevelProperties += "\n\t\"" + property.getName() + "\" : \"" + property.getValue() + "\",";
		}
		additionalLevelProperties = additionalLevelProperties.substring(0, additionalLevelProperties.length());
		
		levelHeader = String.format("{\n\t\"name\" : \"%s\",\n\t\"width\" : \"%d\",\n\t\"height\" : \"%d\",%s\n",
			levelData.getName(), levelData.getWidth(), levelData.getHeight(), additionalLevelProperties);
		
		return levelHeader;
	}
	
	protected String writeBody(CanvasPanel canvasPanel) {
		String levelBody = "";
		
		String[] layers = canvasPanel.getLayers();
		if (layers != null) {
			levelBody += "\t\"layers\" : {\n";
			for (String layer : layers) {
				if (!layer.equals("")) {
					levelBody += String.format("\t\t\"%s\" : [", layer);
			
					Graphic[] graphics = canvasPanel.getGraphicsByLayer(layer);
					if (graphics != null) {
						for (Graphic graphic : graphics) {
							ObjectData objectData = graphic.getObjectData();
							levelBody += this.writeObjectData(objectData);
						}
					}
					
					levelBody += String.format("\t\t],\n");
				}
			}
			levelBody += "\t}\n";
		}
		
		return levelBody;
	}
	
	protected String writeObjectData(ObjectData objectData) {
		String object;
		
		object = String.format("\n\t\t\t{\n\t\t\t\t\"type\" : \"%s\",", objectData.getObjectClass());
		for (Property property : objectData.getProperties()) {
			object += this.writeProperty(property, "\t\t\t\t");
		}
		object += "\n\t\t\t},\n";
		
		return object;
	}
	
	protected String writeProperty(Property property, String indent) {
		if (property instanceof CompositeProperty) {
			CompositeProperty compositeProperty = (CompositeProperty)property;
			String properties;
			
			properties = String.format("\n%s\"%s\" : {", indent, compositeProperty.getName());
			for (Property innerProperty : compositeProperty.getProperties()) {
				properties += this.writeProperty(innerProperty, indent + "\t");
			}
			properties += String.format("\n%s},\n", indent, compositeProperty.getName());
			
			return properties;
		}
		
		return String.format("\n%s\"%s\" : \"%s\",",
			indent, property.getName(), property.getValue());
	}
	
	protected String endLevel() {
		return "}";
	}
}