// File: XmlLevelFormat.java
// Summary: Formats level files in XML.

package io.formats;

import base.*;
import ui.panels.CanvasPanel;
import ui.panels.canvas.Graphic;

public class XmlLevelFormat implements LevelFormat {
	// Format options
	protected String rootElement;
	protected String layerElement;
	protected String objectElement;
	
	public XmlLevelFormat(String root, String layer, String object) {
		rootElement   = root;
		layerElement  = layer;
		objectElement = object;
	}
	
	public XmlLevelFormat() {
		this("level", "layer", "object");
	}
	
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
			additionalLevelProperties += property.getName() + "=\"" + property.getValue() + "\" ";
		}
		additionalLevelProperties = additionalLevelProperties.substring(0, additionalLevelProperties.length());
		
		levelHeader = String.format("<%s name=\"%s\" width=\"%d\" height=\"%d\"%s>\n",
			rootElement, levelData.getName(), levelData.getWidth(), levelData.getHeight(), additionalLevelProperties);
		
		return levelHeader;
	}
	
	protected String writeBody(CanvasPanel canvasPanel) {
		String levelBody = "";
		
		String[] layers = canvasPanel.getLayers();
		if (layers != null) {
			for (String layer : layers) {
				if (!layer.equals("")) {
					levelBody += String.format("\t<%s name=\"%s\">\n", layerElement, layer);
			
					Graphic[] graphics = canvasPanel.getGraphicsByLayer(layer);
					if (graphics != null) {
						for (Graphic graphic : graphics) {
							ObjectData objectData = graphic.getObjectData();
							levelBody += this.writeObjectData(objectData);
						}
					}
					
					levelBody += String.format("\t</" + layerElement + ">\n");
				}
			}
		}
		
		return levelBody;
	}
	
	protected String writeObjectData(ObjectData objectData) {
		String object;
		
		object = String.format("\t\t<%s class=\"%s\">\n", objectElement, objectData.getObjectClass());
		for (Property property : objectData.getProperties()) {
			object += this.writeProperty(property, "\t\t\t");
		}
		object += "\t\t</" + objectElement +  ">\n";
		
		return object;
	}
	
	protected String writeProperty(Property property, String indent) {
		if (property instanceof CompositeProperty) {
			CompositeProperty compositeProperty = (CompositeProperty)property;
			String properties;
			
			properties = String.format("%s<%s>\n", indent, compositeProperty.getName());
			for (Property innerProperty : compositeProperty.getProperties()) {
				properties += this.writeProperty(innerProperty, indent + "\t");
			}
			properties += String.format("%s</%s>\n", indent, compositeProperty.getName());
			
			return properties;
		}
		
		return String.format("%s<%s>%s</%s>\n",
			indent, property.getName(), property.getValue(), property.getName());
	}
	
	protected String endLevel() {
		return "</" + rootElement + ">";
	}
}