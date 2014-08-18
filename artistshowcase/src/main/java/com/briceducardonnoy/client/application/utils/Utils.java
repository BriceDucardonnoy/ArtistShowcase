package com.briceducardonnoy.client.application.utils;

import com.allen_sauer.gwt.log.client.Log;
import com.briceducardonnoy.client.lang.Translate;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Utils {
	private static final Translate translate = GWT.create(Translate.class);
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
	
	public static void switchLocale(String locale) {
		UrlBuilder builder = Location.createUrlBuilder().setParameter("locale", locale);
		String newUrl = builder.buildString();
		// Lame hack for debug execution only
		String debugParam = Location.getParameter("gwt.codesvr");
		if(debugParam != null && !debugParam.isEmpty()) {
			newUrl = newUrl.replace("gwt.codesvr=127.0.1.1%3A", "gwt.codesvr=127.0.1.1:");
		}
		Window.Location.replace(newUrl);
	}
	// TODO BDY: implement progress waiting bar
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
	
	public static final native int getScreenWidth() /*-{
		return screen.width;
	}-*/;

	public static final native int getScreenHeight() /*-{
		return screen.height;
	}-*/;
	
	public static boolean isLandscape() {
		return Window.getClientWidth() >= Window.getClientHeight();
	}
	
}
