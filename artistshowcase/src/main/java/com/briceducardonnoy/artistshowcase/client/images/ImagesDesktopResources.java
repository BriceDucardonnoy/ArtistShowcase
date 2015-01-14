package com.briceducardonnoy.artistshowcase.client.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImagesDesktopResources extends ClientBundle {
	
	public static final ImagesDesktopResources INSTANCE = GWT.create(ImagesDesktopResources.class);

	@Source("france_24.png")
	ImageResource france24();
	
	@Source("united_kingdom_great_britain_24.png")
	ImageResource uk24();
	
	@Source("france_16.png")
	ImageResource france16();
	
	@Source("france.png")
	ImageResource france();
	
	@Source("united_kingdom_great_britain_16.png")
	ImageResource uk16();
	
	@Source("united_kingdom_great_britain.png")
	ImageResource uk();
	
	@Source("Header_200.png")
	ImageResource header200();
	
	@Source("Header_150.png")
	ImageResource header150();
	
	@Source("Header_100.png")
	ImageResource header100();
	
	@Source("calendar.png")
	ImageResource exposition();
	
	ImageResource contact();
	
	ImageResource copyright();
	
	@Source("demarche.png")
	ImageResource approach();
	
	ImageResource gallery();
	
	ImageResource home();
	
	ImageResource link();
	
	ImageResource arrow();
	
	ImageResource split();
}
