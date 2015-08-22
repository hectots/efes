// File: Data.java
// Summary: Abstract base for objects with iterable properties.

package base;

import java.util.ArrayList;

public class Data {
	protected ArrayList<Property> properties;
	
	public Data() {
		properties = new ArrayList<Property>();
	}
	
	public boolean hasProperty(String name) {
		for (Property p : properties) {
			if (p.getName().equals(name)) {
				return true;
			}
		}
		
		return false;
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
}