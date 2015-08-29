// File: SelectTool.java
// Summary: A tool for selecting and moving objects on a canvas.

package ui.tools;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import ui.panels.*;
import ui.panels.canvas.*;

public class SelectTool extends Tool {
	private ArrayList<Graphic> selectedGraphics;
	private ArrayList<String> selectedGraphicsLayers;
	private ArrayList<Point> graphicsCoordsOnSelection;

	private Point mouseCoordsOnSelection;

	private Point selectionBoxStart;
	private Point selectionBoxEnd;

	private final Color BOUNDING_BOX_COLOR = new Color(0, 0, 255);
	private final Color OVERLAY_COLOR = new Color(0, 152, 255, 64);

	private PropertiesPanel propertiesPanel;
	
	public SelectTool(CanvasPanel cp, PropertiesPanel pp) {
		super(cp);
		setPropertiesPanel(pp);

		selectionBoxStart = new Point();
		selectionBoxEnd = new Point();
		selectedGraphics = new ArrayList<Graphic>();
		selectedGraphicsLayers = new ArrayList<String>();
		graphicsCoordsOnSelection = new ArrayList<Point>();
	}

	public ArrayList<Graphic> getSelectedGraphics() {
		return selectedGraphics;
	}

	public void setPropertiesPanel(PropertiesPanel pp) {
		propertiesPanel = pp;
	}
	
	public void mousePressed(MouseEvent e) {
		CanvasPanel cp = getCanvas();
		
		boolean found = false;
		String[] layers = cp.getLayers();
		ListIterator<String> layerIterator = 
			Arrays.asList(layers).listIterator(layers.length);
		while (layerIterator.hasPrevious()) {
			String layer = layerIterator.previous();
			
			Graphic[] graphics = cp.getGraphicsByLayer(layer);
			ListIterator<Graphic> graphicsIterator =
				Arrays.asList(graphics).listIterator(graphics.length);
			
			while (graphicsIterator.hasPrevious()) {
				Graphic graphic = graphicsIterator.previous();
				int mouseX = e.getX();
				int mouseY = e.getY();
				
				if (graphic.onBounds(mouseX, mouseY)) {
					found = true;
					
					mouseCoordsOnSelection = new Point(mouseX, mouseY);

					boolean isSelected = false;
					for (Graphic selectedGraphic : selectedGraphics) {
						if (graphic == selectedGraphic) {
							isSelected = true;
						}
					}

					if (!isSelected) {
						resetSelected();
						selectGraphic(graphic, layer);
					}
					
					break;
				}
			}
			
			if (found) {
				break;
			}
		}
		
		if (!found) {
			resetSelected();
		}

		selectionBoxStart = new Point(e.getX(), e.getY());
		selectionBoxEnd =  new Point(e.getX(), e.getY());
		
		cp.repaint();
	}
	
	public void mouseDragged(MouseEvent e) {
		CanvasPanel cp = getCanvas();
		
		if (selectedGraphics.size() != 0) {
			for (int i = 0; i != selectedGraphics.size(); i++) {
				Graphic graphic = selectedGraphics.get(i);
				Point graphicCoordsOnSelection = graphicsCoordsOnSelection.get(i);

				int newMouseX = graphicCoordsOnSelection.x + e.getX() - mouseCoordsOnSelection.x;
				int newMouseY = graphicCoordsOnSelection.y + e.getY() - mouseCoordsOnSelection.y;
				
				if (cp.isSnapToGrid()) {
					newMouseX = newMouseX - newMouseX % cp.getGridSize().width;
					newMouseY = newMouseY - newMouseY % cp.getGridSize().height;
				}
				
				graphic.setX(newMouseX);
				graphic.setY(newMouseY);
			}
		} else {
			selectionBoxEnd =  new Point(e.getX(), e.getY());
		}

		cp.repaint();
	}

