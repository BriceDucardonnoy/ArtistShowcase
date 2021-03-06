package com.briceducardonnoy.artistshowcase.client.application.unauthorized;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class UnauthorizedModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(UnauthorizedPresenter.class,
				UnauthorizedPresenter.MyView.class, UnauthorizedView.class,
				UnauthorizedPresenter.MyProxy.class);
	}
}
