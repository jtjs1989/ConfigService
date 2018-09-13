package com.tcl.configservice;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.tcl.configservice.annotation.ConfigCen;

@Service
public class TestBean {

	@Value(value="${config.t1}")
	@ConfigCen(name="config.t1")
	private String configT1;
	@ConfigCen(name="config.t2")
	private int configT2;
	@ConfigCen(name="config.t3")
	private String[] configT3;
	@ConfigCen(name="config.t4")
	private Map<String, String> map;
	
	@Value(value="${config.t5}")
	private String configT5;
	
	
	public void print() {
		System.out.println(String.format("configT1:%s,configT2:%s,configT5:%s", configT1, configT2, configT5));
		System.out.println(map);
	}
	@PostConstruct
	public void start() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				print();
			}
		}, 10000,10000);
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		try {
			TimeUnit.DAYS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
