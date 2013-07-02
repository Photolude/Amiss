package com.photolude.monitoring.amiss.logic;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.photolude.monitoring.amiss.logic.IMailClient;
import com.photolude.monitoring.amiss.logic.IStatusHttpClient;
import com.photolude.monitoring.amiss.logic.MonitorManagement;

@RunWith(Parameterized.class)
public class MonitorManagementUnitTests {
	private static final String URL_VALID = "http://localhost/testendpoint";
	private static final int HTTP_HEALTHY = 200;
	private static final int HTTP_UNHEALTHY = 500;
	private static final String EMAIL_VALID = "testuser@photolude.com";
	
	@SuppressWarnings("rawtypes")
	@Parameters
    public static Collection data() 
	{
        return Arrays.asList(new Object[][] 
		{
    		{
    			// 0. Valid Single
    			new String[]{URL_VALID},
    			new int[]{HTTP_HEALTHY},
    			null,
    			new String[]{URL_VALID},
    			new String[]{EMAIL_VALID},
    			false
    		},
    		{
    			// 1. Valid Multiple
    			new String[]{URL_VALID, URL_VALID},
    			new int[]{HTTP_HEALTHY, HTTP_HEALTHY},
    			null,
    			new String[]{URL_VALID, URL_VALID},
    			new String[]{EMAIL_VALID},
    			false
    		},
    		{
    			// 2. Valid Unhealthy
    			new String[]{URL_VALID},
    			new int[]{HTTP_UNHEALTHY},
    			new String[]{URL_VALID},
    			new String[]{URL_VALID},
    			new String[]{EMAIL_VALID},
    			false
    		},
    		{
    			// 3. Invalid url empty string
    			new String[]{""},
    			null,
    			null,
    			new String[0],
    			new String[]{EMAIL_VALID},
    			false
    		},
    		{
    			// 4. Invalid url null string
    			new String[]{null},
    			null,
    			null,
    			new String[0],
    			new String[]{EMAIL_VALID},
    			false
    		},
    		{
    			// 5. No urls
    			new String[0],
    			null,
    			null,
    			new String[0],
    			new String[]{EMAIL_VALID},
    			false
    		},
    		{
    			// 6. Null array
    			null,
    			null,
    			null,
    			new String[0],
    			new String[]{EMAIL_VALID},
    			false
    		},
    		{
    			// 7. No emails
    			new String[]{URL_VALID},
    			new int[]{HTTP_UNHEALTHY},
    			null,
    			new String[]{URL_VALID},
    			new String[]{},
    			false
    		},
    		{
    			// 8. No emails
    			new String[]{URL_VALID},
    			new int[]{HTTP_UNHEALTHY},
    			null,
    			new String[]{URL_VALID},
    			null,
    			false
    		},
    		{
    			// 9. Valid Single
    			new String[]{URL_VALID},
    			new int[]{HTTP_HEALTHY},
    			new String[]{URL_VALID},
    			new String[]{URL_VALID},
    			new String[]{EMAIL_VALID},
    			true
    		},
		});
	}
	
	private MonitorManagement manager;
	private TestHttpClient httpClient;
	private TestMailClient mailClient;
	private String[] urlsToCheck;
	private String[] termsInMailBody;
	
	public MonitorManagementUnitTests(String[] urls, int[] statusCodes, String[] termsInMailBody, String[] urlsToCheck, String[] emails, boolean firstRun)
	{
		this.manager = new MonitorManagement();
		this.httpClient = new TestHttpClient();
		this.mailClient = new TestMailClient();
		this.urlsToCheck = urlsToCheck;
		this.termsInMailBody = termsInMailBody;
		
		if(statusCodes != null)
		{
			for(int status : statusCodes)
			{
				this.httpClient.executeGetResponses.add(status);
			}
		}
		
		this.manager.setHttpClient((IStatusHttpClient)this.httpClient)
					.setMailClient((IMailClient)this.mailClient)
					.setNotifyEmails(emails)
					.setUrls(urls)
					.setFirstRun(firstRun);
	}
	
	@Test
	public void TestRun() throws Exception
	{
		this.manager.run();
		
		if(this.urlsToCheck != null)
		{
			Assert.assertArrayEquals(this.urlsToCheck, this.httpClient.calledEndpoints.toArray(new String[0]));
		}
		else
		{
			Assert.assertArrayEquals(new String[0], this.httpClient.calledEndpoints.toArray(new String[0]));
		}
		Assert.assertTrue("Http Client has remaining status codes (count: " + this.httpClient.executeGetResponses.size() + ")",
				this.httpClient.executeGetResponses.size() == 0);
		
		if(termsInMailBody != null && termsInMailBody.length > 0)
		{
			Assert.assertTrue("Sent mail not as expected (expected: 1, sent: " + this.mailClient.sentBodies.size() + ")",
					this.mailClient.sentBodies.size() == 1);
			
			for(String term : this.termsInMailBody)
			{
				Assert.assertTrue("Term not found (Term: " + term + " )",
						this.mailClient.sentBodies.get(0).contains(term));
			}
		}
		else
		{
			Assert.assertTrue("Mail sent unexpectedly (" + this.mailClient.sentBodies.size() + ")", 
					this.mailClient.sentBodies.size() == 0);
		}
	}
}
