package org.coldis.library.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * DTO type metadata.
 */
public class DtoTypeMetadata implements Serializable {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = -8977720832301173320L;

	/**
	 * Type context.
	 */
	private String context;

	/**
	 * Resources path.
	 */
	private String resourcesPath;

	/**
	 * Template relative path (from resources).
	 */
	private String templatePath;

	/**
	 * The DTO file extension.
	 */
	private String fileExtension;

	/**
	 * Type namespace.
	 */
	private String namespace;

	/**
	 * Type name.
	 */
	private String name;

	/**
	 * Type description.
	 */
	private String description;

	/**
	 * Type attributes metadata.
	 */
	private List<DtoAttributeMetadata> attributes;

	/**
	 * Default constructor.
	 * 
	 * @param context       Type context.
	 * @param resourcesPath Resources path.
	 * @param templatePath  Template relative path (from resources).
	 * @param fileExtension The DTO file extension.
	 * @param namespace     Type namespace.
	 * @param name          Type name.
	 * @param description   Type description.
	 * @param attributes    Type attributes metadata.
	 */
	public DtoTypeMetadata(String context, String resourcesPath, String templatePath, String fileExtension,
			String namespace, String name, String description, List<DtoAttributeMetadata> attributes) {
		super();
		this.context = context;
		this.resourcesPath = resourcesPath;
		this.templatePath = templatePath;
		this.fileExtension = fileExtension;
		this.namespace = namespace;
		this.name = name;
		this.description = description;
		this.attributes = attributes;
	}

	/**
	 * Gets the context.
	 * 
	 * @return The context.
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Sets the context.
	 * 
	 * @param context New context.
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * Gets the resourcesPath.
	 * 
	 * @return The resourcesPath.
	 */
	public String getResourcesPath() {
		return resourcesPath;
	}

	/**
	 * Sets the resourcesPath.
	 * 
	 * @param resourcesPath New resourcesPath.
	 */
	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}

	/**
	 * Gets the templatePath.
	 * 
	 * @return The templatePath.
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * Sets the templatePath.
	 * 
	 * @param templatePath New templatePath.
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * Gets the fileExtension.
	 * 
	 * @return The fileExtension.
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * Sets the fileExtension.
	 * 
	 * @param fileExtension New fileExtension.
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * Gets the namespace.
	 * 
	 * @return The namespace.
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Sets the namespace.
	 * 
	 * @param namespace New namespace.
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Gets the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name New name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description.
	 * 
	 * @return The description.
	 */
	public String getDescription() {
		return StringUtils.isEmpty(description) ? getName() : description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description New description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the attributes.
	 * 
	 * @return The attributes.
	 */
	public List<DtoAttributeMetadata> getAttributes() {
		// If list is not initialized.
		if (attributes == null) {
			// Initializes it as an empty list.
			attributes = new ArrayList<>();
		}
		// Returns the list.
		return attributes;
	}

	/**
	 * Sets the attributes.
	 * 
	 * @param attributes New attributes.
	 */
	public void setAttributes(List<DtoAttributeMetadata> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(attributes, context, description, fileExtension, name, namespace, resourcesPath,
				templatePath);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DtoTypeMetadata)) {
			return false;
		}
		DtoTypeMetadata other = (DtoTypeMetadata) obj;
		return Objects.equals(attributes, other.attributes) && Objects.equals(context, other.context)
				&& Objects.equals(description, other.description) && Objects.equals(fileExtension, other.fileExtension)
				&& Objects.equals(name, other.name) && Objects.equals(namespace, other.namespace)
				&& Objects.equals(resourcesPath, other.resourcesPath)
				&& Objects.equals(templatePath, other.templatePath);
	}

}
