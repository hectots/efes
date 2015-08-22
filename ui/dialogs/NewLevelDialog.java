// File: NewLevelDialog.java
// Summary: A dialog to start the creation of a new level.

package ui.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class NewLevelDialog extends JDialog {
	private JButton okButton;
	private JButton cancelButton;
	private JLabel instructionsLabel;
	
	private int optionChoosed;
	
	public final static int OK_OPTION = 1;
	public final static int CANCEL_OPTION = 2;
	
	private String levelName;
	private int levelWidth;
	private int levelHeight;
	
	private JTextField levelTextField;
	private JTextField widthTextField;
	private JTextField heightTextField;
	
	public NewLevelDialog(Frame frame) {
		super(frame, true);
		
		setSize(340, 140);
		setTitle("New Level");
		
		initComponents();
		registerListeners();
		
		setOptionChoosed(CANCEL_OPTION);
	}
	
	public int getOptionChoosed() {
		return optionChoosed;
	}
	
	private void setOptionChoosed(int optionChoosed) {
		this.optionChoosed = optionChoosed;
	}
	
	public String getLevelName(){
		return levelName;
	}
	
	private void setLevelName(String levelName){
		this.levelName = levelName;
	}
	
	public int getLevelWidth(){
		return levelWidth;
	}
	
	private void setLevelWidth(int levelWidth){
		this.levelWidth = levelWidth;
	}
	
	public int getLevelHeight(){
		return levelHeight;
	}
	
	private void setLevelHeight(int levelHeight){
		this.levelHeight = levelHeight;
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
		
		levelTextField = new JTextField(15);
		widthTextField = new JTextField(5);
		heightTextField = new JTextField(5);
		
		instructionsLabel = new JLabel();
		instructionsLabel.setText("Specify the name and dimension of the new level");
		
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
		
		JPanel newLevelPanel = new JPanel();
		newLevelPanel.setLayout(
			new BoxLayout(
				newLevelPanel,
				BoxLayout.Y_AXIS));
		
		newLevelPanel.add(levelPanel);
		newLevelPanel.add(widthHeightPanel);
		
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		centerPanel.add(instructionsLabel);
		centerPanel.add(newLevelPanel);
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.validate();
	}
	
	private void registerListeners() {
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!levelTextField.getText().toString().equals("") &&
					!widthTextField.getText().toString().equals("") &&
					!heightTextField.getText().toString().equals("")){
						
					try {
						setLevelName(levelTextField.getText().toString());
						setLevelWidth(Integer.parseInt(widthTextField.getText().toString()));
						setLevelHeight(Integer.parseInt(heightTextField.getText().toString()));
						setOptionChoosed(OK_OPTION);
						setVisible(false);
					} catch (NumberFormatException numberFormatException) {
						JOptionPane.showMessageDialog(
						NewLevelDialog.this,
						"Width and Height must be whole numbers.",
						"Error",
						JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					JOptionPane.showMessageDialog(
						NewLevelDialog.this,
						"All the fields must be filled.",
						"Error",
						JOptionPane.ERROR_MESSAGE);
				}
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