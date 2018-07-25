/*
 * Copyright 2018 B2i Healthcare Pte Ltd, http://b2i.sg
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
package com.b2international.snowowl.snomed.fhir;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.b2international.commons.http.ExtendedLocale;
import com.b2international.snowowl.core.exceptions.NotFoundException;
import com.b2international.snowowl.datastore.CodeSystemVersionEntry;
import com.b2international.snowowl.fhir.core.LogicalId;
import com.b2international.snowowl.fhir.core.codesystems.IdentifierUse;
import com.b2international.snowowl.fhir.core.codesystems.PublicationStatus;
import com.b2international.snowowl.fhir.core.exceptions.FhirException;
import com.b2international.snowowl.fhir.core.model.dt.Identifier;
import com.b2international.snowowl.fhir.core.model.dt.Uri;
import com.b2international.snowowl.fhir.core.model.valueset.ValueSet;
import com.b2international.snowowl.fhir.core.provider.CodeSystemApiProvider;
import com.b2international.snowowl.fhir.core.provider.FhirApiProvider;
import com.b2international.snowowl.fhir.core.provider.ICodeSystemApiProvider;
import com.b2international.snowowl.fhir.core.provider.IValueSetApiProvider;
import com.b2international.snowowl.snomed.common.SnomedTerminologyComponentConstants;
import com.b2international.snowowl.snomed.core.domain.SnomedConcept;
import com.b2international.snowowl.snomed.core.domain.refset.SnomedReferenceSet;
import com.b2international.snowowl.snomed.datastore.SnomedDatastoreActivator;
import com.b2international.snowowl.snomed.datastore.request.SnomedRequests;
import com.b2international.snowowl.snomed.snomedrefset.SnomedRefSetType;
import com.b2international.snowowl.terminologyregistry.core.request.CodeSystemRequests;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Provider for the SNOMED CT FHIR support
 * @since 6.4
 * @see ICodeSystemApiProvider
 * @see CodeSystemApiProvider
 */
public final class SnomedValueSetApiProvider extends FhirApiProvider implements IValueSetApiProvider {

	//private static final String URI_BASE = "http://snomed.info";
	//private static final Uri SNOMED_CT_URI = new Uri(URI_BASE + "/sct");
	private static final Path SNOMED_INT_PATH = Paths.get(SnomedDatastoreActivator.REPOSITORY_UUID, SnomedTerminologyComponentConstants.SNOMED_SHORT_NAME);
	
	private static final Set<String> SUPPORTED_URIS = ImmutableSet.of(
		SnomedTerminologyComponentConstants.SNOMED_SHORT_NAME,
		SnomedTerminologyComponentConstants.SNOMED_INT_LINK,
		SnomedUri.SNOMED_BASE_URI_STRING
	);
	
	private String repositoryId;
	
	public SnomedValueSetApiProvider() {
		this.repositoryId = SnomedDatastoreActivator.REPOSITORY_UUID;
	}
	
	@Override
	protected String getRepositoryId() {
		return repositoryId;
	}
	
	@Override
	public boolean isSupported(LogicalId logicalId) {
		return logicalId.getRepositoryId().startsWith(SnomedDatastoreActivator.REPOSITORY_UUID);
	}
	
	@Override
	public Collection<String> getSupportedURIs() {
		return SUPPORTED_URIS;
	}
	
	@Override
	public final boolean isSupported(String uri) {
		if (Strings.isNullOrEmpty(uri)) return false;
		
		boolean foundInList = getSupportedURIs().stream()
				.filter(uri::equalsIgnoreCase)
				.findAny()
				.isPresent();
			
		//extension and version is part of the URI
		boolean extensionUri = uri.startsWith(SnomedUri.SNOMED_BASE_URI_STRING);
		
		return foundInList || extensionUri;
	}

