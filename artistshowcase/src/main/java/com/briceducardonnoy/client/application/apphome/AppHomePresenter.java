package com.briceducardonnoy.client.application.apphome;

import com.briceducardonnoy.client.application.header.HeaderPresenter;
import com.briceducardonnoy.client.place.NameTokens;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class AppHomePresenter extends
		Presenter<AppHomePresenter.MyView, AppHomePresenter.MyProxy> implements
		AppHomeUiHandlers {
	interface MyView extends View, HasUiHandlers<AppHomeUiHandlers> {
	}

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

	protected void onBind() {
		super.onBind();
	}

	protected void onHide() {
		super.onHide();
	}

	protected void onReset() {
		super.onReset();
	}

}
