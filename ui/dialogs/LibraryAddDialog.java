// File: LibraryAddDialog.java
// Summary: A dialog for adding items to the library.

package ui.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;

import java.io.File;

import ui.panels.canvas.GraphicMetadata;

public class LibraryAddDialog extends JDialog {
	private JButton okButton;
	private JButton cancelButton;
	private JRadioButton imageRadioButton;
	private JRadioButton triggerRadioButton;
	private JRadioButton spriteRadioButton;
	private JPanel additionalOptionsPanel;
	private JLabel additionalOptionsLabel;
	
	private File directory;
	
	private int optionChoosed;
	
	public final static int OK_OPTION = 1;
	public final static int CANCEL_OPTION = 2;
	
	private GraphicMetadata newItem;
	
	// Controls that appear when imageRadioButton is selected.
	private JTextField imagePathTextField;
	private JButton browseButton;
	
	// Controls that appear when triggerRadioButton or spriteRadioButon
	// are selected.
	private JTextField widthTextField;
	private JTextField heightTextField;
	
	// Controls that appear when spriteRadioButton is selected.
	private JTextField classTextField;
	
	public LibraryAddDialog(Frame frame) {
		super(frame, true);
		
		setSize(360, 240);
		setTitle("Add New Object");
		
		initComponents();
		registerListeners();
		
		setOptionChoosed(CANCEL_OPTION);
		setNewItem(null);
	}
	
	public int getOptionChoosed() {
		return optionChoosed;
	}
	
	private void setOptionChoosed(int optionChoosed) {
		this.optionChoosed = optionChoosed;
	}
	
	public GraphicMetadata getNewItem() {
		return newItem;
	}
	
	public void setNewItem(GraphicMetadata newItem) {
		this.newItem = newItem;
	}
	
	public String getDirectory() {
		return directory.getAbsolutePath();
	}
	
	public void setDirectory(String directoryPath) {
		directory = new File(directoryPath);
	}
	
	private void initComponents() {
		this.setLocationRelativeTo(null);
		
		imageRadioButton = new JRadioButton("Image");
		triggerRadioButton = new JRadioButton("Trigger");
		spriteRadioButton = new JRadioButton("Sprite"); 
		
		ButtonGroup radioGroup = new ButtonGroup();
		
		radioGroup.add(imageRadioButton);
		radioGroup.add(triggerRadioButton);
		radioGroup.add(spriteRadioButton);
		
		JPanel radioButtonsPanel = new JPanel();
		
		radioButtonsPanel.setLayout(new BoxLayout(
			radioButtonsPanel, BoxLayout.Y_AXIS));
		
		radioButtonsPanel.setBorder(
			BorderFactory.createTitledBorder("Type"));
		
		radioButtonsPanel.add(imageRadioButton);
		radioButtonsPanel.add(triggerRadioButton);
		radioButtonsPanel.add(spriteRadioButton);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		topPanel.add(radioButtonsPanel);
		
		additionalOptionsLabel = new JLabel("Choose an option from above");
		additionalOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		additionalOptionsPanel.add(additionalOptionsLabel);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		centerPanel.add(topPanel);
		centerPanel.add(additionalOptionsPanel);
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);
		
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		// Initialize controls that appear as the radio buttons are selected.
		imagePathTextField = new JTextField(20);
		browseButton = new JButton("Browse");
		
		widthTextField = new JTextField(5);
		heightTextField = new JTextField(5);
		
