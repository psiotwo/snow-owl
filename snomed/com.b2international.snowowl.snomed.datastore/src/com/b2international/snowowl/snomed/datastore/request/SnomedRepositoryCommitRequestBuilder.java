/*
 * Copyright 2011-2019 B2i Healthcare Pte Ltd, http://b2i.sg
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
package com.b2international.snowowl.snomed.datastore.request;

import com.b2international.snowowl.core.domain.TransactionContext;
import com.b2international.snowowl.core.events.AsyncRequest;
import com.b2international.snowowl.core.events.Request;
import com.b2international.snowowl.datastore.request.BranchRequest;
import com.b2international.snowowl.datastore.request.CommitResult;
import com.b2international.snowowl.datastore.request.HealthCheckingRequest;
import com.b2international.snowowl.datastore.request.IndexReadRequest;
import com.b2international.snowowl.datastore.request.RepositoryCommitRequestBuilder;
import com.b2international.snowowl.datastore.request.RepositoryRequest;
import com.b2international.snowowl.datastore.request.RevisionIndexReadRequest;

/**
 * @since 4.5
 */
public final class SnomedRepositoryCommitRequestBuilder extends RepositoryCommitRequestBuilder {

	private String defaultModuleId = null;
	
	SnomedRepositoryCommitRequestBuilder() {
		super();
	}
	
	public SnomedRepositoryCommitRequestBuilder setDefaultModuleId(String defaultModuleId) {
		this.defaultModuleId = defaultModuleId;
		return this;
	}
	
	@Override
	protected Request<TransactionContext, ?> getBody() {
		return new ModuleRequest<>(
			new IdRequest<>(new SnomedBulkRequest<>(super.getBody())),
			defaultModuleId);
	}
	
	@Override
	public AsyncRequest<CommitResult> build(String repositoryId, String branch) {
		return new AsyncRequest<>(
			new RepositoryRequest<>(repositoryId,
				new HealthCheckingRequest<>(
					new IndexReadRequest<>(
						new BranchRequest<>(branch,
							new RevisionIndexReadRequest<>(
								build()
							)
						)
					),
					allowedHealthstates()
				)
			)
		);
	}

}