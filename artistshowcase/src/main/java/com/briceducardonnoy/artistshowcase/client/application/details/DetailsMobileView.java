/**
 * Copyright 2015 © Brice DUCARDONNOY
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
package com.briceducardonnoy.artistshowcase.client.application.details;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.artistshowcase.client.application.apphome.AppHomeMobileView;
import com.briceducardonnoy.artistshowcase.client.application.utils.Utils;
import com.briceducardonnoy.artistshowcase.client.application.widgets.UpdatableGrid;
import com.briceducardonnoy.artistshowcase.client.lang.Translate;
import com.briceducardonnoy.artistshowcase.shared.model.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

public class DetailsMobileView extends ViewImpl implements DetailsPresenter.MyView  {

	@UiField ResizeLayoutPanel main;
	@UiField ScrollPanel scrollPane;
	@UiField (provided = true) UpdatableGrid grid;
	
	private final Translate translate = GWT.create(Translate.class);
	
	private List<Picture> picturesList;
	private List<FitImage> imagesList;
	private List<HandlerRegistration> imageHandlers;
	private int maxC = AppHomeMobileView.MAX_BIG;
	private int maxR = 1;
	private int width, height;
	
	interface Binder extends UiBinder<Widget, DetailsMobileView> {
	}
	
	@Inject
	DetailsMobileView(Binder uiBinder) {
		grid = new UpdatableGrid(1, maxC);
		initWidget(uiBinder.createAndBindUi(this));
		picturesList = new ArrayList<>();
		imagesList = new ArrayList<>();
		imageHandlers = new ArrayList<>();
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == DetailsPresenter.SLOT_details) {
			main.setWidget(content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	@Override
	public ResizeLayoutPanel getMainPane() {
		return main;
	}

	@Override
	public Image getMainImage() {
		return null;
	}

	@Override
	public FitImage getCenterImage() {// No center image on mobile view
		return null;
	}

	@Override
	public void updateMainImage(final String url) {}

	@Override
	public void updateDetailInfo(final String html) {
		// TODO BDY: add a div with html content in 1st place
	}

	@Override
	public void updateThumbs(final List<String> urls) {
		/*
		 * read is per line:
		 * 1 2 3
		 * 4 5 6
		 * Define max number of column and size of each item depending
		 * of orientation and size
		 * Maybe get it in onReset because it cannot be done in @media
		 */
		int pos = 0;
		picturesList.clear();
		imagesList.clear();
		grid.clear();
		for(String url : urls) {
			final Picture p = new Picture(translate.Details());
			p.getProperties().put("imageUrl", url);
			picturesList.add(p);
			final FitImage image = new FitImage(p.getImageUrl());
			imageHandlers.add(image.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// TODO BDY: do something to show in fullscreen
				}
			}));
			imagesList.add(image);
			// Insert in grid
			Utils.addFitImageInGrid(grid, pos++, image, maxC, maxR, width, height);
		}
	}
	
	@Override
	public List<Picture> getPicturesList() {
		return picturesList;
	}

	@Override
	public void resize(int width, int height) {
		Log.info("Resize::Size is " + width + " x " + height + " and original size is " + this.width + "x" + this.height);
		this.width = width;
		this.height = height;
		boolean isLandscape = Utils.isLandscape();// To be in accordance with @media css
		maxC = isLandscape ? AppHomeMobileView.MAX_BIG : AppHomeMobileView.MAX_SMALL;
		maxR = isLandscape ? AppHomeMobileView.MAX_SMALL : AppHomeMobileView.MAX_BIG;
		if(grid.getColumnCount() != maxC) {// Switch landscape -> portrait or portrait -> landscape
			refreshGrid(imagesList);
		}
		else {
			Log.info("Just resize");
			Utils.resize(grid, maxC, maxR, width, height);
		}
	}
	
	/**
	 * Clear the grid and add images among the <code>images</code> that are in current category <code>currentCategoryId</code>
	 * It also relayout the images.
	 * @param images The list of images to sort (current category) and to display
	 */
	private void refreshGrid(final List<FitImage> images) {
		Log.info("RefreshGrid::Clear grid with (" + images.size() + " elements)");
		int pos = 0;
		grid.clear();
		grid.resize(maxR, maxC);
		for(int i = 0 ; i < picturesList.size() ; i++) {
			Log.info("Add picture " + picturesList.get(i).getTitleOrName() + " (" + images.get(i).getAltText() + ")");
			Utils.addFitImageInGrid(grid, pos++, images.get(i), maxC, maxR, width, height);
		}
	}

	@Override
	public void clearData() {
		for(HandlerRegistration hr : imageHandlers) {
			hr.removeHandler();
		}
		imageHandlers.clear();
		picturesList.clear();
		imagesList.clear();
	}


}
