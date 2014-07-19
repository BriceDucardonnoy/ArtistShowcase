package com.briceducardonnoy.client.application.home;

import javax.inject.Singleton;

import com.briceducardonnoy.client.application.AbstractApplicationPresenter;
import com.briceducardonnoy.client.application.ApplicationPresenter;
import com.briceducardonnoy.client.application.ApplicationView;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationDesktopModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		// Application Presenters
		bind(ApplicationDesktopPresenter.class).in(Singleton.class);
		bind(ApplicationDesktopView.class).in(Singleton.class);
		bind(AbstractApplicationPresenter.MyProxy.class).asEagerSingleton();
		bind(AbstractApplicationPresenter.MyView.class).to(ApplicationDesktopView.class);
		bind(AbstractApplicationPresenter.class).to(ApplicationDesktopPresenter.class);
		
		bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class, ApplicationPresenter.MyProxy.class);
	}
}
