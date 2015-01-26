package com.briceducardonnoy.artistshowcase.client.application.utils;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.artistshowcase.client.application.widgets.UpdatableGrid;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.reveregroup.gwt.imagepreloader.client.FitImage;

public class Utils {
//	private static final Translate translate = GWT.create(Translate.class);
//	private static ProgressMessageBox box;

	/**
	 * Cursor
	 */
	public static void showWaitCursor(Element e) {
//		DOM.setStyleAttribute(e, "cursor", "wait");
		e.getStyle().setProperty("cursor", "wait");
	}

	public static void showDefaultCursor(Element e) {
//		DOM.setStyleAttribute(e, "cursor", "default");
		e.getStyle().setProperty("cursor", "default");
	}

	public static void loadFile(final AsyncCallback<String> callback, final String filename) {
		if(filename == null || filename.isEmpty()) {
			return;
		}
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, filename);
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {
                public void onError(Request request, Throwable exception) {
                    if(callback != null) callback.onFailure(exception);
                }
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if(callback != null) callback.onSuccess(response.getText());
                }
            });
        } catch (RequestException e) {
            Log.error("failed file reading: " + e.getMessage());
        }
    }
	
	public static void switchLocale(final String locale) {
		UrlBuilder builder = Location.createUrlBuilder().setParameter("locale", locale);
		String newUrl = builder.buildString();
		// Lame hack for debug execution only
		String debugParam = Location.getParameter("gwt.codesvr");
		if(debugParam != null && !debugParam.isEmpty()) {
			newUrl = newUrl.replace("gwt.codesvr=127.0.1.1%3A", "gwt.codesvr=127.0.1.1:");
		}
		Window.Location.replace(newUrl);
	}
	
	// TODO BDY: implement progress waiting bar (GWT-Bootstrap?)
//	public static void showLoadingProgressToolbar() {
//		if(box != null && box.isVisible()) return;
//		if(box == null) {
//			box = new ProgressMessageBox(translate.Loading(), translate.LoadingMessage());
//			box.setProgressText(translate.Loading());
//		}
//		box.show();
//		box.getButtonBar().setVisible(false);
//	}
//	
//	public static void updateProgressToolbar(double percent) {
//		if(box == null || !box.isVisible()) {
//			showLoadingProgressToolbar();
//		}
//		box.updateProgress(percent/100,  "{0}%");
//	}
//	
//	public static void hideLoadingProgressToolbar() {
//		if(box == null || !box.isVisible()) return;
//		box.hide();
//	}
//	
//	public static final native int getScreenWidth() /*-{
//		return screen.width;
//	}-*/;
//
//	public static final native int getScreenHeight() /*-{
//		return screen.height;
//	}-*/;
	
	public static boolean isLandscape() {
		return Window.getClientWidth() >= Window.getClientHeight();
	}
	
	public static String getSeparatorDependingLocale(final String locale) {
		if(locale.equals("fr")) {
			return " : ";
		}
		else if(locale.equals("en")) {
			return ": ";
		}
		else {
			return " : ";
		}
	}
	
	/**
	 * Add a {@link FitImage} <code>image</code> into the {@link UpdatableGrid} <code>grid</code> at the given <code>position</code>
	 * depending of its number of row and columns.<br>
	 * Function calculates the row and column indexes <code>pos</code> corresponds.
	 * @param grid The grid to add the image in
	 * @param position The position of the image
	 * @param image The image to add
	 * @param maxC The maximum number of columns
	 * @param maxR The maximum number or rows
	 * @param width The width of the panel container
	 * @param height The height of the panel container
	 */
	public static void addFitImageInGrid(final UpdatableGrid grid, int position, final FitImage image, int maxC, int maxR, int width, int height) {
		int idxC = position % maxC;
		int idxR = position / maxC;
		grid.insertCell(idxR, idxC, maxC);
		
		image.setMaxSize(Math.max((int) (width / maxC) - 5, 0), (int) (height / maxR));// - 5 for horizontal scroll
		grid.getCellFormatter().setWidth(idxR, idxC, image.getMaxWidth() + "px");
		grid.getCellFormatter().setHeight(idxR, idxC, image.getMaxHeight() + "px");
		
		grid.getCellFormatter().getElement(idxR, idxC).setPropertyString("align", "center");
		grid.setWidget(idxR, idxC, image);
		grid.getWidget(idxR, idxC).getElement().getStyle().setCursor(Cursor.POINTER);
		if(Log.isInfoEnabled()) {
			((FitImage)grid.getWidget(idxR, idxC)).setTitle(image.getAltText());
		}
	}
	
	public static void resize(final UpdatableGrid grid, int maxC, int maxR, int width, int height) {
		for(int r = 0 ; r < grid.getRowCount() ; r++) {
			for(int c = 0 ; c < grid.getColumnCount() ; c++) {
				FitImage img = (FitImage) grid.getWidget(r, c);
				if(img != null) {
//					Log.info("Set max size to " + ((width / maxC) - 5) + "x" + (height / maxR));
					img.setMaxSize(Math.max((int) (width / maxC) - 5, 0), (int) (height / maxR));// - 5 for horizontal scroll
					grid.getCellFormatter().setWidth(r, c, img.getMaxWidth() + "px");
					grid.getCellFormatter().setHeight(r, c, img.getMaxHeight() + "px");
				}
			}
		}
	}
	
}
