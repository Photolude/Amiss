package com.photolude.monitoring.amiss.clients;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuthenticator extends Authenticator {
    String user;
    String pw;
    public SmtpAuthenticator (String username, String password)
    {
       super();
       this.user = username;
       this.pw = password;
    }
   public PasswordAuthentication getPasswordAuthentication()
   {
      return new PasswordAuthentication(user, pw);
   }
}