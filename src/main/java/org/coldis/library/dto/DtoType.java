package org.coldis.library.dto;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * DTO type metadata.
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface DtoType {

	/**
	 * Context is used to identify types and attributes that should be bound
	 * together.
	 */
	public String context() default "";

	/**
	 * Resources path. Default is "src/main/resources".
	 */
	public String resourcesPath() default "src/main/resources/";

	/**
	 * Template relative path (from resources).
	 */
	public String templatePath() default "template/dto/JavaDto.java";

	/**
	 * The DTO file extension.
	 */
	public String fileExtension() default "java";

	/**
	 * DTO type namespace.
	 */
	public String namespace();

	/**
	 * DTO type name. Default is the origin class name with the "Dto" append.
	 */
	public String name() default "";

	/**
	 * DTO type description.
	 */
	public String description() default "";

}
