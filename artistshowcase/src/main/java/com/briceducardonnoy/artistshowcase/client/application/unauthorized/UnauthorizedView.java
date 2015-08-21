package com.briceducardonnoy.artistshowcase.client.application.unauthorized;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

class UnauthorizedView extends ViewImpl implements UnauthorizedPresenter.MyView {
	interface Binder extends UiBinder<Widget, UnauthorizedView> {
	}

	@UiField
	SimplePanel main;

	@Inject
	UnauthorizedView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		bindSlot(UnauthorizedPresenter.SLOT_Unauthorized, main);
	}

//	@Override
//	public void setInSlot(Object slot, IsWidget content) {
//		if (slot == UnauthorizedPresenter.SLOT_Unauthorized) {
//			main.setWidget(content);
//		} else {
//			super.setInSlot(slot, content);
//		}
//	}
}
