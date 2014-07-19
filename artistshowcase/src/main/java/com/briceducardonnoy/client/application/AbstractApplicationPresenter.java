package com.briceducardonnoy.client.application;

import com.briceducardonnoy.client.place.NameTokens;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.Title;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public abstract class AbstractApplicationPresenter extends Presenter<AbstractApplicationPresenter.MyView, AbstractApplicationPresenter.MyProxy> {
	
	@ProxyCodeSplit
    @NameToken(NameTokens.homePage)
    @Title("Home")
    public interface MyProxy extends ProxyPlace<AbstractApplicationPresenter> {
    }

    public interface MyView extends View, HasUiHandlers<ApplicationUiHandlers> {
    }

    public AbstractApplicationPresenter(EventBus eventBus,
							    		MyView view,
							            MyProxy proxy,
							            GwtEvent.Type<RevealContentHandler<?>> slot) {
        super(eventBus, view, proxy, slot);
    }
}
