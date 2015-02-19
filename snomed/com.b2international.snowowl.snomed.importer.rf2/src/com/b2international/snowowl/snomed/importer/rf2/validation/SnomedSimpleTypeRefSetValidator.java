/*
 * Copyright 2011-2015 B2i Healthcare Pte Ltd, http://b2i.sg
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
package com.b2international.snowowl.snomed.importer.rf2.validation;

import java.net.URL;
import java.util.Set;

import com.b2international.snowowl.snomed.common.SnomedRf2Headers;
import com.b2international.snowowl.snomed.importer.net4j.ImportConfiguration;
import com.b2international.snowowl.snomed.importer.net4j.SnomedValidationDefect;
import com.b2international.snowowl.snomed.importer.rf2.model.ComponentImportType;
import com.b2international.snowowl.snomed.importer.rf2.util.ValidationUtil;

/**
 * Represents a release file validator that validates the simple type reference set.
 * 
 */
public class SnomedSimpleTypeRefSetValidator extends SnomedRefSetValidator {
	
	public SnomedSimpleTypeRefSetValidator(final ImportConfiguration configuration, final URL releaseUrl, final Set<SnomedValidationDefect> defects, final ValidationUtil validationUtil) {
		super(configuration, releaseUrl, ComponentImportType.SIMPLE_TYPE_REFSET, defects, validationUtil, SnomedRf2Headers.SIMPLE_TYPE_HEADER.length);
	}

	@Override
	protected String getName() {
		return "simple type";
	}
	
	@Override
	protected String[] getExpectedHeader() {
		return SnomedRf2Headers.SIMPLE_TYPE_HEADER;
	}
}