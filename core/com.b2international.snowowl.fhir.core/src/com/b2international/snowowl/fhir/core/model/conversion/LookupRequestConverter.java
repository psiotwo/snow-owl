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
package com.b2international.snowowl.fhir.core.model.conversion;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import com.b2international.snowowl.fhir.core.model.LookupRequest;
import com.b2international.snowowl.fhir.core.model.serialization.SerializableParameter;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * @since 6.3
 */
public class LookupRequestConverter extends StdConverter<LookupRequest,LookupRequest> {
	
	/**
	 * Converts the set of parameters into this populated domain object.
	 * This method is called right after the deserialization.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Override
	public LookupRequest convert(LookupRequest lookupRequest) {
		try {
			for (SerializableParameter serializableParameter : lookupRequest.getParameters()) {
				
				String fieldName = serializableParameter.getName();
				
				Field[] fields = LookupRequest.class.getDeclaredFields();
				Optional<Field> fieldOptional = Arrays.stream(fields)
					.map(f -> { 
						f.setAccessible(true);
						return f;
					})
					.filter(f -> f.getName().equals(fieldName))
					.findFirst();
				
				fieldOptional.orElseThrow(() -> new NullPointerException("Could not find field '" + fieldName + "'."));
				Field field = fieldOptional.get();
				field.set(lookupRequest, serializableParameter.getValue());
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalArgumentException("Error when converting lookup request." + e);
		}
		return lookupRequest;
	}

}