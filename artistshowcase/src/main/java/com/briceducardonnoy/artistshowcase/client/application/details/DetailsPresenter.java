package com.briceducardonnoy.artistshowcase.client.application.details;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.artistshowcase.client.application.ApplicationPresenter;
import com.briceducardonnoy.artistshowcase.client.application.context.ApplicationContext;
import com.briceducardonnoy.artistshowcase.client.application.events.PicturesLoadedEvent;
import com.briceducardonnoy.artistshowcase.client.application.events.PicturesLoadedEvent.PicturesLoadedHandler;
import com.briceducardonnoy.artistshowcase.client.application.gateKeepers.DetailsGateKeeper;
import com.briceducardonnoy.artistshowcase.client.application.pictureviewer.PictureViewerPresenter;
import com.briceducardonnoy.artistshowcase.client.application.utils.Utils;
import com.briceducardonnoy.artistshowcase.client.lang.Translate;
import com.briceducardonnoy.artistshowcase.client.place.NameTokens;
import com.briceducardonnoy.artistshowcase.shared.model.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

public class DetailsPresenter extends Presenter<DetailsPresenter.MyView, DetailsPresenter.MyProxy> {
	
	interface MyView extends View {
		ResizeLayoutPanel getMainPane();
		Image getMainImage();
		FitImage getCenterImage();
		void updateMainImage(String url);
		void updateDetailInfo(String html);
		void updateThumbs(ArrayList<String> urls);
		List<Picture> getPicturesList();
		void resize();
		void clearData();
	}
	
	private final Translate translate = GWT.create(Translate.class);
	private final int MAXWAITTIME = 20;

	@Inject PlaceManager placeManager;
	@Inject PictureViewerPresenter pictureViewer;
	private String pictureFolder = "";
	private ArrayList<Picture> pictures = null;
	private Picture currentPicture = null;
	private String locale;
	private boolean arePicturesLoaded;
	private int waitTime;
	
	private PicturesLoadedHandler pictureLoadedHandler = new PicturesLoadedHandler() {
		@Override
		public void onPicturesLoaded(PicturesLoadedEvent event) {
			pictures = event.getPictures();
			if(Log.isTraceEnabled()) {
				Log.trace("Pictures loaded: " + pictures.size());
			}
			arePicturesLoaded = true;
		}
	};
	
	private ClickHandler centerImageHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			pictureViewer.setPictures(getView().getPicturesList());
			pictureViewer.setImage(getView().getCenterImage().getUrl());
			pictureViewer.update();
			addToPopupSlot(pictureViewer);//, true);
		}
	};
	
	private ResizeHandler resize = new ResizeHandler() {
		@Override
		public void onResize(ResizeEvent event) {
			Log.info(event.getWidth() + " x " + event.getHeight());
			getView().resize();
		}
	};
	
	private RepeatingCommand loadPicturesWaitCmd = new RepeatingCommand() {
		@Override
		public boolean execute() {
			if(arePicturesLoaded) {
//				waitBox.hide();
				Utils.showDefaultCursor(getView().getMainPane().getElement());
				if(initializeCurrentPicture()) {
					// Shows picture information
					showPictureMainThumbAndInfo();
					// Shows pictures thumbs
					showPicturesThumb();
				}
				return false;
			}
			if(waitTime >= MAXWAITTIME) {
//				waitBox.hide();
				Utils.showDefaultCursor(getView().getMainPane().getElement());
				placeManager.revealErrorPlace(NameTokens.details);
				return false;
			}
			waitTime++;
			return true;
		}
	};
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_details = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.details)
	@ProxyCodeSplit
	@UseGatekeeper(DetailsGateKeeper.class)
	interface MyProxy extends ProxyPlace<DetailsPresenter> {
	}

	@SuppressWarnings("unchecked")
	@Inject
	DetailsPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		// FIXME BDY: if resize on this page, back to home is empty.
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);
		locale = LocaleInfo.getCurrentLocale().getLocaleName();
		pictures = (ArrayList<Picture>) ApplicationContext.getInstance().getProperty("pictures");
		arePicturesLoaded = false;
