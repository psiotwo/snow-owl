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
package com.b2international.snowowl.snomed.datastore.index.entry;

import static com.b2international.index.query.Expressions.exactMatch;
import static com.b2international.index.query.Expressions.match;
import static com.b2international.index.query.Expressions.matchAny;
import static com.b2international.index.query.Expressions.matchAnyInt;
import static com.b2international.index.query.Expressions.matchAnyLong;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.b2international.collections.PrimitiveSets;
import com.b2international.collections.longs.LongSet;
import com.b2international.commons.StringUtils;
import com.b2international.commons.collections.Collections3;
import com.b2international.commons.functions.StringToLongFunction;
import com.b2international.index.Doc;
import com.b2international.index.Script;
import com.b2international.index.query.Expression;
import com.b2international.index.query.SortBy;
import com.b2international.index.revision.Revision;
import com.b2international.snowowl.core.date.EffectiveTimes;
import com.b2international.snowowl.core.terminology.TerminologyRegistry;
import com.b2international.snowowl.snomed.common.SnomedConstants.Concepts;
import com.b2international.snowowl.snomed.core.domain.SnomedConcept;
import com.b2international.snowowl.snomed.core.domain.SnomedDescription;
import com.b2international.snowowl.snomed.core.domain.refset.SnomedRefSetType;
import com.b2international.snowowl.snomed.core.domain.refset.SnomedReferenceSet;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;

/**
 * A transfer object representing a SNOMED CT concept.
 */
@Doc(
	type="concept",
	revisionHash = { 
		SnomedDocument.Fields.ACTIVE, 
		SnomedDocument.Fields.EFFECTIVE_TIME, 
		SnomedDocument.Fields.MODULE_ID,
		SnomedDocument.Fields.RELEASED,
		SnomedConceptDocument.Fields.PRIMITIVE,
		SnomedConceptDocument.Fields.EXHAUSTIVE
	}
)
@JsonDeserialize(builder=SnomedConceptDocument.Builder.class)
@Script(
	name="doiFactor", 
	script=
	"double interest = params.useDoi ? (doc.doi.value - params.minDoi) / (params.maxDoi - params.minDoi) : 0;"
	+ "String id = doc.id.value;" 
	+ "return params.termScores.containsKey(id) ? params.termScores.get(id) + interest : 0.0d;")
@Script(name="doi", script="return doc.doi.value")
@Script(
	name="termSort", 
	script=
	// select first preferred SYNONYM
	  "for (locale in params.locales) {"
	+ "	for (description in params._source.preferredDescriptions) {"
	+ "		if (!params.synonymIds.contains(description.typeId)) {"
	+ "			continue;"
	+ "		}"
	+ "		if (description.languageRefSetIds.contains(locale)) {"
	+ "			return description.term;"
	+ "		}"
	+ "	}"
	+ "}"
	// if there is no first preferred synonym then select first preferred FSN
	+ "for (locale in params.locales) {"
	+ "	for (description in params._source.preferredDescriptions) {"
	+ "		if (!\"900000000000003001\".equals(description.typeId)) {"
	+ "			continue;"
	+ "		}"
	+ "		if (description.languageRefSetIds.contains(locale)) {"
	+ "			return description.term;"
	+ "		}"
	+ "	}"
	+ "}"
	// if there is no first preferred FSN, then select the first FSN in the list (the index will contain only active description in creation order)
	+ "for (description in params._source.preferredDescriptions) {"
	+ "	if (\"900000000000003001\".equals(description.typeId)) {"
	+ "		return description.term;"
	+ "	}"
	+ "}"
	// Otherwise select the ID for sorting
	+ "return doc.id.value")
public final class SnomedConceptDocument extends SnomedComponentDocument {

	public static final float DEFAULT_DOI = 1.0f;
	private static final long serialVersionUID = -824286402410205210L;