	public void mouseReleased(MouseEvent e) {
		CanvasPanel cp = getCanvas();

		int deltaX = selectionBoxEnd.x - selectionBoxStart.x;
		int deltaY = selectionBoxEnd.y - selectionBoxStart.y;

		if (deltaX != 0 && deltaY != 0)
		{
			ArrayList<Graphic> selectedGraphics = selectGraphicsInsideSelectionBox();
		}

		selectionBoxStart = new Point(0, 0);
		selectionBoxEnd =  new Point(0, 0);

		cp.repaint();
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
			e.getKeyCode() == KeyEvent.VK_DELETE) {
			removeSelected();
		}
	}

	public void removeSelected() {
		CanvasPanel cp = getCanvas();

		for (int i = 0; i != selectedGraphics.size(); i++) {
			String layer = selectedGraphicsLayers.get(i);
			Graphic graphic = selectedGraphics.get(i);

			cp.removeGraphic(graphic, layer);
		}

		cp.repaint();
	}

	public void moveUpSelected() {
		CanvasPanel cp = getCanvas();

		for (int i = 0; i != selectedGraphics.size(); i++) {
			String selectedGraphicLayer = selectedGraphicsLayers.get(i);
			Graphic selectedGraphic = selectedGraphics.get(i);
			ArrayList<Graphic> layer = cp.getGraphicsListByLayer(selectedGraphicLayer);
			int selectedGraphicIndex = layer.indexOf(selectedGraphic);

			if (selectedGraphicIndex > 0) {
				Collections.swap(
					layer, selectedGraphicIndex, selectedGraphicIndex - 1);
			}
		}

		cp.repaint();
	}
	
	public void moveDownSelected() {
		CanvasPanel cp = getCanvas();

		for (int i = 0; i != selectedGraphics.size(); i++) {
			String selectedGraphicLayer = selectedGraphicsLayers.get(i);
			Graphic selectedGraphic = selectedGraphics.get(i);
			ArrayList<Graphic> layer = cp.getGraphicsListByLayer(selectedGraphicLayer);
			int selectedGraphicIndex = layer.indexOf(selectedGraphic);

			if (selectedGraphicIndex < layer.size() - 1) {
				Collections.swap(
					layer, selectedGraphicIndex, selectedGraphicIndex + 1);
			}
		}

		cp.repaint();
	}

	private ArrayList<Graphic> selectGraphicsInsideSelectionBox() {
		resetSelected();

		CanvasPanel cp = getCanvas();
		String[] layers = cp.getLayers();
		ListIterator<String> layerIterator = 
			Arrays.asList(layers).listIterator(layers.length);
		while (layerIterator.hasPrevious()) {
			String layer = layerIterator.previous();
			
			Graphic[] graphics = cp.getGraphicsByLayer(layer);
			ListIterator<Graphic> graphicsIterator =
				Arrays.asList(graphics).listIterator(graphics.length);
			
			while (graphicsIterator.hasPrevious()) {
				Graphic graphic = graphicsIterator.previous();
				
				if (isInsideSelectionBox(
					graphic.getX(),
					graphic.getY(),
					graphic.getWidth(),
					graphic.getHeight())) {

					selectGraphic(graphic, layer);
				}
			}
		}

		return selectedGraphics;
	}

	private boolean isInsideSelectionBox(int x, int y, int width, int height) {
		int selectionLeft = selectionBoxStart.x < selectionBoxEnd.x ? selectionBoxStart.x : selectionBoxEnd.x;
		int selectionRight = selectionBoxStart.x > selectionBoxEnd.x ? selectionBoxStart.x : selectionBoxEnd.x;
		int selectionTop = selectionBoxStart.y < selectionBoxEnd.y ? selectionBoxStart.y : selectionBoxEnd.y;
		int selectionBottom = selectionBoxStart.y > selectionBoxEnd.y ? selectionBoxStart.y : selectionBoxEnd.y;

		int graphicLeft = x;
		int graphicRight = x + width;
		int graphicTop = y;
		int graphicBottom = y + height;

		if ((graphicLeft < selectionRight) &&
			(graphicRight > selectionLeft) &&
			(graphicTop < selectionBottom) &&
			(graphicBottom > selectionTop)) {

			return true;
		}

		return false;
	}

	private void selectGraphic(Graphic graphic, String layer) {
		graphic.setSelected(true);
		selectedGraphics.add(graphic);
		graphicsCoordsOnSelection.add(new Point(graphic.getX(), graphic.getY()));
		selectedGraphicsLayers.add(layer);

		if (selectedGraphics.size() == 1) {
			propertiesPanel.getProperties(graphic.getObjectData());
		}
	}

	private void resetSelected() {
		CanvasPanel cp = getCanvas();

		for (String layer : cp.getLayers()) {
			for (Graphic graphic : cp.getGraphicsByLayer(layer)) {
				graphic.setSelected(false);
			}
		}

		selectedGraphics = new ArrayList<Graphic>();
		selectedGraphicsLayers = new ArrayList<String>();
		graphicsCoordsOnSelection = new ArrayList<Point>();

		cp.repaint();
	}

	public void draw(Graphics2D g2d) {
		int deltaX = selectionBoxEnd.x - selectionBoxStart.x;
		int deltaY = selectionBoxEnd.y - selectionBoxStart.y;

		if (deltaX != 0 && deltaY != 0)
		{
			int startX = deltaX > 0 ? selectionBoxStart.x : selectionBoxEnd.x;
			int startY = deltaY > 0 ? selectionBoxStart.y : selectionBoxEnd.y;
			int width = Math.abs(deltaX);
			int height = Math.abs(deltaY);

			g2d.setColor(BOUNDING_BOX_COLOR);
			g2d.drawRect(startX, startY, width, height);
			g2d.setColor(OVERLAY_COLOR);
			g2d.fillRect(startX, startY, width, height);
		}
	}

	public void unsetCanvas() {
		super.unsetCanvas();
		resetSelected();
	}
}