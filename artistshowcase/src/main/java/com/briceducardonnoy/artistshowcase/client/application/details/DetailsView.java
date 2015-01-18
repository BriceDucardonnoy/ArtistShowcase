package com.briceducardonnoy.artistshowcase.client.application.details;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.artistshowcase.client.lang.Translate;
import com.briceducardonnoy.artistshowcase.shared.model.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.shared.HandlerRegistration;
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
import com.reveregroup.gwt.imagepreloader.client.FitImageLoadEvent;

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
	private static final String ACTIVE = "#444444";
	
	private List<Picture> picturesList;
	private List<FitImage> imagesList;
	private List<HandlerRegistration> imageHandlers;
	
	interface Binder extends UiBinder<Widget, DetailsView> {
	}
	
	static class Constants {
		public static int getWestWidth() {return 300;}
		public static int getEastWidth() {return 160;}
	}

	@Inject
	DetailsView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
		picturesList = new ArrayList<>();
		imagesList = new ArrayList<>();
		imageHandlers = new ArrayList<>();
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
	
	@UiHandler("centerImage")
	void centerLoadHandle(FitImageLoadEvent event) {
		resizeCenterImage();
	}
	
	@UiHandler("mainImage")
	void clickMainImageHandle(ClickEvent event) {
		boolean found = false;
		for(FitImage other : imagesList) {
			if(other.getUrl().equals(mainImage.getUrl())) {
				other.getElement().getStyle().setBorderColor(ACTIVE);
				centerImage.setUrl(other.getUrl());
				found = true;
			}
			else {
				other.getElement().getStyle().clearBorderColor();
			}
		}
		if(!found) {
			Log.warn("Main image not found. Select 1st one by default");
			imagesList.get(0).getElement().getStyle().setBorderColor(ACTIVE);
			centerImage.setUrl(imagesList.get(0).getUrl());
		}
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
		resizeCenterImage();
		updateDetailInfo("");
	}

	@Override
	public void updateThumbs(ArrayList<String> urls) {
		picturesList.clear();
		imagesList.clear();
		thumbPane.clear();
		for(String url : urls) {
			final Picture p = new Picture(translate.Details());
			p.getProperties().put("imageUrl", url);
			picturesList.add(p);
			final FitImage image = new FitImage(p.getImageUrl());
			image.setFixedWidth(Constants.getEastWidth() - 28);
			// With setProperty, replace style property '-' by upper-case => camelCase syntax: http://blog.francoismaillet.com/?p=68
//			image.getElement().getStyle().setProperty("borderLeftWidth", "5px");
			image.setStyleName("thumbPane");
//			image.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
//			image.getElement().getStyle().setCursor(Cursor.POINTER);
			imageHandlers.add(image.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					for(FitImage other : imagesList) {
						other.getElement().getStyle().clearBorderColor();
					}
					image.getElement().getStyle().setBorderColor(ACTIVE);
					centerImage.setUrl(image.getUrl());
				}
			}));
			imagesList.add(image);
			thumbPane.add(image);
		}

		if(imagesList.size() > 0) {
			imagesList.get(0).getElement().getStyle().setBorderColor(ACTIVE);
			centerImage.setUrl(imagesList.get(0).getUrl());
		}
		else {
			centerImage.setUrl("");
		}
	}

	@Override
	public List<Picture> getPicturesList() {
		return picturesList;
	}

	@Override
	public ResizeLayoutPanel getMainPane() {
		return main;
	}

	@Override
	public void clearData() {
		for(HandlerRegistration hr : imageHandlers) {
			hr.removeHandler();
		}
		imageHandlers.clear();
		picturesList.clear();
		imagesList.clear();
		thumbPane.clear();
	}
	
	private void resizeCenterImage() {
		Log.trace("Resize center image");
		int parentWidth = centerImage.getParent().getOffsetWidth() - Constants.getWestWidth() - Constants.getEastWidth();
		// Fit image
		centerImage.setMaxSize(parentWidth, centerImage.getParent().getOffsetHeight());
		// Align it
		centerImage.getElement().getStyle().setTop((centerImage.getParent().getOffsetHeight() - centerImage.getHeight()) / 2, Unit.PX);
		centerImage.getElement().getStyle().setLeft((parentWidth - centerImage.getOffsetWidth()) / 2, Unit.PX);
	}

}
