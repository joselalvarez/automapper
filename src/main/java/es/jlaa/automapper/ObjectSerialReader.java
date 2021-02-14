package es.jlaa.automapper;

public interface ObjectSerialReader {
	void object(FieldPath path, Class<?> type);
	void collection(FieldPath path, int size);
	void serial(FieldPath path, String value);
}
