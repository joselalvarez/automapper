package es.jlaa.automapper.options;

import es.jlaa.formatter.formatters.DateFormatter;

public enum DateFormat {
	
	DEFAULT(""), DATE_TIME(DateFormatter.FORMAT_DATE_TIME), DATE(DateFormatter.FORMAT_DATE), TIME(DateFormatter.FORMAT_TIME);
	
	private String value;
	
	DateFormat(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

}
