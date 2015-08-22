// File: EditGridDialog.java
// Summary: A dialog to change the size of the grid.

package ui.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class EditGridDialog extends JDialog {
	private JButton okButton;
	private JButton cancelButton;
	private JPanel editPanel;
	private JLabel instructionsLabel;
	private JSpinner gridSizeSpinner;
	
	private int optionChoosed;
	
	public final static int OK_OPTION = 1;
	public final static int CANCEL_OPTION = 2;
	
	private int initialValue;
	private Dimension gridSize;
	
	public EditGridDialog(Frame frame, int initialValue) {
		super(frame, true);
		
		setSize(250, 120);
		setTitle("Select Grid Size");
		
		this.initialValue = initialValue;
		
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
	
	public Dimension getGridSize() {
		return gridSize;
	}
	
	private void setGridSize(Dimension gridSize) {
		this.gridSize = gridSize;
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
		
		gridSizeSpinner = new JSpinner(new SpinnerNumberModel(initialValue, 5, 100, 1));
		
		instructionsLabel = new JLabel();
		instructionsLabel.setText("Specify the size of the grid");
		
		JLabel levelNameLabel = new JLabel("Grid Size ");
		
		JPanel editPanel = new JPanel();
		editPanel.setLayout(
			new BoxLayout(editPanel, BoxLayout.X_AXIS));
		
		editPanel.add(gridSizeSpinner);
		
		JPanel newEditPanel = new JPanel();
		newEditPanel.setLayout(
			new BoxLayout(
				newEditPanel,
				BoxLayout.Y_AXIS));
		
		newEditPanel.add(editPanel);
		
		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		centerPanel.add(instructionsLabel);
		centerPanel.add(newEditPanel);
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		centerPanel.validate();
	}
	
	private void registerListeners() {
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int value = (Integer)gridSizeSpinner.getValue();
				setGridSize(new Dimension(value, value));
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