/*
 * Copyright 2011-2016 B2i Healthcare Pte Ltd, http://b2i.sg
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
package com.b2international.snowowl.datastore.request;

import java.util.UUID;

/**
 * @since 4.6
 */
public final class Merging {

	Merging() {}
	
	public CreateMergeRequestBuilder prepareCreate() {
		return new CreateMergeRequestBuilder();
	}

	public MergeGetRequestBuilder prepareGet(UUID id) {
		return new MergeGetRequestBuilder(id);
	}
	
	public SearchMergeRequestBuilder prepareSearch() {
		return new SearchMergeRequestBuilder();
	}

	public MergeDeleteRequestBuilder prepareDelete(UUID id) {
		return new MergeDeleteRequestBuilder(id);
	}
}
