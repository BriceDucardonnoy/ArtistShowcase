/*
 * Copyright © Brice DUCARDONNOY
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
 * In no event shall the authors or copyright holders be liable for any claim, 
 * damages or other liability, whether in an action of contract, tort or otherwise, 
 * arising from, out of or in connection with the software or the use or other 
 * dealings in the Software.
 */
package com.briceducardonnoy.client.application.apphome;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.gwt.contentflow4gwt.client.ContentFlow;
import org.gwt.contentflow4gwt.client.PhotoView;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.client.application.utils.Utils;
import com.briceducardonnoy.shared.model.Category;
import com.briceducardonnoy.shared.model.Picture;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.reveregroup.gwt.imagepreloader.client.FitImage;
import com.reveregroup.gwt.imagepreloader.client.FitImageLoadEvent;
import com.reveregroup.gwt.imagepreloader.client.FitImageLoadHandler;

public class AppHomeView extends ViewWithUiHandlers<AppHomeUiHandlers> implements AppHomePresenter.MyView {
	interface Binder extends UiBinder<Widget, AppHomeView> {
	}

	@UiField ResizeLayoutPanel main;
	@UiField ContentFlow<Picture> contentFlow;
	
	private ArrayList<PhotoView> allPictures = null;
	private ArrayList<Integer> orderedPictures = null;
	private ArrayList<Category> categories = null;// TODO BDY: not sure it's necessary
	// TODO BDY: add "Vendu" below the dimension in desktop coverflow
	private FitImageLoadHandler flh;
	
	private String sortName;
	private int loadedPictures = 0;
	private boolean isResizing = false;
	private Integer currentCategoryId = null;

