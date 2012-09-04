package com.apps.foursquare.sms.client;

import com.apps.foursquare.sms.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Foursquaresms implements EntryPoint {
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
		final Button sendButton = new Button("Submit");
		final TextBox nameField = new TextBox();
		nameField.setText("");
		final Label errorLabel = new Label();
		final Label nameLabel = new Label();
		final Label responseLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		nameLabel.setText("Please enter your Mobilenumber:");
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameLabelContainer").add(nameLabel);
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		RootPanel.get("responseContainer").add(responseLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();


		// Create a handler for the sendButton and nameField
		class PINHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendPINToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendPINToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendPINToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.checkEmpty(textToServer)) {
					errorLabel.setText("Please Enter the PIN Code.");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				greetingService.validatePin(textToServer,
						new AsyncCallback<Boolean>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								
								errorLabel.addStyleName("serverResponseLabelError");
								errorLabel.setText(SERVER_ERROR);
								sendButton.setEnabled(true);
								sendButton.setFocus(true);
							}

							public void onSuccess(Boolean result) {
								if(result.booleanValue()) {
									responseLabel.setText("Registration successful.");
								} else {
									errorLabel.setText("Invalid PIN Code.");
									sendButton.setEnabled(true);
									nameField.setFocus(true);
								}
							}

						});
			}
		}

		
		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least 10 characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				greetingService.getSubscriberStatus(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								
								errorLabel.addStyleName("serverResponseLabelError");
								errorLabel.setText(SERVER_ERROR);
								sendButton.setEnabled(true);
								sendButton.setFocus(true);
							}

							public void onSuccess(String result) {
								if(result.equalsIgnoreCase("SUCCESS")) {
									responseLabel.setText("Your number is already Registered.");
								} else if(result.equalsIgnoreCase("SMS_REG_PENDING")) {
									nameLabel.setText("Please Enter the PIN Code:");
									nameField.setText("Enter PIN Code");
									PINHandler handler = new PINHandler();
									sendButton.addClickHandler(handler);
									nameField.addKeyUpHandler(handler);
									sendButton.setEnabled(true);
									sendButton.setFocus(true);
									
								}else {
									redirect(result);
								}
							}

						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
	//redirect the browser to the given url

	public static native void redirect(String url)/*-{
	      $wnd.location = url;
	  }-*/;

}
