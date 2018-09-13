package com.tcl.configservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class InitConfigData implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ZookeeperWatch zkWatch;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("onApplicationEvent");
		zkWatch.init();
	}

	public void setZkWatch(ZookeeperWatch zkWatch) {
		this.zkWatch = zkWatch;
	}
	
}
