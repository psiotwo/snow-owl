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
package com.b2international.snowowl.fhir.api.service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.b2international.commons.platform.Extensions;
import com.b2international.snowowl.core.exceptions.BadRequestException;
import com.b2international.snowowl.fhir.api.RestApiError;
import com.b2international.snowowl.fhir.api.model.LookupRequest;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * 
 *  Code system resource operations:
 *  <ul>
 *  <li>Concept lookup and decomposition</li>
 *  <li>Subsumption testing</li>
 *  <li>Code composition based on supplied properties</li>
 *  </ul>
 *  @see <a href="https://www.hl7.org/fhir/codesystem-operations.html">FHIR:CodeSystem:Operations</a>
 * 
 */
@Api("Code Systems")
@RestController //no need for method level @ResponseBody annotations
@RequestMapping(value="/CodeSystem")
public class FhirCodeSystemRestService {
	
	private static final String FHIR_EXTENSION_POINT = "com.b2international.snowowl.fhir.api.provider"; //$NON-NLS-N$
	
	@ApiOperation(
			value="FHIR REST API Ping Test",
			notes="This is only an FHIR ping test.")
	@RequestMapping(value="/ping", method=RequestMethod.GET)
	public String ping() {
		System.out.println("FhirCodeSystemRestService.ping()");
		return "Ping!";
	}
	
	/**
	 * GET-based lookup endpoint.
	 * @param code
	 * @param uri
	 * @param version
	 * @param date
	 * @param displayLanguage
	 * @param properties
	 * @throws ParseException 
	 */
	@ApiOperation(
			value="Concept lookup",
			notes="Given a code/system, or a Coding, get additional details about the concept.\n"
					+ "https://www.hl7.org/fhir/2016May/datatypes.html#dateTime")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "Code system not found", response = RestApiError.class)
	})
	@RequestMapping(value="/$lookup", method=RequestMethod.GET)
	public void lookupViaParameters(
		
		@ApiParam(value="The code to look up") @RequestParam(value="code") final String code,
		@ApiParam(value="The code system uri") @RequestParam(value="uri") final String uri,
		@ApiParam(value="The code system version") @RequestParam(value="version", required=false) final String version,
		@ApiParam(value="Lookup date in datetime format") @RequestParam(value="date", required=false) final String date,
		@ApiParam(value="Language code for display") @RequestParam(value="displayLanguage", required=false) final String displayLanguage,
		@ApiParam(value="Properties to return in the output") @RequestParam(value="property", required=false) Set<String> properties) throws ParseException {
		
		System.err.println("Code: " + code + " uri: " + uri +
				" version:" + version + " lookup date: " + date + " display language: " + displayLanguage);
		
		if (properties !=null) {
			System.out.println(" properties: " + Arrays.toString(properties.toArray()));
		}
		
		LookupRequest lookupRequest = new LookupRequest(code, uri, version, date, displayLanguage, properties);
		
		//all good, now do something
		lookup(lookupRequest);
	}
	
	/**
	 * POST-based lookup endpoint.
	 * All parameters are in the request body.
	 * @param coding
	 * @param date
	 * @param displayLanguage
	 * @param properties
	 */
	@ApiOperation(
			value="Concept lookup",
			notes="Given a code/system, or a Coding, get additional details about the concept.\n"
					+ "https://www.hl7.org/fhir/2016May/datatypes.html#dateTime")
	@RequestMapping(value="/$lookup", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void lookupViaCodingAndParameters(@ApiParam(value="The lookup request parameters") 
	
		@Valid 
		@RequestBody 
		final LookupRequest lookupRequest) {
		
		//all good, now do something
		lookup(lookupRequest);
	}
	
	private void lookup(LookupRequest lookupRequest) {
		
		if (false) {
			throw new BadRequestException("Display language code format is incorrect..");
		}
		
		Collection<IFhirProvider> fhirProviders = Extensions.getExtensions(FHIR_EXTENSION_POINT, IFhirProvider.class);
		
		Optional<IFhirProvider> fhirProviderOptional = fhirProviders.stream().findFirst();
		
		fhirProviderOptional.orElseThrow(() -> new BadRequestException("Did not find FHIR provider for URI: " + lookupRequest.getSystem()));
		
		IFhirProvider iFhirProvider = fhirProviderOptional.get();
		iFhirProvider.lookup(lookupRequest.getVersion(), lookupRequest.getCode().getCodeValue());
		
	}

}