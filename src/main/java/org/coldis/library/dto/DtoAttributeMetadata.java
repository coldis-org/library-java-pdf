package org.coldis.library.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * DTO attribute metadata.
 */
public class DtoAttributeMetadata implements Serializable {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 6528596189706828445L;

	/**
	 * Attribute modifiers.
	 */
	private List<String> modifiers;

	/**
	 * Attribute type.
	 */
	private String type;

	/**
	 * Attribute name.
	 */
	private String name;

	/**
	 * Attribute description.
	 */
	private String description;

	/**
	 * Attribute default value.
	 */
	private String defaultValue;

	/**
	 * If attribute should be used when comparing the DTO.
	 */
	private Boolean usedInComparison;

	/**
	 * Complete constructor.
	 * 
	 * @param modifiers        Attribute modifiers.
	 * @param type             Attribute type.
	 * @param name             Attribute name.
	 * @param description      Attribute description.
	 * @param defaultValue     Attribute default value.
	 * @param usedInComparison If attribute should be used when comparing the DTO.
	 */
	public DtoAttributeMetadata(List<String> modifiers, String type, String name, String description,
			String defaultValue, Boolean usedInComparison) {
		super();
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
		this.description = description;
		this.defaultValue = defaultValue;
		this.usedInComparison = usedInComparison;
	}

	/**
	 * Gets the modifiers.
	 * 
	 * @return The modifiers.
	 */
	public List<String> getModifiers() {
		// If list is not initialized.
		if (modifiers == null) {
			// Initializes it as an empty list.
			modifiers = new ArrayList<>();
		}
		// Returns the list.
		return modifiers;
	}

	/**
	 * Sets the modifiers.
	 * 
	 * @param modifiers New modifiers.
	 */
	public void setModifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	/**
	 * Gets the type.
	 * 
	 * @return The type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type New type.
	 */
	public void setType(String type) {
		this.type = type;
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
	 * Gets the capitalized name.
	 * 
	 * @return The capitalized name.
	 */
	public String getCapitalizedName() {
		return getName() == null ? null : getName().substring(0, 1).toUpperCase() + getName().substring(1);
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
	 * Gets the defaultValue.
	 * 
	 * @return The defaultValue.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets the defaultValue.
	 * 
	 * @param defaultValue New defaultValue.
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Gets the usedInComparison.
	 * 
	 * @return The usedInComparison.
	 */
	public Boolean getUsedInComparison() {
		return usedInComparison;
	}

	/**
	 * Sets the usedInComparison.
	 * 
	 * @param usedInComparison New usedInComparison.
	 */
	public void setUsedInComparison(Boolean usedInComparison) {
		this.usedInComparison = usedInComparison;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(defaultValue, description, modifiers, name, type, usedInComparison);
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
		if (!(obj instanceof DtoAttributeMetadata)) {
			return false;
		}
		DtoAttributeMetadata other = (DtoAttributeMetadata) obj;
		return Objects.equals(defaultValue, other.defaultValue) && Objects.equals(description, other.description)
				&& Objects.equals(modifiers, other.modifiers) && Objects.equals(name, other.name)
				&& Objects.equals(type, other.type) && Objects.equals(usedInComparison, other.usedInComparison);
	}

}