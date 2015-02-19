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
package com.b2international.snowowl.snomed.datastore.services;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.b2international.snowowl.core.api.IComponent;
import com.b2international.snowowl.snomed.Concept;
import com.b2international.snowowl.snomed.Description;
import com.b2international.snowowl.snomed.datastore.CaseSignificance;
import com.b2international.snowowl.snomed.datastore.SnomedDescriptionFragment;

/**
 * Lookup service for SNOMED&nbsp;CT ontology.
 *
 */
public interface ISnomedLookupService {

	/**
	 * Checks whether a relationship with the given relationship type exists between the two SNOMED&nbsp;CT concept. 
	 * Returns {@code true} if a relationship between the source and destination concepts given by their unique ID exists.
	 * Otherwise returns with {@code false}.  
	 * 
	 * @param sourceConceptId the source concept ID.
	 * @param destinationConceptId the destination concept ID.
	 * @param relationshipTypeId the relation type concept ID.
	 * @return return {@code true}, if there is a relationship between the two concepts, otherwise returns with {@code false}.
	 */
	boolean hasRelationship(final long sourceConceptId, final long destinationConceptId, final long relationshipTypeId);

	/**
	 * Checks whether a relationship with the given relationship type exists between the two SNOMED&nbsp;CT concept. 
	 * Returns {@code true} if a relationship between the source and destination concepts given by their unique ID exists.
	 * Otherwise returns with {@code false}.  
	 * 
	 * @param sourceConceptId the source concept ID.
	 * @param destinationConceptId the destination concept ID.
	 * @param relationshipTypeId the relation type concept ID.
	 * @return return {@code true}, if there is a relationship between the two concepts, otherwise returns with {@code false}.
	 */	
	boolean hasRelationship(final String sourceConceptId, final String destinationConceptId, final String relationshipTypeId);

	/**
	 * Returns the owner SNOMED&nbsp;CT concept ID associated with the given SNOMED&nbsp;CT description ID.
	 * Returns with {@code null} if not found.
	 * 
	 * @param descriptionId the unique ID of the SNOMED&nbsp;CT description.
	 * @return concept ID the particular description belongs to.
	 */
	@Nullable String getConceptId(final String descriptionId);

	/**
	 * Returns with an array all the description terms of the concept.
	 * 
	 * @param conceptId SNOMED CT id
	 * @return array of description terms.
	 */
	String[] getDescriptionTerms(final String conceptId);

	/**
	 * Returns with an array of description terms for a particular SNOMED&nbsp;CT concept given by its ID where the 
	 * description type concept ID matches with the specified one.
	 * 
	 * @param conceptId SNOMED&nbsp;CT concept ID.
	 * @param descriptionTypeConceptId the description type concept ID
	 * @return an array of description terms.
	 */
	String[] getDescriptionTerms(final long conceptId, final long descriptionTypeConceptId);

	/**
	 * Returns with all active {@link SnomedDescriptionFragment description}s for a concept. 
	 * @param conceptId the unique ID of the concept.
	 * @return a collection of all active {@link SnomedDescriptionFragment description}s of the concept.
	 */
	Collection<SnomedDescriptionFragment> getAllDescriptionsForConcept(final String conceptId);
	
	/**
	 * Returns with all active {@link SnomedDescriptionFragment description}s of a concept from a given language.
	 * @param conceptId the unique concept ID.
	 * @param languageRefSetId the identifier concept ID of the language reference set.
	 * @return a collection of all active {@link SnomedDescriptionFragment description}s of a concept from a given language.
	 */
	Collection<SnomedDescriptionFragment> getAllDescriptionsForConcept(final String conceptId, final String languageRefSetId);
	
	/**
	 * Returns with a collection of active {@link SnomedDescriptionFragment description}s of a concept based on the given description type concept ID.
	 * @param conceptId the concept ID.
	 * @param descriptionTypeId the description type concept ID. 
	 * @return a collection of active description of a concept from a given description type.
	 */
	Collection<SnomedDescriptionFragment> getDescriptionsForConcept(final String conceptId, final String descriptionTypeId);

