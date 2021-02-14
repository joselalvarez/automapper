package es.jlaa.automapper.impl;

import java.util.HashMap;
import java.util.Hashtable;

import es.jlaa.automapper.FieldPath;
import es.jlaa.automapper.ObjectSerialReader;

public class HashtableWriter implements ObjectSerialReader{
	
	private HashMap<FieldPath, Hashtable<String, Object>> index;
	
	public HashtableWriter() {
		this.index = new HashMap<FieldPath, Hashtable<String, Object>>();
		this.index.put(FieldPath.root(), new Hashtable<String, Object>());
	}
	
	public HashtableWriter(Hashtable<String, Object> table) {
		this.index = new HashMap<FieldPath, Hashtable<String, Object>>();
		this.index.put(FieldPath.root(), table);
	}

	@Override
	public void object(FieldPath path, Class<?> type) {
		
		//Estamos en un objeto complejo
		//Si estamos en la raiz no hay que hacer nada mas, en caso contrario hay que setear esta tabla en el campo padre correspondiente
		if (!path.isRoot()) {
			//Indexamos la nueva tabla
			Hashtable<String, Object> table = new Hashtable<String, Object>();
			index.put(path, table);
			//Buscamos el padre
			Hashtable<String, Object> parent = index.get(path.getParent());
			if (path.isCollectionItem()) {
				//Si es un objeto perteneciente a una colección primero obtenemos el array para añadirlo
				Hashtable <String, Object> [] coll = (Hashtable<String, Object>[]) parent.get(path.getFieldName());
				coll[path.getIndex()] = table;
			}else {
				//Si es un objeto normal lo seteamos en el campo padre
				parent.put(path.getFieldName(), table);
			}
		}
		
	}

	@Override
	public void collection(FieldPath path, int size) {
		//Buscamos el objeto padre y creamos un array en el campo correspondiente
		Hashtable<String, Object> parent = index.get(path.getParent());
		parent.put(path.getFieldName(), new Hashtable [size]);
	}

	@Override
	public void serial(FieldPath path, String value) {
		if (value == null) return;//Las Hastables no soportan objetos nulos
		//Buscamos el objeto padre y mapeamos el campo correspondiente
		Hashtable<String, Object> table = index.get(path.getParent());
		table.put(path.getFieldName(), value);
	}
	
	public Hashtable<String, Object> getHashtable(){
		//Retornamos el objeto de la raiz
		return index.get(FieldPath.root());
	}

}
