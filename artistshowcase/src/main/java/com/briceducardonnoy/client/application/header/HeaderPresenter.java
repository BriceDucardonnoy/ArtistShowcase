package com.briceducardonnoy.client.application.header;

import javax.inject.Inject;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

/**
 * This is the top-level presenter of the hierarchy. Other presenters reveal themselves within this presenter.
 */
public class HeaderPresenter extends Presenter<HeaderPresenter.MyView, HeaderPresenter.MyProxy> {
	@Inject
	PlaceManager placeManager;
	
    public interface MyView extends View {
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
    }
}
