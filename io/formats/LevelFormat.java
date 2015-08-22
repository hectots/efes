// File: LevelFormat.java
// Summary: Interface for all level file formats.

package io.formats;

import base.LevelData;
import ui.panels.CanvasPanel;

public interface LevelFormat {
	public String encode(LevelData levelData, CanvasPanel canvasPanel);
}