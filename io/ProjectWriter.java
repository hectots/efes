// File: ProjectWriter.java
// Summary: Writes a project file, which is a zip file with the project's assets and the level file.

package io;

import java.io.*;

import base.*;
import ui.panels.CanvasPanel;
import ui.panels.LibraryPanel;

import io.formats.ProjectXmlLevelFormat;

public class ProjectWriter {
	private ProjectWriter() {}
	
	public static void write(
		File file, LevelData levelData, CanvasPanel canvasPanel, LibraryPanel libraryPanel) {
		
		try {
			FileWriter writer = new FileWriter(file);
			ProjectXmlLevelFormat format = new ProjectXmlLevelFormat();
			writer.write(format.encode(levelData, canvasPanel, libraryPanel));
			writer.close();
		} catch (IOException ioException) {
			System.err.printf("Error writting to file %s\n",
				file.getAbsolutePath());
			// System.exit(1);
		} catch (Exception exception) {
			exception.printStackTrace();
			System.exit(1);
		}
	}
}