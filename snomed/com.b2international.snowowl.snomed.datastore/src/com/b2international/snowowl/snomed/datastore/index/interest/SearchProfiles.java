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
package com.b2international.snowowl.snomed.datastore.index.interest;

import com.b2international.snowowl.snomed.SnomedConstants.Concepts;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Contains a built-in set of profiles for use with {@link SearchProfilePreferences}.
 * 
 */
public abstract class SearchProfiles {

	public static final SearchProfile DEFAULT = new SearchProfile("Default", false, ImmutableList.of(
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "308916002", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "260787004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123037004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_NAMESPACE, "373872000", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "272379006", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447562003", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "71388002", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "404684003", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447563008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "106237007", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000012004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000207008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "410607006", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "370115009", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "446608001", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "254291000", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "900000000000441003", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "48176007", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "363787002", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "373873005", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "419891008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123038009", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "78621006", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "243796009", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "105590001", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "362981000", SearchProfileInterest.AVERAGE)));

	private static final SearchProfile ENDOCRINOLOGY_UROLOGY_NEPHROLOGY = new SearchProfile("Endocrinology, Urology and Nephrology", false, ImmutableList.of(
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "106237007", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_NAMESPACE, "373872000", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "308916002", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447562003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "404684003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "272379006", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "105590001", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "71388002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "260787004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "410607006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447563008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000012004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000207008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.ENDOCRINOLOGY_UROLOGY_NEPHROLOGY_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123037004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "446608001", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123038009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "363787002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "373873005", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "243796009", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "900000000000441003", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "370115009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "78621006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "362981000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "254291000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "48176007", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "419891008", SearchProfileInterest.BELOW_AVERAGE)));

	private static final SearchProfile CLINICIAN = new SearchProfile("Clinician (General or multiple specialties)", false, ImmutableList.of(
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "362981000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "419891008", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.NEUROLOGY_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "105590001", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.MENTAL_HEALTH_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "370115009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "106237007", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123037004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "410607006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "260787004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "363787002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.OPHTHALMOLOGY_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447562003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "373873005", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "48176007", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.MUSCULOSKELETAL_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "71388002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "404684003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123038009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "272379006", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "254291000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.ENDOCRINOLOGY_UROLOGY_NEPHROLOGY_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "308916002", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.CARDIOLOGY_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.HEMATOLOGY_ONCOLOGY_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "78621006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000207008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "446608001", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_NAMESPACE, "373872000", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "243796009", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000012004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447563008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "900000000000441003", SearchProfileInterest.BELOW_AVERAGE)));

	private static final SearchProfile MENTAL_HEALTH = new SearchProfile("Mental health", false, ImmutableList.of(
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "106237007", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_NAMESPACE, "373872000", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "308916002", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447562003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "404684003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "272379006", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "105590001", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "71388002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "260787004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "410607006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447563008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000012004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000207008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.MENTAL_HEALTH_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123037004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "446608001", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123038009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "363787002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "373873005", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "243796009", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "900000000000441003", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "370115009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "78621006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "362981000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "254291000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "48176007", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "419891008", SearchProfileInterest.BELOW_AVERAGE)));

	private static final SearchProfile HEMATOLOGY_ONCOLOGY = new SearchProfile("Hematology and Oncology", false, ImmutableList.of(
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "106237007", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_NAMESPACE, "373872000", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "308916002", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447562003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "404684003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "272379006", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "105590001", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "71388002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "260787004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "410607006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447563008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000012004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000207008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.HEMATOLOGY_ONCOLOGY_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123037004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "446608001", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123038009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "363787002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "373873005", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "243796009", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "900000000000441003", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "370115009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "78621006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "362981000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "254291000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "48176007", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "419891008", SearchProfileInterest.BELOW_AVERAGE)));

	private static final SearchProfile OPHTALMOLOGY = new SearchProfile("Ophthalmology", false, ImmutableList.of(
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "308916002", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "260787004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123037004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.OPHTHALMOLOGY_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_NAMESPACE, "373872000", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "272379006", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447562003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "71388002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "404684003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447563008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "106237007", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000012004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000207008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "410607006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "370115009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "446608001", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "254291000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "900000000000441003", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "48176007", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "363787002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "373873005", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "419891008", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123038009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "78621006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "243796009", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "105590001", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "362981000", SearchProfileInterest.BELOW_AVERAGE)));

	private static final SearchProfile CARDIOLOGY = new SearchProfile("Cardiology", false, ImmutableList.of(
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "373873005", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123038009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "363787002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123037004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "446608001", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000207008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.CARDIOLOGY_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447563008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000012004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "900000000000441003", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "243796009", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "370115009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "78621006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "362981000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "254291000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "48176007", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "419891008", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447562003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "308916002", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "410607006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_NAMESPACE, "373872000", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "106237007", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "105590001", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "272379006", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "404684003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "260787004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "71388002", SearchProfileInterest.ABOVE_AVERAGE)));

	private static final SearchProfile MUSCULOSKELETAL = new SearchProfile("Musculoskeletal", false, ImmutableList.of(
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "106237007", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_NAMESPACE, "373872000", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "308916002", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447562003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "404684003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "272379006", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "105590001", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "71388002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "260787004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "410607006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447563008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000012004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000207008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.MUSCULOSKELETAL_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123037004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "446608001", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123038009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "363787002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "373873005", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "243796009", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "900000000000441003", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "370115009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "78621006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "362981000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "254291000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "48176007", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "419891008", SearchProfileInterest.BELOW_AVERAGE)));

	private static final SearchProfile NEUROLOGY = new SearchProfile("Neurology", false, ImmutableList.of(
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "106237007", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_NAMESPACE, "373872000", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "308916002", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447562003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "404684003", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "272379006", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "105590001", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "71388002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "260787004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "410607006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "447563008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000012004", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.WITHIN_A_MODULE, "900000000000207008", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.REFERENCE_SET_MEMBERS, Concepts.NEUROLOGY_REFERENCE_SET, SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123037004", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.MAPPING_SOURCE_CONCEPTS, "446608001", SearchProfileInterest.AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "123038009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "363787002", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "373873005", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "243796009", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "900000000000441003", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "370115009", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "78621006", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "362981000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "254291000", SearchProfileInterest.BELOW_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "48176007", SearchProfileInterest.ABOVE_AVERAGE),
			new SearchProfileRule(SearchProfileDomain.DESCENDANTS_OF_CONCEPT, "419891008", SearchProfileInterest.BELOW_AVERAGE)));

	public static final ImmutableSet<SearchProfile> BUILT_IN = ImmutableSet.of(
			DEFAULT,
			ENDOCRINOLOGY_UROLOGY_NEPHROLOGY,
			CLINICIAN,
			MENTAL_HEALTH,
			HEMATOLOGY_ONCOLOGY,
			OPHTALMOLOGY,
			CARDIOLOGY,
			MUSCULOSKELETAL,
			NEUROLOGY);

	private SearchProfiles() {
		// Prevent instantiation
	}
}