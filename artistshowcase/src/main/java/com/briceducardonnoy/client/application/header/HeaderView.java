/**
 * Copyright © Brice DUCARDONNOY
 * 
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
 * 
 * In no event shall the authors or copyright holders be liable for any claim, 
 * damages or other liability, whether in an action of contract, tort or otherwise, 
 * arising from, out of or in connection with the software or the use or other 
 * dealings in the Software.
 */
package com.briceducardonnoy.client.application.header;

import javax.inject.Inject;

import com.briceducardonnoy.client.application.widgets.ImageButton;
import com.briceducardonnoy.client.imagepreloader.FitImage;
import com.briceducardonnoy.client.lang.Translate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class HeaderView extends ViewImpl implements HeaderPresenter.MyView {
    public interface Binder extends UiBinder<Widget, HeaderView> {
    }
    
    private final Translate translate = GWT.create(Translate.class);

	@UiField FitImage logo;
	@UiField ImageButton home;
	@UiField ImageButton gallery;
	@UiField SimplePanel main;

    @Inject
    HeaderView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		
		home.setText(translate.Home());
		gallery.setText(translate.Gallery());// TODO BDY: use split button and set icons size to 16x16
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