//		waitBox = new AutoProgressMessageBox(translate.Loading());
//		waitBox.setTitle(translate.Loading());
//		waitBox.setMessage(translate.LoadingMessage());
//		waitBox.setProgressText(translate.Loading());
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		pictureFolder = request.getParameter(ApplicationContext.DETAIL_KEYWORD, "wildCard");
		// PictureName is now available in onReveal and onReset method
		// If pictureName isn't ok, redirect to unauthorized page (URL set manually)
	}

	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getEventBus().addHandler(PicturesLoadedEvent.getType(), pictureLoadedHandler));
		registerHandler(getView().getMainPane().addResizeHandler(resize));
		registerHandler(getView().getCenterImage().addClickHandler(centerImageHandler));
	}
	
	@Override
	protected void onReveal() {
		super.onReveal();
		// Retrieves chosen picture
		Log.info("Picture folder name is " + pictureFolder);
		if(pictures == null || pictures.isEmpty()) {
			// If pictures aren't loaded, wait for 20s that's done to display good one or redirect to error place
			if(arePicturesLoaded) {
				placeManager.revealErrorPlace(NameTokens.details);
				return;				
			}
			waitTime = 0;
			// TODO BDY: waitbox
//			waitBox.show();
//			waitBox.auto();
			Utils.showWaitCursor(getView().getMainPane().getElement());
			Scheduler.get().scheduleFixedDelay(loadPicturesWaitCmd, 1000);// 1s
			return;
		}
		if(initializeCurrentPicture()) {
			// Shows picture information
			showPictureMainThumbAndInfo();
			// Shows pictures thumbs
			showPicturesThumb();
		}
	}
	
	@Override
	protected void onHide() {
		super.onHide();
		getView().clearData();
		Log.info("OnHide");
		removeFromPopupSlot(pictureViewer);
	}

	private boolean initializeCurrentPicture() {
		// No need to reload info, already in picture, just need to load images
		if(Log.isTraceEnabled()) {
			Log.trace("Picture folder: " + pictureFolder);
		}
		for(Picture picture : pictures) {
			if(Log.isTraceEnabled()) {
				Log.trace("Prop " + picture.getProperty(ApplicationContext.FILEINFO, ""));
			}
			if(picture.getProperty(ApplicationContext.FILEINFO, "").equals(pictureFolder)) {
				currentPicture = picture;
				break;
			}
		}
		if(currentPicture == null) {
			placeManager.revealUnauthorizedPlace(NameTokens.details);
			return false;
		}
		return true;
	}
	
	private void showPictureMainThumbAndInfo() {
		// Main thumb
		getView().updateMainImage(currentPicture.getImageUrl());
		// Info
		String info = "<div style=\"" +
				"color: #DDDDDD;" +
				"font-family: helvetica; " +
				"font-size: 14px;" +
				"padding-left: 10px;" +
				"overflow: auto;" +
				"\">";
		info += "<div style=\"text-align: center; font-size: 18px;\"><b>" + translate.Details() + "</b></div><br />";
		info += "<div style=\"padding-left: 10px;\">";
		info += translate.Title() + Utils.getSeparatorDependingLocale(locale) + currentPicture.getTranslatedTitle(locale) + "<br />";
		info += translate.Dimension() + Utils.getSeparatorDependingLocale(locale) + currentPicture.getProperty("Dimension", "") + "<br />";
		info += translate.Medium() + Utils.getSeparatorDependingLocale(locale) + currentPicture.getProperty("Medium", "") + "<br />";
		info += translate.Date() + Utils.getSeparatorDependingLocale(locale) + currentPicture.getProperty("Date", "") + "<br />";
		if(((String) currentPicture.getProperty("Price", "")).equalsIgnoreCase("vendu")) {
			info += translate.Price() + Utils.getSeparatorDependingLocale(locale) + translate.Sold() + "<br />";
		}
		info += "</div></div>";
		getView().updateDetailInfo(info);
	}
	
	private void showPicturesThumb() {
		String details = (String) currentPicture.getProperty("Details");
		ArrayList<String> thumbsArray = new ArrayList<String>();
		if(details != null && !details.isEmpty()) {
			String thumbs[] = details.substring(details.indexOf(":") + 1).split(",");
//			thumbsArray.add(currentPicture.getImageUrl());// Add main picture to the details
			if(thumbs.length > 0) {// Add the others details
				for(String thumb : thumbs) {
					thumbsArray.add(GWT.getHostPageBaseURL() + ApplicationContext.PHOTOSFOLDER + "/" + pictureFolder + "/" + thumb.trim());
					if(Log.isTraceEnabled()) {
						Log.trace("Add detail " + thumbsArray.get(thumbsArray.size() - 1));
					}
				}
			}
		}
		getView().updateThumbs(thumbsArray);
	}
	
}
