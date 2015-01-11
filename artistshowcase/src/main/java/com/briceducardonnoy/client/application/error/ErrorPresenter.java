package com.briceducardonnoy.client.application.error;

import com.briceducardonnoy.client.application.header.HeaderPresenter;
import com.briceducardonnoy.client.place.NameTokens;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class ErrorPresenter extends
		Presenter<ErrorPresenter.MyView, ErrorPresenter.MyProxy> {
	interface MyView extends View {
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> SLOT_Error = new Type<RevealContentHandler<?>>();

	@NameToken(NameTokens.error)
	@ProxyCodeSplit
	interface MyProxy extends ProxyPlace<ErrorPresenter> {
	}

	@Inject
	ErrorPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, HeaderPresenter.SLOT_SetMainContent);

	}

}
