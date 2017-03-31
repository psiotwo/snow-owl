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
package com.b2international.snowowl.snomed.api.rest.components;

import static com.b2international.snowowl.core.ApplicationContext.getServiceForClass;
import static com.b2international.snowowl.snomed.api.rest.SnomedBranchingRestRequests.createBranchRecursively;
import static com.b2international.snowowl.snomed.api.rest.SnomedComponentRestRequests.createComponent;
import static com.b2international.snowowl.snomed.api.rest.SnomedComponentRestRequests.deleteComponent;
import static com.b2international.snowowl.snomed.api.rest.SnomedComponentRestRequests.getComponent;
import static com.b2international.snowowl.snomed.api.rest.SnomedComponentRestRequests.updateComponent;
import static com.b2international.snowowl.snomed.api.rest.SnomedRestFixtures.createNewConcept;
import static com.b2international.snowowl.snomed.api.rest.SnomedRestFixtures.createNewRelationship;
import static com.b2international.snowowl.snomed.api.rest.SnomedRestFixtures.createRelationshipRequestBody;
import static com.b2international.snowowl.test.commons.rest.RestExtensions.lastPathSegment;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.b2international.snowowl.core.api.IBranchPath;
import com.b2international.snowowl.core.terminology.ComponentCategory;
import com.b2international.snowowl.datastore.BranchPathUtils;
import com.b2international.snowowl.snomed.SnomedConstants.Concepts;
import com.b2international.snowowl.snomed.api.rest.AbstractSnomedApiTest;
import com.b2international.snowowl.snomed.api.rest.SnomedComponentType;
import com.b2international.snowowl.snomed.core.domain.CharacteristicType;
import com.b2international.snowowl.snomed.core.domain.RelationshipModifier;
import com.b2international.snowowl.snomed.datastore.id.ISnomedIdentifierService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

/**
 * @since 4.0
 */
public class SnomedRelationshipApiTest extends AbstractSnomedApiTest {

	@Test
	public void createRelationshipNonExistentBranch() {
		Map<?, ?> requestBody = createRelationshipRequestBody(Concepts.ROOT_CONCEPT, Concepts.PART_OF, Concepts.NAMESPACE_ROOT)
				.put("commitComment", "Created new relationship on non-existent branch")
				.build();

		createComponent(BranchPathUtils.createPath("MAIN/x/y/z"), SnomedComponentType.RELATIONSHIP, requestBody).statusCode(404);
	}

	@Test
	public void createRelationshipInvalidSource() {
		Map<?, ?> requestBody = createRelationshipRequestBody("11110000", Concepts.PART_OF, Concepts.NAMESPACE_ROOT)
				.put("commitComment", "Created new relationship with invalid sourceId")
				.build();

		createComponent(branchPath, SnomedComponentType.RELATIONSHIP, requestBody).statusCode(400);
	}

	@Test
	public void createRelationshipInvalidType() {
		Map<?, ?> requestBody = createRelationshipRequestBody(Concepts.ROOT_CONCEPT, "11110000", Concepts.NAMESPACE_ROOT)
				.put("commitComment", "Created new relationship with invalid typeId")
				.build();

		createComponent(branchPath, SnomedComponentType.RELATIONSHIP, requestBody).statusCode(400);
	}

	@Test
	public void createRelationshipInvalidDestination() {
		Map<?, ?> requestBody = createRelationshipRequestBody(Concepts.ROOT_CONCEPT, Concepts.PART_OF, "11110000")
				.put("commitComment", "Created new relationship with invalid destinationId")
				.build();

		createComponent(branchPath, SnomedComponentType.RELATIONSHIP, requestBody).statusCode(400);
	}

	@Test
	public void createRelationshipInvalidModule() {
		Map<?, ?> requestBody = createRelationshipRequestBody(Concepts.ROOT_CONCEPT, Concepts.PART_OF, Concepts.NAMESPACE_ROOT, "11110000")
				.put("commitComment", "Created new relationship with invalid moduleId")
				.build();

		createComponent(branchPath, SnomedComponentType.RELATIONSHIP, requestBody).statusCode(400);
	}

	@Test
	public void createRelationship() {
		Map<?, ?> requestBody = createRelationshipRequestBody(Concepts.ROOT_CONCEPT, Concepts.PART_OF, Concepts.NAMESPACE_ROOT)
				.put("commitComment", "Created new relationship")
				.build();

		createComponent(branchPath, SnomedComponentType.RELATIONSHIP, requestBody).statusCode(201);
	}

