// File: ObjectData.java
// Summary: Contains information about an object in the level.

package base;

import java.util.ArrayList;

public class ObjectData extends Data {
	private String cls;
	
	public ObjectData(String cls) {
		super();
		
		setObjectClass(cls);
		properties = new ArrayList<Property>();
	}
	
	public ObjectData() {
		this("");
	}
	
	public String getObjectClass() {
		return cls;
	}
	
	public void setObjectClass(String cls) {
		this.cls = cls;
	}
		
	public String toXML(String indent) {
		String xml;
		
		xml = String.format("%s<object class=\"%s\">\n",
			indent, getObjectClass());
		for (Property property : properties) {
			xml += property.toXML(indent + "\t");
		}
		xml += indent + "</object>\n";
		
		return xml;
	}
	
	public String toXML() {
		return toXML("");
	}
}