// File: PropertiesPanel.java
// Summary: Manage collection of libraries.

package ui.panels;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import java.util.ArrayList;

import base.*;

public class PropertiesPanel extends JPanel {
	private JButton addButton;
	private JButton removeButton;
	private PropertiesTableModel propertiesTableModel;
	private JTable propertiesTable;
	
	private Data currentData;
	
	public PropertiesPanel() {
		super(new BorderLayout());
		
		currentData = null;
		
		initComponents();
		registerListeners();
	}
	
	private void initComponents() {
		JPanel centerPanel = new JPanel();
		
		propertiesTableModel = new PropertiesTableModel();
		propertiesTable = new JTable(propertiesTableModel);
		propertiesTable.setPreferredScrollableViewportSize(
			new Dimension(200, 200));
		propertiesTable.setRowSelectionAllowed(false);
		propertiesTable.setGridColor(Color.GRAY);
		propertiesTable.setShowGrid(true);
		
		centerPanel.add(new JScrollPane(propertiesTable));
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		
		addButton = new JButton(new ImageIcon("images/add.png"));
		removeButton = new JButton(new ImageIcon("images/remove.png"));
		
		bottomPanel.add(addButton);
		bottomPanel.add(removeButton);
		
		this.add(centerPanel);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private void registerListeners() {
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (propertiesTable != null) {
					propertiesTableModel.addRow();
				}
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (propertiesTable != null) {
					int selectedIndex = propertiesTable.getSelectedRow();
					
					if (selectedIndex != -1) {
						propertiesTableModel.removeRow(selectedIndex);
					}
				}
			}
		});
	}
	
	// Get the properties from the objectData.
	public void getProperties(Data data) {
		currentData = data;
		
		propertiesTableModel.clear();
		for (Property property : data.getProperties()) {
			if (!(property instanceof CompositeProperty)) {
				if (!property.getName().equals("x") &&
					!property.getName().equals("y")) {
					
					propertiesTableModel.addRow(
						property.getName(),
						property.getValue());
				}
			}
		}
	}
	
	private class PropertiesTableModel extends AbstractTableModel {
		private int rowCount;
		private ArrayList<Object[]> rowData;
		
		private final int COLUMN_COUNT = 2;
		private final String[] COLUMN_NAMES = {"Name", "Value"};
		
		public PropertiesTableModel() {
			rowCount = 0;
			rowData = new ArrayList<Object[]>();
		}
		
		public int getRowCount() {
			return rowCount;
		}
		
		public String getColumnName(int col) {
			return COLUMN_NAMES[col];
		}
		
		public int getColumnCount() {
			return COLUMN_COUNT;
		}
		
		public Object getValueAt(int row, int col) {
			return rowData.get(row)[col];
		}
		
		public void setValueAt(Object value, int row, int col) {
			if (col == 0 && currentData != null) {
				String currentName = getValueAt(row, 0).toString();
				
				if (!currentName.equals("")) {
					Property property = currentData.getProperty(
						currentName);
			
					if (property != null) {
						String newName = value.toString();
						String propertyValue = getValueAt(row, 1).toString();
				
						currentData.removeProperty(property);
						currentData.addProperty(
							new Property(newName, propertyValue));
					}
				}
			}
			else if (col == 1 && currentData != null) {
				String name = getValueAt(row, 0).toString();
				
				if (!name.equals("")) {
					if (currentData.getProperty(name) == null) {
						currentData.addProperty(
							new Property(name, value.toString()));
					}
					else {
						currentData.getProperty(name).setValue(
							value.toString());
					}
				}
			}
			
			rowData.get(row)[col] = value;
			fireTableCellUpdated(row, col);
		}
		
		public boolean isCellEditable(int row, int col) {
			return true;
		}
		
		public void addRow(String name, String value) {
			rowCount++;
			
			rowData.add(new Object[2]);
			rowData.get(rowCount-1)[0] = name;
			rowData.get(rowCount-1)[1] = value;
			
			fireTableStructureChanged();
		}
		
		public void addRow() {
			addRow("", "");
		}
		
		public void removeRow(int row) {
			rowCount--;
			
			Property property =
				currentData.getProperty(getValueAt(row, 0).toString());
			
			if (property != null) {
				currentData.removeProperty(property);
			}
			
			rowData.remove(row);
			
			fireTableStructureChanged();
		}
		
		public void clear() {
			while (rowCount > 0) {
				removeRow(rowCount - 1);
			}
		}
	}
}