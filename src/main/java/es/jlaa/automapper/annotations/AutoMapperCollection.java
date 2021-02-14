package es.jlaa.automapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import es.jlaa.automapper.options.DefaultComparator;
import es.jlaa.automapper.options.Order;

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoMapperCollection {
	Class<?> comparator() default DefaultComparator.class;
	Order order() default Order.DEFAULT;
}
