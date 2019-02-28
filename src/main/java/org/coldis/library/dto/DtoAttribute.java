package org.coldis.library.dto;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * DTO attribute metadata.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface DtoAttribute {

	/**
	 * Context is used to identify types and attributes that should be bound
	 * together.
	 */
	public String context() default "";

	/**
	 * If attribute should be ignored in the DTO.
	 */
	public boolean ignore() default false;

	/**
	 * DTO attribute name.
	 */
	public String name() default "";

	/**
	 * DTO attribute description.
	 */
	public String description() default "";

	/**
	 * DTO attribute type. By default, the original class (only for Java) or the
	 * compatible DTO class is used.
	 */
	public String type() default "";

	/**
	 * DTO attribute default value.
	 */
	public String defaultValue() default "";

	/**
	 * DTO attribute modifiers.
	 */
	public String[] modifiers() default {};

	/**
	 * If attribute method should be used in comparison methods.
	 */
	public boolean usedInComparison() default true;

}
