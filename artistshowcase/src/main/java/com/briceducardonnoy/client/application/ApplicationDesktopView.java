package com.briceducardonnoy.client.application;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ApplicationDesktopView extends ViewWithUiHandlers<ApplicationUiHandlers> implements AbstractApplicationPresenter.MyView {
	interface Binder extends UiBinder<Widget, ApplicationDesktopView> {
	}

	@UiField
	HTMLPanel panel;

	@Inject
	ApplicationDesktopView(Binder binder) {
		initWidget(binder.createAndBindUi(this));
	}
}
