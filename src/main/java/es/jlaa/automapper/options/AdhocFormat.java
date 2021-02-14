package es.jlaa.automapper.options;

import java.util.Date;

import es.jlaa.formatter.FormatterConfig;
import es.jlaa.formatter.FormatterConfigProvider;
import es.jlaa.formatter.config.Format;
import es.jlaa.formatter.config.providers.FormatterConfigBuilder;


public class AdhocFormat implements FormatterConfigProvider{
	
	private FormatterConfigBuilder builder;
	
	public AdhocFormat() {

		this.builder = Format.config();
		builder.forType(Boolean.class).withBooleanValues("1", "0");
		builder.forType(Date.class).withPattern("dd/MM/yyyy");
		builder.forType(Double.class).withPattern("#,###,##0.00").withNumberSeparators(',', '.');
		builder.forType(Float.class).withPattern("#,###,##0.00").withNumberSeparators(',', '.');
		
	}

	@Override
	public FormatterConfig getFormatterConfig(Class<?>... types) {
		return builder.getFormatterConfig(types);
	}

}
