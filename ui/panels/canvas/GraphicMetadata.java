// File: GraphicMetadata.java
// Summary: Represents an item in the library.

package ui.panels.canvas;

import java.io.File;

public class GraphicMetadata {
	private String itemType;
	private String itemClass;
	private String image;
	private int width;
	private int height;
	
	public GraphicMetadata(
		String itemType, String itemClass, String image, int width, int height) {
		
		setGraphicType(itemType);
		setGraphicClass(itemClass);
		setImage(image);
		setWidth(width);
		setHeight(height);
	}
	
	public GraphicMetadata() {
		this("", "", "", 0, 0);
	}
	
	public String getGraphicType() {
		return itemType;
	}
	
	public void setGraphicType(String itemType) {
		this.itemType = itemType;
	}
	
	public String getGraphicClass() {
		return itemClass;
	}
	
	public void setGraphicClass(String itemClass) {
		this.itemClass = itemClass;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public String toString() {
		if (itemClass.equals("Image")) {
			File imageFile = new File(image);
			return imageFile.getName();
		}
		
		return itemClass;
	}
}