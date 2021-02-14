package es.jlaa.automapper.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import es.jlaa.formatter.utils.FormatterUtil;

class ReflectionHelper {
	
	public static Collection<Field> getDeclaredFields(Class<?> clazz) {
		ArrayList<Field> fields = new ArrayList<Field>();
		Class<?> currentClazz = clazz;
		do {
			fields.addAll(Arrays.asList(currentClazz.getDeclaredFields()));
			currentClazz = currentClazz.getSuperclass();
		}while(currentClazz != null);
		return fields;
	}
	
	public static <T> T getFieldAnnotation(Class<T> type, Field field) {
		for (Annotation ann : field.getAnnotations()) {
			if (ann.annotationType().equals(type)) {
				return (T) ann;
			}
		}
		return null;
	}
	
	public static <T> T getObjectAnnotation(Class<T> type, Object obj) {
		for (Annotation ann : obj.getClass().getAnnotations()) {
			if (ann.annotationType().equals(type)) {
				return (T) ann;
			}
		}
		return null;
	}
	
	public static String decamelize(String s) {
		if (s == null || s.length() == 0) return s;
		if (s.length() == 1) return s.toUpperCase();
		
		StringBuilder out = new StringBuilder();
		out.append(s.charAt(0));
		
		for (int i = 1; i < s.length() ; i++) {
			char a = s.charAt(i - 1);
			char b = s.charAt(i);
			if (Character.isLowerCase(a) && Character.isUpperCase(b)) {
				out.append('_').append(b);
			}else {
				out.append(b);
			}
		}
		
		return out.toString().toUpperCase();
	}
	
	public static Object getFieldValue(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException {
		Boolean access = field.isAccessible();//java 1.6
		field.setAccessible(true);
		Object value = field.get(obj);
		field.setAccessible(access);
		return value;
	}
	
	public static void setFieldValue(Object obj, Field field, Object value) throws IllegalArgumentException, IllegalAccessException  {
		Boolean access = field.isAccessible();//java 1.6
		field.setAccessible(true);
		field.set(obj, value);
		field.setAccessible(access);
	}
	
	public static Object newInstance(Class<?> type) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return type.getDeclaredConstructor(new Class<?> [] {}).newInstance(new Object [] {});
	}
	
	public static Object newArrayInstance(Class<?> componentType, int size)  {
		return Array.newInstance(componentType, size);
	}
	
	public static boolean isCollectionOrArrayType(Class<?> type) {
		return Collection.class.isAssignableFrom(type) || type.isArray();
	}
	
	public static boolean isMapType(Class<?> type) {
		return Map.class.isAssignableFrom(type);
	}

	public static boolean isSerialType(Class<?> type) {
		return FormatterUtil.getDefaultFormatter().support(type);
	}
	
	public static boolean hasTransientModifier(Field field) {
		return Modifier.isTransient(field.getModifiers());
	}

	private static String hashtableToString(Hashtable<String, Object> [] arr) {
		StringBuilder out = new StringBuilder();
		out.append("[");
		for (Hashtable<String, Object> t : arr) {
			out.append(hashtableToString(t));
		}
		out.append("]");
		return out.toString();
	}
	
	public static String hashtableToString(Hashtable<String, Object> table) {
		if (table == null) return "null";
		StringBuilder out = new StringBuilder();
		out.append("{");
		boolean first = true;
		for (Entry<String, Object> entry : table.entrySet()) {
			if (!first) out.append(", ");
			first = false;
			out.append(entry.getKey()).append("=");
			if (entry.getValue().getClass().equals(Hashtable.class)) {
				out.append(hashtableToString((Hashtable<String, Object>) entry.getValue()));
			}else if (entry.getValue().getClass().isArray()){
				out.append(hashtableToString((Hashtable<String, Object> []) entry.getValue()));
			}else {
				out.append("'").append(entry.getValue()).append("'");
			}
		}
		out.append("}");
		return out.toString();
	}
}
