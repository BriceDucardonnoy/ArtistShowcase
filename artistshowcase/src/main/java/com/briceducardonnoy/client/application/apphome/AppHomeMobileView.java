package com.briceducardonnoy.client.application.apphome;

import java.util.ArrayList;
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
	private int maxC = 0;
	private int maxR = 1;
	private int nbPictures = 0;
	private boolean isLandscape = true;
	private String sortName;
	private Integer currentCategoryId = null;
	
	private ArrayList<Picture> allPictures = null;
	private ArrayList<Integer> orderedPictures = null;

	@Inject
	AppHomeMobileView(Binder uiBinder) {
		grid = new Grid(1, 0);
		initWidget(uiBinder.createAndBindUi(this));
		sortName = "Date";
		allPictures = new ArrayList<>();
		orderedPictures = new ArrayList<>();
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
	public void addCategories(List<Category> categories) {}

	@Override
	public void init() {}

	@Override
	public void addItems(List<Picture> pictures) {}

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
		Log.info("One picture (" + picture.getProperty("Date") + ") loaded: " + picture.getTitleOrName());
		allPictures.add(picture);
		int pos = addInOrderedData(picture, allPictures.size() - 1);
		int idxC = pos % maxC;
		int idxR = pos / maxC;
		insertCell(idxR, idxC);
//		int idxC = nbPictures % maxC;
//		int idxR = nbPictures / maxC;
//		if((nbPictures / maxC) >= grid.getRowCount()) {
//			grid.resizeRows(grid.getRowCount() + 1);
//		}
		// TODO BDY: order
		grid.getCellFormatter().getElement(idxR, idxC).setPropertyString("align", "center");
		grid.setWidget(idxR, idxC, new FitImage(picture.getImageUrl(), (int) (width / maxC) - 5, (int) (height / maxR)));// - 5 for scrollBar if present
		grid.getWidget(idxR, idxC).getElement().getStyle().setCursor(Cursor.POINTER);
		if(Log.isInfoEnabled()) {
			String tooltip = picture.getProperty("Date") == null ? "NULL" : picture.getProperty("Date").toString();
			((FitImage)grid.getWidget(idxR, idxC)).setTitle(tooltip);
		}
		Log.info("Picture inserted");
		nbPictures++;
	}
	
	private void insertCell(int idxR, int idxC) {
		if(idxR >= grid.getRowCount()) {
			grid.resizeRows(grid.getRowCount() + 1);
		}
		for(int r = grid.getRowCount() - 1 ; r >= idxR ; r--) {
			for(int c = maxC-1 ; c >= 0/*idxC*/ ; c--) {// Column min is 0 because on row r+1, all the columns are concerned
				if(grid.getWidget(r, c) != null) {
					Log.info("Move (rxc): (" + r + "x" + c + ") " + ((FitImage)grid.getWidget(r, c)).getTitle());
					shiftCell(r, c);
				}
				if(idxR == r && idxC == c) break;// Work is finished
			}
		}
	}
	
	private void shiftCell(int r, int c) {
		int newC;
		int newR = r;
		if(c == grid.getColumnCount() - 1) {// Shift to next row
			newC = 0;
			newR = r + 1;
		}
		else {
			newC = c + 1;
		}
		if(newR >= grid.getRowCount()) {
			grid.resizeRows(grid.getRowCount() + 1);
		}
		grid.setWidget(newR, newC, grid.getWidget(r, c));
		grid.getCellFormatter().getElement(newR, newC).setPropertyString("align", "center");
		grid.getWidget(newR, newC).getElement().getStyle().setCursor(Cursor.POINTER);
		grid.clearCell(r, c);
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
		maxC = isLandscape ? maxBig : maxSmall;
		maxR = isLandscape ? maxSmall : maxBig;
		if(grid.getColumnCount() != maxC) {
			grid.resize(maxR, maxC);
			// TODO BDY: redo grid
		}
		else {
			for(int r = 0 ; r < grid.getRowCount() ; r++) {
				for(int c = 0 ; c < grid.getColumnCount() ; c++) {
					FitImage img = (FitImage) grid.getWidget(r, c);
					if(img != null) {
						img.setMaxSize((int) (width / maxC) - 5, (int) (height / maxR));// - 5 for scrollBar if present
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private int addInOrderedData(Picture pojo, Integer refIdx) {
		// If POJO doesn't contain sortName property, add it at the end
		if(pojo.getProperty(sortName) == null) {
			orderedPictures.add(refIdx);
			return orderedPictures.size() - 1;
		}
		for(int i = 0 ; i < orderedPictures.size() ; i++) {
			Picture pict = allPictures.get(orderedPictures.get(i));
			if(pojo == pict) continue;// Doesn't test with itself
			if(pict.getProperty(sortName) == null || 
					((Comparable<Object>)pict.getProperty(sortName)).compareTo(pojo.getProperty(sortName)) < 0) {
				orderedPictures.add(i, refIdx);
				return i;
			}
		}
		orderedPictures.add(refIdx);
		return orderedPictures.size() - 1;
	}
	
}
