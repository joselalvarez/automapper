package es.jlaa.automapper.impl;

import java.util.Hashtable;
import java.util.Map.Entry;

import es.jlaa.automapper.FieldPath;
import es.jlaa.automapper.ObjectSerialReader;
import es.jlaa.automapper.ObjectSerializer;

public class HashtableSerializer implements ObjectSerializer{

	public void serial(FieldPath path, String serial, ObjectSerialReader reader) {
		reader.serial(path, serial);
	}
	
	public void collection(FieldPath path, Hashtable<String, Object> [] coll, ObjectSerialReader reader) {
		reader.collection(path.addCollection(), coll.length);
		int i = 0;
		for (Hashtable<String, Object> table : coll) {
			object(path.addCollectionItem(i++), table, reader);
		}
	}
	
	public void object(FieldPath path, Hashtable<String, Object> table, ObjectSerialReader reader) {
		
		reader.object(path, Hashtable.class);
		
		for (Entry<String, Object> entry : table.entrySet()) {
			String fielName = entry.getKey();
			Object obj = entry.getValue();
			FieldPath currentPath = path.add(new FieldPath(fielName));
			if (obj instanceof Hashtable) {
				object(currentPath, (Hashtable<String, Object>) obj, reader);
			}else if (ReflectionHelper.isCollectionOrArrayType(obj.getClass())) {
				collection(currentPath, (Hashtable<String, Object>[]) obj, reader);
			}else {
				serial(currentPath, obj.toString(), reader);
			}
		}
	}
	
	@Override
	public void serialize(Object obj, ObjectSerialReader reader) {
		if (obj == null) return;
		if(!(obj instanceof Hashtable)) throw new IllegalArgumentException("Tipo de objeto no soportado :" + obj.getClass());
		
		object(FieldPath.root(), (Hashtable<String, Object>) obj, reader);
		
	}

}
