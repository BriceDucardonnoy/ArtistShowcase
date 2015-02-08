package com.briceducardonnoy.artistshowcase.client.application.pictureviewer;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PictureViewerMobileModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
    	bindSingletonPresenterWidget(PictureViewerPresenter.class, PictureViewerPresenter.MyView.class, PictureViewerMobileView.class);
    }
}