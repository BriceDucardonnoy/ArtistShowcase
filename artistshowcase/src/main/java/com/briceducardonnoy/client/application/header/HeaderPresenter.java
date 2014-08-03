/**
 * Copyright © Brice DUCARDONNOY
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
package com.briceducardonnoy.client.application.header;

import java.util.ArrayList;

import javax.inject.Inject;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.client.application.context.ApplicationContext;
import com.briceducardonnoy.client.application.events.CategoryChangedEvent;
import com.briceducardonnoy.client.application.events.CategoryChangedEvent.CategoryChangedHandler;
import com.briceducardonnoy.client.application.events.PicturesLoadedEvent;
import com.briceducardonnoy.client.application.utils.Utils;
import com.briceducardonnoy.client.application.widgets.ImageSplitButton;
import com.briceducardonnoy.client.lang.Translate;
import com.briceducardonnoy.client.place.NameTokens;
import com.briceducardonnoy.shared.model.Category;
import com.briceducardonnoy.shared.model.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

/**
 * This is the top-level presenter of the hierarchy. Other presenters reveal themselves within this presenter.
 */
public class HeaderPresenter extends Presenter<HeaderPresenter.MyView, HeaderPresenter.MyProxy> {
	@Inject	PlaceManager placeManager;
	
	private final Translate translate = GWT.create(Translate.class);
	
	private ArrayList<Picture> pictures;
	private ArrayList<Category> categories;
	private String[] picts;
	private int categoriesNumber = 0;
	
    public interface MyView extends View {
    	public FitImage getLogo();
		public Image getFrBtn();
		public Image getEnBtn();
		Panel getMain();
		ImageSplitButton getGallery();
		void addGalleries(ArrayList<Category> categories);
    }

//	@ContentSlot
//	public static final Type<RevealContentHandler<?>> SLOT_SetHeaderContent = new Type<>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_SetMainContent = new Type<>();

    @ProxyStandard
    public interface MyProxy extends Proxy<HeaderPresenter> {
    }

    @Inject
    HeaderPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
        super(eventBus, view, proxy, RevealType.Root);
        categories = new ArrayList<Category>();
		pictures = new ArrayList<Picture>();
		Log.info("Current local is " + LocaleInfo.getCurrentLocale().getLocaleName() + ". "
				+ "Platform and user agent are " + Navigator.getPlatform() + " | " + Navigator.getUserAgent());
    }
    
    @Override
    protected void onBind() {
    	super.onBind();
    	Utils.loadFile(loadListAC, GWT.getHostPageBaseURL() + "Documents/List.txt");
		Utils.showWaitCursor(getView().getMain().getElement());
    	if(getView().getLogo() != null) {// Mobile view hasn't any logo
    		registerHandler(getView().getLogo().addClickHandler(logoClick));
    	}
    	registerHandler(getView().getGallery().getClickHandlerRegistration());
    	registerHandler(getEventBus().addHandler(CategoryChangedEvent.getType(), categoryChangedHandler));
    	registerHandler(getView().getFrBtn().addClickHandler(frHandler));
		registerHandler(getView().getEnBtn().addClickHandler(enHandler));
    }
    
