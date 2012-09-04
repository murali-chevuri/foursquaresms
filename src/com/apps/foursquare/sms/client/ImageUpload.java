package com.apps.foursquare.sms.client;

import com.apps.foursquare.sms.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ImageUpload implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.addStyleName("table-center");
		form.addStyleName("demo-panel-padded");
		form.setWidth("275px");
		
		VerticalPanel holder = new VerticalPanel();
		
		FileUpload upload = new FileUpload();
		upload.setName("upload");
		holder.add(upload);
		
		holder.add(new HTML("<hr />"));
		holder.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
		Button submit = new Button("Submit");
		submit.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				// form.submit();
				
			}
		}); 
		holder.add(submit);
		form.add(holder);
		form.setAction("http://foursquaresms.appspot.com/uploadimage");
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Window.alert(event.getResults());
				
			}
		});
//		form.addFormHandler(new FormHandler() {
//			public void onSubmit(FormSubmitEvent event)
//			{
//				// if (something_is_wrong)
//				// {
//				// Take some action
//				// event.setCancelled(true);
//				// }
//			}
//			
//			public void onSubmitComplete(FormSubmitCompleteEvent event)
//			{
//				
//			}
//	});

	RootPanel.get("demo").add(form);
 }
	//redirect the browser to the given url

	public static native void redirect(String url)/*-{
	      $wnd.location = url;
	  }-*/;

}
