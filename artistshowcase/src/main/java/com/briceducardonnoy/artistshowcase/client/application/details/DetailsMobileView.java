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

import com.briceducardonnoy.artistshowcase.client.lang.Translate;
import com.briceducardonnoy.artistshowcase.shared.model.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

public class DetailsMobileView extends ViewImpl implements DetailsPresenter.MyView  {

	@UiField ResizeLayoutPanel main;
	
	private final Translate translate = GWT.create(Translate.class);
	private static final String ACTIVE = "#444444";
	
	private List<Picture> picturesList;
	private List<FitImage> imagesList;
	private List<HandlerRegistration> imageHandlers;
	
	interface Binder extends UiBinder<Widget, DetailsMobileView> {
	}
	
	@Inject
	DetailsMobileView(Binder uiBinder) {
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
	public FitImage getCenterImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateMainImage(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDetailInfo(String html) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateThumbs(ArrayList<String> urls) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Picture> getPicturesList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearData() {
		// TODO Auto-generated method stub
		
	}

}
