// File: LevelData.java
// Summary: Encapsulates the level attributes like the name and conditions.

package base;

import java.util.ArrayList;

public class LevelData extends Data {
	private String name;
	private int width;
	private int height;
	
	public LevelData(String name, int width, int height) {
		super();
		
		setName(name);
		setWidth(width);
		setHeight(height);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
		
	public String toXML() {
		String xml;
		
		// String propertiesString = " ";
		// for (Property property : properties) {
		// 	propertiesString += property.getName() + "=" + property.getValue() + " ";
		// }
		
		xml = String.format("<level name=\"%s\" width=\"%d\" height=\"%d\"%s>\n",
			getName(), getWidth(), getHeight(), " ");
		
		return xml;
	}
}