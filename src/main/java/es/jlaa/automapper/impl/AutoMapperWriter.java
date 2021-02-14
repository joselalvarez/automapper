package es.jlaa.automapper.impl;

import java.util.HashMap;

import es.jlaa.automapper.FieldPath;
import es.jlaa.automapper.ObjectSerialReader;
import es.jlaa.automapper.exceptions.AutoMapperException;

public class AutoMapperWriter implements ObjectSerialReader{

	private HashMap<FieldPath, Object> index;

	public AutoMapperWriter(Class<?> type) {
		this.index = new HashMap<FieldPath, Object>();
		try {
			this.index.put(FieldPath.root(), ReflectionHelper.newInstance(type));
		} catch (Exception e) {
			throw new AutoMapperException("Se ha producido un error al instanciar la clase:" + e);
		}
	}
	
	public AutoMapperWriter(Object obj) {
		this.index = new HashMap<FieldPath, Object>();
		this.index.put(FieldPath.root(), obj);
	}

	@Override
	public void object(FieldPath path, Class<?> type) {
		//Nos llega un objeto complejo
		//Si estamos en la raiz no hay que hacer nada mas
		if (!path.isRoot()) {
			Object obj = null;
			//Buscamos el objeto padre en el indice
			Object parent = index.get(path.getParent());
			//Obtenemos el binder del objeto padre
			AutoMapperObjectBinder parentBinder = new AutoMapperObjectBinder(parent);
			//Obtenemos el binder del campo del objeto
			AutoMapperFieldBinder parentFieldBinder = parentBinder.getFieldBinder(path.getFieldName());
			if (path.isCollectionItem()) {
				//Es un objeto dentro de una colección/array, lo instanciamos
				obj = parentFieldBinder.instantiateCollectionItem();
				//Lo añadimos a la colección/array del campo padre
				parentFieldBinder.addItemToCollection(path.getIndex(), obj);
			}else {
				//Instanciamos el objeto 
				obj = parentFieldBinder.instantiateObject();
				//Seteamos el objeto en el campo del padre
				parentFieldBinder.setValue(obj);
			}
			//Añadimos el nuevo objeto instanciado al indice en el path correspondiente
			index.put(path, obj);
		}

	}

	@Override
	public void collection(FieldPath path, int size) {
		//Estamos en una colección o array
		//Buscamos en el indice el objeto padre y obtenemos su binder
		Object parent = index.get(path.getParent());
		AutoMapperObjectBinder parentBinder = new AutoMapperObjectBinder(parent);
		//Obtenemos el binder del campo
		AutoMapperFieldBinder parentFieldBinder = parentBinder.getFieldBinder(path.getFieldName());
		//Instanciamos la colección/array
		Object collection = parentFieldBinder.instantiateCollection(size);
		//La seteamos en el campo
		parentFieldBinder.setValue(collection);
	}

	@Override
	public void serial(FieldPath path, String value) {
		//Estamos en un objeto serializado
		//Buscamos el objeto padre en el indice
		Object parent = index.get(path.getParent());
		AutoMapperObjectBinder parentBinder = new AutoMapperObjectBinder(parent);
		//Obtenemos el campo que vamos a setear
		AutoMapperFieldBinder parentFieldBinder = parentBinder.getFieldBinder(path.getFieldName());
		//Seteamos el campo como serial
		parentFieldBinder.setSerialValue(value);
	}
	
	public Object getObject() {
		return index.get(FieldPath.root());
	}

}
