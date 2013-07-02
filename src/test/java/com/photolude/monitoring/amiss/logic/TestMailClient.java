package com.photolude.monitoring.amiss.logic;

import java.util.LinkedList;
import java.util.List;

import com.photolude.monitoring.amiss.logic.IMailClient;

public class TestMailClient implements IMailClient {
	List<String> sentBodies = new LinkedList<String>();
	
	public void sendMail(String title, String body, String to) {
		this.sentBodies.add(body);
	}
}