	@Test
	public void createRelationshipWithReservedId() {
		ISnomedIdentifierService identifierService = getServiceForClass(ISnomedIdentifierService.class);
		String relationshipId = Iterables.getOnlyElement(identifierService.reserve(null, ComponentCategory.RELATIONSHIP, 1));

		Map<?, ?> requestBody = createRelationshipRequestBody(Concepts.ROOT_CONCEPT, Concepts.PART_OF, Concepts.NAMESPACE_ROOT)
				.put("id", relationshipId)
				.put("commitComment", "Created new relationship with reserved identifier")
				.build();

		createComponent(branchPath, SnomedComponentType.RELATIONSHIP, requestBody).statusCode(201)
		.header("Location", endsWith("/" + relationshipId));
	}

	@Test
	public void createDuplicateRelationship() {
		String relationshipId = createNewRelationship(branchPath);
		Map<?, ?> requestBody = createRelationshipRequestBody(Concepts.ROOT_CONCEPT, Concepts.PART_OF, Concepts.NAMESPACE_ROOT)
				.put("id", relationshipId)
				.put("commitComment", "Created new relationship with duplicate identifier")
				.build();

		createComponent(branchPath, SnomedComponentType.RELATIONSHIP, requestBody).statusCode(409);
	}

	@Test
	public void createRelationshipInferred() {
		Map<?, ?> requestBody = createRelationshipRequestBody(Concepts.ROOT_CONCEPT, Concepts.PART_OF, Concepts.NAMESPACE_ROOT,  
				CharacteristicType.INFERRED_RELATIONSHIP)
				.put("commitComment", "Created new relationship with inferred characteristic type")
				.build();

		String relationshipId = lastPathSegment(createComponent(branchPath, SnomedComponentType.RELATIONSHIP, requestBody)
				.statusCode(201)
				.extract().header("Location"));

		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200)
		.body("characteristicType", equalTo(CharacteristicType.INFERRED_RELATIONSHIP.name()));
	}

	@Test
	public void deleteRelationship() {
		String relationshipId = createNewRelationship(branchPath);
		deleteComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId, false).statusCode(204);
		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(404);
	}

	@Test
	public void inactivateRelationship() {
		String relationshipId = createNewRelationship(branchPath);
		Map<?, ?> requestBody = ImmutableMap.builder()
				.put("active", false)
				.put("commitComment", "Inactivated relationship")
				.build();

		updateComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId, requestBody).statusCode(204);
		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200)
		.body("active", equalTo(false));
	}

	@Test
	public void deleteInactiveNonIsaRelationship() throws Exception {
		deleteInactiveRelationship(Concepts.PART_OF);
	}

	@Test
	public void deleteInactiveIsaRelationship() throws Exception {
		deleteInactiveRelationship(Concepts.IS_A);
	}

	private void deleteInactiveRelationship(String typeId) {
		String sourceId = createNewConcept(branchPath);
		String relationshipId = createNewRelationship(branchPath, sourceId, typeId, Concepts.NAMESPACE_ROOT);

		Map<?, ?> requestBody = ImmutableMap.builder()
				.put("active", false)
				.put("commitComment", "Inactivated relationship")
				.build();

		updateComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId, requestBody).statusCode(204);
		deleteComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId, false).statusCode(204);
		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(404);

		/* 
		 * Source concept should still exist at this point (the deletion plan should not 
		 * consider removing it with the relationship).
		 */
		getComponent(branchPath, SnomedComponentType.CONCEPT, sourceId).statusCode(200);
	}

	@Test
	public void createInactiveNonIsaRelationship() throws Exception {
		createInactiveRelationship(Concepts.PART_OF);
	}

	@Test
	public void createInactiveIsaRelationship() throws Exception {
		createInactiveRelationship(Concepts.IS_A);
	}

	private void createInactiveRelationship(String typeId) {
		Map<?, ?> requestBody = createRelationshipRequestBody(Concepts.ROOT_CONCEPT, typeId, Concepts.NAMESPACE_ROOT)
				.put("active", false)
				.put("commitComment", "Created inactive relationship")
				.build();

		String relationshipId = lastPathSegment(createComponent(branchPath, SnomedComponentType.RELATIONSHIP, requestBody)
				.statusCode(201)
				.extract().header("Location"));

		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200)
		.body("active", equalTo(false));
	}

	@Test
	public void createIsARelationshipToSelf() throws Exception {
		Map<?, ?> requestBody = createRelationshipRequestBody(Concepts.NAMESPACE_ROOT, Concepts.IS_A, Concepts.NAMESPACE_ROOT)
				.put("commitComment", "Created new relationship pointing to itself")
				.build();

		createComponent(branchPath, SnomedComponentType.RELATIONSHIP, requestBody).statusCode(400);
	}

	@Test
	public void updateGroup() {
		String relationshipId = createNewRelationship(branchPath);
		Map<?, ?> requestBody = ImmutableMap.builder()
				.put("group", 99)
				.put("commitComment", "Updated relationship group")
				.build();

		updateComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId, requestBody).statusCode(204);
		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200)
		.body("group", equalTo(99));
	}

	@Test
	public void updateGroupToInvalidValue() {
		String relationshipId = createNewRelationship(branchPath);
		Map<?, ?> requestBody = ImmutableMap.builder()
				.put("group", -5)
				.put("commitComment", "Updated relationship group to invalid value")
				.build();

		updateComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId, requestBody).statusCode(400);
		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200)
		.body("group", equalTo(0));
	}

	@Test
	public void changeRelationshipUnionGroup() {
		String relationshipId = createNewRelationship(branchPath);
		Map<?, ?> requestBody = ImmutableMap.builder()
				.put("unionGroup", 101)
				.put("commitComment", "Updated relationship union group")
				.build();

		updateComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId, requestBody).statusCode(204);
		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200)
		.body("unionGroup", equalTo(101));
	}

	@Test
	public void changeRelationshipCharacteristicType() {
		String relationshipId = createNewRelationship(branchPath);
		Map<?, ?> requestBody = ImmutableMap.builder()
				.put("characteristicType", CharacteristicType.ADDITIONAL_RELATIONSHIP)
				.put("commitComment", "Updated relationship characteristic type")
				.build();

		updateComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId, requestBody).statusCode(204);
		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200)
		.body("characteristicType", equalTo(CharacteristicType.ADDITIONAL_RELATIONSHIP.name()));
	}

	@Test
	public void changeRelationshipModifier() {
		String relationshipId = createNewRelationship(branchPath);
		Map<?, ?> requestBody = ImmutableMap.builder()
				.put("modifier", RelationshipModifier.UNIVERSAL)
				.put("commitComment", "Updated relationship modifier")
				.build();

		updateComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId, requestBody).statusCode(204);
		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200)
		.body("modifier", equalTo(RelationshipModifier.UNIVERSAL.name()));
	}

	@Test
	public void createRelationshipOnNestedBranch() {
		IBranchPath a = BranchPathUtils.createPath(branchPath, "a");
		IBranchPath b = BranchPathUtils.createPath(a, "b");
		createBranchRecursively(b);

		String relationshipId = createNewRelationship(b);

		getComponent(b, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200);
		getComponent(a, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(404);
		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(404);
	}

	@Test
	public void deleteRelationshipOnNestedBranch() {
		String conceptId = createNewConcept(branchPath);

		List<String> typeIds = newArrayList();
		for (int i = 0; i < 10; i++) {
			String typeId = createNewConcept(branchPath);
			typeIds.add(typeId);
		}

		List<String> relationshipIds = newArrayList();
		for (int i = 0; i < 10; i++) {
			String relationshipId = createNewRelationship(branchPath, conceptId, typeIds.get(i), Concepts.NAMESPACE_ROOT);
			relationshipIds.add(relationshipId);
		}

		IBranchPath a = BranchPathUtils.createPath(branchPath, "a");
		IBranchPath b = BranchPathUtils.createPath(a, "b");
		createBranchRecursively(b);

		// New relationship on nested branch resets the concept's version to 1 again
		createNewRelationship(b, conceptId, Concepts.PART_OF, Concepts.NAMESPACE_ROOT);

		// Deleting a relationship from the middle should work
		String relationshipToDeleteId = relationshipIds.remove(7);
		deleteComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipToDeleteId, false).statusCode(204);
		getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipToDeleteId).statusCode(404);

		deleteComponent(b, SnomedComponentType.RELATIONSHIP, relationshipToDeleteId, false).statusCode(204);
		getComponent(b, SnomedComponentType.RELATIONSHIP, relationshipToDeleteId).statusCode(404);

		// All the remaining relationships should be visible
		for (String relationshipId : relationshipIds) {
			getComponent(branchPath, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200);
			getComponent(b, SnomedComponentType.RELATIONSHIP, relationshipId).statusCode(200);
		}
	}

}