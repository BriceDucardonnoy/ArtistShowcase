package com.briceducardonnoy.artistshowcase.client.application.pictureviewer;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

class PictureViewerView extends PopupViewImpl implements PictureViewerPresenter.MyView {
   
	interface Binder extends UiBinder<Widget, PictureViewerView> {
    }

	@UiField FocusPanel focusPane;
	@UiField HTMLPanel pane;
	@UiField FitImage image;
	@UiField Button close;
	@UiField Button prev;
	@UiField Button next;
	@UiField Label countLabel;

    @Inject
    PictureViewerView(EventBus eventBus, Binder uiBinder) {
        super(eventBus);

        initWidget(uiBinder.createAndBindUi(this));
        prev.setHTML("<<");// provided true if do it before createAndBindUi
        next.setHTML(">>");
    }
    
    static class Constants {
    	public static int getTopHeight() {return 20;}
		public static int getBottomHeight() {return 40;}
		public static String getBottomHeightPx() {return "40px";}
    }
    
	@Override
	public HTMLPanel getHtmlPanel() {
		return pane;
	}

	@Override
	public FitImage getImage() {
		return image;
	}

	@Override
	public FocusPanel getFocusPanel() {
		return focusPane;
	}

	@Override
	public Label getCountLabel() {
		return countLabel;
	}
	
	@Override
	public Button getPrevButton() {
		return prev;
	}
	
	@Override
	public Button getNextButton() {
		return next;
	}

	@Override
	public Button getCloseButton() {
		return close;
	}

	@Override
	public int getMaxWidth() {
		return Window.getClientWidth() - 30;
	}

	@Override
	public int getMaxHeight() {
		return Window.getClientHeight() - Constants.getTopHeight() - Constants.getBottomHeight();
	}
	
//	@UiHandler("prev")
//	void handlePrev(ClickEvent e) { }
    
}