package com.briceducardonnoy.client.application;

import javax.inject.Singleton;

import com.briceducardonnoy.client.application.apphome.AppHomeModule;
import com.briceducardonnoy.client.application.header.HeaderPresenter;
import com.briceducardonnoy.client.application.header.HeaderView;
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
		
		bindPresenter(HeaderPresenter.class, HeaderPresenter.MyView.class, HeaderView.class, HeaderPresenter.MyProxy.class);
//		bindPresenter(AppHomePresenter.class, AppHomePresenter.MyView.class, AppHomeView.class, AppHomePresenter.MyProxy.class);
		
		install(new AppHomeModule());
	}
}