	/**
	 * Returns with a collection of active {@link SnomedDescriptionFragment description}s of a concept based on the given description type concept ID from a 
	 * given language.
	 * @param conceptId the concept ID.
	 * @param descriptionTypeId the description type concept ID.
	 * @param languageRefSetId the identifier concept ID of the language type reference set. 
	 * @return a collection of active description of a concept from a given description type from a given language.
	 */
	Collection<SnomedDescriptionFragment> getDescriptionsForConcept(final String conceptId, final String descriptionTypeId, final String languageRefSetId);
	
	/**
	 * Returns with the preferred term of the concept.
	 * @param conceptId the concept ID.
	 * @return the the preferred {@link SnomedDescriptionFragment description} of a concept.
	 */
	SnomedDescriptionFragment getPreferredTermForConcept(final String conceptId);
	
	/**
	 * Returns with the preferred term of the concept from a given language.
	 * @param conceptId the concept identifier.
	 * @param languageRefSetId the identifier concept ID of the language type reference set.
	 * @return the preferred description of a concept from a given language.
	 */
	SnomedDescriptionFragment getPreferredTermForConcept(final String conceptId, final String languageRefSetId);

	/**
	 * Returns with the previous preferred description of the concept if it has changed.
	 * <p>This method will return with {@code null} if:
	 * <ul>
	 * <li>Concept PT has not changed between the current and the most recent version.</li>
	 * <li>The concept did not have a PT on the given point of time.</li>
	 * </ul> 
	 * @param conceptId the concept ID.
	 * @return the previous preferred description of the concept if has changed. If not changed. It returns with {@code null}.
	 */
	SnomedDescriptionFragment getPreviousPreferredTerm(final String conceptId);

	/**
	 * Returns with the previous preferred description from the given version of the concept if it has changed.
	 * <p>This method will return with {@code null} if:
	 * <ul>
	 * <li>Previous version does not exist.</li>
	 * <li>Concept PT has not changed between the current and the given version.</li>
	 * <li>The concept did not have a PT on the given point of time.</li>
	 * </ul>
	 * @param conceptId the concept ID.
	 * @return one of the preferred description from a given version of the concept if has changed. If not changed. It returns with {@code null}.
	 */
	SnomedDescriptionFragment getPreviousPreferredTerm(final String conceptId, final String previousVersionId);
	
	/**
	 * Returns with all active and accepted {@link SnomedDescriptionFragment description}s for a concept.
	 * <p><b>NOTE:&nbsp;</b>The returning does not contain preferred descriptions such as fully specified name and/or preferred terms.
	 * @param conceptId the unique ID of the concept.
	 * @return a collection of all active {@link SnomedDescriptionFragment description}s of the concept. Preferred descriptions such as PT and/or FSN are excluded.
	 */
	Collection<SnomedDescriptionFragment> getAllAcceptedDescriptionsForConcept(final String conceptId);
	
	/**
	 * Returns with all active and accepted {@link SnomedDescriptionFragment description}s of a concept from a given language.
	 * <p><b>NOTE:&nbsp;</b>The returning does not contain preferred descriptions such as fully specified name and/or preferred terms.
	 * @param conceptId the unique concept ID.
	 * @param languageRefSetId the identifier concept ID of the language reference set.
	 * @return a collection of all active {@link SnomedDescriptionFragment description}s of a concept from a given language. Preferred descriptions such as PT and/or FSN are excluded.
	 */
	Collection<SnomedDescriptionFragment> getAllAcceptedDescriptionsForConcept(final String conceptId, final String languageRefSetId);
	
	/**
	 * Returns with a collection of active and accepted {@link SnomedDescriptionFragment description}s of a concept based on the given description type concept ID.
	 * <p><b>NOTE:&nbsp;</b>The returning does not contain preferred descriptions such as fully specified name and/or preferred terms.
	 * @param conceptId the concept ID.
	 * @param descriptionTypeId the description type concept ID. 
	 * @return a collection of active description of a concept from a given description type. Preferred descriptions such as PT and/or FSN are excluded.
	 */
	Collection<SnomedDescriptionFragment> getAcceptedDescriptionsForConcept(final String conceptId, final String descriptionTypeId);
	
