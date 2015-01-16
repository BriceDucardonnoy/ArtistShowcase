package com.briceducardonnoy.artistshowcase.client.application.pictureviewer;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PictureViewerModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
            bindSingletonPresenterWidget(PictureViewerPresenter.class, PictureViewerPresenter.MyView.class, PictureViewerView.class);
    }
}