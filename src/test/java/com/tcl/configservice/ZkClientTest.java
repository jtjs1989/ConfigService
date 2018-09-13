package com.tcl.configservice;

import org.I0Itec.zkclient.ZkClient;

import com.tcl.configservice.serializer.DefaultSerializer;

public class ZkClientTest {

	public static void main(String[] args) {
		ZkClient zkClient = new ZkClient("localhost:2181", 1000, 3000, new DefaultSerializer());
		zkClient.createPersistent("/configService/test/config.t4", "map\nkey1=value1\nkey2=value2\nkey3=value3");
//		zkClient.writeData("/configService/test/config.t4", "map\nkey1=value1\nkey2=value2\nkey3=value3");
		String value = zkClient.readData("/configService/test/config.t3");
		
		System.out.println(value.split("\\n")[1]);
	}
}
