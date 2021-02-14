package es.jlaa.automapper.options;

import es.jlaa.formatter.FormatterConfig;
import es.jlaa.formatter.FormatterConfigProvider;
import es.jlaa.formatter.config.Format;
import es.jlaa.formatter.config.providers.FormatterConfigBuilder;

public class DefaultFormat implements FormatterConfigProvider{

	private FormatterConfigBuilder builder;
	
	public DefaultFormat() {
		this.builder = Format.config();
	}
	
	@Override
	public FormatterConfig getFormatterConfig(Class<?>... types) {
		return builder.getFormatterConfig(types);
	}

}
