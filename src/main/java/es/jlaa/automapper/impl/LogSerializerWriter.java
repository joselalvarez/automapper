package es.jlaa.automapper.impl;

import es.jlaa.automapper.FieldPath;
import es.jlaa.automapper.ObjectSerialReader;

public class LogSerializerWriter implements ObjectSerialReader{

	private StringBuffer buffer;
	
	public LogSerializerWriter() {
		this.buffer = new StringBuffer();  
	}
	
	@Override
	public void object(FieldPath path, Class<?> type) {
		buffer.append(path).append(" -> (Type: '").append(type).append("')\n");
	}

	@Override
	public void collection(FieldPath path, int size) {
		buffer.append(path).append(" -> (Collection - size: '").append(size).append("')\n");
	}

	@Override
	public void serial(FieldPath path, String value) {
		buffer.append(path).append(" -> (Serial - value: '").append(value).append("')\n");
	}

	public String getLog() {
		return buffer.toString();
	}

}
