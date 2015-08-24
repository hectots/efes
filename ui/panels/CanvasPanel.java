// File: CanvasPanel.java
// Summary: Surface where graphics are drawn and manipulated.

package ui.panels;

import java.util.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;

import ui.panels.canvas.*;

import ui.tools.*;

public class CanvasPanel extends JPanel
	implements MouseListener, MouseMotionListener, KeyListener {
	
	private String currentLayer;
	private HashMap<String, ArrayList<Graphic>> graphics;
	private ArrayList<String> layers;
	
	private Graphic selectedGraphic;
	
	// private int selectedGraphicXWhenClicked;
	// private int selectedGraphicYWhenClicked;
	// private int mouseXOnSelectedWhenClicked;
	// private int mouseYOnSelectedWhenClicked;
	
	private String selectedGraphicLayer;
	
	// private int snapPointX;
	// private int snapPointY;
	
	private boolean snapToGrid;
	private Dimension gridSize;
	private Dimension viewSize;
	
	private boolean gridAvailable = false;
	private Color gridColor;
	
	private PropertiesPanel propertiesPanel;
	
	private Tool currentTool;
	
	public CanvasPanel(Dimension size, PropertiesPanel propertiesPanel) {
		super();
		setGridSize(new Dimension(10, 10));
		setSnapToGrid(false);
		
		setViewSize(size);
		
		setBackground(Color.WHITE);
		setGridColor(Color.GRAY);
		
		graphics = new HashMap<String, ArrayList<Graphic>>();
		layers = new ArrayList<String>();
		selectedGraphic = null;
		
		setLayer("");
		
		setPropertiesPanel(propertiesPanel);
		
		setTool(new SelectTool(this));
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		setTransferHandler(new CanvasTransferHandler());
	}
	
	public CanvasPanel(Dimension size) {
		this(size, null);
	}
	
	public void setPropertiesPanel(PropertiesPanel propertiesPanel) {
		this.propertiesPanel = propertiesPanel;
	}
	
	public void setTool(Tool tool) {
		if (currentTool != null) {
			currentTool.unsetCanvas();
		}
		currentTool = tool;
	}
	
	public Dimension getViewSize() {
		return viewSize;
	}
	
	public void setViewSize(Dimension size) {
		viewSize = size;

		setSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
	}
	
	public Graphic getSelectedGraphic() {
		return selectedGraphic;
	}
	
	public void setSelectedGraphic(Graphic graphic) {
		selectedGraphic = graphic;
		
		if (graphic != null) {
			propertiesPanel.getProperties(graphic.getObjectData());
		}
	}
	
	public String getSelectedGraphicLayer() {
		return selectedGraphicLayer;
	}
	
	public void setSelectedGraphicLayer(String layer) {
		selectedGraphicLayer = layer;
	}
	
	// Returns the current layer name.
	public String getLayer() {
		return currentLayer;
	}
	
	// Sets the current layer.
	// If the layer doesn't exists then a new layer is added with
	// the layer name specified.
	public void setLayer(String layerName) {
		if (!graphics.containsKey(layerName)) {
			addLayer(layerName);
		}
		
		this.currentLayer = layerName;
	}
	
	private boolean getGridAvailability() {
		return gridAvailable;
	}
	
	public void setGridAvailability(boolean gridAvailable) {
		this.gridAvailable = gridAvailable;
	}
	
	public Color getGridColor() {
		return gridColor;
	}
	
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}
	
	public Dimension getGridSize() {
		return gridSize;
	}
	
	public void setGridSize(Dimension gridSize) {
		this.gridSize = gridSize;
	}
	
	public boolean isSnapToGrid() {
		return snapToGrid;
	}
	
	public void setSnapToGrid(boolean snapToGrid) {
		this.snapToGrid = snapToGrid;
	}
	
	public void addLayer(String layerName) {
		// It can't be two layers with the same name.
		if (!graphics.containsKey(layerName)) {
			graphics.put(layerName, new ArrayList<Graphic>());
			layers.add(layerName);
		}
	}
	
	// Removes the layer specified.
	// If the layer to be removed is the current layer
	// then the current layer is set to the first layer.
	public void removeLayer(String layerName) {
		if (graphics.containsKey(layerName)) {
			graphics.get(layerName).clear();
			graphics.remove(layerName);
			layers.remove(layerName);
		
			if (getLayer().equals(layerName)) {
				if (layers.size() != 0) {
					setLayer(layers.get(0));
				}
				else { // This is not supposed to happen.
					setLayer("");
				}
			}
			
			repaint();
		}
	}
	
	public String[] getLayers() {
		if (layers.size() > 0) {
			String[] layersArray = new String[layers.size()];
			return layers.toArray(layersArray);
		}
		
		return null;
	}
	
	public void changeLayerName(String oldName, String newName) {
		int layerIndex = layers.indexOf(oldName);
		
		if (layerIndex != -1) {
			layers.set(layerIndex, newName);
			
			graphics.put(newName, graphics.get(oldName));
			graphics.remove(oldName);
		}
	}
	
	public void swapLayers(String firstLayer, String secondLayer) {
		if (graphics.containsKey(firstLayer) && graphics.containsKey(secondLayer)) {
			int firstLayerIndex = layers.indexOf(firstLayer);
			int secondLayerIndex = layers.indexOf(secondLayer);
			
			Collections.swap(layers, firstLayerIndex, secondLayerIndex);
			
			repaint();
		}
	}
	
	public void addGraphic(Graphic graphic, String layer) {
		if (!layer.equals("")) {
			if (!graphics.containsKey(layer)) {
				addLayer(layer);
			}
			
			// Snap to grid
			// graphic.setX(graphic.getX() - graphic.getX() % gridSize.width);
			// graphic.setY(graphic.getY() - graphic.getY() % gridSize.height);
			
			graphics.get(layer).add(graphic);
			repaint();
		}
	}
	
	// Adds a graphic to the current layer.
	public void addGraphic(Graphic graphic) {
		addGraphic(graphic, getLayer());
	}
	
	public void addGraphicOnDropPoint(Graphic graphic) {
		Point mousePosition = getMousePosition();
		graphic.setX(mousePosition.x - graphic.getWidth()/2);
		graphic.setY(mousePosition.y - graphic.getHeight()/2);
		addGraphic(graphic);
	}
	
	public void removeGraphic(Graphic graphic, String layer) {
		if (graphics.containsKey(layer)) {
			graphics.get(layer).remove(graphic);
			repaint();
		}
	}
	
	// Removes a graphic from the current layer.
	public void removeGraphic(Graphic graphic) {
		removeGraphic(graphic, getLayer());
	}
	
	public void removeSelectedGraphic() {
		for (String layer : layers) {
			int selectedGraphicIndex = graphics.get(layer).indexOf(getSelectedGraphic());
			if (selectedGraphicIndex != -1) {
				graphics.get(layer).remove(getSelectedGraphic());
				setSelectedGraphic(null);
				repaint();
			}
		}
	}
	
	public Graphic[] getGraphicsByLayer(String layer) {
		if (graphics.containsKey(layer)) {
			Graphic[] graphicsArray = new Graphic[graphics.get(layer).size()];
			return graphics.get(layer).toArray(graphicsArray);
		}
		
		return null;
	}
	
	public void moveUpSelected() {
		if (getSelectedGraphic() != null) {
			ArrayList<Graphic> layer = graphics.get(selectedGraphicLayer);
			int selectedGraphicIndex = layer.indexOf(getSelectedGraphic());
			
			if (selectedGraphicIndex > 0) {
				Collections.swap(
					layer, selectedGraphicIndex, selectedGraphicIndex - 1);
			}
			
			repaint();
		}
	}
	
	public void moveDownSelected() {
		if (getSelectedGraphic() != null) {
			ArrayList<Graphic> layer = graphics.get(selectedGraphicLayer);
			int selectedGraphicIndex = layer.indexOf(getSelectedGraphic());
			
			if (selectedGraphicIndex < layer.size() - 1) {
				Collections.swap(
					layer, selectedGraphicIndex, selectedGraphicIndex + 1);
			}
			
			repaint();
		}
	}
		
	private void drawGrid(Graphics2D g2d, boolean gridAvailable) {
		if (gridAvailable) {
			g2d.setColor(getGridColor());
			//Vertical Lines
			for (int i = getGridSize().width; i < getWidth(); i+=getGridSize().width) {
				g2d.drawLine(i, 0, i, getHeight());
			}
			//Horizontal Lines
			for (int j = getGridSize().height; j < getHeight(); j+=getGridSize().height) {
				g2d.drawLine(0, j, getWidth(), j);
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {
		requestFocusInWindow();
	}
	
	public void mousePressed(MouseEvent e) {
		// boolean found = false;
		// ListIterator<String> layerIterator = 
		// 	layers.listIterator(layers.size());
		// while (layerIterator.hasPrevious()) {
		// 	String layer = layerIterator.previous();
		// 	ListIterator<Graphic> iterator =
		// 		graphics.get(layer).listIterator(graphics.get(layer).size());
		// 	
		// 	while (iterator.hasPrevious()) {
		// 		Graphic graphic = iterator.previous();
		// 		int mouseX = e.getX();
		// 		int mouseY = e.getY();
		// 		
		// 		if (graphic.onBounds(mouseX, mouseY)) {
		// 			if (getSelectedGraphic() != null) {
		// 				getSelectedGraphic().setSelected(false);
		// 			}
		// 			
		// 			setSelectedGraphic(graphic);
		// 			getSelectedGraphic().setSelected(true);
		// 			
		// 			found = true;
		// 			
		// 			selectedGraphicXWhenClicked = getSelectedGraphic().getX();
		// 			selectedGraphicYWhenClicked = getSelectedGraphic().getY(); 
		// 			mouseXOnSelectedWhenClicked = mouseX;
		// 			mouseYOnSelectedWhenClicked = mouseY;
		// 			
		// 			selectedGraphicLayer = layer;
		// 			
		// 			break;
		// 		}
		// 	}
		// 	
		// 	if (found) {
		// 		break;
		// 	}
		// }
		// 
		// if (!found) {
		// 	if (getSelectedGraphic() != null) {
		// 		getSelectedGraphic().setSelected(false);
		// 	}
		// 	
		// 	setSelectedGraphic(null);
		// }
		// 
		// repaint();
	}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseDragged(MouseEvent e) {
		// if (selectedGraphic != null) {
		// 	int newMouseX = selectedGraphicXWhenClicked + e.getX() - mouseXOnSelectedWhenClicked;
		// 	int newMouseY = selectedGraphicYWhenClicked + e.getY() - mouseYOnSelectedWhenClicked;
		// 	int moduleX = newMouseX % getGridSize().width;
		// 	int moduleY = newMouseY % getGridSize().height;
		// 	int snapPointX;
		// 	int snapPointY;
		// 
		// 	if (moduleX < (getGridSize().width/2)) {
		// 		snapPointX = newMouseX - moduleX;
		// 	}
		// 	else {
		// 		snapPointX = newMouseX+(getGridSize().width-moduleX);	
		// 	}
		// 
		// 	if (moduleY < (getGridSize().height/2)) {
		// 		snapPointY = newMouseY - moduleY;
		// 	}
		// 	else {
		// 		snapPointY = newMouseY+(getGridSize().height-moduleY);
		// 	}
		// 	
		// 	getSelectedGraphic().setX(snapPointX);
		// 	getSelectedGraphic().setY(snapPointY);
		// 
		// 	repaint();
		// }
	}
	
	public void mouseMoved(MouseEvent e) {}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
			e.getKeyCode() == KeyEvent.VK_DELETE) {
			removeSelectedGraphic();
		}
	}
	
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		for (String layer : layers) {
			for (Graphic graphic : graphics.get(layer)) {
				graphic.draw(g2d);
			}
		}
		
		drawGrid(g2d, getGridAvailability());
	}
}