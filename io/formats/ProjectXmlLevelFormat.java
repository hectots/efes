// File: ProjectXmlLevelFormat.java
// Summary: Format project files.

package io.formats;

import java.util.List;

import java.io.File;

import base.*;
import ui.panels.CanvasPanel;
import ui.panels.LibraryPanel;
import ui.panels.canvas.GraphicMetadata;

public class ProjectXmlLevelFormat extends XmlLevelFormat {
	public ProjectXmlLevelFormat() {
		super("level", "layer", "object");
	}

	public String encode(LevelData levelData, CanvasPanel canvasPanel, LibraryPanel libraryPanel) {
		return writeLevel(levelData, canvasPanel, libraryPanel);
	}

	protected String writeLevel(
		LevelData levelData, CanvasPanel canvasPanel, LibraryPanel libraryPanel) {
		
		String level;
		
		level  = this.startLevel(levelData);
		level += this.writeBody(canvasPanel);
		level += this.writeLibrary(libraryPanel);
		level += this.endLevel();
		
		return level;
	}

	protected String writeLibrary(LibraryPanel libraryPanel) {
		String levelLibrary = "\t<library>";

		List<GraphicMetadata> libraryItems = libraryPanel.getItems();
		for (GraphicMetadata libraryItem : libraryItems) {
			String item = "\n\t\t<item>\n";

			item += String.format("\t\t\t<itemType>%s</itemType>\n", libraryItem.getGraphicType());
			item += String.format("\t\t\t<itemClass>%s</itemClass>\n", libraryItem.getGraphicClass());
			item += String.format("\t\t\t<image>%s</image>\n", libraryItem.getImage());
			item += String.format("\t\t\t<width>%s</width>\n", libraryItem.getWidth());
			item += String.format("\t\t\t<height>%s</height>", libraryItem.getHeight());

			item += "\n\t\t</item>";
			levelLibrary += item;
		}

		levelLibrary += "\n\t</library>\n";

		return levelLibrary;
	}
}