package es.jlaa.automapper.impl;

import java.lang.reflect.Field;
import java.util.Collection;

import es.jlaa.automapper.FieldPath;
import es.jlaa.automapper.ObjectSerializer;
import es.jlaa.automapper.ObjectSerialReader;
import es.jlaa.automapper.exceptions.AutoMapperException;

public class AutoMapperSerializer implements ObjectSerializer{

	public void serial(FieldPath path, String serial, ObjectSerialReader reader) {
		reader.serial(path, serial);
	}
	
	public void collection(FieldPath path, Collection<Object> coll, ObjectSerialReader reader) {
		
		if (coll == null) return;
		
		reader.collection(path.addCollection(), coll.size());
		int i = 0;
		for (Object obj : coll) {
			object(path.addCollectionItem(i++), obj, reader);
		}
	}
	
	public void object(FieldPath path, Object obj, ObjectSerialReader reader) {
		
		if (obj == null) return;
		
		if (ReflectionHelper.isCollectionOrArrayType(obj.getClass()) || ReflectionHelper.isMapType(obj.getClass()) || 
				ReflectionHelper.isSerialType(obj.getClass())) {
			throw new AutoMapperException("No se esperaba el tipo de objeto: " + obj.getClass());
		}

		reader.object(path, obj.getClass());
		
		for (Field field : ReflectionHelper.getDeclaredFields(obj.getClass())) {
			
			AutoMapperFieldBinder binder = new AutoMapperFieldBinder(obj, field);
			if (!binder.isTransient()) {
				String fieldName = binder.getFieldName();
				FieldPath currentPath = path.add(new FieldPath(fieldName));
				
				if (binder.isSerial()) {
					serial(currentPath, binder.getSerialValue(), reader);
				}else if (binder.isCollection()) {
					collection(currentPath, binder.getCollection(), reader);
				}else {
					object(currentPath, binder.getValue(), reader);
				}
			}
		}
	}
	
	@Override
	public void serialize(Object obj, ObjectSerialReader reader) {
		if (obj == null) return;
		object(FieldPath.root(), obj, reader);
	}

}
