// File: FlashXmlLevelFormat.java
// Summary: Format level files in an XML idiom suitable for Flash games.

package io.formats;

import java.io.File;

import base.*;
import ui.panels.CanvasPanel;
import ui.panels.canvas.Graphic;

public class FlashXmlLevelFormat extends XmlLevelFormat {
	public FlashXmlLevelFormat() {
		super("view", "layer", "entity");
	}
	
	protected String writeObjectData(ObjectData objectData) {
		String object;
		
		if (objectData.hasProperty("image")) {
			CompositeProperty imageProperty = (CompositeProperty) objectData.getProperty("image");
			
			String name = getNameFromPath(imageProperty.getProperty("filename").getValue());
						
			object  = String.format("\t\t<%s name=\"%s\">\n", objectElement, name);
			object += String.format("\t\t\t<x>%s</x>\n", imageProperty.getProperty("x"));
			object += String.format("\t\t\t<y>%s</y>\n", imageProperty.getProperty("y"));
		} else {
			object = String.format("\t\t<%s name=\"%s\">\n", objectElement, objectData.getObjectClass());
		}
		
		object += "\t\t</" + objectElement +  ">\n";
		
		return object;
	}
	
	private String getNameFromPath(String path) {
		String filename = new File(path).getName();
		
		// Strip the extension
		filename = filename.substring(0, filename.lastIndexOf('.'));
		
		// Convert to camel case
		
		// First convert to upper case the first letter
		filename = filename.substring(0, 1).toUpperCase() + filename.substring(1, filename.length());
		
		// Then remove every underscore and convert the character next to it to upper case
		int underscore = filename.indexOf("_");
		while (underscore != -1) {
			int afterUnderscore = underscore + 1;
			if (afterUnderscore < filename.length()) {
				filename = filename.substring(0, underscore)
					+ filename.substring(afterUnderscore, afterUnderscore + 1).toUpperCase()
					+ filename.substring(afterUnderscore + 1, filename.length());
			}
			
			underscore = filename.indexOf("_");
		}
		
		return filename;
	}
}