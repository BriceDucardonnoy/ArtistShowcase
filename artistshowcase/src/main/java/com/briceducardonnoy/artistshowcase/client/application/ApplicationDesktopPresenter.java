/*
 * Copyright © Brice DUCARDONNOY
 * Permission is hereby granted, free of charge, to any person obtaining 
 * a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 * - The above copyright notice and this permission notice shall be included 
 * 	in all copies or substantial portions of the Software.
 * - The Software is provided "as is", without warranty of any kind, express 
 * 	or implied, including but not limited to the warranties of merchantability, 
 * 	fitness for a particular purpose and noninfringement. 
 * In no event shall the authors or copyright holders be liable for any claim, 
 * damages or other liability, whether in an action of contract, tort or otherwise, 
 * arising from, out of or in connection with the software or the use or other 
 * dealings in the Software.
 */
package com.briceducardonnoy.artistshowcase.client.application;

import javax.inject.Inject;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.artistshowcase.client.application.header.HeaderPresenter;
import com.briceducardonnoy.artistshowcase.client.lang.Translate;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class ApplicationDesktopPresenter extends AbstractApplicationPresenter implements ApplicationUiHandlers {
	@Inject
	PlaceManager placeManager;
	
	private final Translate translate = GWT.create(Translate.class);

	@Inject
	public ApplicationDesktopPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
        super(eventBus, view, proxy, HeaderPresenter.SLOT_SetMainContent);

        view.setUiHandlers(this);
        
        Log.info("Desktop presenter");
        Log.info(translate.Selection());
        
    }
	
	@Override
	protected void onBind() {
		super.onBind();
//		placeManager.revealPlace(new PlaceRequest.Builder().nameToken(NameTokens.home).build());
	}

}
