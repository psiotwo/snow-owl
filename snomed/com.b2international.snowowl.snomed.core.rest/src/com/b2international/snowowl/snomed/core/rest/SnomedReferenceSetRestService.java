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
package com.b2international.snowowl.snomed.core.rest;

import static com.google.common.collect.Sets.newHashSetWithExpectedSize;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.b2international.commons.exceptions.BadRequestException;
import com.b2international.commons.http.ExtendedLocale;
import com.b2international.snowowl.core.domain.TransactionContext;
import com.b2international.snowowl.core.events.bulk.BulkRequest;
import com.b2international.snowowl.core.events.bulk.BulkRequestBuilder;
import com.b2international.snowowl.core.events.util.Promise;
import com.b2international.snowowl.core.rest.AbstractRestService;
import com.b2international.snowowl.core.rest.RestApiError;
import com.b2international.snowowl.snomed.core.domain.refset.SnomedRefSetType;
import com.b2international.snowowl.snomed.core.domain.refset.SnomedReferenceSet;
import com.b2international.snowowl.snomed.core.domain.refset.SnomedReferenceSets;
import com.b2international.snowowl.snomed.core.rest.domain.ChangeRequest;
import com.b2international.snowowl.snomed.core.rest.domain.SnomedRefSetRestInput;
import com.b2international.snowowl.snomed.core.rest.request.BulkRestRequest;
import com.b2international.snowowl.snomed.core.rest.request.RefSetMemberRequestResolver;
import com.b2international.snowowl.snomed.core.rest.request.RefSetRequestResolver;
import com.b2international.snowowl.snomed.core.rest.request.RequestResolver;
import com.b2international.snowowl.snomed.core.rest.request.RestRequest;
import com.b2international.snowowl.snomed.datastore.request.SnomedRequests;
import com.google.common.collect.Sets;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @since 4.5
 */
@Api(value = "Refsets", description="RefSets", tags = "refSets")
@Controller
@RequestMapping(value = "/{path:**}/refsets")		
public class SnomedReferenceSetRestService extends AbstractSnomedRestService {

	public SnomedReferenceSetRestService() {
		super(SnomedReferenceSet.Fields.ALL);
	}
	
