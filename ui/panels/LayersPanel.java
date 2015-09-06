// File: LayersPanel.java
// Summary: Manage layer creation and arrangement.

package ui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class LayersPanel extends JPanel {
	private JButton addButton;
	private JButton removeButton;
	private JButton upButton;
	private JButton downButton;
	private JList layersList;
	private DefaultListModel layersListModel;
	private CanvasPanel canvasPanel;
	private boolean available;
	
	public LayersPanel(CanvasPanel canvasPanel) {
		super(new BorderLayout());
		
		setCanvasPanel(canvasPanel);
		
		initComponents();
		registerListeners();
	}

	private void initComponents() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		
		addButton = new JButton(new ImageIcon("images/add_layer.png"));
		removeButton = new JButton(new ImageIcon("images/remove_layer.png"));
		upButton = new JButton(new ImageIcon("images/layer_up.png"));
		downButton = new JButton(new ImageIcon("images/layer_down.png"));
		
		layersListModel = new DefaultListModel();
		layersList = new JList(layersListModel);
		layersList.setDragEnabled(true);
		layersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		bottomPanel.add(addButton);
		bottomPanel.add(removeButton);
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(upButton);
		bottomPanel.add(downButton);
		
		
		this.add(new JScrollPane(layersList), BorderLayout.CENTER);
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		this.addLayer("default", true);
		getCanvasPanel().setLayer("default");
	}
	
	public CanvasPanel getCanvasPanel() {
		return canvasPanel;
	}
	
	public void clearListModel() {
		layersListModel.clear();
	}
	
	public void setCanvasPanel(CanvasPanel canvasPanel) {
		this.canvasPanel = canvasPanel;
	}
	
	public boolean getLayerAvailability() {
		return available;
	}
	
	public void setLayerAvailability(boolean available) {
		this.available = available;
	}
	
	private void checkLayerAvailability() {
		if (layersListModel.size() == 0) {
			setLayerAvailability(false);
		}
		else {
			setLayerAvailability(true);
		}
	}
	
	// Return true if the layer was added, false otherwise.
	public boolean addLayer(String layerName, boolean addToCanvas) {
		if (!layerName.equals("") && !layersListModel.contains(layerName)) {
			layersListModel.add(0, layerName);
			
			if (addToCanvas) {
				getCanvasPanel().addLayer(layerName);
			}
			
			layersList.setSelectedIndex(0);
			checkLayerAvailability();
			
			return true;
		}
		
		return false;
	}
	
	private void layersSwap(int firstLayer, int secondLayer) {
		if (firstLayer >=0 && firstLayer <= layersListModel.size() - 1 &&
			secondLayer >= 0 && secondLayer <= layersListModel.size() - 1) {
			
			String oldFirstLayer = layersListModel.get(firstLayer).toString();
			layersListModel.set(firstLayer, layersListModel.get(secondLayer));
			layersListModel.set(secondLayer, oldFirstLayer);
			
			layersList.setSelectedIndex(secondLayer);
		
			getCanvasPanel().swapLayers(
				layersListModel.get(firstLayer).toString(),
				layersListModel.get(secondLayer).toString());
		}
	}
	
	private void registerListeners() {
		addButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				String layerName = JOptionPane.showInputDialog(
					null,
					"Enter layer name",
					"New Layer",
					JOptionPane.QUESTION_MESSAGE);
				if (layerName != null) {
					boolean added = addLayer(layerName, true);
					
					if (!added) {
						JOptionPane.showMessageDialog(
								LayersPanel.this,
								"The layer names can't be empty or repeated.",
								"Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = layersList.getSelectedIndex();
				
				if (selectedIndex != -1) {
					getCanvasPanel().removeLayer(
						layersList.getSelectedValue().toString());
					
					layersListModel.remove(selectedIndex);
				}
				checkLayerAvailability();
			}
		});
		
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layersSwap(
					layersList.getSelectedIndex(),
					layersList.getSelectedIndex() - 1);
			}
		});
		
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layersSwap(
					layersList.getSelectedIndex(),
					layersList.getSelectedIndex() + 1);
			}
		});
		
		layersList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (layersList.getSelectedIndex() != -1) {
					getCanvasPanel().setLayer(
						layersList.getSelectedValue().toString());
				}
			}
		});
	}
}