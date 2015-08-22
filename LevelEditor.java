// File: LevelEditor.java
// Summary: Entry point for Level Editor App.

import javax.swing.*;

import ui.MainFrame;

class LevelEditor {
	public static void main (String [] args)
	{
		// Mac OS X
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("apple.awt.graphics.UseQuartz", "true");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame app = new MainFrame();
			}
		});
	}
}