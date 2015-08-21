package com.briceducardonnoy.artistshowcase.client.application.unauthorized;

import com.briceducardonnoy.artistshowcase.client.application.ApplicationPresenter;
import com.briceducardonnoy.artistshowcase.client.place.NameTokens;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class UnauthorizedPresenter extends
		Presenter<UnauthorizedPresenter.MyView, UnauthorizedPresenter.MyProxy> {
	interface MyView extends View {
	}

	public static final NestedSlot SLOT_Unauthorized = new NestedSlot();

	@NameToken(NameTokens.unauthorized)
	@ProxyStandard
	interface MyProxy extends ProxyPlace<UnauthorizedPresenter> {
	}

	@Inject
	UnauthorizedPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);

	}

}
