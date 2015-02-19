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
package com.b2international.snowowl.snomed.datastore;

import com.b2international.snowowl.core.api.IBranchPath;

/**
 * Service interface for providing a human readable label for SNOMED&nbsp;CT components.
 *
 */
public interface SnomedComponentLabelProviderService {

	/**
	 * Returns with the human readable label of a SNOMED&nbsp;CT component from a given branch.
	 * <p>Returns with {@code null} if the label cannot be found
	 * or the component does not exist.
	 * @param branchPath the branch for the component visibility.
	 * @param componentId the unique ID of the component.
	 * @return the human readable label of the component.
	 */
	String getLabel(final IBranchPath branchPath, final String componentId);
}