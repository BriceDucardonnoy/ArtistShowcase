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
package com.briceducardonnoy.client.application;

import javax.inject.Singleton;

import com.briceducardonnoy.client.application.apphome.AppHomeModule;
import com.briceducardonnoy.client.application.header.HeaderPresenter;
import com.briceducardonnoy.client.application.header.HeaderView;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.briceducardonnoy.client.application.details.DetailsModule;
import com.briceducardonnoy.client.application.error.ErrorModule;
import com.briceducardonnoy.client.application.unauthorized.UnauthorizedModule;

public class ApplicationDesktopModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		install(new UnauthorizedModule());
		install(new ErrorModule());
		// Application Presenters
		bind(ApplicationDesktopPresenter.class).in(Singleton.class);
		bind(ApplicationDesktopView.class).in(Singleton.class);
		bind(AbstractApplicationPresenter.MyProxy.class).asEagerSingleton();
		bind(AbstractApplicationPresenter.MyView.class).to(ApplicationDesktopView.class);
		bind(AbstractApplicationPresenter.class).to(ApplicationDesktopPresenter.class);
		
		bindPresenter(HeaderPresenter.class, HeaderPresenter.MyView.class, HeaderView.class, HeaderPresenter.MyProxy.class);
//		bindPresenter(HeaderPresenter.class, HeaderPresenter.MyView.class, HeaderMobileView.class, HeaderPresenter.MyProxy.class);
//		bindPresenter(AppHomePresenter.class, AppHomePresenter.MyView.class, AppHomeView.class, AppHomePresenter.MyProxy.class);
		
		install(new AppHomeModule());
		install(new DetailsModule());
	}
}
