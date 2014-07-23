package com.briceducardonnoy.client.application.header;

import javax.inject.Inject;

import com.briceducardonnoy.client.imagepreloader.FitImage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class HeaderView extends ViewImpl implements HeaderPresenter.MyView {
    public interface Binder extends UiBinder<Widget, HeaderView> {
    }

	@UiField FitImage logo;
	@UiField SimplePanel main;

    @Inject
    HeaderView(Binder uiBinder) {
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

	@Override
	public FitImage getLogo() {
		return logo;
	}
}
