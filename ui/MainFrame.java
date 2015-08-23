// File: MainFrame.java
// Summary: Application's main window.

package ui;

import java.io.File;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import ui.panels.*;
import ui.panels.canvas.*;
import ui.dialogs.NewLevelDialog;
import ui.dialogs.EditLevelPropertiesDialog;
import ui.dialogs.EditGridDialog;

import base.LevelData;

import io.*;
import io.formats.*;

public class MainFrame extends JFrame {
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem exportMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem editLevelPropertiesItem;
	private JMenuItem gridColorViewItem;
	private JMenuItem gridSizeViewItem;
	private JMenuItem sendToBackItem;
	private JMenuItem sendToFrontItem;
	private JCheckBoxMenuItem gridViewItem;
	private JCheckBoxMenuItem gridSnapItem;
	private JPanel canvasContainerPanel;
	// private JPanel canvasContainerInnerPanel;
	JTabbedPane topTabbedPane;
	private CanvasPanel canvasPanel;
	private LibraryPanel libraryPanel;
	private LayersPanel layersPanel;
	private PropertiesPanel propertiesPanel;
	private ToolsPanel toolsPanel;
	
	private File directory;
	private File saveFilePath;
	private final LevelFormat DEFAULT_FORMAT = new XmlLevelFormat();
	
	private LevelData levelData;
	
	private final Dimension CANVAS_DEFAULT_SIZE = new Dimension(600, 600);
	
	public MainFrame() {
		super("Level Editor");
		
		setSize(1024, 768);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLevelData(new LevelData(
			"Untitled",
			CANVAS_DEFAULT_SIZE.width,
			CANVAS_DEFAULT_SIZE.height));
		
		initComponents();
		registerListeners();
		
		setVisible(true);

		newLevel();
	}
	
	private LevelData getLevelData() {
		return levelData;
	}
	
	private void setLevelData(LevelData levelData) {
		this.levelData = levelData;
	}
	
	private String getSaveFilePath() {
		if (saveFilePath != null) {
			return saveFilePath.getAbsolutePath();	
		}
		else return null;
	}
	
	private void setSaveFilePath(String saveFilePath) {
		if (saveFilePath != "") {
			this.saveFilePath = new File(saveFilePath);
		}
		else this.saveFilePath = null;
	}
	
	public String getDirectory() {
		if (directory != null) {
			return directory.getAbsolutePath();	
		}
		else return null;
	}
	
	public void setDirectory(String directoryPath) {
		directory = new File(directoryPath);
	}

	private void newLevel() {
		NewLevelDialog newDialog = new NewLevelDialog(null);
		newDialog.setVisible(true);
		if (newDialog.getOptionChoosed() == newDialog.OK_OPTION) {
			int newWidth = newDialog.getLevelWidth();
			int newHeight = newDialog.getLevelHeight();
			Dimension canvasSize = new Dimension(newWidth, newHeight);
			
			setSaveFilePath("");
			
			setLevelData(new LevelData(
				newDialog.getLevelName(),
				newWidth,
				newHeight));
			
			// Reset everything to default
			gridViewItem.setSelected(false);
			gridSnapItem.setSelected(false);
			replaceCanvas(new CanvasPanel(canvasSize));
			layersPanel.clearListModel();
			libraryPanel.clearLibraryItems();
		}
	}
	
	private void openLevel() {
		LevelReader levelReader = new LevelReader();
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		if (getDirectory() != null) {
			fileChooser.setCurrentDirectory(new File(getDirectory()));
		}
		
		int option = fileChooser.showOpenDialog(MainFrame.this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File openFile = fileChooser.getSelectedFile();
			setDirectory(fileChooser.getCurrentDirectory().toString());
			setSaveFilePath(openFile.getAbsolutePath());
			
			levelReader.read(openFile);
			
			libraryPanel = levelReader.getLibraryPanel();
			layersPanel = levelReader.getLayersPanel();
			
			replaceCanvas(levelReader.getCanvasPanel());
			setLevelData(levelReader.getLevelData());
			
			topTabbedPane.removeAll();
			topTabbedPane.addTab("Library", libraryPanel);
			topTabbedPane.addTab("Layers", layersPanel);
			topTabbedPane.validate();
		}
		
	}
	
