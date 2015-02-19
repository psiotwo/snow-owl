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
package com.b2international.snowowl.datastore.server.snomed.filteredrefset;

import static com.google.common.collect.Sets.newHashSet;

import java.io.IOException;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ReferenceManager;
import org.apache.lucene.search.TermQuery;

import bak.pcj.LongCollection;
import bak.pcj.LongIterator;
import bak.pcj.map.LongKeyMap;
import bak.pcj.set.LongOpenHashSet;
import bak.pcj.set.LongSet;

import com.b2international.commons.arrays.BidiMapWithInternalId;
import com.b2international.snowowl.core.api.IBranchPath;
import com.b2international.snowowl.core.api.index.CommonIndexConstants;
import com.b2international.snowowl.core.api.index.IndexException;
import com.b2international.snowowl.core.date.EffectiveTimes;
import com.b2international.snowowl.datastore.index.DocIdCollector;
import com.b2international.snowowl.datastore.index.DocIdCollector.DocIdsIterator;
import com.b2international.snowowl.datastore.index.IndexUtils;
import com.b2international.snowowl.datastore.server.index.IndexServerService;
import com.b2international.snowowl.datastore.server.snomed.index.SnomedServerTerminologyBrowser;
import com.b2international.snowowl.snomed.datastore.SnomedConceptIndexEntry;
import com.b2international.snowowl.snomed.datastore.browser.SnomedIndexBrowserConstants;
import com.b2international.snowowl.snomed.datastore.filteredrefset.IRefSetMemberNode;
import com.b2international.snowowl.snomed.datastore.filteredrefset.NewRefSetMemberNode;
import com.b2international.snowowl.snomed.datastore.filteredrefset.RegularRefSetMemberNode;
import com.google.common.collect.ImmutableSet;

/**
 * 
 */
public class CollectReferencedComponentMapRunnable implements Runnable {
	
	private static final Set<String> REFERENCED_COMPONENT_ID_FIELD_TO_LOAD = ImmutableSet.of(SnomedIndexBrowserConstants.REFERENCE_SET_MEMBER_REFERENCED_COMPONENT_ID);
	
	private static final Set<String> ACTIVE_MEMBER_FIELDS_TO_LOAD = ImmutableSet.of(
			SnomedIndexBrowserConstants.REFERENCE_SET_MEMBER_UUID,
			SnomedIndexBrowserConstants.COMPONENT_STORAGE_KEY,
			SnomedIndexBrowserConstants.REFERENCE_SET_MEMBER_UUID,
			SnomedIndexBrowserConstants.REFERENCE_SET_MEMBER_MODULE_ID,
			SnomedIndexBrowserConstants.COMPONENT_LABEL,
			SnomedIndexBrowserConstants.REFERENCE_SET_MEMBER_EFFECTIVE_TIME);
	
	private static final Set<String> ALL_MEMBER_FIELDS_TO_LOAD = ImmutableSet.<String>builder()
			.addAll(ACTIVE_MEMBER_FIELDS_TO_LOAD)
			.add(SnomedIndexBrowserConstants.COMPONENT_ACTIVE)
			.build();
	
	private final int maxDoc;
	private final LongCollection conceptIds;
	private final BidiMapWithInternalId<IRefSetMemberNode, IRefSetMemberNode> refSetMemberNodes;
	private final LongKeyMap referencedComponentToNodeMap;
	private final boolean includeInactive;
	private final IBranchPath branchPath;
	private final IndexServerService<?> indexService;
	private final long refSetId;
	private final SnomedServerTerminologyBrowser terminologyBrowser;

	public CollectReferencedComponentMapRunnable(final int maxDoc, final LongCollection conceptIds,
			final BidiMapWithInternalId<IRefSetMemberNode, IRefSetMemberNode> refSetMemberNodes,
			final LongKeyMap referencedComponentToNodeMap, final boolean includeInactive, final IBranchPath branchPath,
			final IndexServerService<?> indexService, final long refSetId, final SnomedServerTerminologyBrowser terminologyBrowser) {
		
		this.maxDoc = maxDoc;
		this.conceptIds = conceptIds;
		this.refSetMemberNodes = refSetMemberNodes;
		this.referencedComponentToNodeMap = referencedComponentToNodeMap;
		this.includeInactive = includeInactive;
		this.branchPath = branchPath;
		this.indexService = indexService;
		this.refSetId = refSetId;
		this.terminologyBrowser = terminologyBrowser;
	}

