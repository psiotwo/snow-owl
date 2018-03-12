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
package com.b2international.snowowl.fhir.api.tests.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Assert;
import org.junit.Test;

import com.b2international.snowowl.fhir.api.exceptions.ValidationException;
import com.b2international.snowowl.fhir.api.model.LookupRequest;
import com.b2international.snowowl.fhir.api.model.LookupResult;
import com.b2international.snowowl.fhir.api.model.dt.Code;
import com.b2international.snowowl.fhir.api.model.dt.Coding;
import com.b2international.snowowl.fhir.api.model.dt.DateFormats;
import com.b2international.snowowl.fhir.api.model.dt.Uri;
import com.b2international.snowowl.fhir.api.model.serialization.SerializableParameter;
import com.b2international.snowowl.fhir.api.tests.FhirTest;

public class ModelDeserializationTest extends FhirTest {
	
	@Test
	public void codingTest() throws Exception {
		
		String jsonCoding = "{\"code\":\"1234\","
				+ "\"system\":\"http://snomed.info/sct\","
				+ "\"version\":\"20180131\",\"userSelected\":false}";
		
		Coding coding = objectMapper.readValue(jsonCoding, Coding.class);
		
		Assert.assertEquals(new Code("1234"), coding.getCode());
		Assert.assertEquals(new Uri("http://snomed.info/sct"), coding.getSystem());
		Assert.assertEquals("20180131", coding.getVersion());
	}
	
	/**
	 * Validates this operation outcome.
	 * @throws ValidationException
	 */
	public void validate(Coding object) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Coding>> violations = validator.validate(object);
			if (!violations.isEmpty()) {
				
				violations.forEach(cv -> {
					System.out.println(cv.toString());
					System.out.println("Message: " + cv.getMessage());
					System.out.println(" xMessage template: " + cv.getMessageTemplate());
					System.out.println("Invalid value: " + cv.getInvalidValue());
					System.out.println("Leaf bean: " + cv.getLeafBean());
					System.out.println("Property path: " + cv.getPropertyPath());
					System.out.println("Root bean (Coding): " + cv.getRootBean());
					System.out.println("Root bean class: " + cv.getRootBeanClass());
					System.out.println(" xConstraint descriptor: " + cv.getConstraintDescriptor());
					System.out.println(" xExec parameters: " + cv.getExecutableParameters());
					System.out.println(" xExec return value: " + cv.getExecutableReturnValue());
					
				});
				
				//bit of a hack
				ValidationException validationException = new ValidationException(violations);
				validationException.toOperationOutcome();
				//Map<String, Object> additionalInfo = validationException.toApiError().getAdditionalInfo();
				//throw new javax.validation.ValidationException(additionalInfo.toString());
			}
	}

	
	@Test
	public void codingMissingSystemTest() throws Exception {
		
		String jsonCoding = "{\"code\":\"1234\","
				//+ "\"system\":\"http://snomed.info/sct\","
				+ "\"version\":\"20180131\",\"userSelected\":false}";
		
		Coding coding = objectMapper.readValue(jsonCoding, Coding.class);
		validate(coding);
		
		Assert.assertEquals(new Code("1234"), coding.getCode());
		//Assert.assertEquals(new Uri("http://snomed.info/sct"), coding.getSystem());
		Assert.assertEquals("20180131", coding.getVersion());
	}
	
	@Test
	public void lookupRequestTest() throws Exception {
		
		String jsonMini = "{\"resourceType\":\"Parameters\","
				+ "\"parameter\":["
					+ "{\"name\":\"code\",\"valueCode\":\"abcd\"},"
					+ "{\"name\":\"system\",\"valueUri\":\"http://snomed.info/sct\"},"
					+ "{\"name\":\"version\",\"valueString\":\"20180131\"},"
					+ "{\"name\":\"date\",\"valueDateTime\":\"2018-03-09T20:50:21+0100\"},"
					+ "{\"name\":\"coding\", \"valueCoding\":{\"code\":\"1234\","
							+ "\"system\":\"http://snomed.info/sct\","
							+ "\"version\":\"20180131\",\"userSelected\":false}}"
					+ "]}";
		
		LookupRequest request = objectMapper.readValue(jsonMini, LookupRequest.class);
		
		Optional<SerializableParameter> optionalParameter = request.getParameters().stream()
				.filter(p -> p.getName().equals("code")).findFirst();
		assertTrue(optionalParameter.isPresent());
		SerializableParameter param = optionalParameter.get();
		assertEquals("valueCode", param.getType());
		assertEquals(new Code("abcd"), param.getValue());
		assertEquals(Code.class, param.getValueType());
		
		System.out.println("Request: " + request);
		assertEquals(new Code("abcd"), request.getCode());
		assertEquals(new Uri("http://snomed.info/sct"), request.getSystem());
		assertEquals("20180131", request.getVersion());
		assertEquals(new SimpleDateFormat(DateFormats.DATE_TIME_FORMAT).parse("2018-03-09T20:50:21+0100"), request.getDate());
		assertEquals(new Code("1234"), request.getCoding().getCode());
	}
	
	//@Test
	public void lookupRoundTrip() throws Exception {
		String json = "{\"resourceType\":\"Parameters\","
				+ "\"parameter\":["
					+ "{\"name\":\"name\",\"valueString\":\"LOINC\"},"
					+ "{\"name\":\"designation\",\"part\":["
						+ "{\"name\":\"value\",\"valueString\":\"Bicarbonate [Moles/volume] in Serum\"},"
						+ "{\"name\":\"language\",\"valueString\":\"en_uk\"}"
						+ "]}"
					+ "]}";
		
		LookupResult parameterModel = objectMapper.readValue(json, LookupResult.class);
		String serializedModel = objectMapper.writeValueAsString(parameterModel);
		Assert.assertEquals(json, serializedModel);
	}

}