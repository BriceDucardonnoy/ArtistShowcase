package com.briceducardonnoy.client.application.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Integer;
import com.google.gwt.event.shared.HasHandlers;

public class CategoryChangedEvent extends
		GwtEvent<CategoryChangedEvent.CategoryChangedHandler> {

	public static Type<CategoryChangedHandler> TYPE = new Type<CategoryChangedHandler>();
	private Integer categoryId;

	public interface CategoryChangedHandler extends EventHandler {
		void onCategoryChanged(CategoryChangedEvent event);
	}

	public CategoryChangedEvent(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	@Override
	protected void dispatch(CategoryChangedHandler handler) {
		handler.onCategoryChanged(this);
	}

	@Override
	public Type<CategoryChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<CategoryChangedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Integer categoryId) {
		source.fireEvent(new CategoryChangedEvent(categoryId));
	}
}