	@Override
	public void run() {
		
		final BooleanQuery memberQuery = new BooleanQuery(true);
		memberQuery.add(new TermQuery(new Term(SnomedIndexBrowserConstants.REFERENCE_SET_MEMBER_REFERENCE_SET_ID, IndexUtils.longToPrefixCoded(refSetId))), Occur.MUST);
		
		if (!includeInactive) {
			//exclude inactive members
			memberQuery.add(new TermQuery(new Term(SnomedIndexBrowserConstants.COMPONENT_ACTIVE, IndexUtils.intToPrefixCoded(1))), Occur.MUST);
		}

		final LongSet visitedIds = new LongOpenHashSet();
		final DocIdCollector docIdCollector = DocIdCollector.create(maxDoc);
		IndexSearcher searcher = null;
		ReferenceManager<IndexSearcher> manager = null;

		try {

			indexService.search(branchPath, memberQuery, docIdCollector);
			
			manager = indexService.getManager(branchPath);
			searcher = manager.acquire();
			
			for (final DocIdsIterator itr = docIdCollector.getDocIDs().iterator(); itr.next(); /* empty */) {

				final int docId = itr.getDocID();
				Document doc = searcher.doc(docId, REFERENCED_COMPONENT_ID_FIELD_TO_LOAD);
				final long referencedComponentId = Long.parseLong(doc.get(SnomedIndexBrowserConstants.REFERENCE_SET_MEMBER_REFERENCED_COMPONENT_ID));

				if (conceptIds.contains(referencedComponentId)) {
					doc = searcher.doc(docId, includeInactive ? ALL_MEMBER_FIELDS_TO_LOAD : ACTIVE_MEMBER_FIELDS_TO_LOAD);

					final boolean active = includeInactive ? IndexUtils.getBooleanValue(doc.getField(SnomedIndexBrowserConstants.COMPONENT_ACTIVE)) : true;
					final long effectiveTime = IndexUtils.getLongValue(doc.getField(SnomedIndexBrowserConstants.REFERENCE_SET_MEMBER_EFFECTIVE_TIME));
					final boolean released = EffectiveTimes.UNSET_EFFECTIVE_TIME != effectiveTime;
					final String uuid = doc.get(SnomedIndexBrowserConstants.REFERENCE_SET_MEMBER_UUID);
					final String label = doc.get(SnomedIndexBrowserConstants.COMPONENT_LABEL);
					final long storageKey = IndexUtils.getLongValue(doc.getField(CommonIndexConstants.COMPONENT_STORAGE_KEY));
					final long moduleId = IndexUtils.getLongValue(doc.getField(SnomedIndexBrowserConstants.REFERENCE_SET_MEMBER_MODULE_ID));

					final IRefSetMemberNode node = new RegularRefSetMemberNode(referencedComponentId, 
							label, 
							uuid, 
							effectiveTime, 
							moduleId, 
							storageKey, 
							active, 
							released);
					
					refSetMemberNodes.put(node, node);
					put(referencedComponentId, node);
					
					visitedIds.add(referencedComponentId);
				}
			}
			
			conceptIds.removeAll(visitedIds);
			visitedIds.clear();
			
			for (final LongIterator itr = conceptIds.iterator(); itr.hasNext(); /* empty */) {
				final long referencedComponentId = itr.next();
				final IRefSetMemberNode node = new NewRefSetMemberNode(referencedComponentId, getConceptLabel(referencedComponentId));
				refSetMemberNodes.put(node, node);
				put(referencedComponentId, node);
			}

		} catch (final IOException e) {
			throw new IndexException(e);
		} finally {
			if (null != searcher && null != manager) {
				try {
					manager.release(searcher);
				} catch (final IOException e) {
					throw new IndexException(e);
				}
			}
		}
	}

	private String getConceptLabel(final long referencedComponentId) {
		final SnomedConceptIndexEntry concept = terminologyBrowser.getConcept(branchPath, Long.toString(referencedComponentId));
		return (null == concept) ? null : concept.getLabel();
	}
	
	@SuppressWarnings("unchecked")
	private void put(final long conceptId, final IRefSetMemberNode node) {
		Set<IRefSetMemberNode> values = (Set<IRefSetMemberNode>) referencedComponentToNodeMap.get(conceptId);

		if (values == null) {
			values = newHashSet();
			referencedComponentToNodeMap.put(conceptId, values);
		}
		
		values.add(node);
	}
}