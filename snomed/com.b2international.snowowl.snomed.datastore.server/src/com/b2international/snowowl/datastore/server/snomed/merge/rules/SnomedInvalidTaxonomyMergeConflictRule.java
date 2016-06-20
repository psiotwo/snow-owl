/*
 * Copyright 2011-2016 B2i Healthcare Pte Ltd, http://b2i.sg
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
package com.b2international.snowowl.datastore.server.snomed.merge.rules;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.cdo.transaction.CDOTransaction;

import com.b2international.collections.longs.LongCollection;
import com.b2international.snowowl.core.ApplicationContext;
import com.b2international.snowowl.core.api.IBranchPath;
import com.b2international.snowowl.core.merge.MergeConflict;
import com.b2international.snowowl.datastore.BranchPathUtils;
import com.b2international.snowowl.datastore.server.cdo.IMergeConflictRule;
import com.b2international.snowowl.datastore.server.snomed.SnomedMergeConflict;
import com.b2international.snowowl.snomed.datastore.IsAStatementWithId;
import com.b2international.snowowl.snomed.datastore.SnomedStatementBrowser;
import com.b2international.snowowl.snomed.datastore.SnomedTerminologyBrowser;
import com.b2international.snowowl.snomed.datastore.StatementCollectionMode;
import com.b2international.snowowl.snomed.datastore.taxonomy.IncompleteTaxonomyException;
import com.b2international.snowowl.snomed.datastore.taxonomy.InvalidRelationship;
import com.b2international.snowowl.snomed.datastore.taxonomy.SnomedTaxonomyBuilder;
import com.b2international.snowowl.snomed.datastore.taxonomy.SnomedTaxonomyUpdateRunnable;
import com.google.common.collect.ImmutableList;

/**
 * @since 4.7
 */
public class SnomedInvalidTaxonomyMergeConflictRule implements IMergeConflictRule {

	@Override
	public Collection<MergeConflict> validate(CDOTransaction transaction) {
		
		final IBranchPath branchPath = BranchPathUtils.createPath(transaction);
		final ApplicationContext context = ApplicationContext.getInstance();
		final LongCollection conceptIds = context.getService(SnomedTerminologyBrowser.class).getAllConceptIds(branchPath);
		
		List<MergeConflict> conflicts = newArrayList();
		
		for (StatementCollectionMode mode : ImmutableList.of(StatementCollectionMode.STATED_ISA_ONLY, StatementCollectionMode.INFERRED_ISA_ONLY)) {
			try {
				final IsAStatementWithId[] statements = context.getService(SnomedStatementBrowser.class).getActiveStatements(branchPath, mode);
				final SnomedTaxonomyBuilder taxonomyBuilder = new SnomedTaxonomyBuilder(conceptIds, statements);
				new SnomedTaxonomyUpdateRunnable(transaction, taxonomyBuilder, mode.getCharacteristicType()).run();
			} catch (IncompleteTaxonomyException e) {
				for (InvalidRelationship invalidRelationship : e.getInvalidRelationships()) {
					conflicts.add(new SnomedMergeConflict(String.valueOf(invalidRelationship.getMissingConceptId()), null, 
							String.format("Relationship with ID '%s' has a missing %s with ID '%s'", 
									invalidRelationship.getRelationshipId(), invalidRelationship.getMissingConcept(), invalidRelationship.getMissingConceptId())));
				}	
			}
		}
		
		return conflicts;
	}

}
