package com.briceducardonnoy.artistshowcase.client.application.pictureviewer;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.artistshowcase.shared.model.Picture;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.reveregroup.gwt.imagepreloader.client.FitImage;
import com.reveregroup.gwt.imagepreloader.client.FitImageLoadEvent;
import com.reveregroup.gwt.imagepreloader.client.FitImageLoadHandler;
public class PictureViewerPresenter extends PresenterWidget<PictureViewerPresenter.MyView>  {
	
    interface MyView extends PopupView  {
    	HTMLPanel getHtmlPanel();
		FitImage getImage();
		FocusPanel getFocusPanel();
		Label getCountLabel();
		Button getCloseButton();
		Button getPrevButton();
		Button getNextButton();
		int getMaxWidth();
		int getMaxHeight();
    }

	private int currentPicture;
	private int nbPictures;
	private List<Picture> pictures;
	
    @Inject
    PictureViewerPresenter(EventBus eventBus, MyView view) {
        super(eventBus, view);
        currentPicture = -1;
		nbPictures = -1;
//		getView().setPopupPositioner(new PopupPositioner() {
//			@Override
//			protected int getTop(int popupHeight) {
//				Log.info("Height is " + popupHeight + " and image is " + getView().getImage().getOffsetHeight());
//				Log.info("Return top " + Math.abs(getView().getImage().getOffsetHeight() + PictureViewerView.Constants.getTopHeight() + 
//						 + PictureViewerView.Constants.getBottomHeight()- popupHeight) / 2);
//				return Math.abs(getView().getImage().getOffsetHeight() + PictureViewerView.Constants.getTopHeight() + 
//						 + PictureViewerView.Constants.getBottomHeight()- popupHeight) / 2;
//			}
//			@Override
//			protected int getLeft(int popupWidth) {
//				Log.info("Width is " + popupWidth);
//				return (getView().getImage().getOffsetWidth() - popupWidth) / 2;
//			}
//		});
		getView().getImage().setMaxSize(getView().getMaxWidth(), getView().getMaxHeight());
    }
    
    private KeyDownHandler keyHandler = new KeyDownHandler() {
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
				close();
			}
			else if(event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
				previous();
			}
			else if(event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
				next();
			}
		}
	};
	
	private ClickHandler clickClose = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			close();
		}
	};
	
	private ClickHandler clickPrev = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			previous();
		}
	};
	
	private ClickHandler clickNext = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			next();
		}
	};
	
	private FitImageLoadHandler imageLoaded = new FitImageLoadHandler() {
		@Override
		public void imageLoaded(FitImageLoadEvent event) {
			Log.info("Relayout after load");
			event.getFitImage().setMaxSize(getView().getMaxWidth(), getView().getMaxHeight());
			getView().showAndReposition();
//			getView().center();
		}
	};

	@Override
	protected void onBind() {
		super.onBind();
//		maxWidth = Utils.getScreenWidth() - 30;
//		maxHeight = Utils.getScreenHeight() - 30;
//		maxWidth = Window.getClientWidth() - 30;
//		maxHeight = Window.getClientHeight() - 20 - 40;
		registerHandler(getView().getFocusPanel().addKeyDownHandler(keyHandler));
		registerHandler(getView().getCloseButton().addClickHandler(clickClose));
		registerHandler(getView().getPrevButton().addClickHandler(clickPrev));
		registerHandler(getView().getNextButton().addClickHandler(clickNext));
		registerHandler(getView().getImage().addFitImageLoadHandler(imageLoaded));
	}
	
	@Override
	protected void onReveal() {
		Log.info("Reveal Picture Viewer");
		getView().getFocusPanel().setFocus(true);
	}
	
	public void setImage(String url) {
//		getView().getImage().setMaxSize(getView().getMaxWidth(), getView().getMaxHeight());
		getView().getImage().setUrl(url);
	}
	
	public void setPictures(List<Picture> list) {
		this.pictures = list;
	}

	public int getCurrentPicture() {
		return currentPicture;
	}

	public void setCurrentPicture(int currentPicture) {
		this.currentPicture = currentPicture;
	}
	
	public void update() {
		int sz = pictures.size();
		nbPictures = sz;
		
		Log.info("Current Picture is " + getView().getImage().getUrl());
		for(int i = 0 ; i < sz ; i++) {
			Log.info("Thumblist[" + i + "] is " + URL.encode(pictures.get(i).getImageUrl()));
			if(URL.encode(pictures.get(i).getImageUrl()).equals(getView().getImage().getUrl())) {
				setCurrentPicture(i);
				getView().getCountLabel().setText(currentPicture+1 + " / " + nbPictures);
				break;
			}
		}
		checkButtonsEnable();
	}

	public int getNbPictures() {
		return nbPictures;
	}

	public void setNbPictures(int nbPictures) {
		this.nbPictures = nbPictures;
	}
	
	private void checkButtonsEnable() {
		if(currentPicture > 0) {
			getView().getPrevButton().setEnabled(true);
		}
		else {
			getView().getPrevButton().setEnabled(false);
		}
		if(currentPicture < nbPictures - 1) {
			getView().getNextButton().setEnabled(true);
		}
		else {
			getView().getNextButton().setEnabled(false);
		}
	}
	
	private void previous() {
		if(currentPicture <= 0) return;
		currentPicture--;
		setImage(URL.encode(pictures.get(currentPicture).getImageUrl()));
		checkButtonsEnable();
		getView().getCountLabel().setText(currentPicture+1 + " / " + nbPictures);
	}
	
	private void next() {
		if(currentPicture >= nbPictures - 1) return;
		currentPicture++;
		setImage(URL.encode(pictures.get(currentPicture).getImageUrl()));
		checkButtonsEnable();
		getView().getCountLabel().setText(currentPicture+1 + " / " + nbPictures);
	}
	
	private void close() {
		getView().hide();
	}
	
}