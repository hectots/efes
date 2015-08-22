// File: LevelWriter.java
// Summary: Writes a level into a XML formatted file.

package io;

import java.io.*;

import base.*;
import ui.panels.CanvasPanel;
import ui.panels.canvas.Graphic;

import io.formats.LevelFormat;

public class LevelWriter {
	private LevelWriter() {}
	
	public static void write(
		File file, LevelFormat format, LevelData levelData, CanvasPanel canvasPanel) {
		
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(format.encode(levelData, canvasPanel));
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