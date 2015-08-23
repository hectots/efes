// File: LevelReader.java
// Summary: Read a level from an XML formatted file.

package io;

import java.util.HashMap;

import java.io.*;

import java.awt.Dimension;

import org.w3c.dom.*;
import javax.xml.parsers.*;

import org.xml.sax.SAXException;

import base.*;

import ui.panels.*;
import ui.panels.canvas.*;

import utils.*;

public class LevelReader {
	private LevelData levelData;
	private CanvasPanel canvasPanel;
	private LayersPanel layersPanel;
	private LibraryPanel libraryPanel;
	private File levelFile;
	private HashMap<String, GraphicMetadata> graphicMetadataByClass;
	
	public LevelReader() {
		graphicMetadataByClass = new HashMap<String, GraphicMetadata>();
		setLevelData(null);
		setCanvasPanel(null);
	}
	
	public LevelData getLevelData() {
		return levelData;
	}
	
	private void setLevelData(LevelData levelData) {
		this.levelData = levelData;
	}
	
	public CanvasPanel getCanvasPanel() {
		return canvasPanel;
	}
	
	private void setCanvasPanel(CanvasPanel canvasPanel) {
		this.canvasPanel = canvasPanel;
	}
	
	private File getLevelFile() {
		return levelFile;
	}
	
	private void setLevelFile(File levelFile) {
		this.levelFile = levelFile;
	}
	
	public LayersPanel getLayersPanel() {
		return layersPanel;
	}
	
	private void setLayersPanel(LayersPanel layersPanel) {
		this.layersPanel = layersPanel;
	}
	
	public LibraryPanel getLibraryPanel() {
		return libraryPanel;
	}
	
	private void setLibraryPanel(LibraryPanel libraryPanel) {
		this.libraryPanel = libraryPanel;
	}
	
