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
package com.b2international.snowowl.snomed.core.store;

import com.b2international.snowowl.core.domain.TransactionContext;
import com.b2international.snowowl.snomed.Annotatable;
import com.b2international.snowowl.snomed.Concept;
import com.b2international.snowowl.snomed.Relationship;
import com.b2international.snowowl.snomed.SnomedConstants.Concepts;
import com.b2international.snowowl.snomed.common.SnomedTerminologyComponentConstants;
import com.b2international.snowowl.snomed.core.domain.CharacteristicType;
import com.b2international.snowowl.snomed.snomedrefset.SnomedConcreteDataTypeRefSetMember;
import com.b2international.snowowl.snomed.snomedrefset.SnomedRefSet;
import com.b2international.snowowl.snomed.snomedrefset.SnomedRefSetFactory;

/**
 * @since 4.6
 */
public final class SnomedConcreteDomainReferenceSetMemberBuilder extends SnomedMemberBuilder<SnomedConcreteDomainReferenceSetMemberBuilder, SnomedConcreteDataTypeRefSetMember> {

	private String uomId;
	private String operatorId = Concepts.CD_EQUAL;
	private String attributeLabel;
	private String serializedValue;
	private CharacteristicType characteristicType = CharacteristicType.STATED_RELATIONSHIP;
	
	public SnomedConcreteDomainReferenceSetMemberBuilder withUom(String uomId) {
		this.uomId = uomId;
		return getSelf();
	}
	
	public SnomedConcreteDomainReferenceSetMemberBuilder withOperatorId(String operatorId) {
		this.operatorId = operatorId;
		return getSelf();
	}
	
	public SnomedConcreteDomainReferenceSetMemberBuilder withAttributeLabel(String label) {
		this.attributeLabel = label;
		return getSelf();
	}
	
	public SnomedConcreteDomainReferenceSetMemberBuilder withSerializedValue(String serializedValue) {
		this.serializedValue = serializedValue;
		return getSelf();
	}
	
	public SnomedConcreteDomainReferenceSetMemberBuilder withCharacteristicType(CharacteristicType characteristicType) {
		this.characteristicType = characteristicType;
		return getSelf();
	}
	
	@Override
	protected SnomedConcreteDataTypeRefSetMember create() {
		return SnomedRefSetFactory.eINSTANCE.createSnomedConcreteDataTypeRefSetMember();
	}

	@Override
	protected void init(SnomedConcreteDataTypeRefSetMember component, TransactionContext context) {
		super.init(component, context);
		component.setUomComponentId(uomId);
		component.setOperatorComponentId(operatorId);
		component.setLabel(attributeLabel);
		component.setSerializedValue(serializedValue);
		component.setCharacteristicTypeId(characteristicType.getConceptId());
	}

	@Override
	protected void addToList(TransactionContext context, SnomedRefSet refSet, SnomedConcreteDataTypeRefSetMember component) {
		Annotatable annotatable = getAnnotatable(context, component.getReferencedComponentId(), component.getReferencedComponentType());
		annotatable.getConcreteDomainRefSetMembers().add(component);
	}

	private Annotatable getAnnotatable(TransactionContext context, String referencedComponentId, short referencedComponentType) {
		switch (referencedComponentType) {
		case SnomedTerminologyComponentConstants.CONCEPT_NUMBER:
			return context.lookup(referencedComponentId, Concept.class);
		case SnomedTerminologyComponentConstants.RELATIONSHIP_NUMBER:
			return context.lookup(referencedComponentId, Relationship.class);
		default:
			throw new IllegalStateException("Unexpected referenced component type '" + referencedComponentType + "'.");
		}
	}
	
}
