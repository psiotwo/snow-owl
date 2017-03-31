/*
 * Copyright 2011-2017 B2i Healthcare Pte Ltd, http://b2i.sg
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
package com.b2international.snowowl.snomed.core.domain.refset;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import com.b2international.snowowl.core.domain.TransactionContext;
import com.b2international.snowowl.core.events.Request;
import com.b2international.snowowl.snomed.common.SnomedRf2Headers;
import com.b2international.snowowl.snomed.core.domain.SnomedComponent;
import com.b2international.snowowl.snomed.core.domain.SnomedCoreComponent;
import com.b2international.snowowl.snomed.datastore.request.SnomedRequests;
import com.b2international.snowowl.snomed.snomedrefset.SnomedRefSetType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.google.common.base.Function;

/**
 * @since 4.5
 */
public final class SnomedReferenceSetMember extends SnomedComponent {

	public static final Function<SnomedReferenceSetMember, String> GET_REFERENCED_COMPONENT_ID = (member) -> member.getReferencedComponent().getId();
	
	private SnomedRefSetType type;
	private SnomedCoreComponent referencedComponent;
	private String referenceSetId;
	private Map<String, Object> properties = newHashMap();

	/**
	 * @return the containing reference set's type
	 */
	public SnomedRefSetType type() {
		return type;
	}

	/**
	 * Returns the component referenced by this SNOMED CT Reference Set Member. It includes only the SNOMED CT ID property by default, see
	 * {@link SnomedCoreComponent#getId()}.
	 * 
	 * @return
	 */
	public SnomedCoreComponent getReferencedComponent() {
		return referencedComponent;
	}

	/**
	 * Returns the identifier of the SNOMED CT Reference Set this SNOMED CT Reference Set Member belongs to.
	 * 
	 * @return
	 */
	public String getReferenceSetId() {
		return referenceSetId;
	}

	/**
	 * Returns special properties of the SNOMED CT Reference Set or an empty {@link Map} if none found.
	 * 
	 * @return
	 */
	@JsonAnyGetter
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void setType(SnomedRefSetType type) {
		this.type = type;
	}
	
	public void setReferencedComponent(SnomedCoreComponent referencedComponent) {
		this.referencedComponent = referencedComponent;
	}
	
	public void setReferenceSetId(String referenceSetId) {
		this.referenceSetId = referenceSetId;
	}
	
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	@JsonAnySetter
	public void setProperties(String key, Object value) {
		this.properties.put(key, value);
	}
	
	@Override
	public Request<TransactionContext, String> toCreateRequest(String containerId) {
		return SnomedRequests.prepareNewMember()
				.setActive(isActive())
				.setReferencedComponentId(containerId)
				.setReferenceSetId(getReferenceSetId())
				.setModuleId(getModuleId())
				.setProperties(getProperties())
				.build();
	}
	
	@Override
	public Request<TransactionContext, Boolean> toUpdateRequest() {
		final Map<String, Object> changes = newHashMap(getProperties());
		changes.put(SnomedRf2Headers.FIELD_ACTIVE, isActive());
		changes.put(SnomedRf2Headers.FIELD_MODULE_ID, getModuleId());
		return SnomedRequests.prepareUpdateMember()
				.setMemberId(getId())
				.setSource(changes)
				.build();
	}
	
}