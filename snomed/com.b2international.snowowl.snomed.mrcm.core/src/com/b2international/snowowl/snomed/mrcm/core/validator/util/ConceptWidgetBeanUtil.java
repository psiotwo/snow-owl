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
package com.b2international.snowowl.snomed.mrcm.core.validator.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import com.b2international.commons.StringUtils;
import com.b2international.snowowl.core.api.NullComponent;
import com.b2international.snowowl.snomed.SnomedConstants.Concepts;
import com.b2international.snowowl.snomed.mrcm.DataType;
import com.b2international.snowowl.snomed.mrcm.core.widget.bean.ConceptWidgetBean;
import com.b2international.snowowl.snomed.mrcm.core.widget.bean.DataTypeWidgetBean;
import com.b2international.snowowl.snomed.mrcm.core.widget.bean.DescriptionWidgetBean;
import com.b2international.snowowl.snomed.mrcm.core.widget.bean.RelationshipGroupWidgetBean;
import com.b2international.snowowl.snomed.mrcm.core.widget.bean.RelationshipWidgetBean;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 */
public class ConceptWidgetBeanUtil {

	/**
	 * Returns <code>true</code> if the relationship type has been specified. Otherwise returns with <code>false</code>.
	 */
	public static boolean isRelationshipTypeSet(final RelationshipWidgetBean relationship) {
		return !NullComponent.isNullComponent(relationship.getSelectedType());
	}

	/**
	 * Returns <code>true</code> if the relationship destination has been specified. Otherwise returns with <code>false</code>.
	 */
	public static boolean isRelationshipValueSet(final RelationshipWidgetBean relationship) {
		return !NullComponent.isNullComponent(relationship.getSelectedValue());
	}

	/**
	 * Returns <code>true</code> if the relationship type is IS_A. Otherwise returns with <code>false</code>.
	 */
	public static boolean isRelationshipTypeIsA(final RelationshipWidgetBean relationship) {
		return Concepts.IS_A.equals(relationship.getSelectedType().getId());
	}

	/**
	 * Returns <code>true</code> if the boolean value for the given {@link DataTypeWidgetBean} is Yes.
	 * 
	 * @param dataType
	 * @return
	 * @throws IllegalArgumentException
	 *             if the given {@link DataTypeWidgetBean}'s allowed type is not a boolean.
	 */
	public static boolean isTrue(DataTypeWidgetBean dataType) {
		if (DataType.BOOLEAN.equals(dataType.getAllowedType())) {
			return "1".equals(dataType.getSelectedValue());
		}
		throw new IllegalArgumentException("The given dataType is not a boolean.");
	}

	/**
	 * Returns <code>true</code> if the boolean value for the given {@link DataTypeWidgetBean} is No.
	 * 
	 * @param dataType
	 * @return
	 * @throws IllegalArgumentException
	 *             if the given {@link DataTypeWidgetBean}'s allowed type is not a boolean.
	 */
	public static boolean isFalse(DataTypeWidgetBean dataType) {
		if (DataType.BOOLEAN.equals(dataType.getAllowedType())) {
			return "0".equals(dataType.getSelectedValue());
		}
		throw new IllegalArgumentException("The given dataType is not a boolean.");
	}

	/**
	 * Returns <code>true</code> if the boolean value for the given {@link DataTypeWidgetBean} is N/A.
	 * 
	 * @param dataType
	 * @return
	 * @throws IllegalArgumentException
	 *             if the given {@link DataTypeWidgetBean}'s allowed type is not a boolean.
	 */
	public static boolean isNotSpecified(DataTypeWidgetBean dataType) {
		if (dataType != null && DataType.BOOLEAN.equals(dataType.getAllowedType())) {
			return StringUtils.isEmpty(dataType.getSelectedValue());
		}
		throw new IllegalArgumentException("The given dataType is not a boolean.");
	}

	/**
	 * Returns <code>true</code> if the underlying description is a fully specified name.
	 * 
	 * @param description
	 * @return
	 */
	public static boolean isFsn(final DescriptionWidgetBean description) {

		if (!isFsn(description.getSelectedType().getId())) {
			return false;
		}

		return true;
	}

	/**
	 * Returns <code>true</code> if the specified concept ID is the FSN description type concept ID.
	 * 
	 * @param conceptId
	 * @return
	 */
	public static boolean isFsn(final String conceptId) {
		return Concepts.FULLY_SPECIFIED_NAME.equals(conceptId);
	}

