package com.briceducardonnoy.artistshowcase.client.application.details;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class DetailsMobileModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(DetailsPresenter.class, DetailsPresenter.MyView.class, DetailsMobileView.class, DetailsPresenter.MyProxy.class);
	}
}
