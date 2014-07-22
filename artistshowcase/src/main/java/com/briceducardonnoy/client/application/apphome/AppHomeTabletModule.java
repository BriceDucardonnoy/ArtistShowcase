package com.briceducardonnoy.client.application.apphome;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class AppHomeTabletModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenter(AppHomePresenter.class, AppHomePresenter.MyView.class, AppHomeTabletView.class, AppHomePresenter.MyProxy.class);
	}
}
