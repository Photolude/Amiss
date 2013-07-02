package com.photolude.monitoring.amiss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.photolude.monitoring.amiss.logic.MonitorManagement;

@Controller
@RequestMapping("/dashboard")
public class DashboardController implements Runnable 
{
	private static Thread thread = new Thread();
	private MonitorManagement monitor;
	public MonitorManagement getMonitor(){ return this.monitor; }
	public void setMonitor(MonitorManagement value)
	{
		this.monitor = value;
	}
	
	private int refreshDuration;
	public int getRefreshDuration(){ return this.refreshDuration; }
	public void setRefreshDuration(int value)
	{
		this.refreshDuration = value;
	}

	public DashboardController() 
	{
		if(!thread.isAlive())
		{
			thread = new Thread(this);
			thread.start();
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model)
	{
		synchronized(this.monitor)
		{
			model.addAttribute("endpoints", this.monitor.getEndpointHealth());
		}
		
		model.addAttribute("emails", this.monitor.getNotifyEmails());
		
		model.addAttribute("timeout", this.refreshDuration);
		return "dashboard";

	}

	@Override
	public void run() {
		while(true)
		{
			if(this.monitor != null)
			{
				synchronized(this.monitor)
				{
					this.monitor.run();
				}
			}
			
			try {
				Thread.sleep(this.refreshDuration);
			} catch (InterruptedException e) {
			}
		}
	}
	
}