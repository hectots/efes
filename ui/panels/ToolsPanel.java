// File: ToolsPanel.java
// Summary: Contains the tool palette.

package ui.panels;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import ui.tools.*;

public class ToolsPanel extends JPanel {
	// Panels
	private CanvasPanel canvasPanel;
	private LibraryPanel libraryPanel;
	
	public ToolsPanel(CanvasPanel cp, LibraryPanel lp) {
		setCanvasPanel(cp);
		setLibraryPanel(lp);
		
		initComponents();
	}
	
	public void setCanvasPanel(CanvasPanel cp) {
		canvasPanel  = cp;
	}
	
	public void setLibraryPanel(LibraryPanel lp) {
		libraryPanel = lp;
	}
	
	private void initComponents() {
		JPanel toolsContainerPanel = new JPanel();
		toolsContainerPanel.setLayout(new GridLayout(2, 1));
		
		JToggleButton selectToolButton = new JToggleButton(new ImageIcon("images/move.png"));
		selectToolButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				canvasPanel.setTool(new SelectTool(canvasPanel));
			}
		});
		
		JToggleButton brushToolButton = new JToggleButton(new ImageIcon("images/brush.png"));
		brushToolButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				canvasPanel.setTool(new BrushTool(canvasPanel, libraryPanel));
			}
		});
		
		ButtonGroup toolsButtonGroup = new ButtonGroup();
		toolsButtonGroup.add(selectToolButton);
		toolsButtonGroup.add(brushToolButton);
				
		toolsContainerPanel.add(selectToolButton);
		toolsContainerPanel.add(brushToolButton);
		
		this.add(toolsContainerPanel);
	}
}