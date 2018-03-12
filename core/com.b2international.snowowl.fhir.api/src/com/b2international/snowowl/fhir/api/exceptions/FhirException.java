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
package com.b2international.snowowl.fhir.api.exceptions;

import com.b2international.commons.exceptions.FormattedRuntimeException;
import com.b2international.snowowl.fhir.api.codesystems.IssueSeverity;
import com.b2international.snowowl.fhir.api.codesystems.IssueType;
import com.b2international.snowowl.fhir.api.codesystems.OperationOutcomeCode;
import com.b2international.snowowl.fhir.api.model.Issue;
import com.b2international.snowowl.fhir.api.model.OperationOutcome;

/**
 * @since 6.3
 */
public class FhirException extends FormattedRuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @param template
	 * @param args
	 */
	public FhirException(String template, Object[] args) {
		super(template, args);
	}
	
	/**
	 * Returns the issue type associated with this exception.
	 * Subclasses to override.
	 * @return
	 */
	public IssueType getIssueType() {
		return IssueType.EXCEPTION;
	}
	
	/**
	 * Returns the most generic operation outcome code associated with this exception.
	 * Sublasses may override.
	 * @return
	 */
	public OperationOutcomeCode getOperationOutcomeCode() {
		return OperationOutcomeCode.MSG_UNKNOWN_CONTENT;
	}
	
	/**
	 * Creates an OperationOutcome representation from this exception. Useful when the exception must be propagated through protocols where Java serialization
	 * cannot be used (eg. HTTP), or the possible receiver cannot understand serialized Java class and object byte sequences.
	 * 
	 * @return {@link OperationOutcome} representation of this {@link FhirException}, never <code>null</code>.
	 */
	public OperationOutcome toOperationOutcome() {
		
		OperationOutcome operationOutcome = new OperationOutcome();
		Issue issue = Issue.builder()
				.severity(IssueSeverity.ERROR)
				.code(getIssueType())
				.codeableConcept(getOperationOutcomeCode())
				.diagnostics(getMessage())
				.build();
		
		operationOutcome.addIssue(issue);
		return operationOutcome;
	}


}