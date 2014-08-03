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

import com.briceducardonnoy.client.images.ImagesDesktopResources;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Create a button with image and an arrow at the right.
 * On click, it opens a popup panel with item added thanks to addMenuItem.
 * It's recommended to get the basic handler registration from the presenter in order to free it. 
 * @author Brice DUCARDONNOY
 * @see getClickHandlerRegistration
 * @see addItem
 */
public class ImageSplitButton extends ImageButton {
	
	protected HandlerRegistration clickHandlerRegistration;
	protected PopupPanel menuPopup;
	protected MenuBar menuBar;

	@UiConstructor
	public ImageSplitButton(ImageResource res) {
		super(res);
		Image arrow = new Image(ImagesDesktopResources.INSTANCE.arrow());
		getElement().appendChild(arrow.getElement());
		
		menuPopup = new PopupPanel(true);
		menuBar = new MenuBar(true);
		menuPopup.add(menuBar);
		
		clickHandlerRegistration = addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				menuPopup.showRelativeTo(ImageSplitButton.this);
			}
		});
	}
	
	public HandlerRegistration getClickHandlerRegistration() {
		return clickHandlerRegistration;
	}
	
	public void addSeparator() {
		menuBar.addSeparator();
	}
	
	public MenuBar getMenuBar() {
		return menuBar;
	}
}
