// File: Property.java
// Summary: Abstract base of the properties to implement Composite pattern.

package base;

public class Property {
	private String name;
	private String value;
	
	public Property(String name, String value) {
		setName(name);
		setValue(value);
	}
	
	public Property(String name) {
		this(name, "");
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return getValue();
	}
	
	public String toXML(String indent) {
		return String.format("%s<%s>%s</%s>\n",
			indent, getName(), getValue(), getName());
	}
	
	public String toXML() {
		return toXML("");
	}
}