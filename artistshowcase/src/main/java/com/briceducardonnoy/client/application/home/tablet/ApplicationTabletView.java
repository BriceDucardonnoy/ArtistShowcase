package com.briceducardonnoy.client.application.home.tablet;

import javax.inject.Inject;

import com.briceducardonnoy.client.application.AbstractApplicationPresenter;
import com.briceducardonnoy.client.application.ApplicationUiHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ApplicationTabletView extends ViewWithUiHandlers<ApplicationUiHandlers> implements AbstractApplicationPresenter.MyView {
	interface Binder extends UiBinder<Widget, ApplicationTabletView> {
	}

	@UiField
	HTMLPanel panel;

	@Inject
	ApplicationTabletView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
