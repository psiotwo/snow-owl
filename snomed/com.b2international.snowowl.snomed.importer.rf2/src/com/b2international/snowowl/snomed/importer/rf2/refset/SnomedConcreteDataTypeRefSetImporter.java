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
package com.b2international.snowowl.snomed.importer.rf2.refset;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.NullObjectPattern;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.b2international.snowowl.core.CoreTerminologyBroker;
import com.b2international.snowowl.snomed.Annotatable;
import com.b2international.snowowl.snomed.common.SnomedRf2Headers;
import com.b2international.snowowl.snomed.datastore.SnomedConcreteDataTypes;
import com.b2international.snowowl.snomed.importer.rf2.csv.ConcreteDomainRefSetRow;
import com.b2international.snowowl.snomed.importer.rf2.csv.cellprocessor.ParseUuid;
import com.b2international.snowowl.snomed.importer.rf2.model.ComponentImportType;
import com.b2international.snowowl.snomed.importer.rf2.model.IndexConfiguration;
import com.b2international.snowowl.snomed.importer.rf2.model.SnomedImportConfiguration;
import com.b2international.snowowl.snomed.importer.rf2.model.SnomedImportContext;
import com.b2international.snowowl.snomed.snomedrefset.DataType;
import com.b2international.snowowl.snomed.snomedrefset.SnomedConcreteDataTypeRefSet;
import com.b2international.snowowl.snomed.snomedrefset.SnomedConcreteDataTypeRefSetMember;
import com.b2international.snowowl.snomed.snomedrefset.SnomedRefSet;
import com.b2international.snowowl.snomed.snomedrefset.SnomedRefSetFactory;
import com.b2international.snowowl.snomed.snomedrefset.SnomedRefSetType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * SNOMED&nbsp;CT concrete data type reference set importer.
 * @see AbstractSnomedRefSetImporter
 */
public class SnomedConcreteDataTypeRefSetImporter extends AbstractSnomedRefSetImporter<ConcreteDomainRefSetRow, SnomedConcreteDataTypeRefSetMember> {

	public static final List<IndexConfiguration> INDEXES = ImmutableList.<IndexConfiguration>builder()
			.add(new IndexConfiguration("SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER_IDX1000", "SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER", "CDO_CREATED"))
			.add(new IndexConfiguration("SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER_IDX1001", "SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER", "CDO_CONTAINER", "CDO_BRANCH", "CDO_VERSION"))
			.add(new IndexConfiguration("SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER_IDX1002", "SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER", "REFERENCEDCOMPONENTID/*!(255)*/", "CDO_BRANCH", "CDO_VERSION"))
			.add(new IndexConfiguration("SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER_IDX1003", "SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER", "UUID/*!(255)*/", "CDO_BRANCH", "CDO_VERSION"))
			.add(new IndexConfiguration("SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER_IDX1004", "SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER", "OPERATORCOMPONENTID/*!(255)*/", "CDO_BRANCH", "CDO_VERSION"))
			.add(new IndexConfiguration("SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER_IDX1005", "SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER", "SERIALIZEDVALUE/*!(255)*/", "CDO_BRANCH", "CDO_VERSION"))
			.add(new IndexConfiguration("SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER_IDX1006", "SNOMEDREFSET_SNOMEDCONCRETEDATATYPEREFSETMEMBER", "UOMCOMPONENTID/*!(255)*/", "CDO_BRANCH", "CDO_VERSION"))
			.build();
	
	private static SnomedImportConfiguration<ConcreteDomainRefSetRow> createImportConfiguration(final boolean includesLabel) {
	
		final Builder<String, CellProcessor> builder = ImmutableMap.<String, CellProcessor>builder()
				.put(ConcreteDomainRefSetRow.PROP_UUID, new ParseUuid())
				.put(ConcreteDomainRefSetRow.PROP_EFFECTIVE_TIME, createEffectiveTimeCellProcessor())
				.put(ConcreteDomainRefSetRow.PROP_ACTIVE, new ParseBool("1", "0"))
				.put(ConcreteDomainRefSetRow.PROP_MODULE_ID, NullObjectPattern.INSTANCE)
				.put(ConcreteDomainRefSetRow.PROP_REF_SET_ID, NullObjectPattern.INSTANCE)
				.put(ConcreteDomainRefSetRow.PROP_REFERENCED_COMPONENT_ID, NullObjectPattern.INSTANCE)
				.put(ConcreteDomainRefSetRow.PROP_UOM_ID, NullObjectPattern.INSTANCE)
				.put(ConcreteDomainRefSetRow.PROP_OPERATOR_ID, NullObjectPattern.INSTANCE);
		
		if (includesLabel) {
			builder.put(ConcreteDomainRefSetRow.PROP_ATTRIBUTE_NAME, NullObjectPattern.INSTANCE);
		}
		
		builder.put(ConcreteDomainRefSetRow.PROP_DATA_VALUE, NullObjectPattern.INSTANCE);

		if (includesLabel) {
			builder.put(ConcreteDomainRefSetRow.PROP_CHARACTERISTIC_TYPE_ID, NullObjectPattern.INSTANCE);
		}
		
		final Map<String, CellProcessor> cellProcessorMapping = builder.build();
		
		final ComponentImportType type = (includesLabel) 
				? ComponentImportType.EXTENDED_CONCRETE_DOMAIN_REFSET 
				: ComponentImportType.CONCRETE_DOMAIN_REFSET;
		
		final String[] expectedHeader = (includesLabel)
				? SnomedRf2Headers.CONCRETE_DATA_TYPE_HEADER_WITH_LABEL
				: SnomedRf2Headers.CONCRETE_DATA_TYPE_HEADER;
		
		return new SnomedImportConfiguration<ConcreteDomainRefSetRow>(
				type, 
				cellProcessorMapping, 
				ConcreteDomainRefSetRow.class, 
				expectedHeader, 
				INDEXES);
	}