	private Document getDocument(File file) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			return documentBuilder.parse(file);
		} catch (FactoryConfigurationError factoryError) {
			System.err.println(
				"An error has ocurred creating the document builder factory.");
			factoryError.printStackTrace();
			return null;
		} catch (ParserConfigurationException parserException) {
			System.err.println(
				"There's a problem with the parser configuration.");
			parserException.printStackTrace();
			return null;
		} catch (SAXException saxException) {
			System.err.printf(
				"A problem ocurred during the parsing of the file %s.\n",
				file.getAbsolutePath());
			saxException.printStackTrace();
			return null;
		} catch (IOException ioException) {
			System.err.printf(
				"An error ocurred during the reading of the file %s.\n",
				file.getAbsolutePath());
			ioException.printStackTrace();
			return null;
		} catch (Exception exception) {
			exception.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
	private void readLevelData(Node levelNode) {
		try {
			NamedNodeMap attributes = levelNode.getAttributes();
			
			String levelName = attributes.getNamedItem("name").getNodeValue();
			
			int levelWidth = 
				Integer.parseInt(attributes.getNamedItem("width").getNodeValue());
			int levelHeight =
				Integer.parseInt(attributes.getNamedItem("height").getNodeValue());
			
			LevelData ld = new LevelData(levelName, levelWidth, levelHeight);
			
			for (int i = 0; i != attributes.getLength(); i++) {
				Node attr = attributes.item(i);
				
				if (attr.getNodeName() != "name" &&
					attr.getNodeName() != "width" &&
					attr.getNodeName() != "height" ) {
					
					ld.addProperty(new Property(attr.getNodeName(), attr.getNodeValue()));
				}
			}
						
			setLevelData(ld);
			
			setCanvasPanel(
				new CanvasPanel(new Dimension(levelWidth, levelHeight)));
			
			setLayersPanel(new LayersPanel(getCanvasPanel()));
			setLibraryPanel(new LibraryPanel());
			
		} catch (NumberFormatException numberFormatException) {
			System.err.println("Someone likes to play with level files!");
			System.exit(1);
		} catch (Exception exception) {
			exception.printStackTrace();
			System.exit(1);
		}
	}
	
	private Graphic createGraphic(String className, ObjectData objectData) {
		GraphicMetadata graphicMetadata = graphicMetadataByClass.get(className);
		
		int graphicXPos = 0;
		int graphicYPos = 0;
		
		if (objectData.hasProperty("image")) {
			CompositeProperty imageProperty =
				(CompositeProperty)objectData.getProperty("image");
			Property filenameProperty = imageProperty.getProperty("filename");
			String imagePath = filenameProperty.toString();
			
			graphicXPos = Integer.parseInt(
				imageProperty.getProperty("x").getValue());
			graphicYPos = Integer.parseInt(
				imageProperty.getProperty("y").getValue());
		}
		else if (objectData.hasProperty("geometry")) {
			CompositeProperty geometryProperty =
				(CompositeProperty)objectData.getProperty("geometry");
			int width = Integer.parseInt(
				geometryProperty.getProperty("width").getValue());
			int height = Integer.parseInt(
				geometryProperty.getProperty("height").getValue());
			graphicXPos = Integer.parseInt(
				geometryProperty.getProperty("x").getValue());
			graphicYPos = Integer.parseInt(
				geometryProperty.getProperty("y").getValue());
		}
		else {
			graphicXPos = Integer.parseInt(
				objectData.getProperty("x").getValue());
			graphicYPos = Integer.parseInt(
				objectData.getProperty("y").getValue());
		}
		
		Graphic graphic = GraphicFactory.createGraphic(graphicMetadata);
		graphic.setObjectData(objectData);
		graphic.setX(graphicXPos);
		graphic.setY(graphicYPos);
		
		return graphic;
	}
	
	private void readObject(Node objectNode) {
		ObjectData objectData = new ObjectData();
		
		objectNode.normalize();
		NodeList objectChildren = objectNode.getChildNodes();
		for (int i = 0; i != objectChildren.getLength(); i++) {
			Node child = objectChildren.item(i);
			child.normalize();
			
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Property property;
				
				if (child.getNodeName().equals("image") ||
					child.getNodeName().equals("geometry")) {
					
					property = readCompositeProperty(child);
				}
				else {
					String nodeName = child.getNodeName();
					String nodeValue = XMLUtils.getValue(child);
					property = new Property(nodeName, nodeValue);
				}
				
				objectData.addProperty(property);
			}
		}
		
		String className = objectNode.getAttributes().getNamedItem("class").getNodeValue();
		String layer = objectNode.getParentNode().getAttributes().getNamedItem("name").getNodeValue();
		
		objectData.setObjectClass(className);
		
		Graphic graphic = createGraphic(className, objectData);
		getLayersPanel().addLayer(layer, true);
		getCanvasPanel().addGraphic(graphic, layer);
	}
	
	private Property readCompositeProperty(Node node) {
		CompositeProperty property = new CompositeProperty(node.getNodeName());
		
		node.normalize();
		NodeList children = node.getChildNodes();
		for (int i = 0; i != children.getLength(); i++) {
			Node child = children.item(i);
			child.normalize();
			
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = child.getNodeName();
				String nodeValue = XMLUtils.getValue(child);
			
				property.addProperty(new Property(nodeName, nodeValue));
			}
		}
		
		return property;
	}

	private void readLibrary(Node libraryNode) {
		libraryNode.normalize();

		NodeList libraryChildren = libraryNode.getChildNodes();
		for (int i = 0; i != libraryChildren.getLength(); i++) {
			Node child = libraryChildren.item(i);
			child.normalize();
			
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = child.getNodeName();

				if (nodeName.equals("item")) {
					readLibraryItem(child);
				}
			}
		}
	}

	private void readLibraryItem(Node itemNode) {
		GraphicMetadata graphicMetadata = new GraphicMetadata();
		itemNode.normalize();

		NodeList itemChildren = itemNode.getChildNodes();
		for (int i = 0; i != itemChildren.getLength(); i++) {
			Node child = itemChildren.item(i);
			child.normalize();
			
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = child.getNodeName();
				String nodeValue = XMLUtils.getValue(child);

				if (nodeName.equals("itemType")) {
					graphicMetadata.setGraphicType(nodeValue);
				} else if (nodeName.equals("itemClass")) {
					graphicMetadata.setGraphicClass(nodeValue);
				} else if (nodeName.equals("image")) {
					graphicMetadata.setImage(nodeValue);
				} else if (nodeName.equals("width")) {
					graphicMetadata.setWidth(Integer.parseInt(nodeValue));
				} else if (nodeName.equals("height")) {
					graphicMetadata.setHeight(Integer.parseInt(nodeValue));
				}
			}
		}

		String graphicClass = graphicMetadata.getGraphicClass();
		if (!graphicMetadataByClass.containsKey(graphicClass)) {
			graphicMetadataByClass.put(graphicClass, graphicMetadata);
		}

		getLibraryPanel().addItem(graphicMetadata);
	}
	
	public void read(File file) {
		Document document = getDocument(file);
		setLevelFile(file);
		
		if (document != null) {
			readLevelData(document.getElementsByTagName("level").item(0));
			readLibrary(document.getElementsByTagName("library").item(0));
			
			NodeList objects = document.getElementsByTagName("object");
			
			for (int i = 0; i != objects.getLength(); i++) {
				if (objects.item(i).getNodeType() == Node.ELEMENT_NODE) {
					readObject(objects.item(i));
				}
			}
		}
	}
}