	@Inject
	AppHomeView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		
		sortName = "Date";
		allPictures = new ArrayList<>();
		orderedPictures = new ArrayList<>();
		categories = new ArrayList<>();
		flh = new FitImageLoadHandler() {
			@Override
			public void imageLoaded(FitImageLoadEvent event) {
				loadedPictures++;
				Log.info("Image loaded : " + loadedPictures + " / " + allPictures.size() + ": " + ((FitImage)event.getSource()).getUrl());
//				Utils.updateProgressToolbar((loadedPictures * 100) / allPictures.size());
				if(loadedPictures >= allPictures.size()) {
					Utils.showDefaultCursor(main.getElement());
//					Utils.hideLoadingProgressToolbar();
				}
			}
		};
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
	public void init() {
		Utils.showWaitCursor(main.getElement());
//		Utils.showLoadingProgressToolbar();
		Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
			@Override
			public boolean execute() {
				if(contentFlow.isAttached()) {
					contentFlow.init();
					Log.info("Init done");
//					Utils.showDefaultCursor(mainPane.getBody());
					return false;
				}
				return true;
			}
		}, 50);
	}
	
	@Override
	public void addCategories(List<Category> categories) {
		if(categories == null || categories.isEmpty()) return;
		this.categories = new ArrayList<>();
		this.categories.addAll(categories);
		currentCategoryId = categories.get(0).getId();
	}
	
	@Override
	public void addItems(List<Picture> people) {
		int number = people.size();
        for (final Picture picture : generatePictures(people, number)) {
        	createImageView(picture);
        	addInOrderedData(picture, allPictures.size() - 1);
        }
        for(Integer i : orderedPictures) {
			if(!((Picture)allPictures.get(i).getPojo()).getCategoryIds().isEmpty()) {// If belongs to no category, doens't display it
				contentFlow.addItems(allPictures.get(i));
			}
			else {
				loadedPictures++;
			}
		}
    }
	
	private Picture[] generatePictures(List<Picture> pictures, int number) {
        Picture[] result = new Picture[number];

        for (int i = 0; i < number; i++) {
            result[i] = pictures.get(i % number);
        }

        return result;
    }
	
	private PhotoView createImageView(Picture picture) {
		String title = picture.getTitle();
		String dim = picture.getProperty("Dimension", "").toString();
		String date = picture.getProperty("Date", "").toString();
		if(title == null) title = "";
		if(dim == null) dim = "";
		if(date == null) date = "";
		allPictures.add(new PhotoView(new FitImage(picture.getImageUrl(), flh), title + "<br />" + dim + 
				(Log.isInfoEnabled() ? "<br />" + date : ""), picture));
        return allPictures.get(allPictures.size() - 1);
    }
	
	@SuppressWarnings("unchecked")
	private void addInOrderedData(Picture pojo, Integer refIdx) {
		// If POJO doesn't contain sortName property, add it at the end
		if(pojo.getProperty(sortName) == null) {
			orderedPictures.add(refIdx);
			return;
		}
		for(int i = 0 ; i < orderedPictures.size() ; i++) {
			Picture pict = (Picture) allPictures.get(orderedPictures.get(i)).getPojo();
			if(pojo == pict) continue;// Doesn't test with itself
			if(pict.getProperty(sortName) == null || 
					((Comparable<Object>)pict.getProperty(sortName)).compareTo(pojo.getProperty(sortName)) < 0) {
				orderedPictures.add(i, refIdx);
				return;
			}
		}
		orderedPictures.add(refIdx);
	}
	
	@Override
	public void changeCurrentCategory(final Integer categoryId) {
		// Wait for cover flow to be initialized and change current category
		// That case can append if another page is loaded in first and then a category selection is done without going to home before
		Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
			@Override
			public boolean execute() {
				if(contentFlow.isInit() && loadedPictures >= allPictures.size() && !isResizing) {
					Log.info("Start change current category");
					startChangeCurrentCategory(categoryId);
					return false;
				}
				return true;
			}
		}, 1000);// 1s
	}
	
	private void startChangeCurrentCategory(final Integer categoryId) {
		if(categoryId.equals(currentCategoryId)) return;
		currentCategoryId = categoryId;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				refreshCoverFlow();
			}
		});
	}
	
	/**
	 * Display pictures for given category
	 * Clear ordered pictures and add new ones in specific order
	 * Then add it in DOM
	 */
	public void refreshCoverFlow() {
		/*
		 *  Remove objects pushed twice in target (objects from contentflow project in public)
		 */
		/*
		 *  Update data
		 */
		// Pictures are removed from view but PhotoView widget keeps last class/style state (as 'active' or 'display:block') => clean it
		for(Integer i : orderedPictures) {
			allPictures.get(i).getContainer().removeStyleName("active");
			allPictures.get(i).getContainer().setVisible(false);
		}
		orderedPictures.clear();
		int sz = allPictures.size();
		for(int i = 0 ; i < sz ; i++) {
			Picture p = (Picture) allPictures.get(i).getPojo();
			if(p.getCategoryIds().contains(currentCategoryId)) {
				addInOrderedData(p, i);
			}
		}
		/*
		 *  Update contentFlow
		 */
		contentFlow.removeAll();
		for(Integer i : orderedPictures) {
			contentFlow.addItem(allPictures.get(i));
		}
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				if(Log.isTraceEnabled()) {
					Log.trace("Refresh DOM");
				}
				// Lame hack
				contentFlow.refreshActiveItem(orderedPictures.size() * 28);
			}
		});
	}
	
	@Override
	public void resize(int width, int height) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				if(contentFlow.isInit()) {
					isResizing = true;
					contentFlow.resize();
					isResizing = false;
				}
			}
		});
	}
	
	@Override
	public Picture getCurrentPicture() {
		return (Picture) ((PhotoView)contentFlow.getActiveItem()).getPojo();
	}
	
	@Override
	public ContentFlow<Picture> getContentFlow() {
		return contentFlow;
	}
	
	/*
	 * As for UiHandler, method name has no importance, it's the parameter type of the return...
	 */
	@UiFactory
	ContentFlow<Picture> createContentFlow() {
		return new ContentFlow<Picture>(true, true, 1.5f);
	}

	@Override
	public void addPicture(Picture picture) {
		// Does nothing now. That's for mobile version. Maybe usefull for one-by-one loading
	}

	@Override
	public ResizeLayoutPanel getMainPane() {
		return main;
	}

}
