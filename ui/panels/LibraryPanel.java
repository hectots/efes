// File: LibraryPanel.java
// Summary: Manage collection of objects.

package ui.panels;

import java.util.List;
import java.util.ArrayList;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;

import ui.dialogs.LibraryAddDialog;
import ui.panels.LayersPanel;
import ui.panels.library.*;
import ui.panels.canvas.*;

public class LibraryPanel extends JPanel {
	private JButton addButton;
	private JButton removeButton;
	private PreviewPanel previewPanel;
	private JPanel itemListPanel;
	private JList itemsList;
	private DefaultListModel itemsListModel;
	private boolean empty;
	
	private ArrayList<GraphicMetadata> libraryItems;
	
	private String directory;
	
	public LibraryPanel() {
		super(new BorderLayout());
		
		libraryItems = new ArrayList<GraphicMetadata>();
		empty = true;
		directory = System.getProperty("user.home");
		
		initComponents();
		registerListeners();
	}
	
	private void initComponents() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		
		addButton = new JButton(new ImageIcon("images/plus.png"));
		removeButton = new JButton(new ImageIcon("images/minus.png"));
		
		bottomPanel.add(addButton);
		bottomPanel.add(removeButton);
		
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		previewPanel = new PreviewPanel();
		previewPanel.setPreferredSize(new Dimension(
			160, 160));
		previewPanel.setImage("images/no_preview_icon.png");
		
		itemsListModel = new DefaultListModel();
		itemsList = new JList(itemsListModel);
		itemsList.setDragEnabled(true);
		itemsList.setTransferHandler(new LibraryTransferHandler(libraryItems));
		
		itemListPanel = new JPanel(new BorderLayout());
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		centerPanel.add(previewPanel);
		centerPanel.add(itemListPanel);
		
		JPanel flowLayoutPanel = new JPanel();
		flowLayoutPanel.add(centerPanel);
		
		this.add(flowLayoutPanel, BorderLayout.CENTER);
	}
	
	private void registerListeners() {
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LibraryAddDialog addDialog = new LibraryAddDialog(null);
				boolean repeated = false;
				addDialog.setDirectory(directory);
				addDialog.setVisible(true);
				
				if (addDialog.getOptionChoosed() == LibraryAddDialog.OK_OPTION) {
					GraphicMetadata newItem = addDialog.getNewItem();
					
					boolean added = addItem(newItem);
					
					if (!added) {
						JOptionPane.showMessageDialog(
							LibraryPanel.this,
							"There is an image with that name already.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					}
					directory = addDialog.getDirectory();
				}
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!empty && itemsList.getSelectedIndex() != -1) {
					libraryItems.remove(itemsList.getSelectedIndex());
					itemsListModel.remove(itemsList.getSelectedIndex());
					
					if (libraryItems.size() == 0) {
						itemListPanel.removeAll();
						empty = !empty;
						repaint();
					}
					previewPanel.setImage("images/no_preview_icon.png");
				}
			}
		});
		
		itemsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int selectedIndex = itemsList.getSelectedIndex();
				
				if (selectedIndex != -1) {
					GraphicMetadata item = libraryItems.get(selectedIndex);
					if (item.getImage() != "") {
						previewPanel.setImage(item.getImage());	
					}
					else {
						previewPanel.setImage("images/no_preview_icon.png");
					}
				}
			}
		});
	}
	
	public GraphicMetadata getSelectedItem() {
		int selectedIndex = itemsList.getSelectedIndex();
		
		if (selectedIndex != -1) {
			return libraryItems.get(selectedIndex);
		}
		
		return null;
	}

	public List<GraphicMetadata> getItems() {
		return libraryItems;
	}
	
	public boolean addItem(GraphicMetadata newItem) {
		if (!itemsListModel.contains(newItem.toString())) {
			libraryItems.add(newItem);
			
			if (empty) {
				itemListPanel.add(new JScrollPane(
					itemsList,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
					
				empty = !empty;
			}
			
			itemsListModel.addElement(newItem.toString());
			itemsList.setSelectedIndex(libraryItems.size()-1);
			
			if (itemsListModel.getSize() == 1) {
				validate();
			}
			
			return true;
		}
		
		return false;
	}
	
	public void clearLibraryItems() {
		libraryItems.clear();
		itemsListModel.clear();
		previewPanel.setImage("images/no_preview_icon.png");
		itemListPanel.removeAll();
		empty = true;
		repaint();
	}
}