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

import java.util.List;

import org.gwt.contentflow4gwt.client.ContentFlow;
import org.gwt.contentflow4gwt.client.ContentFlowItemClickListener;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.client.application.context.ApplicationContext;
import com.briceducardonnoy.client.application.events.CategoryChangedEvent;
import com.briceducardonnoy.client.application.events.CategoryChangedEvent.CategoryChangedHandler;
import com.briceducardonnoy.client.application.events.PicturesLoadedEvent;
import com.briceducardonnoy.client.application.events.PicturesLoadedEvent.PicturesLoadedHandler;
import com.briceducardonnoy.client.application.header.HeaderPresenter;
import com.briceducardonnoy.client.lang.Translate;
import com.briceducardonnoy.client.place.NameTokens;
import com.briceducardonnoy.shared.model.Category;
import com.briceducardonnoy.shared.model.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class AppHomePresenter extends Presenter<AppHomePresenter.MyView, AppHomePresenter.MyProxy> implements AppHomeUiHandlers {
	interface MyView extends View, HasUiHandlers<AppHomeUiHandlers> {
		void addCategories(List<Category> categories);
		void init();
		void addItems(List<Picture> pictures);
		ContentFlow<Picture> getContentFlow();
		Picture getCurrentPicture();
		void resize();
		void changeCurrentCategory(Integer categoryId);
	}
	
	@Inject	PlaceManager placeManager;
	
	private final Translate translate = GWT.create(Translate.class);

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_AppHome = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.main)
	@ProxyCodeSplit
	public interface MyProxy extends ProxyPlace<AppHomePresenter> {
	}

	@Inject
	public AppHomePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, HeaderPresenter.SLOT_SetMainContent);

		getView().setUiHandlers(this);
	}

	@SuppressWarnings("unchecked")
	protected void onBind() {
		super.onBind();
		registerHandler(getEventBus().addHandler(PicturesLoadedEvent.getType(), pictureLoadedHandler));
		registerHandler(getView().getContentFlow().addItemClickListener(contentFlowClickListener));
		registerHandler(getEventBus().addHandler(CategoryChangedEvent.getType(), categoryChangedHandler));
		if(ApplicationContext.getInstance().getProperty("pictures") != null) {
			initDataAndView((List<Category>) ApplicationContext.getInstance().getProperty("categories"), 
					(List<Picture>)ApplicationContext.getInstance().getProperty("pictures"));
		}
	}
	
//	@Override
//	protected void onReveal() {
//		super.onReveal();
//		getView().resize();
//	}
//
//	protected void onHide() {
//		super.onHide();
//	}
//
//	protected void onReset() {
//		super.onReset();
//	}
	
	private void initDataAndView(List<Category> categories, List<Picture> pictures) {
		getView().addCategories(categories);
		getView().addItems(pictures);// Initialize cover flow
		getView().init();
	}
	
	// Handlers and events
	private ContentFlowItemClickListener contentFlowClickListener = new ContentFlowItemClickListener() {
        public void onItemClicked(Widget widget) {
        	Log.info(translate.Selection(), translate.YouClickOn() + " " + getView().getCurrentPicture().getTitle());
//        	placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.detail).with(ApplicationContext.DETAIL_KEYWORD, 
//        			(String) getView().getCurrentPicture().getProperty(ApplicationContext.FILEINFO)).build());
        }
    };
    
    private PicturesLoadedHandler pictureLoadedHandler = new PicturesLoadedHandler() {
		@Override
		public void onPicturesLoaded(PicturesLoadedEvent event) {
			initDataAndView(event.getCategories(), event.getPictures());
		}
	};
	
	private CategoryChangedHandler categoryChangedHandler = new CategoryChangedHandler() {
		@Override
		public void onCategoryChanged(CategoryChangedEvent event) {
			AppHomePresenter.this.getView().changeCurrentCategory(event.getCategoryId());
		}
	};
}
