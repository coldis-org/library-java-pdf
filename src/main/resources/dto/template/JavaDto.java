package ${metadata.namespace};

import java.io.Serializable;
import java.util.Objects;
import java.util.Arrays;

/**
 * ${metadata.description}.
 */
public class ${metadata.name} implements Serializable {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = ${metadata.namespace.hashCode()}${metadata.name.hashCode()}L;
	
#foreach( ${attribute} in ${metadata.attributes} )
	/**
	 * ${attribute.description}.
	 */
	private #foreach( ${modifier} in ${attribute.modifiers} )${modifier} #end${attribute.type} ${attribute.name}#if("$!attribute.defaultValue" != "") = ${attribute.defaultValue}#end;

#end

	/**
	 * No arguments constructor.
	 */
	public ${metadata.name}() {
		super();
	}

	/**
	 * Default constructor.
#foreach( ${attribute} in ${metadata.attributes} )
 	 * @param ${attribute.name}
 	 *            ${attribute.description}.
#end
	 */
	public ${metadata.name}(
			#set( $currentItemIdx = 0 )#foreach( ${attribute} in ${metadata.attributes} )
#if( !$attribute.modifiers.contains("static") && 
		!$attribute.modifiers.contains("final") )#if( ${currentItemIdx} > 0 ),
			#end#set( $currentItemIdx = $currentItemIdx + 1 )${attribute.type} ${attribute.name}#end
#end) {
		super();
#foreach( ${attribute} in ${metadata.attributes} )
#if( !$attribute.modifiers.contains("static") && 
	!$attribute.modifiers.contains("final") )		this.${attribute.name} = ${attribute.name};
#end
#end
	}

#foreach( ${attribute} in ${metadata.attributes} )
	/**
	 * Gets the ${attribute.description}.
	 * @return The ${attribute.description}.
	 */
	public#if( $attribute.modifiers.contains("static") ) static#end ${attribute.type} get${attribute.capitalizedName}() {
		return ${attribute.name};
	}
#if( !$attribute.modifiers.contains("final") )	
	/**
	 * Sets the ${attribute.description}.
	 *
	 * @param ${attribute.name}
	 *            The ${attribute.description}.
	 */
	public#if( $attribute.modifiers.contains("static") ) static#end void set${attribute.capitalizedName}(final ${attribute.type} ${attribute.name}) {
		this.${attribute.name} = ${attribute.name};
	}
#end
#end

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hash(
				#set( $currentItemIdx = 0 )#foreach( ${attribute} in ${metadata.attributes} )#if( ${attribute.usedInComparison} && !${attribute.type.endsWith("[]")} )
#if( ${currentItemIdx} > 0 ),
				#end${attribute.name}#set( $currentItemIdx = $currentItemIdx + 1 )
#end
#end

			);
#foreach( ${attribute} in ${metadata.attributes} )#if( ${attribute.usedInComparison} && ${attribute.type.endsWith("[]")} )
		result = prime * result + Arrays.hashCode(${attribute.name});
#end#end
		return result;
	}
	
	/**
	 * @see java.lang.Object#[[#]]#equals(java.lang.Object)
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
		final ${metadata.name} other = (${metadata.name}) obj;
#foreach( ${attribute} in ${metadata.attributes} )#if( ${attribute.usedInComparison} )
		if (!#if(${attribute.type.endsWith("[]")}) Arrays#else Objects#end.equals(${attribute.name}, other.${attribute.name})) {
			return false;
		}
#end#end
		return true;
	}
	
}