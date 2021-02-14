package es.jlaa.automapper.impl;

import java.lang.reflect.Field;

public class AutoMapperObjectBinder {
	
	private Object obj;
	
	public AutoMapperObjectBinder(Object obj) {
		this.obj = obj;
	}
	
	public AutoMapperFieldBinder getFieldBinder(String name) {
		for (Field field : ReflectionHelper.getDeclaredFields(obj.getClass())) {
			AutoMapperFieldBinder binder = new AutoMapperFieldBinder(obj, field);
			if (binder.getFieldName().equals(name)) {
				return binder;
			}
		}
		return null;
	}
}
