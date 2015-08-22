// File: XMLUtils.java
// Summary: Contain routines for extracting data from XML nodes.

package utils;

import org.w3c.dom.*;

public class XMLUtils {
	public static String getValue(Node element) {
		NodeList children = element.getChildNodes();
		
		for (int i = 0; i != children.getLength(); i++) {
			String value = children.item(i).getNodeValue().trim();
			
			if (!value.equals("") && !value.equals("\r")) {
				return value;
			}
		}
		
		return "";
	}
}