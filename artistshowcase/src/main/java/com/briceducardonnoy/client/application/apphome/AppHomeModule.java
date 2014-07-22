package com.briceducardonnoy.client.application.apphome;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AppHomeModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(AppHomePresenter.class, AppHomePresenter.MyView.class, AppHomeView.class, AppHomePresenter.MyProxy.class);
	}
}