	public static SortBy sortByTerm(List<String> languageRefSetPreferenceList, Set<String> synonymIds, SortBy.Order order) {
		return SortBy.script("termSort", ImmutableMap.of("locales", languageRefSetPreferenceList, "synonymIds", synonymIds), order);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public final static class Expressions extends SnomedComponentDocument.Expressions {
		
		private Expressions() {
		}

		public static Expression parents(Collection<String> parentIds) {
			return matchAnyLong(Fields.PARENTS, StringToLongFunction.copyOf(parentIds));
		}

		public static Expression ancestors(Collection<String> ancestorIds) {
			return matchAnyLong(Fields.ANCESTORS, StringToLongFunction.copyOf(ancestorIds));
		}

		public static Expression statedParents(Collection<String> statedParentIds) {
			return matchAnyLong(Fields.STATED_PARENTS, StringToLongFunction.copyOf(statedParentIds));
		}
		
		public static Expression statedAncestors(Collection<String> statedAncestorIds) {
			return matchAnyLong(Fields.STATED_ANCESTORS, StringToLongFunction.copyOf(statedAncestorIds));
		}
		
		public static Expression primitive() {
			return match(Fields.PRIMITIVE, true);
		}
		
		public static Expression defining() {
			return match(Fields.PRIMITIVE, false);
		}
		
		public static Expression exhaustive() {
			return match(Fields.EXHAUSTIVE, true);
		}
		
		public static Expression refSetStorageKey(long storageKey) {
			return exactMatch(Fields.REFSET_STORAGEKEY, storageKey);
		}
		
		public static Expression refSetStorageKeys(Iterable<Long> storageKeys) {
			return matchAnyLong(Fields.REFSET_STORAGEKEY, storageKeys);
		}

		public static Expression refSetType(SnomedRefSetType type) {
			return refSetTypes(Collections.singleton(type));
		}
		
		public static Expression refSetTypes(Collection<SnomedRefSetType> types) {
			return matchAny(Fields.REFSET_TYPE, FluentIterable.from(types).transform(type -> type.name()).toSet());
		}
		
		public static Expression referencedComponentType(int referencedComponentType) {
			return match(Fields.REFERENCED_COMPONENT_TYPE, referencedComponentType);
		}
		
		public static Expression referencedComponentTypes(Collection<Integer> referencedComponentTypes) {
			return matchAnyInt(Fields.REFERENCED_COMPONENT_TYPE, referencedComponentTypes);
		}
		
		public static Expression mapTargetComponentType(int mapTargetComponentType) {
			return match(Fields.MAP_TARGET_COMPONENT_TYPE, mapTargetComponentType);
		}
		
		public static Expression mapTargetComponentTypes(Collection<Integer> mapTargetComponentTypes) {
			return matchAnyInt(Fields.MAP_TARGET_COMPONENT_TYPE, mapTargetComponentTypes);
		}
		
		public static Expression referringPredicate(String referringPredicate) {
			return exactMatch(Fields.REFERRING_PREDICATES, referringPredicate);
		}
		
	}

	public static class Fields extends SnomedComponentDocument.Fields {
		
		public static final String REFSET_STORAGEKEY = "refSetStorageKey";
		public static final String REFERRING_PREDICATES = "referringPredicates";
		public static final String PRIMITIVE = "primitive";
		public static final String EXHAUSTIVE = "exhaustive";
		public static final String ANCESTORS = "ancestors";
		public static final String STATED_ANCESTORS = "statedAncestors";
		public static final String PARENTS = "parents";
		public static final String STATED_PARENTS = "statedParents";
		public static final String REFSET_TYPE = "refSetType";
		public static final String REFERENCED_COMPONENT_TYPE = "referencedComponentType";
		public static final String MAP_TARGET_COMPONENT_TYPE = "mapTargetComponentType";
		public static final String DOI = "doi";
		public static final String DESCRIPTIONS = "preferredDescriptions";
	}
	
	public static Builder builder(final SnomedConceptDocument input) {
		final String id = input.getId();
		return builder()
				.id(id)
				.moduleId(input.getModuleId())
				.active(input.isActive())
				.released(input.isReleased())
				.effectiveTime(input.getEffectiveTime())
				.iconId(input.getIconId())
				.primitive(input.isPrimitive())
				.exhaustive(input.isExhaustive())
				.parents(input.getParents())
				.ancestors(input.getAncestors())
				.statedParents(input.getStatedParents())
				.statedAncestors(input.getStatedAncestors())
				.referencedComponentType(input.getReferencedComponentType())
				.mapTargetComponentType(input.getMapTargetComponentType())
				.preferredDescriptions(input.getPreferredDescriptions())
				.refSetType(input.getRefSetType())
				.doi(input.getDoi());
	}
	
	public static Builder builder(SnomedConcept input) {
		String id = input.getId();
		final Builder builder = builder()
				.id(id)
				.moduleId(input.getModuleId())
				.active(input.isActive())
				.released(input.isReleased())
				.effectiveTime(EffectiveTimes.getEffectiveTime(input.getEffectiveTime()))
				.iconId(input.getIconId())
				.primitive(input.getDefinitionStatus().isPrimitive())
				.exhaustive(input.getSubclassDefinitionStatus().isExhaustive())
				.parents(PrimitiveSets.newLongOpenHashSet(input.getParentIds()))
				.ancestors(PrimitiveSets.newLongOpenHashSet(input.getAncestorIds()))
				.statedParents(PrimitiveSets.newLongOpenHashSet(input.getStatedParentIds()))
				.statedAncestors(PrimitiveSets.newLongOpenHashSet(input.getStatedAncestorIds()));
		
		if (input.getReferenceSet() != null) {
			builder.refSet(input.getReferenceSet());
		}
		
//		if (input.getScore() != null) {
//			builder.score(input.getScore());
//		}
		
		return builder;
	}
	
	public static List<SnomedConceptDocument> fromConcepts(Iterable<? extends SnomedConcept> concepts) {
		return FluentIterable.from(concepts).transform((input) -> {
			final SnomedDescription pt = input.getPt();
			final String preferredTerm = pt == null ? input.getId() : pt.getTerm();
			return SnomedConceptDocument.builder(input).label(preferredTerm).build();
		}).toList();
	}

	@JsonPOJOBuilder(withPrefix="")
	public static class Builder extends SnomedComponentDocument.Builder<Builder, SnomedConceptDocument> {

		private boolean primitive;
		private boolean exhaustive;
		private LongSet parents;
		private LongSet ancestors;
		private LongSet statedParents;
		private LongSet statedAncestors;
		private SnomedRefSetType refSetType;
		private short referencedComponentType = TerminologyRegistry.UNSPECIFIED_NUMBER_SHORT;
		private short mapTargetComponentType;
		private float doi = DEFAULT_DOI;
		private List<SnomedDescriptionFragment> preferredDescriptions = Collections.emptyList();

		@JsonCreator
		private Builder() {
			// Disallow instantiation outside static method
		}
		
		@Override
		protected Builder getSelf() {
			return this;
		}
		
		public Builder primitive(final boolean primitive) {
			this.primitive = primitive;
			return getSelf();
		}

		public Builder exhaustive(final boolean exhaustive) {
			this.exhaustive = exhaustive;
			return getSelf();
		}
		
		@JsonIgnore
		public Builder parents(final long...parents) {
			return parents(PrimitiveSets.newLongOpenHashSet(parents));
		}

		@JsonProperty("parents")
		public Builder parents(final LongSet parents) {
			this.parents = parents;
			return getSelf();
		}
		
		@JsonIgnore
		public Builder statedParents(final long...statedParents) {
			return statedParents(PrimitiveSets.newLongOpenHashSet(statedParents));
		}
		
		@JsonProperty("statedParents")
		public Builder statedParents(final LongSet statedParents) {
			this.statedParents = statedParents;
			return getSelf();
		}
		
		public Builder ancestors(final LongSet ancestors) {
			this.ancestors = ancestors;
			return getSelf();
		}
		
		public Builder statedAncestors(final LongSet statedAncestors) {
			this.statedAncestors = statedAncestors;
			return getSelf();
		}
		
		@JsonIgnore
		public Builder clearRefSet() {
			referencedComponentType = 0;
			mapTargetComponentType = 0;
			refSetType = null;
			return getSelf();
		}
		
		@JsonIgnore
		public Builder refSet(final SnomedReferenceSet refSet) {
			if (!StringUtils.isEmpty(refSet.getMapTargetComponentType())) {
				mapTargetComponentType(TerminologyRegistry.INSTANCE.getTerminologyComponentById(refSet.getMapTargetComponentType()).shortId());
			}
			
			if (!Strings.isNullOrEmpty(refSet.getReferencedComponentType())) {
				referencedComponentType(TerminologyRegistry.INSTANCE.getTerminologyComponentById(refSet.getReferencedComponentType()).shortId());
			}
			
			return refSetType(refSet.getType());
		}
		
		public Builder mapTargetComponentType(short mapTargetComponentType) {
			this.mapTargetComponentType = mapTargetComponentType;
			return getSelf();
		}
		
		public Builder referencedComponentType(short referencedComponentType) {
			this.referencedComponentType = referencedComponentType;
			return getSelf();
		}

		public Builder refSetType(SnomedRefSetType refSetType) {
			this.refSetType = refSetType;
			return getSelf();
		}

		public Builder doi(float doi) {
			this.doi = doi;
			return getSelf();
		}
		
		public Builder preferredDescriptions(List<SnomedDescriptionFragment> preferredDescriptions) {
			this.preferredDescriptions = Collections3.toImmutableList(preferredDescriptions);
			for (SnomedDescriptionFragment preferredDescription : this.preferredDescriptions) {
				checkArgument(!preferredDescription.getLanguageRefSetIds().isEmpty(), "At least one language reference set ID is required to create a preferred description fragment for description %s.", preferredDescription.getId());
			}
			return getSelf();
		}
		
		public SnomedConceptDocument build() {
			final SnomedConceptDocument entry = new SnomedConceptDocument(id,
					label,
					iconId, 
					moduleId, 
					released, 
					active, 
					effectiveTime, 
					primitive, 
					exhaustive,
					refSetType, 
					referencedComponentType,
					mapTargetComponentType,
					memberOf,
					activeMemberOf,
					preferredDescriptions);
			
			entry.doi = doi;
			entry.setScore(score);
			
			if (parents != null) {
				entry.parents = parents;
			}
			
			if (statedParents != null) {
				entry.statedParents = statedParents;
			}
			
			if (ancestors != null) {
				entry.ancestors = ancestors;
			}
			
			if (statedAncestors != null) {
				entry.statedAncestors = statedAncestors;
			}
			
			return entry;
		}

	}

	private final boolean primitive;
	private final boolean exhaustive;
	private final SnomedRefSetType refSetType;
	private final short referencedComponentType;
	private final short mapTargetComponentType;
	private final List<SnomedDescriptionFragment> preferredDescriptions;
	
	private LongSet parents;
	private LongSet ancestors;
	private LongSet statedParents;
	private LongSet statedAncestors;
	private float doi;

	private SnomedConceptDocument(final String id,
			final String label,
			final String iconId,
			final String moduleId,
			final boolean released,
			final boolean active,
			final long effectiveTime,
			final boolean primitive,
			final boolean exhaustive, 
			final SnomedRefSetType refSetType, 
			final short referencedComponentType,
			final short mapTargetComponentType,
			final List<String> referringRefSets,
			final List<String> referringMappingRefSets,
			final List<SnomedDescriptionFragment> preferredDescriptions) {

		super(id, label, iconId, moduleId, released, active, effectiveTime, referringRefSets, referringMappingRefSets);
		this.primitive = primitive;
		this.exhaustive = exhaustive;
		this.refSetType = refSetType;
		this.referencedComponentType = referencedComponentType;
		this.mapTargetComponentType = mapTargetComponentType;
		this.preferredDescriptions = preferredDescriptions;
	}
	
	@Override
	protected Revision.Builder<?, ? extends Revision> toBuilder() {
		return builder(this);
	}
	
	public float getDoi() {
		return doi;
	}
	
	/**
	 * @return {@code true} if the concept definition status is 900000000000074008 (primitive), {@code false} otherwise
	 */
	public boolean isPrimitive() {
		return primitive;
	}

	/**
	 * @return {@code true} if the concept subclass definition status is exhaustive, {@code false} otherwise
	 */
	public boolean isExhaustive() {
		return exhaustive;
	}
	
	public LongSet getParents() {
		return parents;
	}
	
	public LongSet getStatedParents() {
		return statedParents;
	}
	
	public LongSet getAncestors() {
		return ancestors;
	}
	
	public LongSet getStatedAncestors() {
		return statedAncestors;
	}
	
	public SnomedRefSetType getRefSetType() {
		return refSetType;
	}
	
	public short getReferencedComponentType() {
		return referencedComponentType;
	}
	
	public short getMapTargetComponentType() {
		return mapTargetComponentType;
	}
	
	@JsonIgnore
	public boolean isRefSet() {
		return getRefSetType() != null;
	}
	
	public List<SnomedDescriptionFragment> getPreferredDescriptions() {
		return preferredDescriptions;
	}
	
	@Override
	protected ToStringHelper doToString() {
		return super.doToString()
				.add("primitive", primitive)
				.add("exhaustive", exhaustive)
				.add("refSetType", refSetType)
				.add("referencedComponentType", referencedComponentType)
				.add("mapTargetComponentType", mapTargetComponentType)
				.add("parents", parents)
				.add("ancestors", ancestors)
				.add("statedParents", statedParents)
				.add("statedAncestors", statedAncestors)
				.add("doi", doi);
	}
	
	/**
	 * Computes whether a reference set is structural or not.
	 * @param refSetId
	 * @param type
	 * @return
	 */
	public static boolean isStructural(final String refSetId, final SnomedRefSetType type) {
		switch (type) {
			case LANGUAGE: //$FALL-THROUGH$
			case CONCRETE_DATA_TYPE: //$FALL-THROUGH$
			case ASSOCIATION: //$FALL-THROUGH$
			case MODULE_DEPENDENCY: //$FALL-THROUGH$
				return true;
			case ATTRIBUTE_VALUE:
				return 
						Concepts.REFSET_DESCRIPTION_INACTIVITY_INDICATOR.equals(refSetId) 
						|| Concepts.REFSET_CONCEPT_INACTIVITY_INDICATOR.equals(refSetId) 
						|| Concepts.REFSET_RELATIONSHIP_REFINABILITY.equals(refSetId);
			default: return false;
		}
	}

}

