package com.tcl.configservice.serializer;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
/**
 * 序列化，采用String序列化
 * @author chenbo
 *
 */
public class DefaultSerializer implements ZkSerializer {
	@Override
	public byte[] serialize(Object data) throws ZkMarshallingError {
		if (data instanceof String) {
			return ((String) data).getBytes();
		} else {
			throw new ZkMarshallingError("data is not instanceof string");
		}
	}

	@Override
	public Object deserialize(byte[] bytes) throws ZkMarshallingError {
		return new String(bytes);
	}

}
