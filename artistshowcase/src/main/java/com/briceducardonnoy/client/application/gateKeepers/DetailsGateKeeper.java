package com.briceducardonnoy.client.application.gateKeepers;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.client.application.context.ApplicationContext;
import com.briceducardonnoy.client.place.NameTokens;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class DetailsGateKeeper implements Gatekeeper {

	@Inject PlaceManager placeManager;
	
	@Override
	public boolean canReveal() {
		if(Log.isTraceEnabled()) {
			Log.trace(placeManager.getCurrentPlaceRequest().getNameToken() + ": " + 
					placeManager.getCurrentPlaceRequest().getParameter(ApplicationContext.DETAIL_KEYWORD, ""));
		}
		
		if(placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.details) &&
				!placeManager.getCurrentPlaceRequest().getParameter(ApplicationContext.DETAIL_KEYWORD, "").isEmpty()) {
			return true;
		}
		return false;
	}

}
