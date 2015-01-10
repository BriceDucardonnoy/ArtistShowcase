package com.briceducardonnoy.client.application.details;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.briceducardonnoy.shared.model.Picture;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

class DetailsView extends ViewImpl implements DetailsPresenter.MyView {
	interface Binder extends UiBinder<Widget, DetailsView> {
	}

	@UiField SimplePanel main;

	@Inject
	DetailsView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
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
	public Image getMainImage() {
		// TODO Auto-generated method stub
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
	public void updateThumbs(ArrayList<String> thumbsArray) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Picture> getPicturesList() {
		// TODO Auto-generated method stub
		return null;
	}
}
