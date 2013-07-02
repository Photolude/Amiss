package com.photolude.monitoring.amiss.logic;

public interface IMailClient {
	void sendMail(String title, String body, String to);
}
