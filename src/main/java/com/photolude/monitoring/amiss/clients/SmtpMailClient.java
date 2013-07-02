package com.photolude.monitoring.amiss.clients;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.photolude.monitoring.amiss.logic.IMailClient;

public class SmtpMailClient implements IMailClient
{
	private Properties properties = new Properties();
	
	private String userName;
	public String getUserName(){ return this.userName; }
	public void setUserName(String value)
	{ 
		this.userName = value;
		this.properties.put("mail.smtp.user", value);
	}
	
	private String password;
	public String getPassword(){ return this.password; }
	public void setPassword(String value)
	{ 
		this.password = value;
		this.properties.put("mail.smtp.password", value);
	}
	
	private String from;
	public String getFrom(){ return this.from; }
	public void setFrom(String value){ this.from = value; }
	
	private String host;
	public String getHost(){ return this.host; }
	public void setHost(String value)
	{
		this.host = value;
		this.properties.put("mail.smtp.host", value);
		
	}
	
	public SmtpMailClient()
	{
		System.setProperty("java.net.preferIPv4Stack" , "true");
		this.properties.put("mail.transport.protocol", "smtp");
		this.properties.put("mail.smtp.port", "25");
		this.properties.put("mail.smtp.auth", "true");
		this.properties.put("mail.smtp.starttls.enable", "true");
		this.properties.put("mail.debug", "true");
	}
	
	@Override
	public void sendMail(String title, String body, String to) {
		try 
		{
			Session session = Session.getInstance(this.properties, new SmtpAuthenticator(this.userName, this.password));
	            
	        Message message = new MimeMessage(session);
	        message.setSubject(title);
	        message.setText(body);
	        message.setFrom(new InternetAddress(this.from));
	        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
	        Transport.send(message, message.getAllRecipients());
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
