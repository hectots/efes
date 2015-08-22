// File: CompositeProperty.java
// Summary: A property that contains other properties.

package base;

import java.util.ArrayList;

public class CompositeProperty extends Property {
	private ArrayList<Property> properties;
	
	public CompositeProperty(String name) {
		super(name);
		properties = new ArrayList<Property>();
	}
	
	public Property getProperty(String name) {
		for (Property p : properties) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		
		return null;
	}
	
	public Property[] getProperties() {
		Property[] propertiesArray = new Property[properties.size()];
		return properties.toArray(propertiesArray);
	}
	
	public void addProperty(Property p) {
		properties.add(p);
	}
	
	public void removeProperty(Property p) {
		properties.remove(p);
	}
	
	public String getValue() {
		String s = "{";
		for (Property p : properties) {
			s += p.getName() + ":" + p.toString() + ",";
		}
		s = s.substring(0, s.length() - 2);
		s += "}";
		
		return s;
	}
	
	public void setValue(String value) {}
	
	public String toString() {
		return getValue();
	}
	
	public String toXML(String indent) {
		String xml;
		
		xml = String.format("%s<%s>\n", indent, getName());
		for (Property property : properties) {
			xml += property.toXML(indent + "\t");
		}
		xml += String.format("%s</%s>\n", indent, getName());
		
		return xml;
	}
	
	public String toXML() {
		return toXML("");
	}
}