	@Override
	public Collection<ValueSet> getValueSets() {
		
		//Collect every version on every extension
		List<CodeSystemVersionEntry> codeSystemVersionList = collectCodeSystemVersions(repositoryId);
		
		List<ValueSet> valueSets = codeSystemVersionList.stream().map(csve -> {
			
			return SnomedRequests.prepareSearchRefSet()
				.all()
				.filterByType(SnomedRefSetType.SIMPLE)
				.build(repositoryId, csve.getPath())
				.execute(getBus())
				.then(refsets -> {
					return refsets.stream()
						.map(r -> createValueSetBuilder(r, csve, displayLanguage))
						.map(ValueSet.Builder::build)
						.collect(Collectors.toList());
				})
				.getSync();
				
		}).collect(Collectors.toList())
		.stream().flatMap(List::stream).collect(Collectors.toList()); //List<List<?> -> List<?>
		
		return valueSets;
	}
	
	@Override
	public ValueSet getValueSet(LogicalId logicalId) {
		
		
		Optional<CodeSystemVersionEntry> codeSystemOptional = CodeSystemRequests.prepareSearchCodeSystemVersion()
			.one()
			.filterByBranchPath(logicalId.getBranchPath())
			.build(repositoryId)
			.execute(getBus())
			.getSync()
			.first();
		
		CodeSystemVersionEntry codeSystemVersion = codeSystemOptional.orElseThrow(() -> new FhirException("Could not find corresponding version for value set id [%s].", "ValueSet.id", logicalId));
		
		//TODO: what to do with the language? Where do i get the locale from the request? 
		String displayLanguage = "en-us";
		
		return SnomedRequests.prepareSearchRefSet()
			.filterById(logicalId.getComponentId())
			.filterByType(SnomedRefSetType.SIMPLE)
			.build(repositoryId, logicalId.getBranchPath())
			.execute(getBus())
			.then(refsets -> {
				return refsets.stream()
					.map(r -> createValueSetBuilder(r, codeSystemVersion, displayLanguage))
					.map(ValueSet.Builder::build)
					.collect(Collectors.toList());
			})
			.getSync()
			.stream()
			.findFirst()
			.orElseThrow(() -> new NotFoundException("Active value set", logicalId.toString()));
		
	}
	
	private ValueSet.Builder createValueSetBuilder(final SnomedReferenceSet referenceSet, final CodeSystemVersionEntry codeSystemVersion, final String displayLanguage) {
		
		String referenceSetId = referenceSet.getId();

		//TODO: module needs to be added as well
		SnomedUri uri = SnomedUri.builder().version(codeSystemVersion.getEffectiveDate()).build();
		
		Identifier identifier = Identifier.builder()
			.use(IdentifierUse.OFFICIAL)
			.system(uri.toString())
			.value(referenceSetId)
			.build();
		
		LogicalId logicalId = new LogicalId(repositoryId, codeSystemVersion.getPath(), referenceSetId);
		
		SnomedConcept refsetConcept = SnomedRequests.prepareGetConcept(referenceSetId)
			.setExpand("pt()")
			.setLocales(ImmutableList.of(ExtendedLocale.valueOf(displayLanguage)))
			.build(getRepositoryId(), codeSystemVersion.getPath())
			.execute(getBus())
			.getSync();
		
		
		return ValueSet.builder(logicalId.toString())
			.identifier(identifier)
			.version(codeSystemVersion.getVersionId())
			.language(displayLanguage)
			.url(uri.toUri())
			.status(referenceSet.isActive() ? PublicationStatus.ACTIVE : PublicationStatus.RETIRED)
			.date(new Date(codeSystemVersion.getEffectiveDate()))
			.name(refsetConcept.getPt().getTerm())
			.title(refsetConcept.getPt().getTerm());
	}
	
	protected Uri getFhirUri() {
		return SnomedUri.SNOMED_BASE_URI;
	}

	@Override
	protected String getCodeSystemShortName() {
		return SnomedTerminologyComponentConstants.SNOMED_SHORT_NAME;
	}
	
}
