package com.briceducardonnoy.artistshowcase.client.application.events;

import com.briceducardonnoy.artistshowcase.shared.model.Picture;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class PictureLoadedEvent extends
		GwtEvent<PictureLoadedEvent.PictureLoadedHandler> {

	public static Type<PictureLoadedHandler> TYPE = new Type<PictureLoadedHandler>();
	private Picture picture;

	public interface PictureLoadedHandler extends EventHandler {
		void onPictureLoaded(PictureLoadedEvent event);
	}

	public PictureLoadedEvent(Picture picture) {
		this.picture = picture;
	}

	public Picture getPicture() {
		return picture;
	}

	@Override
	protected void dispatch(PictureLoadedHandler handler) {
		handler.onPictureLoaded(this);
	}

	@Override
	public Type<PictureLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<PictureLoadedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Picture picture) {
		source.fireEvent(new PictureLoadedEvent(picture));
	}
}
