package com.briceducardonnoy.client.application.details;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class DetailsModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(DetailsPresenter.class, DetailsPresenter.MyView.class, DetailsView.class, DetailsPresenter.MyProxy.class);
	}
}
