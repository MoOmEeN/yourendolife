package com.moomeen.views;

import com.moomeen.email.EmailSender;
import com.moomeen.utils.SpringContextHolder;
import com.sendgrid.SendGridException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class FeedbackForm extends VerticalLayout {
	
	public static final String CONTACT_EMAIL = "contact@yourendolife.com";
	private static final String FEEDBACK_SUBJECT = "yourENDOlife Feedback";
	
	private static final String FEEDBACK_SUCCESS_MESSAGE = "Message sent. Thank you!";
	private static final String FEEDBACK_FAIL_MESSAGE = "Error occured. Please send your message to " + CONTACT_EMAIL;
	
	private EmailSender emailSender;

	private LabelWrapper result = new LabelWrapper();
	
	public FeedbackForm() {
		this.emailSender = SpringContextHolder.lookupBean(EmailSender.class);
		
		initResultLabel();
		
		final TextField email = new TextField();
		email.setWidth("90%");
		email.setCaption("Your email");
		
		final TextArea message = new TextArea();
		message.setWidth("90%");
		message.setCaption("Message");
		message.setRows(10);
		
		Button send = new Button("SEND");
		send.setWidth("90%");
		send.setStyleName("send-button");
		
		send.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					emailSender.sendEmail(email.getValue(), CONTACT_EMAIL, FEEDBACK_SUBJECT, message.getValue());
					showResult(true);
				} catch (SendGridException e) {
					showResult(false);
				}
			}
		});
		
		setSpacing(true);
		addComponent(result.label);
		setComponentAlignment(result.label, Alignment.TOP_CENTER);
		addComponent(email);
		setComponentAlignment(email, Alignment.TOP_CENTER);
		addComponent(message);
		setComponentAlignment(message, Alignment.MIDDLE_CENTER);
		addComponent(send);
		setComponentAlignment(send, Alignment.BOTTOM_CENTER);
	}

	private void initResultLabel() {
		result.label.setWidth("90%");
		result.label.setVisible(false);
	}
	
	private class LabelWrapper {
		Label label = new Label();
	}
	
	private void showResult(boolean success){
		expandLabel();
		if (success){
			result.label.setStyleName("feedback-success");
			result.label.setValue(FEEDBACK_SUCCESS_MESSAGE);
		} else {
			result.label.setStyleName("feedback-error");
			result.label.setValue(FEEDBACK_FAIL_MESSAGE);
		}
	}
	
	private void expandLabel(){
		result.label.setVisible(true);
	}
	
}