	private int saveLevel() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		if (getDirectory() != null) {
			fileChooser.setCurrentDirectory(new File(getDirectory()));
		}
		if (getSaveFilePath() == null) {
			int option = fileChooser.showSaveDialog(MainFrame.this);
			if (option == JFileChooser.APPROVE_OPTION) {
				File saveFile = fileChooser.getSelectedFile();
				
				if (!saveFile.getName().endsWith(".efes")) {
					saveFile = new File(saveFile.getAbsolutePath() + ".efes");
				}
				
				setSaveFilePath(saveFile.getAbsolutePath());
				
				ProjectWriter.write(saveFile, levelData, canvasPanel, libraryPanel);
				return option;
			}
		}
		if (getSaveFilePath() != null) {
			ProjectWriter.write(new File(getSaveFilePath()), levelData, canvasPanel, libraryPanel);
		}
		return 1;
	}
	
	private int saveAsLevel() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		if (getDirectory() != null) {
			fileChooser.setCurrentDirectory(new File(getDirectory()));
		}
		
		int option = fileChooser.showSaveDialog(MainFrame.this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File saveFile = fileChooser.getSelectedFile();
		
			if (!saveFile.getName().endsWith(".efes")) {
				saveFile = new File(saveFile.getAbsolutePath() + ".efes");
			}
		
			setSaveFilePath(saveFile.getAbsolutePath());
		
			ProjectWriter.write(saveFile, levelData, canvasPanel, libraryPanel);
			return option;
		}
		return option;
	}
	
	private void export() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Flash XML", "fxml"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Lua XML", "lxml"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON", "json"));
		
		if (getDirectory() != null) {
			fileChooser.setCurrentDirectory(new File(getDirectory()));
		}
		
		int option = fileChooser.showSaveDialog(MainFrame.this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File saveFile = fileChooser.getSelectedFile();
			FileNameExtensionFilter fileFilter = (FileNameExtensionFilter)fileChooser.getFileFilter();
			String[] acceptableFileExtensions = fileFilter.getExtensions();

			boolean hasAppropiateExtension = false;
			for (int i = 0; i != acceptableFileExtensions.length; i++) {
				String extension = acceptableFileExtensions[i];

				if (saveFile.getName().endsWith(extension)) {
					hasAppropiateExtension = true;
				}
			}

			if (!hasAppropiateExtension) {
				saveFile = new File(saveFile.getAbsolutePath()
					+ "."
					+ acceptableFileExtensions[0]);
			}

			LevelFormat levelFormat = new XmlLevelFormat();

			if (acceptableFileExtensions[0].equals("fxml")) {
				levelFormat = new FlashXmlLevelFormat();
			} else if (acceptableFileExtensions[0].equals("lxml")) {
				levelFormat = new LuaXmlLevelFormat();
			} else if (acceptableFileExtensions[0].equals("json")) {
				levelFormat = new JsonLevelFormat();
			}
		
			LevelWriter.write(saveFile, levelFormat, levelData, canvasPanel);
		}
	}
	
	public void showColorDialog() {
			JColorChooser colorChooser = new JColorChooser();
			Color color = colorChooser.showDialog(this, "Grid Color Selection", canvasPanel.getGridColor());
			
			if (color != null) {
				canvasPanel.setGridColor(color);	
			}
		}
	
	private void replaceCanvas(CanvasPanel canvasPanel) {
		canvasContainerPanel.removeAll();
		
		this.canvasPanel = canvasPanel;
		
		JPanel canvasInnerContainerPanel = new JPanel();
		GridBagConstraints constraints = new GridBagConstraints();
		
		canvasInnerContainerPanel.setLayout(new GridBagLayout());
		canvasInnerContainerPanel.add(canvasPanel, constraints);
		
		canvasContainerPanel.add(new JScrollPane(canvasInnerContainerPanel));
				
		this.canvasPanel.setPropertiesPanel(propertiesPanel);
		layersPanel.setCanvasPanel(this.canvasPanel);
		toolsPanel.setCanvasPanel(this.canvasPanel);
		toolsPanel.setLibraryPanel(this.libraryPanel);
		
		canvasContainerPanel.validate();
		repaint();
	}
	
	private void initComponents() {
		this.setLocationRelativeTo(null); 
		
		propertiesPanel = new PropertiesPanel();
		canvasPanel = new CanvasPanel(CANVAS_DEFAULT_SIZE, propertiesPanel);
		layersPanel = new LayersPanel(canvasPanel);
		libraryPanel = new LibraryPanel();
		toolsPanel = new ToolsPanel(canvasPanel, libraryPanel);
		
		JPanel canvasInnerContainerPanel = new JPanel();
		GridBagConstraints constraints = new GridBagConstraints();
		
		canvasInnerContainerPanel.setLayout(new GridBagLayout());
		canvasInnerContainerPanel.add(canvasPanel, constraints);
		
		canvasContainerPanel = new JPanel(new BorderLayout());
		canvasContainerPanel.add(new JScrollPane(canvasInnerContainerPanel));
		
		topTabbedPane = new JTabbedPane();
		topTabbedPane.addTab("Library", libraryPanel);
		topTabbedPane.addTab("Layers", layersPanel);
		
		JTabbedPane bottomTabbedPane = new JTabbedPane();
		bottomTabbedPane.addTab("Properties", propertiesPanel);
		
		JSplitPane sideBarSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		sideBarSplitPane.setTopComponent(topTabbedPane);
		sideBarSplitPane.setBottomComponent(bottomTabbedPane);
		sideBarSplitPane.setDividerLocation(this.getHeight() - this.getHeight()/2);
		
		JPanel sideBarPanel = new JPanel(new BorderLayout());
		sideBarPanel.add(sideBarSplitPane);
		sideBarPanel.setPreferredSize(new Dimension(
			this.getWidth()/4,
			this.getHeight()));
		
		JSplitPane mainSplitPane = new JSplitPane();
		mainSplitPane.setLeftComponent(canvasContainerPanel);
		mainSplitPane.setRightComponent(sideBarPanel);
		mainSplitPane.setDividerLocation(this.getWidth() - this.getWidth()/4);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(mainSplitPane);
		
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(toolsPanel, BorderLayout.WEST);
		
		newMenuItem = new JMenuItem("New");
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		openMenuItem = new JMenuItem("Open");
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		saveAsMenuItem = new JMenuItem("Save As");
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, (InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		exportMenuItem = new JMenuItem("Export");
		exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		exitMenuItem = new JMenuItem("Close");
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		editLevelPropertiesItem = new JMenuItem("Edit Level Properties");
		gridViewItem = new JCheckBoxMenuItem("Show Grid");
		gridSnapItem = new JCheckBoxMenuItem("Snap to Grid");
		gridSizeViewItem = new JMenuItem("Change Grid Size");
		gridColorViewItem = new JMenuItem("Change Grid Color");
		sendToBackItem = new JMenuItem("Send To Back");
		sendToBackItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		sendToFrontItem = new JMenuItem("Send To Front");
		sendToFrontItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(exportMenuItem);
		fileMenu.add(exitMenuItem);
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.add(editLevelPropertiesItem);
		
		JMenu viewMenu = new JMenu("View");
		JMenu gridMenu = new JMenu("Grid");
		JMenu arrangeMenu = new JMenu("Arrange");
		gridMenu.add(gridViewItem);
		gridMenu.add(gridSnapItem);
		gridMenu.add(gridSizeViewItem);
		gridMenu.add(gridColorViewItem);
		arrangeMenu.add(sendToBackItem);
		arrangeMenu.add(sendToFrontItem);
		viewMenu.add(gridMenu);
		viewMenu.add(arrangeMenu);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		
		this.setJMenuBar(menuBar);
	}
	
	private void registerListeners() {
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newLevel();
			}
		});
		
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openLevel();
				gridViewItem.setSelected(false);
			}
		});
		
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveLevel();
			}
		});
		
		saveAsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAsLevel();
			}
		});
		
		exportMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		
		exitMenuItem.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				int optionChoosed = JOptionPane.showConfirmDialog(
						MainFrame.this,
						"Do you want to save changes?",
						"Exit",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (optionChoosed == JOptionPane.YES_OPTION) {
					if (saveLevel() == JFileChooser.APPROVE_OPTION) {
						System.exit(0);
					}
				}
				else if (optionChoosed == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
			}
		});
		
		editLevelPropertiesItem.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				EditLevelPropertiesDialog EditLevelPropertiesDialog = new EditLevelPropertiesDialog(null, levelData);
				EditLevelPropertiesDialog.setVisible(true);
				if (EditLevelPropertiesDialog.getOptionChoosed() == EditLevelPropertiesDialog.OK_OPTION) {
					// 
				}
			}
		});
		
		gridViewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvasPanel.setGridAvailability(gridViewItem.getState());
				repaint();
			}
		});
		
		gridSnapItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvasPanel.setSnapToGrid(gridViewItem.getState());
				repaint();
			}
		});
		
		gridSizeViewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditGridDialog editGridDialog = new EditGridDialog(null, canvasPanel.getGridSize().width);
				editGridDialog.setVisible(true);
				if (editGridDialog.getOptionChoosed() == EditGridDialog.OK_OPTION) {
					canvasPanel.setGridSize(editGridDialog.getGridSize());
					canvasPanel.repaint();
				}
			}
		});
		
		gridColorViewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showColorDialog();
				canvasPanel.repaint();
			}
		});
		
		sendToBackItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvasPanel.moveUpSelected();
			}
		});
		
		sendToFrontItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvasPanel.moveDownSelected();
			}
		});
	}
}
