package com.photolude.monitoring.amiss.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MonitorManagement {
	private boolean firstRun = true;
	public boolean getFirstRun(){return this.firstRun;}
	public MonitorManagement setFirstRun(boolean value)
	{
		this.firstRun = value;
		return this;
	}
	
	private List<EndpointHealth> endpoints = new LinkedList<EndpointHealth>();
	public EndpointHealth[] getEndpointHealth()
	{
		return this.endpoints.toArray(new EndpointHealth[this.endpoints.size()]);
	}
	
	private IStatusHttpClient httpClient;
	public IStatusHttpClient getHttpClient(){ return this.httpClient; }
	public MonitorManagement setHttpClient(IStatusHttpClient value)
	{
		this.httpClient = value;
		return this;
	}
	
	private IMailClient mailClient;
	public IMailClient getMailClient(){ return this.mailClient; }
	public MonitorManagement setMailClient(IMailClient value)
	{
		this.mailClient = value;
		return this;
	}
	
	private String[] notifyEmails = new String[0];
	public String[] getNotifyEmails(){ return this.notifyEmails; }
	public MonitorManagement setNotifyEmails(String[] value)
	{
		this.notifyEmails = (value == null)? new String[0] : value;
		
		return this;
	}
	
	public String[] getUrls()
	{
		List<String> retval = new ArrayList<String>();
		
		for(EndpointHealth endpoint : this.endpoints)
		{
			retval.add(endpoint.getEndpoint());
		}
		
		return retval.toArray(new String[retval.size()]);
	}
	public MonitorManagement setUrls(String[] urls)
	{
		this.endpoints.clear();
		if(urls != null)
		{
			for(String url : urls)
			{
				if(url != null && !url.equals(""))
				{
					this.endpoints.add(new EndpointHealth().setEndpoint(url).setIsHealthy(true));
				}
			}
		}
		
		return this;
	}
	
	public void initialize(IStatusHttpClient httpClient, IMailClient mailClient)
	{
		this.httpClient = httpClient;
		this.mailClient = mailClient;
		
		this.endpoints.clear();
	}
	
	public void run()
	{
		if(this.httpClient == null || this.mailClient == null)
		{
			return;
		}
		List<EndpointHealth> statusChanged = new LinkedList<EndpointHealth>();
		
		for(EndpointHealth endpoint : this.endpoints)
		{
			int status = this.httpClient.executeGet(endpoint.getEndpoint());
			boolean isHealthy = status == 200;
			
			if(isHealthy != endpoint.getIsHealthy())
			{
				statusChanged.add(endpoint);
				endpoint.setIsHealthy(isHealthy);
			}
		}
		
		if(this.firstRun)
		{
			this.firstRun = false;
			
			StringBuilder body = new StringBuilder();
			body.append("The Amiss monitor service has started.\n\n Below is a status list of current services being monitored.\n\n");
			for(EndpointHealth endpoint : this.endpoints)
			{
				body.append("Service:" + endpoint.getEndpoint() + "\nStatus: " + (endpoint.getIsHealthy()? "Healthy" : "Unhealthy" + "\n\n"));
			}
			
			for(String email : this.notifyEmails)
			{
				this.mailClient.sendMail("Amiss: Monitoring starting up", body.toString(), email);
			}
		}
		else if(statusChanged.size() > 0)
		{
			StringBuilder body = new StringBuilder();
			body.append("Services had their health change:\n");
			for(EndpointHealth endpoint : statusChanged)
			{
				body.append("Status: " + (endpoint.getIsHealthy()? "Healthy" : "Unhealthy") +"Service:" + endpoint.getEndpoint() + "\n");
			}
			
			for(String email : this.notifyEmails)
			{
				this.mailClient.sendMail("Amiss: Service Status Changed", body.toString(), email);
			}
		}
	}
	
}
