package com.briceducardonnoy.client.application.apphome;

import java.util.List;

import javax.inject.Inject;

import org.gwt.contentflow4gwt.client.ContentFlow;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.shared.model.Category;
import com.briceducardonnoy.shared.model.Picture;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

public class AppHomeMobileView extends ViewWithUiHandlers<AppHomeUiHandlers> implements AppHomePresenter.MyView {
	interface Binder extends UiBinder<Widget, AppHomeMobileView> {
	}

	private static final int maxBig = 4;
	private static final int maxSmall = 2;

	@UiField ResizeLayoutPanel main;
	@UiField ScrollPanel sc;
	@UiField (provided = true) Grid grid;
	
	private int width = 0;
	private int height = 0;
	private int nbC = 0;
	private int nbR = 1;
	private int nbPictures = 0;
	private boolean isLandscape = true;

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
		/*
		 * read is per line:
		 * 1 2 3
		 * 4 5 6
		 * Define max number of column and size of each item depending
		 * of orientation and size
		 * Maybe get it in onReset because it cannot be done in @media
		 */
		Log.info("One picture loaded: " + picture.getTitleOrName());
		int idxC = nbPictures % nbC;
		int idxR = nbPictures / nbC;
		if(idxC == 0 || idxR == 0) {
			if(idxR == 0) {
				grid.resizeColumns(grid.getColumnCount() + 1);
			}
			else {
				grid.resizeRows(grid.getRowCount() + 1);
			}
		}
//		grid.getCellFormatter().setWidth(idxR, idxC, "50px");
//		grid.getCellFormatter().setHeight(idxR, idxC, "50px");
		grid.getCellFormatter().getElement(idxR, idxC).setPropertyString("align", "center");
		grid.setWidget(idxR, idxC, new FitImage(picture.getImageUrl(), (int) (width / nbC) - 5, (int) (height / nbR)));// - 5 for scrollBar if present
		grid.getWidget(idxR, idxC).getElement().getStyle().setCursor(Cursor.POINTER);
		// TODO BDY: relayout on resize
		
		nbPictures++;
	}

	@Override
	public ResizeLayoutPanel getMainPane() {
		return main;
	}

	@Override
	public void resize(int width, int height) {
		Log.info("Size is " + width + " x " + height);
		this.width = width;
		this.height = height;
		isLandscape = width > height;
		nbC = isLandscape ? maxBig : maxSmall;
		nbR = isLandscape ? maxSmall : maxBig;
	}
	
}
