// File: LibraryTransferHandler.java
// Summary: Manages the drag and drop actions on the library list of items.

package ui.panels.library;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

import javax.swing.*;

import java.util.ArrayList;

import ui.panels.canvas.*;

public class LibraryTransferHandler extends TransferHandler {
	private ArrayList<GraphicMetadata> libraryItems;
	
	public LibraryTransferHandler(ArrayList<GraphicMetadata> libraryItems) {
		this.libraryItems = libraryItems;
	}
	
	public int getSourceActions(JComponent c) {
		return COPY;
	}
	
	public Transferable createTransferable(JComponent c) {
		if (c instanceof JList) {
			JList itemList = (JList)c;
			int selectedIndex = itemList.getSelectedIndex();
			
			if (selectedIndex != -1) {
				return new GraphicTransferable(libraryItems.get(selectedIndex));
			}
		}
		
		return null;
	}
	
	public class GraphicTransferable implements Transferable {
		private GraphicMetadata item;
		
		private final String graphicDataType =
			DataFlavor.javaJVMLocalObjectMimeType + 
			";class=ui.panels.canvas.Graphic";
		
		private DataFlavor localGraphicDataFlavor;
		private DataFlavor serialGraphicDataFlavor;
		
		public GraphicTransferable(GraphicMetadata item) {
			this.item = item;
			
			try {
				localGraphicDataFlavor = new DataFlavor(graphicDataType);
			} catch (ClassNotFoundException e) {
				System.err.println(
					"LibraryTransferHandler: unable to create data flavor");
			}
			
			serialGraphicDataFlavor = new DataFlavor(Graphic.class, "Graphic");
		}
		
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] {localGraphicDataFlavor,
									serialGraphicDataFlavor};
		}
		
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (localGraphicDataFlavor != null &&
				localGraphicDataFlavor.equals(flavor)) {
				return true;
			}
			
			if (serialGraphicDataFlavor.equals(flavor)) {
				return true;
			}
			
			return false;
		}
		
		public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException {
			
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			
			return GraphicFactory.createGraphic(item);
		}
	}
}