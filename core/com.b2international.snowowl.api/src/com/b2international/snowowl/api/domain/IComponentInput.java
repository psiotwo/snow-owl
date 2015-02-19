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
package com.b2international.snowowl.api.domain;

/**
 * TODO document
 */
public interface IComponentInput {

	/**
	 * Returns the code system short name, eg. "{@code SNOMEDCT}"
	 * @return the code system short name
	 */
	String getCodeSystemShortName();

	/**
	 * Returns the code system version identifier, eg. "{@code 2014-01-31}".
	 * @return the code system version identifier
	 */
	String getCodeSystemVersionId();
	
	/**
	 * Returns the task identifier, eg. "{@code 1747}". A {@code null} value points to the repository version,
	 * when the component is not part of an editing task.
	 * @return the task identifier, or {@code null} in case of a component on a version
	 */
	String getTaskId();
}