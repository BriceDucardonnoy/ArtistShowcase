package com.briceducardonnoy.artistshowcase.client.application.details;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.artistshowcase.client.lang.Translate;
import com.briceducardonnoy.artistshowcase.shared.model.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

class DetailsView extends ViewImpl implements DetailsPresenter.MyView {
	
	@UiField ResizeLayoutPanel main;
	@UiField DockLayoutPanel dockPane;
	// West
	@UiField LayoutPanel westPane;
	@UiField Image mainImage;
	@UiField HTMLPanel description;
	// Center
	@UiField FitImage centerImage;
	// East
	@UiField FlowPanel thumbPane;
	
	private final Translate translate = GWT.create(Translate.class);
	
	private List<Picture> picturesList;
	private List<FitImage> imagesList;
	
	interface Binder extends UiBinder<Widget, DetailsView> {
	}

	@Inject
	DetailsView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		picturesList = new ArrayList<>();
		imagesList = new ArrayList<>();
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == DetailsPresenter.SLOT_details) {
			main.setWidget(content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	@Override
	public Image getMainImage() {
		return mainImage;
	}

	@Override
	public FitImage getCenterImage() {
		return centerImage;
	}

	@Override
	public void updateMainImage(String url) {
		if(Log.isTraceEnabled()) {
			Log.trace("Update to image " + url);
		}
		mainImage.setUrl(url);
	}
	
	@UiHandler("mainImage")
	void loadHandle(LoadEvent event) {
		resizeMainImage();
	}
	
	public void resizeMainImage() {
		// Stretch to the biggest dimension
		// BDY: test with FitImage?
		mainImage.getElement().getStyle().clearHeight();// getElement().getStyle(): magic function!!!
		mainImage.getElement().getStyle().clearWidth();
		mainImage.getElement().getStyle().clearTop();
		mainImage.getElement().getStyle().clearLeft();
		if(mainImage.getHeight() >= mainImage.getWidth()) {// Portrait
			mainImage.setHeight("100%");
			mainImage.getElement().getStyle().setLeft((westPane.getOffsetWidth() - mainImage.getOffsetWidth()) / 2, Unit.PX);
		}
		else {// Landscape
			mainImage.setWidth("100%");
//			Log.info("Vertical align to middle");
			mainImage.getElement().getStyle().setTop(
					(
							(westPane.getOffsetHeight()/2) 
							- mainImage.getHeight()) 
						/ 2, Unit.PX);
		}
//		westPane.forceLayout();
	}

	@Override
	public void updateDetailInfo(String html) {
		if(!html.isEmpty()) {
			description.getElement().setInnerHTML(html);
		}
		description.getElement().getStyle().clearTop();
		description.getElement().getStyle().setTop(
				(
						(westPane.getOffsetHeight()/2)
						- description.getOffsetHeight())
						/ 2, Unit.PX);
	}
	
	public void resize() {
		resizeMainImage();
		updateDetailInfo("");
	}

	@Override
	public void updateThumbs(ArrayList<String> urls) {
		for(String url : urls) {
			Picture p = new Picture(translate.Details());
			p.getProperties().put("imageUrl", url);
			picturesList.add(p);
			imagesList.add(new FitImage(p.getImageUrl()));
			thumbPane.add(imagesList.get(imagesList.size() - 1));
		}
//		Scheduler.get().scheduleDeferred(select1stImageCmd);
	}

	@Override
	public List<Picture> getPicturesList() {
		return picturesList;
	}

	@Override
	public ResizeLayoutPanel getMainPane() {
		return main;
	}
}
