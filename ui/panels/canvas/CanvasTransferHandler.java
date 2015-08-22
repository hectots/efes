// File: CanvasTransferHandler.java
// Summary: Manages drop actions on the canvas.

package ui.panels.canvas;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

import javax.swing.*;

import java.io.IOException;

import ui.panels.CanvasPanel;

public class CanvasTransferHandler extends TransferHandler {
	private final String graphicDataType =
		DataFlavor.javaJVMLocalObjectMimeType + 
		";class=ui.panels.canvas.Graphic";
	
	private DataFlavor localGraphicDataFlavor;
	private DataFlavor serialGraphicDataFlavor;
	
	public CanvasTransferHandler() {
		try {
			localGraphicDataFlavor = new DataFlavor(graphicDataType);
		} catch (ClassNotFoundException e) {
			System.err.println(
				"LibraryTransferHandler: unable to create data flavor");
		}
		
		serialGraphicDataFlavor = new DataFlavor(Graphic.class, "Graphic");
	}
	
	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		for (int i = 0; i != flavors.length; i++) {
			if (localGraphicDataFlavor != null &&
				localGraphicDataFlavor.equals(flavors[i])) {
				return true;
			}
			
			if (serialGraphicDataFlavor.equals(flavors[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean importData(JComponent c, Transferable transferable) {
		if (c instanceof CanvasPanel &&
			canImport(c, transferable.getTransferDataFlavors())) {
			
			CanvasPanel canvasPanel = (CanvasPanel)c;
			
			try {
				Graphic graphic = (Graphic)transferable.getTransferData(
					localGraphicDataFlavor);
				
				canvasPanel.addGraphicOnDropPoint(graphic);
				
				return true;
			} catch (UnsupportedFlavorException unsupportedFlavorException) {
				System.err.println(
					"CanvasTransferHandler.importData: unsupported data flavor");
				return false;
			} catch (IOException ioException) {
				System.err.println(
					"CanvasTransferHandler.importData: I/O exception");
				return false;
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		
		return false;
	}
}