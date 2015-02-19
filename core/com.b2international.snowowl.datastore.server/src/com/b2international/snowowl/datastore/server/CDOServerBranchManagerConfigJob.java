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
package com.b2international.snowowl.datastore.server;

import com.b2international.snowowl.core.api.SnowowlServiceException;
import com.b2international.snowowl.datastore.cdo.ICDOBranchManager;
import com.b2international.snowowl.datastore.server.cdo.CDOBranchManager;
import com.b2international.snowowl.datastore.serviceconfig.AbstractServerServiceConfigJob;

/**
 * Job for initializing and registering {@link ICDOServerBranchManager} to the application.
 *
 */
public class CDOServerBranchManagerConfigJob extends AbstractServerServiceConfigJob<ICDOBranchManager> {

	public CDOServerBranchManagerConfigJob() {
		super("CDO server branch manager configuration...", DatastoreServerActivator.PLUGIN_ID);
	}

	/*
	 * (non-Javadoc)
	 * @see com.b2international.snowowl.datastore.serviceconfig.AbstractServerServiceConfigJob#getServiceClass()
	 */
	@Override
	protected Class<ICDOBranchManager> getServiceClass() {
		return ICDOBranchManager.class;
	}

	/*
	 * (non-Javadoc)
	 * @see com.b2international.snowowl.datastore.serviceconfig.AbstractServerServiceConfigJob#createServiceImplementation()
	 */
	@Override
	protected CDOBranchManager createServiceImplementation() throws SnowowlServiceException {
		return new CDOBranchManager();
	}
}