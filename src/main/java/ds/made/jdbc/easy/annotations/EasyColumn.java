package ds.made.jdbc.easy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation means this field is equivalent to a database row field (from a rowset).
 * @author ds
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EasyColumn
{
	String name();
	Class<?> enumFactoryClass() default Object.class;
	String staticEnumMethod() default "";
	boolean blob() default false;
	boolean clob() default false;
}
