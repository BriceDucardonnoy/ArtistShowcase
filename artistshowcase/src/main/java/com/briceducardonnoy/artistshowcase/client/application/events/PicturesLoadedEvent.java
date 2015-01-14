package com.briceducardonnoy.artistshowcase.client.application.events;

import java.util.ArrayList;

import com.briceducardonnoy.artistshowcase.shared.model.Category;
import com.briceducardonnoy.artistshowcase.shared.model.Picture;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class PicturesLoadedEvent extends GwtEvent<PicturesLoadedEvent.PicturesLoadedHandler> {

	public static Type<PicturesLoadedHandler> TYPE = new Type<PicturesLoadedHandler>();
	private ArrayList<Picture> pictures;
	private ArrayList<Category> categories;

	public interface PicturesLoadedHandler extends EventHandler {
		void onPicturesLoaded(PicturesLoadedEvent event);
	}

//	public interface PicturesLoadedHasHandlers extends HasHandlers {
//		HandlerRegistration addPicturesLoadedHandler(PicturesLoadedHandler handler);
//	}

	public PicturesLoadedEvent(ArrayList<Picture> pictures, ArrayList<Category> categories) {
		this.pictures = pictures;
		this.categories = categories;
	}

	public ArrayList<Picture> getPictures() {
		return pictures;
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	@Override
	protected void dispatch(PicturesLoadedHandler handler) {
		handler.onPicturesLoaded(this);
	}

	@Override
	public Type<PicturesLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PicturesLoadedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, ArrayList<Picture> pictures, ArrayList<Category> categories) {
		source.fireEvent(new PicturesLoadedEvent(pictures, categories));
	}
}