	/**
	 * Returns with a collection of active and accepted {@link SnomedDescriptionFragment description}s of a concept based on the given description type concept ID from a 
	 * given language.
	 * <p><b>NOTE:&nbsp;</b>The returning does not contain preferred descriptions such as fully specified name and/or preferred terms.
	 * @param conceptId the concept ID.
	 * @param descriptionTypeId the description type concept ID.
	 * @param languageRefSetId the identifier concept ID of the language type reference set. 
	 * @return a collection of active description of a concept from a given description type from a given language. Preferred descriptions such as PT and/or FSN are excluded.
	 */
	Collection<SnomedDescriptionFragment> getAcceptedDescriptionsForConcept(final String conceptId, final String descriptionTypeId, final String languageRefSetId);
	
	/**
	 * Returns {@code true} if the SNOMED&nbsp;CT concept specified by its unique ID has a preferred term. Otherwise returns with {@code false}, 
	 * @param conceptId the unique ID of the SNOMED&nbsp;CT concept.
	 * @return {@code true} if the concept has preferred term, otherwise false.
	 */
	boolean isPreferredTermExists(final long conceptId);

	/**
	 * Returns with the preferred term of a SNOMED&nbsp;CT concept.
	 * @param conceptId the ID of the concept.
	 * @return the preferred term of the concept.
	 */
	String getPreferredTerm(final long conceptId);

	/**
	 * Returns the preferred terms of a concept.
	 * @param conceptId the unique SNOMED&nbsp;CT ID of the concept.
	 * @return the preferred term of a SNOMED&nbsp;CT concept identified by its unique ID.
	 */
	String getPreferredTerm(final String conceptId);

	/**
	 * Returns with the term of the fully specified name of a SNOMED&nbsp;CT concept.
	 * @param conceptId the unique ID of the concept.
	 * @return the fully specified name of a concept.
	 */
	String getFullySpecifiedName(final long conceptId);

	/**
	 * Returns with the fully specified name of a SNOMED&nbsp;CT concept as a description.
	 * @param conceptId the unique ID of the SNOMED&nbsp;CT concept.
	 * @return the fully specified name description of a concept.
	 */
	Description getFsnDescription(final long conceptId);

	/**
	 * Returns with the terms of all the synonym type descriptions of a SNOMED&nbsp;CT concept.
	 * @param conceptId the unique ID of a concept.
	 * @return an array of synonym terms.
	 */
	String[] getSynonyms(final long conceptId);

	/**
	 * Returns {@code true} if the term of the given concept exists, otherwise returns with {@code false}.
	 * 
	 * @param conceptId the unique ID of the SNOMED&nbsp;CT concept.
	 * @param termToMatch the term of one of the descriptions to match against.
	 * @param caseSensitivity the description case significance settings.
	 * @return {@code true} if the term of specified concept exists, otherwise returns {@code false}.
	 */
	boolean isDescriptionExist(final String conceptId, final String termToMatch, final CaseSignificance caseSensitivity);

	/**
	 * Returns {@code true} if the specific term of the given concept exists. Otherwise returns with {@code false}.
	 * 
	 * @param conceptId unique ID of the SNOMED&nbsp;CT concept. 
	 * @param termToMatch the term of one of the descriptions to match against.
	 * @param caseSensitivity the description case significance settings.
	 * @param descriptionTypeConceptId the description type concept ID.
	 * @return {@code true} if the term of specified concept exists, otherwise returns {@code false}.
	 */
	boolean isDescriptionExist(final long conceptId, String termToMatch, final CaseSignificance caseSensitivity, final long descriptionTypeConceptId);

	/**
	 * Returns {@code true} if the specific term of the given concept exists. Otherwise returns with {@code false}.
	 * 
	 * @param conceptId unique ID of the SNOMED&nbsp;CT concept. 
	 * @param termToMatch the term of one of the descriptions to match against.
	 * @param caseSensitivity the description case significance settings.
	 * @param descriptionTypeConceptId the description type concept ID.
	 * @return {@code true} if the term of specified concept exists, otherwise returns {@code false}.
	 */
	boolean isDescriptionExist(final String conceptId, final String termToMatch, final CaseSignificance caseSensitivity, final String descriptionTypeConceptId);

