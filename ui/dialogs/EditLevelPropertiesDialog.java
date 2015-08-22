// File: EditConditionDialog.java
// Summary: A dialog to create the wining and losing Condition. 

package ui.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import base.LevelData;
import ui.panels.PropertiesPanel;

public class EditLevelPropertiesDialog extends JDialog {
	private JButton okButton;
	private JButton cancelButton;
	private PropertiesPanel propertiesPanel;
	
	private int optionChoosed;
	
	public final static int OK_OPTION = 1;
	public final static int CANCEL_OPTION = 2;
	
	private LevelData levelData;
		
	public EditLevelPropertiesDialog(Frame frame, LevelData levelData) {
		super(frame, true);
		
		setSize(225, 325);
		setTitle("Edit Level Properties");
		
		initComponents();
		registerListeners();
		
		setOptionChoosed(CANCEL_OPTION);
		setLevelData(levelData);
	}
	
	public int getOptionChoosed() {
		return optionChoosed;
	}
	
	private void setOptionChoosed(int optionChoosed) {
		this.optionChoosed = optionChoosed;
	}
	
	public LevelData getLevelData() {
		return levelData;
	}
	
	private void setLevelData(LevelData levelData) {
		this.levelData = levelData;
		propertiesPanel.getProperties(this.levelData);
	}
	
	private void initComponents() {
		this.setLocationRelativeTo(null);
		
		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);
		
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		propertiesPanel = new PropertiesPanel();
				
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		centerPanel.add(propertiesPanel);
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.validate();
	}
	
	private void registerListeners() {
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOptionChoosed(OK_OPTION);
				setVisible(false);
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOptionChoosed(CANCEL_OPTION);
				setVisible(false);
			}
		});
	}
}