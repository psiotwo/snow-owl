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
package com.b2international.snowowl.snomed.mrcm.core.concepteditor;

import com.b2international.snowowl.datastore.serviceconfig.ClientServiceConfigJob;
import com.b2international.snowowl.snomed.mrcm.core.MrcmCoreActivator;

/**
 * Configuration job to initialize and register the widget model provider on the client side.
 * <p>
 * <b>Note:</b> this class belongs to the {@link MrcmCoreActivator#PLUGIN_ID SNOMED&nbsp;CT data store plug-in identifier} job
 * family.
 * 
 */
public class IndexClientSnomedConceptEditorServiceConfigJob extends ClientServiceConfigJob<ISnomedConceptEditorService, IClientSnomedConceptEditorService> {

	private static final String JOB_NAME = "Client SNOMED CT concept editor service configuration...";
	
	public IndexClientSnomedConceptEditorServiceConfigJob() {
		super(JOB_NAME, MrcmCoreActivator.PLUGIN_ID);
	}
	
	/* (non-Javadoc)
	 * @see com.b2international.snowowl.datastore.serviceconfig.ClientServiceConfigJob#getBranchAwareClass()
	 */
	@Override
	protected Class<ISnomedConceptEditorService> getServiceClass() {
		return ISnomedConceptEditorService.class;
	}

	/* (non-Javadoc)
	 * @see com.b2international.snowowl.datastore.serviceconfig.ClientServiceConfigJob#getTrackingClass()
	 */
	@Override
	protected Class<IClientSnomedConceptEditorService> getTrackingClass() {
		return IClientSnomedConceptEditorService.class;
	}

	/* (non-Javadoc)
	 * @see com.b2international.snowowl.datastore.serviceconfig.ClientServiceConfigJob#createTrackingService(java.lang.Object)
	 */
	@Override
	protected IndexClientSnomedConceptEditorService createTrackingService(final ISnomedConceptEditorService branchAwareService) {
		return new IndexClientSnomedConceptEditorService(branchAwareService);
	}
}