	/**
	 * Returns all {@link RelationshipWidgetBean} within the given {@link ConceptWidgetBean} instance for the given relationship type ID.
	 * 
	 * @param concept
	 * @return
	 */
	public static Iterable<RelationshipWidgetBean> getRelationships(final ConceptWidgetBean concept, final String typeId) {
		checkArgument(!StringUtils.isEmpty(typeId), "TypeId must be specified");
		return Iterables.filter(getRelationships(concept), new Predicate<RelationshipWidgetBean>() {
			@Override
			public boolean apply(RelationshipWidgetBean input) {
				return isRelationshipTypeSet(input) && typeId.equals(input.getSelectedType().getId());
			}
		});
	}

	/**
	 * Returns all {@link RelationshipWidgetBean} within the given {@link ConceptWidgetBean} instance.
	 * 
	 * @param concept
	 * @return
	 */
	public static Iterable<RelationshipWidgetBean> getRelationships(ConceptWidgetBean concept) {
		// all grouped properties
		final Collection<RelationshipWidgetBean> relationships = newArrayList();
		final Iterable<RelationshipGroupWidgetBean> groups = Iterables.filter(concept.getProperties().getElements(),
				RelationshipGroupWidgetBean.class);
		for (RelationshipGroupWidgetBean group : groups) {
			relationships.addAll(newArrayList(Iterables.filter(group.getElements(), RelationshipWidgetBean.class)));
		}
		return relationships;
	}

	/**
	 * Returns all {@link DataTypeWidgetBean} within the given {@link ConceptWidgetBean}.
	 * 
	 * @param concept
	 * @return
	 */
	public static Iterable<DataTypeWidgetBean> getDataTypes(ConceptWidgetBean concept) {
		// all grouped properties
		final Collection<DataTypeWidgetBean> dataTypes = newArrayList();
		final Iterable<RelationshipGroupWidgetBean> groups = Iterables.filter(concept.getProperties().getElements(),
				RelationshipGroupWidgetBean.class);
		for (RelationshipGroupWidgetBean group : groups) {
			dataTypes.addAll(newArrayList(Iterables.filter(group.getElements(), DataTypeWidgetBean.class)));
		}
		return dataTypes;
	}

	/**
	 * Returns the fully specified name bean of the given {@link ConceptWidgetBean}. Or <code>null</code> if the concept does not have fully specified name.
	 * 
	 * @param concept
	 * @return
	 */
	public static DescriptionWidgetBean getFullySpecifiedNameBean(ConceptWidgetBean concept) {
		for (DescriptionWidgetBean description : Iterables.filter(concept.getDescriptions().getElements(),
				DescriptionWidgetBean.class)) {
			if (isFsn(description)) {
				return description;
			}
		}
		return null;
	}

	public static DescriptionWidgetBean getPreferredTerm(ConceptWidgetBean concept) {
		for (DescriptionWidgetBean description : Iterables.filter(concept.getDescriptions().getElements(), DescriptionWidgetBean.class)) {
			if(description.isPreferred()){
				return description;
			}
		}
		return null;
	}

	/**
	 * Returns the fully specified name of the given {@link ConceptWidgetBean}. Or <code>null</code> if the concept does not have fully specified name.
	 * 
	 * @param concept
	 * @return
	 */
	public static String getFullySpecifiedName(ConceptWidgetBean concept) {
		DescriptionWidgetBean description = getFullySpecifiedNameBean(concept);
		return description != null ? description.getTerm() : null;
	}

	/**
	 * Returns the {@link DescriptionWidgetBean} from the given {@link ConceptWidgetBean} with the given typeId.
	 * 
	 * @param concept
	 * @param typeId
	 */
	public static DescriptionWidgetBean getDescription(ConceptWidgetBean concept, String typeId) {
		for (DescriptionWidgetBean description : Iterables.filter(concept.getDescriptions().getElements(),
				DescriptionWidgetBean.class)) {
			if (typeId.equals(description.getSelectedType().getId())) {
				return description;
			}
		}
		return null;
	}

	/**
	 * Returns the {@link DataTypeWidgetBean} from the given {@link ConceptWidgetBean} with the given propertyName.
	 * 
	 * @param concept
	 * @param propertyName
	 * @return
	 */
	public static DataTypeWidgetBean getDataType(ConceptWidgetBean concept, String propertyName) {
		for (DataTypeWidgetBean dataType : getDataTypes(concept)) {
			if (propertyName.equals(dataType.getSelectedLabel())) {
				return dataType;
			}
		}
		return null;
	}

	/**
	 * Returns the {@link RelationshipWidgetBean} with the given typeId from the given {@link ConceptWidgetBean}.
	 * 
	 * @param concept
	 * @param relationshipId
	 * @return
	 */
	public static RelationshipWidgetBean getRelationship(ConceptWidgetBean concept, String relationshipId) {
		for (RelationshipWidgetBean relationship : getRelationships(concept)) {
			if (relationshipId.equals(relationship.getSelectedType().getId())) {
				return relationship;
			}
		}
		return null;
	}
}