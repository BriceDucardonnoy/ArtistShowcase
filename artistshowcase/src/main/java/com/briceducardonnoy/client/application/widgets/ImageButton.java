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

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class ImageButton extends Button {
	Image img;

	public ImageButton() {
	}

	@UiConstructor
	public ImageButton(final ImageResource res) {
		img = new Image(res);
		getElement().appendChild(img.getElement());
	}
	
	@Override
	public void setText(final String text) {
		Label lbl = new Label(text);
		getElement().appendChild(lbl.getElement());
	}
	
	public void setAltText(final String text) {
		if(img != null) {
			img.setAltText(text);
		}
	}
	
	public Image getImage() {
		return img;
	}

}
