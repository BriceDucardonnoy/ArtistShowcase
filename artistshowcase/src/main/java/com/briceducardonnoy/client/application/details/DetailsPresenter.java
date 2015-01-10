package com.briceducardonnoy.client.application.details;

import java.util.ArrayList;
import java.util.List;

import com.briceducardonnoy.client.application.gateKeepers.DetailsGateKeeper;
import com.briceducardonnoy.client.application.header.HeaderPresenter;
import com.briceducardonnoy.client.place.NameTokens;
import com.briceducardonnoy.shared.model.Picture;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

public class DetailsPresenter extends Presenter<DetailsPresenter.MyView, DetailsPresenter.MyProxy> {
	
	interface MyView extends View {
		public Image getMainImage();
		public FitImage getCenterImage();
		public void updateMainImage(String url);
		public void updateDetailInfo(String html);
		public void updateThumbs(ArrayList<String> thumbsArray);
		public List<Picture> getPicturesList();
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_details = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.details)
	@ProxyCodeSplit
	@UseGatekeeper(DetailsGateKeeper.class)
	interface MyProxy extends ProxyPlace<DetailsPresenter> {
	}

	@Inject
	DetailsPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
//		super(eventBus, view, proxy, AppHomePresenter.SLOT_AppHome);
		super(eventBus, view, proxy, HeaderPresenter.SLOT_SetMainContent);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
//		pictureFolder = request.getParameter(ApplicationContext.DETAIL_KEYWORD, "wildCard");
		// PictureName is now available in onReveal and onReset method
		// If pictureName isn't ok, redirect to unauthorized page (URL set manually)
	}

	protected void onBind() {
		super.onBind();
	}

	protected void onReset() {
		super.onReset();
	}
}
