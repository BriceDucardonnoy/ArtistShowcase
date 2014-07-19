package com.briceducardonnoy.client.application.home.tablet;

import javax.inject.Singleton;

import com.briceducardonnoy.client.application.AbstractApplicationPresenter;
import com.briceducardonnoy.client.application.ApplicationPresenter;
import com.briceducardonnoy.client.application.ApplicationView;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationTabletModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		// Application Presenters
		bind(ApplicationTabletPresenter.class).in(Singleton.class);
		bind(ApplicationTabletView.class).in(Singleton.class);
		bind(AbstractApplicationPresenter.MyProxy.class).asEagerSingleton();
		bind(AbstractApplicationPresenter.MyView.class).to(ApplicationTabletView.class);
		bind(AbstractApplicationPresenter.class).to(ApplicationTabletPresenter.class);
		
		bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class, ApplicationPresenter.MyProxy.class);
	}
}
