/*
 * Copyright 2017-2019 B2i Healthcare Pte Ltd, http://b2i.sg
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
package com.b2international.snowowl.datastore.request.job;

import com.b2international.snowowl.core.ServiceProvider;
import com.b2international.snowowl.core.events.Request;
import com.b2international.snowowl.datastore.remotejobs.RemoteJobTracker;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @since 5.7
 */
final class CancelJobRequest implements Request<ServiceProvider, Boolean> {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty
	private final String id;

	CancelJobRequest(String id) {
		this.id = id;
	}

	@Override
	public Boolean execute(ServiceProvider context) {
		context.service(RemoteJobTracker.class).requestCancel(id);
		return Boolean.TRUE;
	}

}
