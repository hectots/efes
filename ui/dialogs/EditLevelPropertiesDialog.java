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
	private JTextField levelTextField;
	private JTextField widthTextField;
	private JTextField heightTextField;

	private JButton okButton;
	private JButton cancelButton;
	private PropertiesPanel propertiesPanel;
	
	private int optionChoosed;

	private boolean changedLevelSize;
	
	public final static int OK_OPTION = 1;
	public final static int CANCEL_OPTION = 2;
	
	private LevelData levelData;
		
	public EditLevelPropertiesDialog(Frame frame, LevelData levelData) {
		super(frame, true);
		
		setSize(280, 385);
		setTitle("Edit Level Properties");

		setLevelData(levelData);
		
		initComponents();
		registerListeners();
		
		setOptionChoosed(CANCEL_OPTION);
	}

	public boolean getChangedLevelSize() {
		return changedLevelSize;
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
	}
	
	private void initComponents() {
		this.setLocationRelativeTo(null);

		levelTextField = new JTextField(15);
		widthTextField = new JTextField(5);
		heightTextField = new JTextField(5);

		levelTextField.setText(levelData.getName());
		widthTextField.setText(String.valueOf(levelData.getWidth()));
		heightTextField.setText(String.valueOf(levelData.getHeight()));
		
		JLabel levelNameLabel = new JLabel("Level Name ");
		
		JPanel levelPanel = new JPanel();
		levelPanel.setLayout(
			new BoxLayout(levelPanel, BoxLayout.X_AXIS));
		
		levelPanel.add(levelNameLabel);
		levelPanel.add(levelTextField);
		
		JLabel widthLabel = new JLabel("Width ");
		JLabel heightLabel = new JLabel("Height ");
		
		JPanel widthHeightPanel = new JPanel();
		widthHeightPanel.setLayout(
			new BoxLayout(widthHeightPanel, BoxLayout.X_AXIS));
		
		widthHeightPanel.add(widthLabel);
		widthHeightPanel.add(widthTextField);
		widthHeightPanel.add(Box.createHorizontalGlue());
		widthHeightPanel.add(heightLabel);
		widthHeightPanel.add(heightTextField);
		
		JPanel editLevelPanel = new JPanel();
		editLevelPanel.setLayout(
			new BoxLayout(
				editLevelPanel,
				BoxLayout.Y_AXIS));
		
		editLevelPanel.add(levelPanel);
		editLevelPanel.add(widthHeightPanel);
		
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		topPanel.add(editLevelPanel);

		this.add(topPanel, BorderLayout.NORTH);
		
		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);
		
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		propertiesPanel = new PropertiesPanel();
		propertiesPanel.getProperties(this.levelData);
				
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		centerPanel.add(propertiesPanel);
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.validate();
	}
	
	private void registerListeners() {
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String levelName = levelData.getName();
				String levelWidth = String.valueOf(levelData.getWidth());
				String levelHeight = String.valueOf(levelData.getHeight());

				if (!levelName.equals(levelTextField.getText())) {
					levelData.setName(levelTextField.getText());
				} else if (!levelWidth.equals(widthTextField.getText()) ||
						!levelHeight.equals(heightTextField.getText())) {
					changedLevelSize = true;

					int newWidth = Integer.parseInt(widthTextField.getText());
					int newHeight = Integer.parseInt(heightTextField.getText());

					levelData.setWidth(newWidth);
					levelData.setHeight(newHeight);
				} else {
					changedLevelSize = false;
				}

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