package com.briceducardonnoy.artistshowcase.client.application.apphome;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.gwt.contentflow4gwt.client.ContentFlow;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.artistshowcase.client.application.widgets.UpdatableGrid;
import com.briceducardonnoy.artistshowcase.client.lang.Translate;
import com.briceducardonnoy.artistshowcase.shared.model.Category;
import com.briceducardonnoy.artistshowcase.shared.model.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

public class AppHomeMobileView extends ViewWithUiHandlers<AppHomeUiHandlers> implements AppHomePresenter.MyView {
	interface Binder extends UiBinder<Widget, AppHomeMobileView> {
	}

	private final Translate translate = GWT.create(Translate.class);
	private static final int maxBig = 4;
	private static final int maxSmall = 2;

	@UiField ResizeLayoutPanel main;
	@UiField ScrollPanel scrollPane;
	@UiField (provided = true) UpdatableGrid grid;
	
	private int width = 0;
	private int height = 0;
	private int maxC = maxBig;
	private int maxR = 1;
	private boolean isLandscape = true;
	private String sortName;
	private Integer currentCategoryId = 0;// All
	
	private ArrayList<Picture> allPictures = null;
	private ArrayList<FitImage> allImages = null;

	@Inject
	AppHomeMobileView(Binder uiBinder) {
		grid = new UpdatableGrid(1, maxC);
		initWidget(uiBinder.createAndBindUi(this));
		sortName = "Date";
		allPictures = new ArrayList<>();
		allImages = new ArrayList<>();
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
		for(Category cat : categories) {
			if(cat.getName().equals(translate.All())) {
				changeCurrentCategory(cat.getId());
				break;
			}
		}
	}

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
		return null;// Can't return anything in mobile view
	}

	@Override
	public void changeCurrentCategory(Integer categoryId) {
		Log.info("Change to category " + categoryId);
		if(categoryId.equals(currentCategoryId)) return;
		currentCategoryId = categoryId;
		refreshGrid(allImages);
	}

	@Override
	public void addPicture(Picture picture) {
		/*
		 * read is per line:
		 * 1 2 3
		 * 4 5 6
		 * Define max number of column and size of each item depending
		 * of orientation and size
		 * Maybe get it in onReset because it cannot be done in @media
		 */
		Log.info("One picture (" + picture.getProperty("Date") + ") loaded: " + picture.getTitleOrName());
		int pos = addInOrderedData(picture);
		String tooltip = picture.getProperty("Date") == null ? "NULL" : picture.getProperty("Date").toString();
		Log.info("width is " + width + " and maxC is " + maxC);
		FitImage image = new FitImage(picture.getImageUrl());
		allImages.add(pos, image);
		
		image.setAltText(tooltip);
		addFitImage(pos, image);
		Log.info("Picture inserted");
	}
	
	@Override
	public ResizeLayoutPanel getMainPane() {
		return main;
	}

	@Override
	public void resize(int width, int height) {
		/*
		 * For an unknown reason, when resizing (and so switching orientation), the resize is done in 2 times: 
		 * a 1st one with small update: Resize::Size is 433 x 751 and original size is 423x730
		 * and the real one.
		 */
//		if(Math.abs(this.width - width) + Math.abs(this.height - height) < 35) return;
		Log.info("Resize::Size is " + width + " x " + height + " and original size is " + this.width + "x" + this.height);
		this.width = width;
		this.height = height;
		isLandscape = Window.getClientWidth() >= Window.getClientHeight();// To be in accordance with @media css
		maxC = isLandscape ? maxBig : maxSmall;
		maxR = isLandscape ? maxSmall : maxBig;
		if(grid.getColumnCount() != maxC) {// Switch landscape -> portrait or portrait -> landscape
			refreshGrid(allImages);
		}
		else {
			Log.info("Just resize");
			for(int r = 0 ; r < grid.getRowCount() ; r++) {
				for(int c = 0 ; c < grid.getColumnCount() ; c++) {
					FitImage img = (FitImage) grid.getWidget(r, c);
					if(img != null) {
//						Log.info("Set max size to " + ((width / maxC) - 5) + "x" + (height / maxR));
						img.setMaxSize(Math.max((int) (width / maxC) - 5, 0), (int) (height / maxR));// - 5 for horizontal scroll
						grid.getCellFormatter().setWidth(r, c, img.getMaxWidth() + "px");
						grid.getCellFormatter().setHeight(r, c, img.getMaxHeight() + "px");
					}
				}
			}
		}
	}
	
	/**
	 * Clear the grid and add images among the <code>images</code> that are in current category <code>currentCategoryId</code>
	 * It also relayout the images.
	 * @param images The list of images to sort (current category) and to display
	 */
	private void refreshGrid(ArrayList<FitImage> images) {
		Log.info("RefreshGrid::Clear grid with category " + currentCategoryId);
		int pos = 0;
		grid.clear();
		grid.resize(maxR, maxC);
		for(int i = 0 ; i < allPictures.size() ; i++) {
			if(allPictures.get(i).getCategoryIds().contains(currentCategoryId)) {
				Log.info("Add picture " + allPictures.get(i).getTitleOrName() + " (" + images.get(i).getAltText() + ")");
				addFitImage(pos++, images.get(i));
			}
		}
	}
	
	/**
	 * Add and image in <code>grid</code> at position <code>pos</code>
	 * Function calculates the row and column indexes <code>pos</code> corresponds
	 * @param pos The position of the image to insert
	 * @param image The image to insert 
	 */
	private void addFitImage(int pos, FitImage image) {
		int idxC = pos % maxC;
		int idxR = pos / maxC;
		grid.insertCell(idxR, idxC, maxC);
		
		image.setMaxSize(Math.max((int) (width / maxC) - 5, 0), (int) (height / maxR));// - 5 for horizontal scroll
		grid.getCellFormatter().setWidth(idxR, idxC, image.getMaxWidth() + "px");
		grid.getCellFormatter().setHeight(idxR, idxC, image.getMaxHeight() + "px");
		
		grid.getCellFormatter().getElement(idxR, idxC).setPropertyString("align", "center");
		grid.setWidget(idxR, idxC, image);
		grid.getWidget(idxR, idxC).getElement().getStyle().setCursor(Cursor.POINTER);
		if(Log.isInfoEnabled()) {
			((FitImage)grid.getWidget(idxR, idxC)).setTitle(image.getAltText());
		}
	}
	
	@SuppressWarnings("unchecked")
	private int addInOrderedData(Picture pojo) {
		// If POJO doesn't contain sortName property, add it at the end
		if(pojo.getProperty(sortName) == null) {
			allPictures.add(pojo);
			return allPictures.size() - 1;
		}
		for(int i = 0 ; i < allPictures.size() ; i++) {
			Picture pict = allPictures.get(i);
			if(pojo == pict) continue;// Doesn't test with itself
			if(pict.getProperty(sortName) == null || 
					((Comparable<Object>)pict.getProperty(sortName)).compareTo(pojo.getProperty(sortName)) < 0) {
				allPictures.add(i, pojo);
				return i;
			}
		}
		allPictures.add(pojo);
		return allPictures.size() - 1;
	}
	
}
