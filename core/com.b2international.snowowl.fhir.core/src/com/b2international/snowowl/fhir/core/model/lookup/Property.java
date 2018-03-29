/*
 * Copyright 2011-2018 B2i Healthcare Pte Ltd, http://b2i.sg
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.b2international.snowowl.fhir.core.model.lookup;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.b2international.snowowl.fhir.core.model.ValidatingBuilder;
import com.b2international.snowowl.fhir.core.model.conversion.Order;
import com.b2international.snowowl.fhir.core.model.conversion.PropertyConverter;
import com.b2international.snowowl.fhir.core.model.dt.Code;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

/**
 * Lookup Operation property
 * @since 6.3
 */
@JsonSerialize(converter=PropertyConverter.class)
@JsonInclude(Include.NON_EMPTY) //covers nulls as well
public class Property extends ParametersModel {
	
	//Identifies the property returned (1..1)
	@Order(value=1)
	@Valid
	@NotNull
	private final Code code;
	
	/*
	 * The value of the property returned (0..1)
	 * code | Coding | string | integer | boolean | dateTime
	 */
	@Order(value=2)
	private final Object value;
	
	//Human Readable representation of the property value (e.g. display for a code) 0..1
	@Order(value=3)
	private final String description;
	
	@Order(value=4)
	private final Collection<SubProperty> subProperties;
	
	Property(final Code code, final Object value, final String description, final Collection<SubProperty> subproperties) {
		this.code = code;
		this.value = value;
		this.description = description;
		this.subProperties = subproperties;
	}
	
	public Code getCode() {
		return code;
	}
	
	public String getCodeValue() {
		return code.getCodeValue();
	}

	/**
	 * How are we going to get the proper type serialized?
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}
	
	public Collection<SubProperty> getSubProperties() {
		return subProperties;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends ValidatingBuilder<Property> {
		
		private Code code;
		private Object value;
		private String description;
		private Collection<SubProperty> subProperties = Lists.newArrayList();

		public Builder code(final String code) {
			this.code = new Code(code);
			return this;
		}
		
		public Builder code(final Code code) {
			this.code = code;
			return this;
		}
		
		public Builder value(final Object value) {
			this.value = value;
			return this;
		}

		public Builder description(final String description) {
			this.description = description;
			return this;
		}
		
		public Builder addSubProperty(final SubProperty subProperty) {
			subProperties.add(subProperty);
			return this;
		}
		
		@Override
		protected Property doBuild() {
			return new Property(code, value, description, subProperties);
		}
	}

}