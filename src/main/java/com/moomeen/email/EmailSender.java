package com.moomeen.email;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

@Service
public class EmailSender {
	
	private static Logger LOG = LoggerFactory.getLogger(EmailSender.class);
	
	@Value("#{environment.SENDGRID_USERNAME}")
	private String username;

	@Value("#{environment.SENDGRID_PASSWORD}")
	private String password;
	
	private SendGrid sendGrid;
	
	@PostConstruct
	public void init(){
		sendGrid = new SendGrid(username, password);
	}
	
	public void sendEmail(String sender, String recipient, String subject, String message) throws SendGridException{
		SendGrid.Email email = new SendGrid.Email();
	    email.addTo(recipient);
	    email.setFrom(sender);
	    email.setSubject(subject);
	    email.setText(message);

	    try {
	      SendGrid.Response response = sendGrid.send(email);
	      LOG.trace("Feedback email sent: {}, from: {}, status: {}", message, sender, response.getStatus());
	    } catch (SendGridException e) {
	    	LOG.error("Couldn't send email", e);
	    	throw e;
	    }
	}
	
}
