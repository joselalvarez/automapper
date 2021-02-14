package es.jlaa.automapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import es.jlaa.automapper.options.DateFormat;
import es.jlaa.automapper.options.NullValue;
import es.jlaa.formatter.formatters.DefaultFormatter;

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoMapperFormat {
	NullValue nullValue() default NullValue.NULL;
	Class<?> formatter() default DefaultFormatter.class;
	String pattern() default "";
	String locale() default "";
	String trueValue() default "";
	String falseValue() default "";
	char decimalSeparator() default '\0';
	char groupingSeparator() default '\0';
	DateFormat dateFormat() default DateFormat.DEFAULT;
	AutoMapperProperty [] with() default {};
}