//	Log.info("getHostPageBaseURL: " + GWT.getHostPageBaseURL());// http://127.0.1.1:8888/
//	Log.info("getModuleName: " + GWT.getModuleName());// liliShowcase
//	Log.info("getModuleBaseForStaticFiles: " + GWT.getModuleBaseForStaticFiles());// http://127.0.1.1:8888/liliShowcase/ 
//	Log.info("getModuleBaseURL: " + GWT.getModuleBaseURL());// http://127.0.1.1:8888/liliShowcase/
    
    private void initPictures(String list) {
//		picts = list.replaceAll("\r", "").replaceAll("\n", "").split(";");
		picts = list.replaceAll("\r",  "").split("\n");
		Log.info("Picture " + picts[0]);
		Utils.loadFile(loadInfoAC, GWT.getHostPageBaseURL() + ApplicationContext.PHOTOSFOLDER + "/" + picts[0] + "/Details.txt");
	}
	
	private void loadPictureInfo(String infos, int nextInd) {
		// Store info
		if(!infos.isEmpty() && !infos.contains("HTTP ERROR: 404")) {
			Picture p = new Picture();
			p.addProperty(ApplicationContext.FILEINFO, picts[nextInd - 1]);
			// Special case for imageUrl to build from 'Show'
			String []entries = infos.replaceAll("\r", "").split("\n");
			for(String entry : entries) {
				if(entry.endsWith(";")) {
					entry = entry.replaceAll(";$", "");
				}
				if(entry.startsWith("Categories")) {
					// Add property Categories
					String []categories = entry.substring(entry.indexOf(":") + 1).replaceAll(" ", "").split(",");
					if(categories.length == 0) continue;
					for(String category : categories) {
						if(category.isEmpty()) continue;
						if(category.equalsIgnoreCase("tout")) {
							category = translate.All();			
						}
						boolean found = false;
						for(Category cat : this.categories) {
							if(cat != null && cat.getName().equalsIgnoreCase(category)) {
								found = true;
								p.addCategoryId(cat.getId());
								break;
							}
						}
						if(!found) {
							if(Log.isTraceEnabled()) {
								Log.info("Add category <" + category + ">");
							}
							addNewCategory(new Category(categoriesNumber, category, category));
							p.addCategoryId(categoriesNumber++);
						}
					}
//					p.addProperty("imageUrl", GWT.getHostPageBaseURL() + "photos/" + p.getNameOrTitle() + "/" + p.getProperty("Show"));
					p.addProperty("imageUrl", GWT.getHostPageBaseURL() + ApplicationContext.PHOTOSFOLDER + "/" + picts[nextInd - 1] + "/" + p.getProperty("Show"));
					continue;
				}// End of categories process
				String []prop = entry.split(":");
				if(prop.length == 2) {
					p.addProperty(prop[0].trim(), prop[1].trim());
				}
				else {
					if(Log.isTraceEnabled()) {
						Log.warn("Line <" + entry + "> doesn't contain 2 properties");
					}
					if(prop.length == 1) p.addProperty(prop[0], null);
				}
			}// End of properties process
			pictures.add(p);
		}
		// Browse next picture
		if(nextInd < picts.length) {
			if(!picts[nextInd].isEmpty()) {
				Log.info("Picture " + picts[nextInd]);
				Utils.loadFile(loadInfoAC, GWT.getHostPageBaseURL() + ApplicationContext.PHOTOSFOLDER + "/" + picts[nextInd++] + "/Details.txt");
			}
		}
		// Launch view initialization
		else if(nextInd == picts.length) {
			Log.info("Log picture done!!!");
			getView().addGalleries(categories);
			getEventBus().fireEvent(new PicturesLoadedEvent(pictures, categories));
			ApplicationContext.getInstance().addProperty("categories", categories);
			ApplicationContext.getInstance().addProperty("pictures", pictures);
			Utils.showDefaultCursor(getView().getMain().getElement());
			return;
		}
	}
	
	private void addNewCategory(Category newCat) {
		if(newCat == null) return;
		if(newCat.getName().equalsIgnoreCase(translate.All())) {
			categories.add(0, newCat);
			return;
		}
		for(int i = 0 ; i < categories.size() ; i++) {
			Category cat = categories.get(i);
			// Add new category with alphabetic sort if doesn't exist.
			if(!cat.getName().equalsIgnoreCase(translate.All()) && newCat.getName().compareToIgnoreCase(cat.getName()) > 0) {
				categories.add(i, newCat);
				return;
			}
		}
		categories.add(newCat);
	}
    
    /*
     * Handlers
     */
    private ClickHandler logoClick = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			if(!placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.main)) {
				placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.main).build());
			}
		}
	};
	
	private ClickHandler frHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent arg0) {
			Utils.switchLocale("fr");
		}
	};

	private ClickHandler enHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent arg0) {
			Utils.switchLocale("en");
		}
	};
	
	private CategoryChangedHandler categoryChangedHandler = new CategoryChangedHandler() {
		@Override
		public void onCategoryChanged(CategoryChangedEvent event) {
			if(!placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.main)) {
				placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.main).build());
			}
		}
	};
	
	/*
	 * AsyncCallbacks
	 */
	private AsyncCallback<String> loadListAC = new AsyncCallback<String>() {
		@Override
		public void onFailure(Throwable caught) {
			Log.error("Loading list failed: " + caught.getMessage());
			caught.printStackTrace();
		}
		@Override
		public void onSuccess(String result) {
			if(Log.isTraceEnabled()) {
            	Log.trace("List: \n" + result);
            }
			if(result.isEmpty()) {
				Log.error("List of pictures is empty");
			}
			else {
				initPictures(result);
			}
		}
	};
	
	private AsyncCallback<String> loadInfoAC = new AsyncCallback<String>() {
		private int ind = 0;
		@Override
		public void onFailure(Throwable caught) {
			Log.error("Loading details failed: " + caught.getMessage());
			caught.printStackTrace();
			loadPictureInfo("", ++ind);
		}
		@Override
		public void onSuccess(String result) {
			if(Log.isTraceEnabled()) {
            	Log.trace("Info: \n" + result);
            }
			if(result.isEmpty()) {
				Log.warn("List of pictures is empty");
			}
			loadPictureInfo(result, ++ind);
		}
	};
	
}