	/**
	 * Returns {@code true} two description terms matches based on the desired case significance.
	 * @param descriptionTerm the term to check.
	 * @param termToMatch the term to match.
	 * @param caseSensitivity the desired case significance settings.
	 * @return {@code true} if the two specified descriptions match, otherwise {@code false}.
	 */
	boolean descriptionTermMatches(final String descriptionTerm, final String termToMatch, final CaseSignificance caseSensitivity);

	/**
	 * This method returns with an ordered list of SNOMED&nbsp;CT descriptions associated with the 
	 * specified SNOMED&nbsp;CT concept identified by its unique ID.
	 * <p>
	 * The returning list will contain the elements as described below:
	 * <ul>
	 * <li>The list will not contain descriptions with <b>inactive</b> state.</li>
	 * <li>The first element of the list will represent the preferred term of the SNOMED&nbsp;CT concept.</li>
	 * <li>The additional descriptions' (if any) having the description type concept as specified with the <i>descriptionTypeId</i> argument.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * This method returns with an empty list if one of the following conditions are true:
	 * <ul>
	 * <li>The SNOMED&nbsp;CT concept does not exist in the ontology.</li>
	 * <li>The specified <b>conceptId</b> is {@code null}.</li>
	 * <li>The specified <b>descriptionTypeId</b> is {@code null}.</li>
	 * <li>The specified description type concept ID is the {@code |900000000000003001|Fully specified name|} concept. 
	 * </ul>
	 * </p>
	 * @param conceptId the unique identifier of the SNOMED&nbsp;CT concept.
	 * @param descriptionTypeId the unique ID of the description type SNOMED&nbsp;CT concept.
	 * @param duplicatePreferredTerm {@code true} if the preferred term should be included multiple time in the results. Otherwise {@code false}.
	 * @return an ordered list of SNOMED&nbsp;CT descriptions.
	 */
	List<IComponent<String>> getDescriptionsWithPreferredTerm(final String conceptId, final String descriptionTypeId, final boolean duplicatePreferredTerm);

	/**
	 * Returns with the SNOMED&nbsp;CT concept identified by its unique ID.
	 * <br>This method may return with {@code null} if error occurred while creating the editing context or
	 * concept does not exists in the database with the specified SNOMED CT ID.
	 * 
	 * @param conceptId the unique ID of the concept.
	 * @return the SNOMED&nbsp;CT concept. 
	 */
	@Nullable Concept getConcept(final long conceptId);

	/**
	 * Returns with the SNOMED&nbsp;CT concept identified by its unique ID.
	 * <br>This method may return with {@code null} if error occurred while creating the editing context or
	 * concept does not exists in the database with the specified SNOMED CT ID.
	 * 
	 * @param conceptId the unique ID of the concept.
	 * @return the SNOMED&nbsp;CT concept. 
	 */
	@Nullable Concept getConceptById(final String conceptId);

	/**
	 * Return true if the SNOMED CT concept exist identified by its unique ID.
	 * 
	 * @param conceptId the unique ID of the concept.
	 * @return {@code true} if the SNOMED&nbsp;CT concept exists with the given ID, otherwise {@code false}.
	 */
	boolean isConceptExist(final long conceptId);

	/**
	 * Return true if the SNOMED CT concept exist identified by its unique ID.
	 * 
	 * @param conceptId the unique ID of the concept.
	 * @return {@code true} if the SNOMED&nbsp;CT concept exists with the given ID, otherwise {@code false}.
	 */
	boolean isConceptExist(final String conceptId);
	
	/**
	 * Generates and returns with a brand new non-existing SNOMED&nbsp;CT concept ID.
	 * @return a new non-existing concept ID.
	 */
	String generateNewConceptId();
	
	/**
	 * Generates and returns with a brand new non-existing SNOMED&nbsp;CT description ID.
	 * @return a new non-existing description ID.
	 */
	String generateNewDescriptionId();
	
	/**
	 * Generates and returns with a brand new non-existing SNOMED&nbsp;CT relationship ID.
	 * @return a new non-existing relationship ID.
	 */
	String generateNewRelationshipId();

}