	@ApiOperation(
		value="Retrieve Reference Sets from a branch", 
		notes="Returns a list with all reference sets from a branch."
	)
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK", response = SnomedReferenceSets.class),
		@ApiResponse(code = 404, message = "Branch not found", response = RestApiError.class)
	})
	@GetMapping(produces = { AbstractRestService.JSON_MEDIA_TYPE })	
	public @ResponseBody Promise<SnomedReferenceSets> search(
			@ApiParam(value = "The branch path", required = true)
			@PathVariable(value="path")
			final String branchPath,
			
			@ApiParam(value = "The reference set type to match")
			@RequestParam(value="refSetTypes", required=false)
			final String[] refSetTypes,
			
			@ApiParam(value = "The scrollKeepAlive to start a scroll using this query")
			@RequestParam(value="scrollKeepAlive", required=false) 
			final String scrollKeepAlive,
			
			@ApiParam(value = "A scrollId to continue scrolling a previous query")
			@RequestParam(value="scrollId", required=false) 
			final String scrollId,

			@ApiParam(value = "The search key to use for retrieving the next page of results")
			@RequestParam(value="searchAfter", required=false) 
			final String searchAfter,

			@ApiParam(value = "The maximum number of items to return", defaultValue = "50")
			@RequestParam(value="limit", required=false) 
			final int limit,
			
			@ApiParam(value = "Sort keys")
			@RequestParam(value="sort", required=false)
			final List<String> sortKeys) {
		
		return SnomedRequests.prepareSearchRefSet()
				.filterByTypes(getRefSetTypes(refSetTypes))
				.setScroll(scrollKeepAlive)
				.setScrollId(scrollId)
				.setSearchAfter(searchAfter)
				.setLimit(limit)
				.sortBy(extractSortFields(sortKeys))
				.build(repositoryId, branchPath)
				.execute(getBus());
	}
	
	private Collection<SnomedRefSetType> getRefSetTypes(String[] refSetTypes) {
		if (refSetTypes == null) {
			return null;
		}
		
		final Set<String> unresolvedRefSetTypes = Sets.newTreeSet();
		final Set<SnomedRefSetType> resolvedRefSetTypes = newHashSetWithExpectedSize(refSetTypes.length);
		
		for (String refSetTypeString : refSetTypes) {
			final SnomedRefSetType refSetType = SnomedRefSetType.get(refSetTypeString.toUpperCase());
			if (refSetType != null) {
				resolvedRefSetTypes.add(refSetType);
			} else {
				unresolvedRefSetTypes.add(refSetTypeString);
			}
		}
		
		if (!unresolvedRefSetTypes.isEmpty()) {
			BadRequestException e = new BadRequestException("Unknown reference set types: %s", unresolvedRefSetTypes);
			e.setDeveloperMessage("Available reference set types are: " + SnomedRefSetType.VALUES);
			throw e;
		}
		
		return resolvedRefSetTypes;
	}

	@ApiOperation(
		value="Retrieve Reference Set",
		notes="Returns all properties of the specified Reference set."
				+ "<p>The following properties can be expanded:"
				+ "<p>"
				+ "&bull; members(offset:0,limit:50,expand(referencedComponent(expand(pt(),...))) &ndash; the reference set members, and any applicable nested expansions<br>"
	)
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK", response = Void.class),
		@ApiResponse(code = 404, message = "Branch or Reference set not found", response = RestApiError.class)
	})
	@GetMapping(value = "/{id}", produces = { AbstractRestService.JSON_MEDIA_TYPE })
	public @ResponseBody Promise<SnomedReferenceSet> get(
			@ApiParam(value = "The branch path")
			@PathVariable(value="path")
			final String branchPath,

			@ApiParam(value = "The Reference set identifier")
			@PathVariable(value="id")
			final String referenceSetId,
			
			@ApiParam(value = "Expansion parameters")
			@RequestParam(value="expand", required=false)
			final String expand,

			@ApiParam(value = "Accepted language tags, in order of preference")
			@RequestHeader(value="Accept-Language", defaultValue="en-US;q=0.8,en-GB;q=0.6", required=false) 
			final String acceptLanguage) {

		final List<ExtendedLocale> extendedLocales = getExtendedLocales(acceptLanguage);
		
		return SnomedRequests
				.prepareGetReferenceSet(referenceSetId)
				.setExpand(expand)
				.setLocales(extendedLocales)
				.build(repositoryId, branchPath)
				.execute(getBus());
	}
	
	@ApiOperation(
		value="Create a reference set",
		notes="Creates a new reference set directly on a branch. Creates the corresponding identifier concept as well based on the given JSON body."
			+ "<p>Reference Set type and referenced component type properties are immutable and cannot be modified, "
			+ "thus there is no update endpoint for reference sets. "
			+ "To update the corresponding identifier concept properties, use the concept update endpoint.</p>")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Created", response = Void.class),
		@ApiResponse(code = 404, message = "Branch not found", response = RestApiError.class)
	})
	@PostMapping(consumes = { AbstractRestService.JSON_MEDIA_TYPE })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Void> create(
			@ApiParam(value = "The branch path")
			@PathVariable(value="path")
			final String branchPath,
			
			@ApiParam(value = "Reference set parameters")
			@RequestBody 
			final ChangeRequest<SnomedRefSetRestInput> body,

			@RequestHeader(value = X_AUTHOR, required = false)
			final String author) {
		
		final SnomedRefSetRestInput change = body.getChange();
		final String commitComment = body.getCommitComment();
		final String defaultModuleId = body.getDefaultModuleId(); 
		
		final String createdRefSetId = change.toRequestBuilder() 
			.build(repositoryId, branchPath, author, commitComment, defaultModuleId)
			.execute(getBus())
			.getSync(COMMIT_TIMEOUT, TimeUnit.MILLISECONDS)
			.getResultAs(String.class);
		
		return ResponseEntity.created(getRefSetLocationURI(branchPath, createdRefSetId)).build();
	}
	
	@ApiOperation(
		value="Executes an action on a reference set",
		notes="Executes an action specified via the request body on the reference set identified by the given identifier."
				+ "<p>Supported actions are:"
				+ "&bull; 'sync' - Executes sync action on all members of this query reference set, see sync on /members/:id/actions endpoint"
				+ "</p>"
	)
	@ApiResponses({
		@ApiResponse(code = 204, message = "No content"),
		@ApiResponse(code = 404, message = "Branch or reference set not found", response = RestApiError.class)
	})
	@PostMapping(value = "/{id}/actions", consumes = { AbstractRestService.JSON_MEDIA_TYPE })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void executeAction(
			@ApiParam(value = "The branch path")
			@PathVariable(value="path")
			final String branchPath,
			
			@ApiParam(value = "The reference set identifier")
			@PathVariable(value="id")
			final String refSetId,
			
			@ApiParam(value = "Reference set action")
			@RequestBody 
			final ChangeRequest<RestRequest> body,
			
			@RequestHeader(value = X_AUTHOR)
			final String author) {
		
		final RequestResolver<TransactionContext> resolver = new RefSetRequestResolver();
		
		final RestRequest change = body.getChange();
		final String commitComment = body.getCommitComment();
		final String defaultModuleId = body.getDefaultModuleId();
		
		change.setSource("referenceSetId", refSetId);
		
		SnomedRequests.prepareCommit()
			.setDefaultModuleId(defaultModuleId)
			.setBody(change.resolve(resolver))
			.setCommitComment(commitComment)
			.setAuthor(author)
			.build(repositoryId, branchPath)
			.execute(getBus())
			.getSync();
	}
	
	@ApiOperation(
		value="Executes multiple requests (bulk request) on the members of a single reference set.",
		notes="Execute reference set member create, update, delete requests at once on a single reference set. "
				+ "See bulk requests section for more details."
	)
	@ApiResponses({
		@ApiResponse(code = 204, message = "No content"),
		@ApiResponse(code = 404, message = "Branch or reference set not found", response = RestApiError.class)
	})
	@PutMapping(value="/{id}/members", consumes = { AbstractRestService.JSON_MEDIA_TYPE })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateMembers(
			@ApiParam(value = "The branch path")
			@PathVariable(value="path")
			final String branchPath,
			
			@ApiParam(value = "The reference set identifier")
			@PathVariable(value="id")
			final String refSetId,
			
			@ApiParam(value = "The reference set member changes")
			@RequestBody
			final ChangeRequest<BulkRestRequest> request,
			
			@RequestHeader(value = X_AUTHOR, required = false)
			final String author) {
		
		final RequestResolver<TransactionContext> resolver = new RefSetMemberRequestResolver();
		
		final BulkRestRequest bulkRequest = request.getChange();
		final String commitComment = request.getCommitComment();
		final String defaultModuleId = request.getDefaultModuleId();
		
		// FIXME setting referenceSetId even if defined??? 
		// enforces that new members will be created in the defined refset
		for (RestRequest req : bulkRequest.getRequests()) {
			req.setSource("referenceSetId", refSetId);
		}
		
		final BulkRequestBuilder<TransactionContext> updateRequestBuilder = BulkRequest.create();
		bulkRequest.resolve(resolver).forEach(updateRequestBuilder::add);
		
		SnomedRequests.prepareCommit()
			.setDefaultModuleId(defaultModuleId)
			.setBody(updateRequestBuilder.build())
			.setAuthor(author)
			.setCommitComment(commitComment)
			.build(repositoryId, branchPath)
			.execute(getBus())
			.getSync();
	}
	
	@ApiOperation(
		value="Delete Reference Set",
		notes="Removes the reference set from the terminology store."
	)
	@ApiResponses({
		@ApiResponse(code = 204, message = "OK", response = Void.class),
		@ApiResponse(code = 404, message = "Branch or Reference set not found", response = RestApiError.class)
	})
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(
			@ApiParam(value = "The branch path")
			@PathVariable(value="path")
			final String branchPath,

			@ApiParam(value = "The Reference set identifier")
			@PathVariable(value="id")
			final String referenceSetId,
			
			@ApiParam(value = "Force deletion flag")
			@RequestParam(defaultValue="false", required=false)
			final Boolean force,

			@RequestHeader(value = X_AUTHOR, required = false)
			final String author) {
		
		SnomedRequests.prepareDeleteReferenceSet(referenceSetId, force)
				.build(repositoryId, branchPath, author, String.format("Deleted Reference Set '%s' from store.", referenceSetId))
				.execute(getBus())
				.getSync();
	}
	
	private URI getRefSetLocationURI(String branchPath, String refSetId) {
		return MvcUriComponentsBuilder.fromController(SnomedReferenceSetRestService.class).pathSegment(branchPath, refSetId).build().toUri();
	}
}
