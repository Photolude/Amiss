package com.photolude.monitoring.amiss.logic;

public class EndpointHealth {
	private String endpoint;
	public String getEndpoint(){ return this.endpoint; }
	public EndpointHealth setEndpoint(String value)
	{
		this.endpoint = value;
		return this;
	}
	
	private boolean isHealthy = true;
	public boolean getIsHealthy(){ return this.isHealthy; }
	public EndpointHealth setIsHealthy(boolean value)
	{
		this.isHealthy = value;
		return this;
	}
}
