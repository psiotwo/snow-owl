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
package com.b2international.index.revision;

import java.util.List;

/**
 * @since 5.0
 */
interface InternalRevisionIndex extends RevisionIndex {

	<T> T read(RevisionBranchRef branch, RevisionIndexRead<T> read);
	
	/**
	 * Returns all commits that changed the given identifier or any of its related components.
	 * @param id
	 * @return
	 */
	List<Commit> history(String id);

	RevisionCompare compare(RevisionBranchRef base, RevisionBranchRef compare, int limit);
	
}