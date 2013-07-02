package com.photolude.monitoring.amiss.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.photolude.monitoring.amiss.logic.IStatusHttpClient;

public class TestHttpClient implements IStatusHttpClient {
	public Queue<Integer> executeGetResponses = new LinkedList<Integer>();
	public List<String> calledEndpoints = new LinkedList<String>();
	
	public int executeGet(String endpointUrl) {
		this.calledEndpoints.add(endpointUrl);
		int retval = 500;
		
		if(this.executeGetResponses.size() > 0)
		{
			retval = this.executeGetResponses.poll();
		}
		
		return retval;
	}

}
