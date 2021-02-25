package es.jlaa.automapper.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.jlaa.automapper.annotations.AutoMapper;
import es.jlaa.automapper.annotations.AutoMapperCollection;
import es.jlaa.automapper.annotations.AutoMapperField;
import es.jlaa.automapper.annotations.AutoMapperFormat;
import es.jlaa.automapper.annotations.AutoMapperProperty;
import es.jlaa.automapper.exceptions.AutoMapperException;
import es.jlaa.automapper.options.DateFormat;
import es.jlaa.automapper.options.NullValue;
import es.jlaa.automapper.options.Order;
import es.jlaa.formatter.Formatter;
import es.jlaa.formatter.FormatterConfigProvider;
import es.jlaa.formatter.config.Format;
import es.jlaa.formatter.config.providers.FormatterConfigBuilder;
import es.jlaa.formatter.utils.FormatterUtil;

public class AutoMapperFieldBinder {
	
	private Object obj;
	private Field field;
	private FormatterConfigProvider defaultFormatterConfigProvider;
	
	public AutoMapperFieldBinder(Object obj, Field field) {
		this.obj = obj;
		this.field = field;
		this.defaultFormatterConfigProvider = Format.config();
		
		AutoMapper ann = ReflectionHelper.getObjectAnnotation(AutoMapper.class, obj);
		if (ann != null) {
			try {
				this.defaultFormatterConfigProvider = (FormatterConfigProvider) ReflectionHelper.newInstance(ann.format());
			} catch(Exception e) {
				throw new AutoMapperException("Se ha producido un error al instanciar la clase '" + ann.format() + "': " + e);
			}
		}
	}
	
	public Object getValue() {
		try {
			return ReflectionHelper.getFieldValue(obj, field);
		}catch(Exception e) {
			throw new AutoMapperException("Se ha producido un error al obtener el valor del campo '" + field.getName() + "': " + e);
		}
	}
	
	public void setValue(Object value) {
		try {
			ReflectionHelper.setFieldValue(obj, field, value);
		}catch(Exception e) {
			throw new AutoMapperException("Se ha producido un error al obtener el valor del campo '" + field.getName() + "': " + e);
		}
	}
	
	public String getFieldName() {
		String name = ReflectionHelper.decamelize(field.getName());
		AutoMapperField fieldAutoMapper = ReflectionHelper.getFieldAnnotation(AutoMapperField.class, field);
		if (fieldAutoMapper != null && !fieldAutoMapper.name().isEmpty()) {
			name = fieldAutoMapper.name();
		}
		return name;
	}
	
	public boolean isSerial() {
		return ReflectionHelper.getFieldAnnotation(AutoMapperFormat.class, field) != null || ReflectionHelper.isSerialType(field.getType());
	}
	
	protected Formatter getFieldFormatter(AutoMapperFormat formatAutoMapper) {
		if (formatAutoMapper == null) return FormatterUtil.getDefaultFormatter();
		try {
			return (Formatter) ReflectionHelper.newInstance(formatAutoMapper.formatter());
		} catch (Exception e) {
			throw new AutoMapperException("Se ha producido un error al instanciar la clase '" + formatAutoMapper.formatter() + "': " + e);
		}
	}
	
	protected FormatterConfigProvider getFieldFormatConfig(AutoMapperFormat formatAutoMapper, FormatterConfigProvider def) {
		
		if (formatAutoMapper == null) return def;
		FormatterConfigBuilder builder = Format.config();
		if (!formatAutoMapper.pattern().isEmpty()) { builder.withPattern(formatAutoMapper.pattern());}
		if (!formatAutoMapper.locale().isEmpty()) {	builder.withLocale(formatAutoMapper.locale());}
		if (!formatAutoMapper.trueValue().isEmpty()) { builder.withTrueValue(formatAutoMapper.trueValue());}
		if (!formatAutoMapper.falseValue().isEmpty()) {	builder.withFalseValue(formatAutoMapper.falseValue());}
		if (formatAutoMapper.decimalSeparator() != '\0') {	builder.withDecimalSeparator(formatAutoMapper.decimalSeparator());}
		if (formatAutoMapper.groupingSeparator() != '\0') {	builder.withGroupingSeparator(formatAutoMapper.groupingSeparator());}
		
		if (formatAutoMapper.dateFormat() != DateFormat.DEFAULT) {builder.with(Format.Keys.DATE_FORMAT, formatAutoMapper.dateFormat().getValue());}
		
		for (AutoMapperProperty prop : formatAutoMapper.with()) {
			builder.with(prop.name(), prop.value());
		}
		
		return builder;
	}
	