		classTextField = new JTextField(15);
	}
	
	private void addClassAndDimensionsComponents() {
		additionalOptionsPanel.removeAll();
		
		additionalOptionsLabel.setText(
			"Specify the class and dimension for the new object");
			
		JLabel classLabel = new JLabel("Class");
		
		JPanel classPanel = new JPanel();
		classPanel.setLayout(
			new BoxLayout(classPanel, BoxLayout.X_AXIS));
		
		classPanel.add(classLabel);
		classPanel.add(classTextField);
		
		JLabel widthLabel = new JLabel("Width");
		JLabel heightLabel = new JLabel("Height");
		
		JPanel widthHeightPanel = new JPanel();
		widthHeightPanel.setLayout(
			new BoxLayout(widthHeightPanel, BoxLayout.X_AXIS));
		
		widthHeightPanel.add(widthLabel);
		widthHeightPanel.add(widthTextField);
		widthHeightPanel.add(Box.createHorizontalGlue());
		widthHeightPanel.add(heightLabel);
		widthHeightPanel.add(heightTextField);
		
		JPanel classAndDimensionsPanel = new JPanel();
		classAndDimensionsPanel.setLayout(
			new BoxLayout(
				classAndDimensionsPanel,
				BoxLayout.Y_AXIS));
		
		classAndDimensionsPanel.add(classPanel);
		classAndDimensionsPanel.add(widthHeightPanel);
		
		additionalOptionsPanel.add(additionalOptionsLabel);
		additionalOptionsPanel.add(classAndDimensionsPanel);
		
		additionalOptionsPanel.validate();
		setSize(360, 260);
		repaint();
	}
	
	private void registerListeners() {
		imageRadioButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (imageRadioButton.isSelected()) {
					additionalOptionsPanel.removeAll();
										
					additionalOptionsLabel.setText(
						"Choose the image for the new object");
					
					JPanel browseImagePanel = new JPanel();
					browseImagePanel.setLayout(
						new BoxLayout(browseImagePanel, BoxLayout.X_AXIS));
					
					browseImagePanel.add(imagePathTextField);
					browseImagePanel.add(browseButton);
					
					additionalOptionsPanel.add(additionalOptionsLabel);
					additionalOptionsPanel.add(browseImagePanel);
					
					additionalOptionsPanel.validate();
					setSize(360, 240);
					repaint();
				}
			}
		});
		
		triggerRadioButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (triggerRadioButton.isSelected()) {
					addClassAndDimensionsComponents();
				}
			}
		});
		
		spriteRadioButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (spriteRadioButton.isSelected()) {
					addClassAndDimensionsComponents();
				}
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (imageRadioButton.isSelected()) {
					if (!imagePathTextField.getText().equals("")) {
						setNewItem(new GraphicMetadata(
							"Image",
							"Image",
							imagePathTextField.getText(),
							0, 0));
						
						setOptionChoosed(OK_OPTION);
						setVisible(false);
					}
					else {
						JOptionPane.showMessageDialog(
							LibraryAddDialog.this,
							"An image must be choosen.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					}
				}
				else {
					if (!classTextField.getText().equals("") &&
						!widthTextField.getText().equals("") &&
						!heightTextField.getText().equals("")) {
						
						try {
							String type = triggerRadioButton.isSelected() ?
								"Trigger" : "Sprite";
							
							setNewItem(new GraphicMetadata(
								type,
								classTextField.getText(),
								"images/no_preview_icon.png",
								Integer.parseInt(widthTextField.getText()),
								Integer.parseInt(heightTextField.getText())));
							
							setOptionChoosed(OK_OPTION);
							setVisible(false);
						} catch (NumberFormatException numberFormatException) {
							JOptionPane.showMessageDialog(
								LibraryAddDialog.this,
								"Width and Height must be whole numbers.",
								"Error",
								JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(
							LibraryAddDialog.this,
							"All the fields must be filled.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setOptionChoosed(CANCEL_OPTION);
				setVisible(false);
			}
		});
		
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				fileChooser.setFileFilter(new FileFilter() {
					public boolean accept(File file) {
						return file.getName().endsWith(".gif")  ||
						       file.getName().endsWith(".jpg")  ||
						       file.getName().endsWith(".jpeg") ||
						       file.getName().endsWith(".png")  ||
						       file.isDirectory();
					}
			
					public String getDescription() {
						return "Any image (.gif, .jpg, .jpeg, .png)";
					}
				});
				
				if (getDirectory() != null) {
					fileChooser.setCurrentDirectory(new File(getDirectory()));
				}
				
				int option = fileChooser.showOpenDialog(LibraryAddDialog.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					File imageFile = fileChooser.getSelectedFile();
					setDirectory(fileChooser.getCurrentDirectory().toString());
					imagePathTextField.setText(imageFile.getAbsolutePath());
				}
			}
		});
	}
}