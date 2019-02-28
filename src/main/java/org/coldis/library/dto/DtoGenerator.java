package org.coldis.library.dto;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.tools.StandardLocation;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DTO generator.
 */
@SupportedSourceVersion(value = SourceVersion.RELEASE_10)
@SupportedAnnotationTypes(value = { "org.coldis.library.dto.DtoType" })
public class DtoGenerator extends AbstractProcessor {

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DtoGenerator.class);

	/**
	 * Gets a DTO template.
	 *
	 * @param velocityEngine  Velocity engine.
	 * @param resourcesFolder The resources folder to be used.
	 * @param templatePath    The template path.
	 * @return The velocity template.
	 */
	private Template getTemplate(final VelocityEngine velocityEngine, String resourcesFolder,
			final String templatePath) {
		// Velocity template.
		Template velocityTemplate = null;
		// Tries to get the template for the given path.
		try {
			velocityTemplate = velocityEngine.getTemplate(resourcesFolder + templatePath);
		}
		// If the template cannot be retrieved
		catch (final Exception exception) {
			// Ignores it.
		}
		// If the template has not been found yet.
		if (velocityTemplate == null) {
			// Tries to get the template from the class path.
			velocityTemplate = velocityEngine.getTemplate(templatePath);
		}
		// Returns the found template.
		return velocityTemplate;
	}

	/**
	 * Generates a DTO from a original type.
	 *
	 * @param originalType    Original type information.
	 * @param dtoTypeMetadata DTO type metadata.
	 * @throws IOException If the class cannot be generated.
	 */
	private void generateDto(final TypeElement originalType, DtoTypeMetadata dtoTypeMetadata) throws IOException {
		// Velocity engine.
		final VelocityEngine velocityEngine = new VelocityEngine();
		// Configures the resource loader to also look at the classpath.
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		// Initializes the engine.
		velocityEngine.init();
		// Creates a new velocity context.
		final VelocityContext velocityContext = new VelocityContext();
		// Sets the context values.
		velocityContext.put("metadata", dtoTypeMetadata);
		velocityContext.put("newLine", "\r\n");
		velocityContext.put("tab", "\t");
		// Gets the template for the DTO.
		final Template dtoTemplate = getTemplate(velocityEngine, dtoTypeMetadata.getResourcesPath(),
				dtoTypeMetadata.getTemplatePath());
		// Prepares the writer for the DTO.
		Boolean javaSource = dtoTypeMetadata.getTemplatePath().endsWith(".java");
		final Writer dtoWriter = javaSource
				? processingEnv.getFiler()
						.createSourceFile(dtoTypeMetadata.getNamespace() + "." + dtoTypeMetadata.getName()).openWriter()
				: processingEnv.getFiler()
						.createResource(StandardLocation.SOURCE_PATH /* FIXME */, dtoTypeMetadata.getNamespace(),
								dtoTypeMetadata.getName() + "." + dtoTypeMetadata.getFileExtension())
						.openWriter();
		// Writes the generated class code.
		dtoTemplate.merge(velocityContext, dtoWriter);
		// Closes the class writer.
		dtoWriter.close();
	}

	/**
	 * Gets the DTO type metadata from an original type and a context.
	 * 
	 * @param originalTypeElement Original type.
	 * @param context             DTO context.
	 * @return The DTO type metadata from an original type and a context.
	 */
	private DtoType getDtoTypeAnno(TypeElement originalTypeElement, String context) {
		// Gets DTO attribute return type DTO metadata annotation.
		DtoType dtoTypeAnno = originalTypeElement.getAnnotation(DtoType.class);
		// If the DTO type annotation does not match the DTO context.
		if (dtoTypeAnno == null || !dtoTypeAnno.context().equals(context)) {
			// Re-sets the DTO type annotation.
			dtoTypeAnno = null;
			// Gets DTO types metadata annotation.
			DtoTypes dtoTypesAnno = originalTypeElement.getAnnotation(DtoTypes.class);
			// If there is a DTO types annotation.
			if (dtoTypesAnno != null) {
				// For each DTO type metadata annotation.
				for (DtoType currentDtoTypeAnno : dtoTypesAnno.types()) {
					// If the current DTO type annotation matches the DTO context.
					if (currentDtoTypeAnno.context().equals(context)) {
						// Updates the DTO type metadata annotation.
						dtoTypeAnno = currentDtoTypeAnno;
					}
				}
			}
		}
		// Returns the DTO type metadata.
		return dtoTypeAnno;
	}

	/**
	 * Gets the DTO type metadata.
	 * 
	 * @param originalType              The original type.
	 * @param dtoTypeAnno               The DTO type annotation.
	 * @param alsoGetAttributesMetadata If attributes metadata should also be
	 *                                  retrieved.
	 * @return The DTO type metadata.
	 */
	private DtoTypeMetadata getDtoTypeMetadata(final TypeElement originalType, DtoType dtoTypeAnno,
			Boolean alsoGetAttributesMetadata) {
		// Gets the default type metadata.
		DtoTypeMetadata dtoTypeMetadata = new DtoTypeMetadata(dtoTypeAnno.context(), dtoTypeAnno.resourcesPath(),
				dtoTypeAnno.templatePath(), dtoTypeAnno.fileExtension(), dtoTypeAnno.namespace(),
				dtoTypeAnno.name().isEmpty() ? originalType.getSimpleName() + "Dto" : dtoTypeAnno.name(),
				dtoTypeAnno.description(), null);
		// If attributes metadata should also be retrieved.
		if (alsoGetAttributesMetadata) {
			// Attributes of DTO.
			final List<String> alreadyAddedAttributes = new ArrayList<>();
			// Current type. Initially the given one, an then its super types.
			TypeElement currentClass = originalType;
			// For each type in the hierarchy.
			while ((currentClass != null) && !(currentClass instanceof NoType)) {
				// For each class enclosed element.
				for (final Element currentClassElement : currentClass.getEnclosedElements()) {
					// If the element is a public getter.
					if (currentClassElement.getKind().equals(ElementKind.METHOD)
							&& currentClassElement.getModifiers().contains(Modifier.PUBLIC)
							&& (!currentClassElement.getModifiers().contains(Modifier.STATIC))
							&& (currentClassElement.getSimpleName().toString().startsWith("get")
									|| currentClassElement.getSimpleName().toString().startsWith("is"))) {
						// If it is a boolean getter.
						final Boolean booleanGetter = currentClassElement.getSimpleName().toString().startsWith("is");
						// Gets the default attribute name.
						String attributeName = currentClassElement.getSimpleName().toString();
						attributeName = attributeName.substring(booleanGetter ? 2 : 3, booleanGetter ? 3 : 4)
								.toLowerCase() + attributeName.substring(booleanGetter ? 3 : 4);
						// If the attribute has not been added yet (for override attributes).
						if (!alreadyAddedAttributes.contains(currentClassElement.getSimpleName().toString())
								&& (!"class".equals(attributeName))) {
							// Gets the attribute metadata.
							DtoAttributeMetadata dtoAttributeMetadata = getDtoAttributeMetadata(
									dtoTypeMetadata.getContext(), currentClassElement, attributeName);
							// If the attribute metadata is retrieved.
							if (dtoAttributeMetadata != null) {
								// Adds the DTO attribute for later conversion.
								dtoTypeMetadata.getAttributes().add(dtoAttributeMetadata);
							}
							// Adds the attribute to the already added list.
							alreadyAddedAttributes.add(currentClassElement.getSimpleName().toString());
						}
					}
				}
				// The current class is the late class superclass.
				currentClass = currentClass.getSuperclass() instanceof DeclaredType
						? (TypeElement) ((DeclaredType) currentClass.getSuperclass()).asElement()
						: null;
			}
		}
		// Returns the type metadata.
		return dtoTypeMetadata;
	}

	/**
	 * Gets the DTO attribute metadata from an attribute getter and a context.
	 * 
	 * @param attributeGetter Attribute getter.
	 * @param context         DTO context.
	 * @return The DTO attribute metadata from an attribute getter and a context.
	 */
	private DtoAttribute getDtoAttributeAnno(Element attributeGetter, String context) {
		// Gets DTO attribute metadata annotation.
		DtoAttribute dtoAttributeAnno = attributeGetter.getAnnotation(DtoAttribute.class);
		// If the DTO attribute annotation does not match the DTO context.
		if (dtoAttributeAnno == null || !dtoAttributeAnno.context().equals(context)) {
			// Re-sets the DTO attribute annotation.
			dtoAttributeAnno = null;
			// Gets DTO attributes metadata annotation.
			DtoAttributes dtoAttributesAnno = attributeGetter.getAnnotation(DtoAttributes.class);
			// If there is a DTO attributes annotation.
			if (dtoAttributesAnno != null) {
				// For each DTO attribute metadata annotation.
				for (DtoAttribute currentDtoAttributeAnno : dtoAttributesAnno.attributes()) {
					// If the current DTO attribute annotation matches the DTO context.
					if (currentDtoAttributeAnno.context().equals(context)) {
						// Updates the DTO attribute metadata annotation.
						dtoAttributeAnno = currentDtoAttributeAnno;
					}
				}
			}
		}
		// Returns the attribute metadata.
		return dtoAttributeAnno;
	}

	/**
	 * Gets the DTO types in hierarchy recursively.
	 * 
	 * @param type                The type to get the DTOs type recursively.
	 * @param context             The DTO generation context.
	 * @param dtoTypesInHierarchy The map with already found DTO types in hierarchy.
	 * @return The DTO types in hierarchy recursively.
	 */
	private Map<String, String> getDtoTypesInHierarchy(TypeMirror type, String context,
			Map<String, String> dtoTypesInHierarchy) {
		// If the type is a array of a declared type.
		Boolean isDeclaredArrayType = (type instanceof ArrayType)
				&& ((ArrayType) type).getComponentType() instanceof DeclaredType;
		// Only if it is a declared type or an array of.
		if (type instanceof DeclaredType || isDeclaredArrayType) {
			// Gets the declared type and type element.
			DeclaredType declaredType = isDeclaredArrayType ? ((DeclaredType) ((ArrayType) type).getComponentType())
					: ((DeclaredType) type);
			TypeElement currentTypeElement = (TypeElement) declaredType.asElement();
			// Gets the DTO type metadata for a given context.
			DtoType dtoAttributeTypeAnno = getDtoTypeAnno(currentTypeElement, context);
			// If the attribute type should also be a DTO.
			if (dtoAttributeTypeAnno != null) {
				// Gets the attribute type metadata.
				DtoTypeMetadata dtoAttributeTypeMetadata = getDtoTypeMetadata(currentTypeElement, dtoAttributeTypeAnno,
						false);
				// Adds the DTO type to the map.
				dtoTypesInHierarchy.put(currentTypeElement.getQualifiedName().toString(),
						dtoAttributeTypeMetadata.getNamespace() + "." + dtoAttributeTypeMetadata.getName());
			}
			// For each type parameter of the current type.
			if (declaredType.getTypeArguments() != null) {
				for (TypeMirror currentTypeArgument : declaredType.getTypeArguments()) {
					// Gets the DTO types in hierarchy recursively.
					getDtoTypesInHierarchy(currentTypeArgument, context, dtoTypesInHierarchy);
				}
			}
		}
		// Returns the updated DTO types in hierarchy.
		return dtoTypesInHierarchy;
	}

	/**
	 * Gets the DTO attribute metadata.
	 * 
	 * @param context         The DTO context.
	 * @param attributeGetter The attribute getter.
	 * @param defaultAttrName The default attribute name.
	 * @return The DTO attribute metadata.
	 */
	private DtoAttributeMetadata getDtoAttributeMetadata(String context, Element attributeGetter,
			String defaultAttrName) {
		// Gets the default attribute metadata.
		DtoAttributeMetadata dtoAttributeMetadata = null;
		// Gets the DTO attribute metadata annotation.
		DtoAttribute dtoAttributeAnno = getDtoAttributeAnno(attributeGetter, context);
		// If the attribute should not be ignored.
		if (dtoAttributeAnno == null || (dtoAttributeAnno != null && !dtoAttributeAnno.ignore())) {
			// Gets the attribute original type.
			TypeMirror attributeOriginalType = ((ExecutableType) attributeGetter.asType()).getReturnType();
			// Gets the default attribute metadata.
			dtoAttributeMetadata = new DtoAttributeMetadata(new ArrayList<>(), attributeOriginalType.toString(),
					defaultAttrName, defaultAttrName, "", true);
			// DTOs in attribute hierarchy.
			Map<String, String> dtoTypesInAttrHier = getDtoTypesInHierarchy(attributeOriginalType, context,
					new HashMap<>());
			// For each other DTO in the hierarchy.
			for (Entry<String, String> dtoTypeInAttrHier : dtoTypesInAttrHier.entrySet()) {
				// Replaces the original attribute type for the correspondent DTO type.
				dtoAttributeMetadata.setType(dtoAttributeMetadata.getType().replaceAll(dtoTypeInAttrHier.getKey(),
						dtoTypeInAttrHier.getValue()));
			}
			// If the attribute metadata annotation is present.
			if (dtoAttributeAnno != null) {
				// Updates the DTO attribute metadata from the annotation information.
				dtoAttributeMetadata.setModifiers(Arrays.asList(dtoAttributeAnno.modifiers()));
				dtoAttributeMetadata.setType(
						dtoAttributeAnno.type().isEmpty() ? dtoAttributeMetadata.getType() : dtoAttributeAnno.type());
				dtoAttributeMetadata.setName(
						dtoAttributeAnno.name().isEmpty() ? dtoAttributeMetadata.getName() : dtoAttributeAnno.name());
				dtoAttributeMetadata
						.setDescription(dtoAttributeAnno.description().isEmpty() ? dtoAttributeMetadata.getDescription()
								: dtoAttributeAnno.description());
				dtoAttributeMetadata.setDefaultValue(dtoAttributeAnno.defaultValue());
				dtoAttributeMetadata.setUsedInComparison(dtoAttributeAnno.usedInComparison());
			}
		}
		// Returns the attribute metadata.
		return dtoAttributeMetadata;
	}

	/**
	 * Generates the DTO from type and metadata.
	 * 
	 * @param originalType Original type generating the DTO.
	 * @param dtoMetadata  DTO metadata.
	 */
	private void generateDto(final TypeElement originalType, final DtoType dtoMetadata) {
		// Gets the DTO metadata.
		DtoTypeMetadata dtoTypeMetadata = getDtoTypeMetadata(originalType, dtoMetadata, true);
		// Tries to generate the DTOs.
		try {
			// Generates the classes.
			DtoGenerator.LOGGER.debug("Genrating DTO " + dtoTypeMetadata.getName() + ".");
			generateDto(originalType, dtoTypeMetadata);
			DtoGenerator.LOGGER.debug("DTO " + dtoTypeMetadata.getName() + " created successfully.");
		}
		// If there is a problem generating the DTOs.
		catch (final IOException exception) {
			// Logs it.
			DtoGenerator.LOGGER.error("DTO " + dtoTypeMetadata.getName() + " not created successfully.", exception);
		}
	}

	/**
	 * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set,
	 *      javax.annotation.processing.RoundEnvironment)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		DtoGenerator.LOGGER.debug("Initializing DTOs generation...");
		// For each type generating multiple DTOs.
		for (final TypeElement originalType : (Set<TypeElement>) roundEnv.getElementsAnnotatedWith(DtoTypes.class)) {
			DtoGenerator.LOGGER.debug("Creating DTOs from " + originalType.getSimpleName() + "...");
			// Gets the DTOs metadata.
			final DtoTypes dtosMetadata = originalType.getAnnotation(DtoTypes.class);
			// For each DTO metadata.
			for (DtoType dtoMetadata : dtosMetadata.types()) {
				// Generates the DTO.
				generateDto(originalType, dtoMetadata);
			}
		}
		// For each type generating a single DTO.
		for (final TypeElement originalType : (Set<TypeElement>) roundEnv.getElementsAnnotatedWith(DtoType.class)) {
			DtoGenerator.LOGGER.debug("Creating DTO from " + originalType.getSimpleName() + "...");
			// Gets the DTO metadata.
			final DtoType dtoMetadata = originalType.getAnnotation(DtoType.class);
			// Generates the DTO.
			generateDto(originalType, dtoMetadata);
		}
		// Mark that the message sources annotations have been processed.
		DtoGenerator.LOGGER.debug("Finishing DtoGenerator...");
		return true;
	}

}
