package com.briceducardonnoy.client.application.events;

import java.util.Vector;

import com.briceducardonnoy.shared.model.Category;
import com.briceducardonnoy.shared.model.Picture;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class PicturesLoadedEvent extends GwtEvent<PicturesLoadedEvent.PicturesLoadedHandler> {

	public static Type<PicturesLoadedHandler> TYPE = new Type<PicturesLoadedHandler>();
	private Vector<Picture> pictures;
	private Vector<Category> categories;

	public interface PicturesLoadedHandler extends EventHandler {
		void onPicturesLoaded(PicturesLoadedEvent event);
	}

//	public interface PicturesLoadedHasHandlers extends HasHandlers {
//		HandlerRegistration addPicturesLoadedHandler(PicturesLoadedHandler handler);
//	}

	public PicturesLoadedEvent(Vector<Picture> pictures, Vector<Category> categories) {
		this.pictures = pictures;
		this.categories = categories;
	}

	public Vector<Picture> getPictures() {
		return pictures;
	}

	public Vector<Category> getCategories() {
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

	public static void fire(HasHandlers source, Vector<Picture> pictures, Vector<Category> categories) {
		source.fireEvent(new PicturesLoadedEvent(pictures, categories));
	}
}
