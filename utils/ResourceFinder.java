// File: ResourceFinder.java
// Summary: Search for a certain file in the files directory.

package utils;

import java.io.*;

public class ResourceFinder {
	// Search for a file in a specified directory and it's subdirectories.
	// If the file isn't found then null is returned.
	public static String findFile(File dir, String fileName) {
		String filePath = findFileHelper(dir, fileName);
		
		if (!filePath.equals("")) {
			return filePath;
		}
		
		return null;
	}
	
	private static String findFileHelper(File dir, String fileName) {
		File[] fileList = dir.listFiles();
		String filePath = "";
		
		if (dir.isDirectory()) {
			fileList = dir.listFiles();
			
			for (File file : fileList) {
				if (file.isFile() && file.getName().equals(fileName)) {
					return file.getAbsolutePath();
				}
				
				if (!file.getName().startsWith(".") && file.isDirectory()) {
					filePath = findFileHelper(file, fileName);
					
					if (!filePath.equals("")) {
						return filePath;
					}
				}
			}
		}
		
		return "";
	}
}