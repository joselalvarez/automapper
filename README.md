# Automapper
Utilidades para el mapeo de objetos a Hashtable y viceversa.

## Uso

	Hashtable table = AutoMapperUtil.map(new Hashtable<String, Object>(), sample);
	Sample copy = AutoMapperUtil.map(new Sample(), table);
	
## Anotaciones a nivel de clase

	@AutoMapper(format = [FormatterConfigProvider].class) // Define el formato por defecto del mapeado
	
## Anotaciones a nivel de campo

	@AutoMapperField(name = [name], //Nombre del mapeo
							ignored = [true|false]) // Si se ignora o no el campo, equivalente a declarar el campo como "transient"

	@AutoMapperFormat(nullValue = [NullValue.NULL|NullValue.EMPTY],
							formatter = [Formatter].class,
							pattern = [patrón de formato];
							locale = [locale];
							trueValue = [true value];
							falseValue = [false value];
							decimalSeparator = [decimal separator];
							groupingSeparator = [grouping separator];
							dateFormat = [DateFormat.DEFAULT|DateFormat.DATE|DateFormat.TIME|DateFormat.DATE_TIME])

	@AutoMapperCollection (comparator = [Comparator].class;
									order = [Order.DEFAULT|Order.REVERSE];