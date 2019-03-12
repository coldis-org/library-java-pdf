package org.coldis.library.dto;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * DTO attributes metadata.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface DtoAttributes {

	/**
	 * Attributes of DTO to be generated.
	 */
	public DtoAttribute[] attributes();

}
