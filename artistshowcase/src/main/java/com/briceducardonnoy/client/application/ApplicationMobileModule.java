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

import com.briceducardonnoy.client.application.apphome.AppHomeMobileModule;
import com.briceducardonnoy.client.application.details.DetailsModule;
import com.briceducardonnoy.client.application.error.ErrorModule;
import com.briceducardonnoy.client.application.header.HeaderMobileView;
import com.briceducardonnoy.client.application.header.HeaderPresenter;
import com.briceducardonnoy.client.application.unauthorized.UnauthorizedModule;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationMobileModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		install(new AppHomeMobileModule());
		install(new DetailsModule());
		install(new ErrorModule());
		install(new UnauthorizedModule());
		// TODO BDY: add here detailMobileModule
		// See below in case of compilation failure
		// https://github.com/ArcBees/GWTP/issues/291
		// Application Presenters
		bind(ApplicationMobilePresenter.class).in(Singleton.class);
		bind(ApplicationMobileView.class).in(Singleton.class);
		bind(AbstractApplicationPresenter.MyProxy.class).asEagerSingleton();
		bind(AbstractApplicationPresenter.MyView.class).to(ApplicationMobileView.class);
		bind(AbstractApplicationPresenter.class).to(ApplicationMobilePresenter.class);
		
		bindPresenter(HeaderPresenter.class, HeaderPresenter.MyView.class, HeaderMobileView.class, HeaderPresenter.MyProxy.class);		
	}
}
