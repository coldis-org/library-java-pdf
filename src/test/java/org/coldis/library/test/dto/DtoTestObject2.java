package org.coldis.library.test.dto;

import org.coldis.library.dto.DtoType;
import org.coldis.library.model.IdentifiedObject;

/**
 * DTO test object.
 */
@DtoType(namespace = "org.coldis.library.test.dto.dto")
public class DtoTestObject2 implements IdentifiedObject {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = -6904605762253009838L;

	/**
	 * Object identifier.
	 */
	private Long id;

	/**
	 * Test attribute.
	 */
	private String test;

	/**
	 * @see br.com.rebelbank.common.spring.model.generic.IdentifiedEntity#getId()
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the identifier.
	 *
	 * @param id New identifier.
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Gets the test.
	 *
	 * @return The test.
	 */
	public String getTest() {
		return test;
	}

	/**
	 * Sets the test.
	 *
	 * @param test New test.
	 */
	public void setTest(final String test) {
		this.test = test;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
		result = (prime * result) + ((test == null) ? 0 : test.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DtoTestObject2 other = (DtoTestObject2) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (test == null) {
			if (other.test != null) {
				return false;
			}
		} else if (!test.equals(other.test)) {
			return false;
		}
		return true;
	}

}
