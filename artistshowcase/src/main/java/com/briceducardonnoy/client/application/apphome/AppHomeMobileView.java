package com.briceducardonnoy.client.application.apphome;

import java.util.List;

import javax.inject.Inject;

import org.gwt.contentflow4gwt.client.ContentFlow;

import com.briceducardonnoy.shared.model.Category;
import com.briceducardonnoy.shared.model.Picture;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class AppHomeMobileView extends ViewWithUiHandlers<AppHomeUiHandlers>
		implements AppHomePresenter.MyView {
	interface Binder extends UiBinder<Widget, AppHomeMobileView> {
	}

	@UiField
	SimplePanel main;

	@Inject
	AppHomeMobileView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == AppHomePresenter.SLOT_AppHome) {
			main.setWidget(content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	@Override
	public void addCategories(List<Category> categories) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addItems(List<Picture> pictures) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContentFlow<Picture> getContentFlow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Picture getCurrentPicture() {
		// TODO Auto-generated method stub
		return null;
	}
}
