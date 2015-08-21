package com.briceducardonnoy.artistshowcase.client.application.error;

import com.briceducardonnoy.artistshowcase.client.application.ApplicationPresenter;
import com.briceducardonnoy.artistshowcase.client.place.NameTokens;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class ErrorPresenter extends
		Presenter<ErrorPresenter.MyView, ErrorPresenter.MyProxy> {
	interface MyView extends View {
	}

	public static final NestedSlot SLOT_Error = new NestedSlot();

	@NameToken(NameTokens.error)
	@ProxyCodeSplit
	interface MyProxy extends ProxyPlace<ErrorPresenter> {
	}

	@Inject
	ErrorPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);

	}

}