	public String getSerialValue() {
		
		AutoMapperFormat formatAutoMapper = ReflectionHelper.getFieldAnnotation(AutoMapperFormat.class, field);
		Object fieldValue = getValue();
		if (fieldValue != null) {
			Formatter formatter = getFieldFormatter(formatAutoMapper);
			if (formatter.support(field.getType())) {
				FormatterConfigProvider config = getFieldFormatConfig(formatAutoMapper, defaultFormatterConfigProvider);
				return formatter.format(field.getType(), fieldValue, config);
			}else {
				throw new AutoMapperException("No se ha encontrado un formateador valido para el valor del campo '" + field.getName() + "' tipo:'" + fieldValue.getClass() + "'");
			}
		}else {
			return formatAutoMapper != null && NullValue.EMPTY.equals(formatAutoMapper.nullValue()) ? "" : null;
		}
	}
	
	public void setSerialValue(String value) {
		Object obj = null;
		AutoMapperFormat formatAutoMapper = ReflectionHelper.getFieldAnnotation(AutoMapperFormat.class, field);
		
		if ((value != null && !value.isEmpty()) || (value != null && value.isEmpty() && NullValue.NULL.equals(formatAutoMapper.nullValue()))) {
			Formatter formatter = getFieldFormatter(formatAutoMapper);
			if (formatter.support(field.getType())) {
				FormatterConfigProvider config = getFieldFormatConfig(formatAutoMapper, defaultFormatterConfigProvider);
				obj = formatter.parse(field.getType(), value, config);
			}else {
				throw new AutoMapperException("No se ha encontrado un formateador valido para el valor del campo '" + field.getName());
			}
		}
		
		setValue(obj);
	}
	
	public boolean isCollection() {
		return ReflectionHelper.isCollectionOrArrayType(field.getType());
	}
	
	public Collection<Object> getCollection(){
		
		if (!isCollection()) throw new AutoMapperException("El campo '" + field.getName() + "' no es una colección o un array");
		
		ArrayList<Object> list = null;
		Object obj = getValue();
		
		if (obj != null) {
			
			if (Collection.class.isAssignableFrom(field.getType())) {
				list = new ArrayList<>((Collection<Object>) obj);
			}else {
				list = new ArrayList<>(Arrays.asList((Object[]) obj));
			}
			
			AutoMapperCollection autoMapperCollection = ReflectionHelper.getFieldAnnotation(AutoMapperCollection.class, field);
			if (autoMapperCollection != null) {
				try {
					Comparator<Object> comparator = (Comparator<Object>) ReflectionHelper.newInstance(autoMapperCollection.comparator());
					if (Order.REVERSE.equals(autoMapperCollection.order())) {
						comparator = Collections.reverseOrder(comparator);
					}
					Collections.sort(list, comparator);
				} catch (Exception e) {
					throw new AutoMapperException("Error en el campo '" + field.getName() + "' al ordenar la colección:" + e);
				}
			}
		}
		return list;
	}
	
	public Object instantiateCollection(int size) {
		if (!isCollection()) throw new AutoMapperException("El campo '" + field.getName() + "' no es una colección o un array");

		Object collection = null;
		
		if (Collection.class.isAssignableFrom(field.getType())) {
			try {
				if (List.class.equals(field.getType()) || Collection.class.equals(field.getType())) {
					collection = new ArrayList();
				}else if (Set.class.equals(field.getType())) {
					collection = new HashSet();
				}else {
					collection = ReflectionHelper.newInstance(field.getType());
				}
			} catch (Exception e) {
				throw new AutoMapperException("Error al instanciar la colección '" + field.getName() + "' :" + e);
			}
		}else {
			collection = ReflectionHelper.newArrayInstance(field.getType().getComponentType(), size);
		}

		return collection;
		
	}
	
	public void addItemToCollection(Integer index, Object item) {
		if (!isCollection()) throw new AutoMapperException("El campo '" + field.getName() + "' no es una colección o un array");

		Object collection = null;
		
		Object obj = getValue();
		
		if (Collection.class.isAssignableFrom(field.getType())) {
			Collection coll = (Collection) obj;
			coll.add(item);
		}else {
			Array.set(obj, index, item);
		}
		
	}
	
	public Object instantiateCollectionItem(){
		if (!isCollection()) throw new AutoMapperException("El campo '" + field.getName() + "' no es una colección o un array");
		try {
			if (Collection.class.isAssignableFrom(field.getType())) {
				ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
				return ReflectionHelper.newInstance((Class<?>) parameterizedType.getActualTypeArguments()[0]);
			}else {
				return ReflectionHelper.newInstance(field.getType().getComponentType());
			}
		} catch (Exception e) {
			throw new AutoMapperException("Se ha producido un error al instanciar el objeto:" + e);
		}
	}
	
	public Object instantiateObject(){
		try {
			return ReflectionHelper.newInstance(field.getType());
		} catch (Exception e) {
			throw new AutoMapperException("Se ha producido un error al instanciar la clase:" + e);
		}
	}
	
	public boolean isTransient() {
		AutoMapperField autoMapperField = ReflectionHelper.getFieldAnnotation(AutoMapperField.class, field);
		return autoMapperField != null &&  autoMapperField.ignored() == true || ReflectionHelper.hasTransientModifier(field);
	}
	
}
