package es.jlaa.automapper.utils;

import java.util.Hashtable;

import es.jlaa.automapper.impl.AutoMapperSerializer;
import es.jlaa.automapper.impl.AutoMapperWriter;
import es.jlaa.automapper.impl.HashtableSerializer;
import es.jlaa.automapper.impl.HashtableWriter;
import es.jlaa.automapper.impl.LogSerializerWriter;

public class AutoMapperUtil {
	
	public static Hashtable<String, Object> map(Hashtable<String, Object> table, Object obj){
		
		HashtableWriter hashtableWriter = new HashtableWriter(table);
    	new AutoMapperSerializer().serialize(obj, hashtableWriter);
		
    	return hashtableWriter.getHashtable();

	}
	
	public static <T> T map(T obj, Hashtable<String, Object> table){
		
    	AutoMapperWriter objectWriter = new AutoMapperWriter(obj);
    	new HashtableSerializer().serialize(table, objectWriter);
    	
		return (T) objectWriter.getObject();
	}
	
	public static String mapLog(Hashtable<String, Object> table) {
		
		LogSerializerWriter logWriter = new LogSerializerWriter();
		new HashtableSerializer().serialize(table, logWriter);
		
		return logWriter.getLog();
	}
	
	public static String mapLog(Object obj) {
		
		LogSerializerWriter logWriter = new LogSerializerWriter();
		new AutoMapperSerializer().serialize(obj, logWriter);
		
		return logWriter.getLog();
	}
}
