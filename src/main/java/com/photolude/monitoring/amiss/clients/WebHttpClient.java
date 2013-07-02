package com.photolude.monitoring.amiss.clients;

import java.io.IOException;
import java.net.URISyntaxException;

import com.photolude.monitoring.amiss.logic.IStatusHttpClient;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class WebHttpClient implements IStatusHttpClient {

	public int executeGet(String endpointUrl) {
		DefaultHttpClient client = new DefaultHttpClient();
		int retval = 0;
		try {
			HttpResponse response = client.execute(new HttpGet(endpointUrl));
			
			retval = response.getStatusLine().getStatusCode();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retval;
	}

}