	public SnomedConcreteDataTypeRefSetImporter(final SnomedImportContext importContext, final InputStream releaseFileStream, 
			final boolean includesLabel, final String releaseFileIdentifier) {
		
		super(createImportConfiguration(includesLabel), importContext, releaseFileStream, releaseFileIdentifier);
	}

	@Override
	protected SnomedRefSet createUninitializedRefSet(final String identifierConceptId) {
		return SnomedRefSetFactory.eINSTANCE.createSnomedConcreteDataTypeRefSet();
	}
	
	@Override
	protected void initRefSet(final SnomedRefSet refSet, final String referencedComponentId) {
		((SnomedConcreteDataTypeRefSet) refSet).setDataType(getDataType(refSet.getIdentifierId()));
		refSet.setReferencedComponentType(CoreTerminologyBroker.UNSPECIFIED_NUMBER_SHORT);
	}

	@Override
	protected SnomedRefSetType getRefSetType() {
		return SnomedRefSetType.CONCRETE_DATA_TYPE;
	}

	@Override
	protected SnomedConcreteDataTypeRefSetMember doImportRow(final ConcreteDomainRefSetRow currentRow) {

		final SnomedConcreteDataTypeRefSetMember editedMember = getOrCreateMember(currentRow.getUuid());
		
		if (skipCurrentRow(currentRow, editedMember)) {
			return null;
		}

		editedMember.setRefSet(getOrCreateRefSet(currentRow.getRefSetId(), currentRow.getReferencedComponentId()));
		editedMember.setActive(currentRow.isActive());
		editedMember.setEffectiveTime(currentRow.getEffectiveTime());
		editedMember.setModuleId(currentRow.getModuleId());
		editedMember.setReferencedComponentId(currentRow.getReferencedComponentId());
		editedMember.setUomComponentId(currentRow.getUomId());
		editedMember.setOperatorComponentId(currentRow.getOperatorId());
		editedMember.setLabel(currentRow.getAttributeName());
		editedMember.setSerializedValue(currentRow.getDataValue());
		editedMember.setCharacteristicTypeId(currentRow.getCharacteristicTypeId());
		editedMember.setReleased(true);
		
		return editedMember;
	}
	
	/**
	 * Returns with the {@link DataType} based on the specified reference set identifier concept ID.
	 * @param identifierConceptId the reference set identifier concept ID.
	 * @return the data type associated with the reference set specified by the identifier concept ID.
	 */
	protected DataType getDataType(final String identifierConceptId) {
		return SnomedConcreteDataTypes.getDataTypeByRefSetId(identifierConceptId);
	}

	@Override
	protected String getIdentifierParentConceptId(final String refSetId) {
		return SnomedConcreteDataTypes.getParentConceptId(refSetId);
	}

	@Override
	protected SnomedConcreteDataTypeRefSetMember createRefSetMember() {
		return SnomedRefSetFactory.eINSTANCE.createSnomedConcreteDataTypeRefSetMember();
	}
	
	@Override
	protected boolean addToMembersList(final SnomedConcreteDataTypeRefSetMember currentMember) {
		
		final Annotatable annotatableComponent = getAnnotatableComponent(currentMember.getReferencedComponentId());
		
		if (null == annotatableComponent) {
			String message = MessageFormat.format("Annotatable component with ID ''{0}'' could not be found, skipping refset member.", currentMember.getReferencedComponentId());
			getLogger().warn(message);
			log(message);
			return false;
		}
		
		annotatableComponent.getConcreteDomainRefSetMembers().add(currentMember);
		return true;
	}
}