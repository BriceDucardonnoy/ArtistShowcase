package com.briceducardonnoy.client.application.apphome;

import java.util.List;

import javax.inject.Inject;

import org.gwt.contentflow4gwt.client.ContentFlow;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.shared.model.Category;
import com.briceducardonnoy.shared.model.Picture;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

public class AppHomeMobileView extends ViewWithUiHandlers<AppHomeUiHandlers> implements AppHomePresenter.MyView {
	interface Binder extends UiBinder<Widget, AppHomeMobileView> {
	}

	@UiField SimplePanel main;
	@UiField (provided = true) Grid grid;

	@Inject
	AppHomeMobileView(Binder uiBinder) {
		grid = new Grid(1, 0);
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
		// Nothing to do
	}

	@Override
	public ContentFlow<Picture> getContentFlow() {
		return null;// No cover flow for mobile view
	}

	@Override
	public Picture getCurrentPicture() {
		return null;// Can't return anything
	}

	@Override
	public void changeCurrentCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPicture(Picture picture) {
//		grid.setText(row, column, text);
		// TODO BDY: next step
		/*
		 * read is per line:
		 * 1 2 3
		 * 4 5 6
		 * Define max number of column and size of each item depending
		 * of orientation and size
		 * Maybe get it in onReset because it cannot be done in @media
		 */
		Log.info("One picture loaded: " + picture.getTitleOrName());
		int c, r;
		if(grid.getColumnCount() < 6) {
			grid.resizeColumns(grid.getColumnCount() + 1);
			c = grid.getColumnCount() - 1;
//			grid.setText(0, c, picture.getTitleOrName());
			grid.getCellFormatter().setWidth(0, c, "50px");
			grid.getCellFormatter().setHeight(0, c, "50px");
//			grid.setWidget(0, c, new Image(picture.getImageUrl(), 0, 0, 50, 50));
			grid.setWidget(0, c, new FitImage(picture.getImageUrl(), 50, 50));
		}
		else {
			// ...
		}
	}
}
