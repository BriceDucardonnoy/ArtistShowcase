package com.briceducardonnoy.client.application.home;

import javax.inject.Inject;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.client.application.AbstractApplicationPresenter;
import com.briceducardonnoy.client.application.ApplicationPresenter;
import com.briceducardonnoy.client.application.ApplicationUiHandlers;
import com.briceducardonnoy.client.lang.Translate;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class ApplicationDesktopPresenter extends AbstractApplicationPresenter implements ApplicationUiHandlers {
	@Inject
	PlaceManager placeManager;
	
	private final Translate translate = GWT.create(Translate.class);

	@Inject
	public ApplicationDesktopPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);

        view.setUiHandlers(this);
        
        Log.info("Desktop presenter");
        Log.info(translate.Selection());
        
    }

}
