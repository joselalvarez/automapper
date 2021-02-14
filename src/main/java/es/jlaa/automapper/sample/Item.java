package es.jlaa.automapper.sample;

import es.jlaa.automapper.annotations.AutoMapperFormat;

public class Item {
	
	@AutoMapperFormat(pattern = "0000")
	private Long id;
	private String stringField;

	public Item() {
	}
	
	public Item(Long id, String stringField) {
		super();
		this.id = id;
		this.stringField = stringField;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getStringField() {
		return stringField;
	}
	
	public void setStringField(String stringField) {
		this.stringField = stringField;
	}

}
