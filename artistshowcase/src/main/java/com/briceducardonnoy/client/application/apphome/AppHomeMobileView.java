package com.briceducardonnoy.client.application.apphome;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.gwt.contentflow4gwt.client.ContentFlow;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.client.application.widgets.UpdatableGrid;
import com.briceducardonnoy.shared.model.Category;
import com.briceducardonnoy.shared.model.Picture;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
	@UiField (provided = true) UpdatableGrid grid;
	
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
		grid = new UpdatableGrid(1, 0);
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
		String tooltip = picture.getProperty("Date") == null ? "NULL" : picture.getProperty("Date").toString();
		FitImage image = new FitImage(picture.getImageUrl(), (int) (width / maxC) - 5, (int) (height / maxR));
		
		image.setAltText(tooltip);
		addFitImage(pos, image);
		Log.info("Picture inserted");
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
		maxC = isLandscape ? maxBig : maxSmall;
		maxR = isLandscape ? maxSmall : maxBig;
		if(grid.getColumnCount() != maxC) {// Switch landscape -> portrait or portrait -> landscape
			changeOrientation();
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
	
	private void changeOrientation() {
		ArrayList<FitImage> images = new ArrayList<>();
		for(int r = 0 ; r < grid.getRowCount() ; r++) {
			for(int c = 0 ; c < grid.getColumnCount() ; c++) {
				images.add((FitImage) grid.getWidget(r, c));
			}
		}
		grid.clear();
		grid.resize(maxR, maxC);
		for(int pos = 0 ; pos < allPictures.size() ; pos++) {
			addFitImage(pos, images.get(pos));
		}
	}
	
	private void addFitImage(int pos, FitImage image) {// Landscape state different for css and resize() because of header panel
		int idxC = pos % maxC;
		int idxR = pos / maxC;
		grid.insertCell(idxR, idxC, maxC);
		image.setMaxSize((int) (width / maxC) - 5, (int) (height / maxR));
		grid.getCellFormatter().getElement(idxR, idxC).setPropertyString("align", "center");
		grid.setWidget(idxR, idxC, image);// - 5 for scrollBar if present
		grid.getWidget(idxR, idxC).getElement().getStyle().setCursor(Cursor.POINTER);
		if(Log.isInfoEnabled()) {
			((FitImage)grid.getWidget(idxR, idxC)).setTitle(image.getAltText());
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
