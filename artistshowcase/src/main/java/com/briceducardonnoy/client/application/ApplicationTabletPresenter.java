package com.briceducardonnoy.client.application;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.client.application.header.HeaderPresenter;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class ApplicationTabletPresenter extends AbstractApplicationPresenter implements ApplicationUiHandlers {
	
	@Inject
	public ApplicationTabletPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy, HeaderPresenter.SLOT_SetMainContent);
		
		view.setUiHandlers(this);
		Log.info("Tablet presenter");
	}

}
