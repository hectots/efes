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
	private PropertiesPanel propertiesPanel;

	private Tool activeTool;
	
	public ToolsPanel(CanvasPanel cp, LibraryPanel lp, PropertiesPanel pp) {
		setCanvasPanel(cp);
		setLibraryPanel(lp);
		setPropertiesPanel(pp);
		
		initComponents();
		switchToSelectTool();
	}

	public Tool getActiveTool() {
		return activeTool;
	}
	
	public void setCanvasPanel(CanvasPanel cp) {
		canvasPanel  = cp;
	}
	
	public void setLibraryPanel(LibraryPanel lp) {
		libraryPanel = lp;
	}

	public void setPropertiesPanel(PropertiesPanel pp) {
		propertiesPanel = pp;
	}

	public void switchToSelectTool() {
		activeTool = new SelectTool(canvasPanel, propertiesPanel);
		canvasPanel.setTool(activeTool);
	}

	public void switchToBrushTool() {
		activeTool = new BrushTool(canvasPanel, libraryPanel);
		canvasPanel.setTool(activeTool);
	}
	
	private void initComponents() {
		JPanel toolsContainerPanel = new JPanel();
		toolsContainerPanel.setLayout(new GridLayout(2, 1));
		
		JToggleButton selectToolButton = new JToggleButton(new ImageIcon("images/selection_tool.png"));
		selectToolButton.setSelected(true);

		selectToolButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				switchToSelectTool();
			}
		});
		
		JToggleButton brushToolButton = new JToggleButton(new ImageIcon("images/brush_tool.png"));
		brushToolButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				switchToBrushTool();
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