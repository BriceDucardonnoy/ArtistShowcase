package com.briceducardonnoy.client.application.header;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class HeaderTabletView extends ViewImpl implements HeaderPresenter.MyView {
    public interface Binder extends UiBinder<Widget, HeaderTabletView> {
    }

    @UiField
    SimplePanel main;

    @Inject
    HeaderTabletView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
//		if (slot == HeaderPresenter.SLOT_SetHeaderContent) {
//		} else
		if (slot == HeaderPresenter.SLOT_SetMainContent) {
		    main.setWidget(content);
		}
		else {
		    super.setInSlot(slot, content);
		}
    }
}
