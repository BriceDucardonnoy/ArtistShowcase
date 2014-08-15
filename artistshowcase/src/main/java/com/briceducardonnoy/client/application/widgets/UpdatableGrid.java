/**
 * Copyright © Brice DUCARDONNOY
 * 
 * Permission is hereby granted, free of charge, to any person obtaining 
 * a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 * - The above copyright notice and this permission notice shall be included 
 * 	in all copies or substantial portions of the Software.
 * - The Software is provided "as is", without warranty of any kind, express 
 * 	or implied, including but not limited to the warranties of merchantability, 
 * 	fitness for a particular purpose and noninfringement.
 * 
 * In no event shall the authors or copyright holders be liable for any claim, 
 * damages or other liability, whether in an action of contract, tort or otherwise, 
 * arising from, out of or in connection with the software or the use or other 
 * dealings in the Software.
 */
package com.briceducardonnoy.client.application.widgets;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Grid;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

public class UpdatableGrid extends Grid {
	
	@UiConstructor
	public UpdatableGrid(int rows, int columns) {
		super(rows, columns);
	}
	
	public void insertCell(int idxR, int idxC, int maxC) {
		if(idxR >= getRowCount()) {
			resizeRows(getRowCount() + 1);
		}
		for(int r = getRowCount() - 1 ; r >= idxR ; r--) {
			for(int c = maxC-1 ; c >= 0/*idxC*/ ; c--) {// Column min is 0 because on row r+1, all the columns are concerned
				if(getWidget(r, c) != null) {
					Log.info("Move (rxc): (" + r + "x" + c + ") " + ((FitImage)getWidget(r, c)).getTitle());
					shiftCell(r, c);
				}
				if(idxR == r && idxC == c) break;// Work is finished
			}
		}
	}
	
	private void shiftCell(int r, int c) {
		int newC;
		int newR = r;
		if(c == getColumnCount() - 1) {// Shift to next row
			newC = 0;
			newR = r + 1;
		}
		else {
			newC = c + 1;
		}
		if(newR >= getRowCount()) {
			resizeRows(getRowCount() + 1);
		}
		setWidget(newR, newC, getWidget(r, c));
		getCellFormatter().getElement(newR, newC).setPropertyString("align", "center");
		getWidget(newR, newC).getElement().getStyle().setCursor(Cursor.POINTER);
		clearCell(r, c);
	}

}
