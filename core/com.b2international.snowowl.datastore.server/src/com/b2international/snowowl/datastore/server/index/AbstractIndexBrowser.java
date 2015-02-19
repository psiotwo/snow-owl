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
package com.b2international.snowowl.datastore.server.index;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.unmodifiableSet;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;

import com.b2international.commons.ClassUtils;
import com.b2international.commons.CompareUtils;
import com.b2international.snowowl.core.CoreTerminologyBroker;
import com.b2international.snowowl.core.api.ComponentIdAndLabel;
import com.b2international.snowowl.core.api.IBranchPath;
import com.b2international.snowowl.core.api.IComponent;
import com.b2international.snowowl.core.api.index.CommonIndexConstants;
import com.b2international.snowowl.core.api.index.IIndexEntry;
import com.b2international.snowowl.core.api.index.IIndexService;
import com.b2international.snowowl.datastore.ICodeSystem;
import com.b2international.snowowl.datastore.ICodeSystemVersion;
import com.b2international.snowowl.datastore.InternalTerminologyRegistryService;
import com.b2international.snowowl.datastore.index.DocIdCollector.DocIdsIterator;
import com.b2international.snowowl.datastore.index.IndexUtils;
import com.b2international.snowowl.datastore.server.TerminologyRegistryServiceWrapper;
import com.b2international.snowowl.terminologyregistry.core.index.TerminologyRegistryIndexConstants;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Sets;

/**
 * Abstract base class for index based terminology and statement browser implementations.
 * 
 *
 * @param <E> the type of the result object
 */
public abstract class AbstractIndexBrowser<E extends IIndexEntry> implements InternalTerminologyRegistryService {

	protected static final int LIMIT = 1000000;
	protected final IndexServerService<E> service;
	
	public AbstractIndexBrowser(final IIndexService<?> service) {
		this.service = getService(service);
		if (service instanceof IndexServerService<?>) {
			InternalTerminologyRegistryServiceRegistry.INSTANCE.register(((IndexServerService<?>) service).getRepositoryUuid(), this);
		}
	}

	private static final Set<String> ID_LABEL_FIELD_TO_LOAD = Collections.unmodifiableSet(Sets.newHashSet(CommonIndexConstants.COMPONENT_ID, CommonIndexConstants.COMPONENT_LABEL));
	private static final Set<String> COMPONENT_TYPE_FIELD_TO_LOAD = unmodifiableSet(newHashSet(CommonIndexConstants.COMPONENT_TYPE));
	
	/**
	 * Returns with the terminology dependent unique ID and the human readable label of a component specified by its unique storage key.
	 * <br>This method could return with {@code null} if the component does not exist in the store on the specified branch.  
	 * @param branchPath the branch path.
	 * @param storageKey the primary storage key of the component
	 * @return the {@link ComponentIdAndLabel ID and label pair} of a component. May return with {@code null} if the component does not exist in store. 
	 */
	@Nullable public ComponentIdAndLabel getComponentIdAndLabel(final IBranchPath branchPath, final long storageKey) {
		
		Preconditions.checkNotNull(branchPath, "Branch path argument cannot be null.");
		
		final Query query = new TermQuery(IndexUtils.getStorageKeyTerm(storageKey));
		
		final TopDocs topDocs = service.search(branchPath, query, 1);
		
		if (null == topDocs || CompareUtils.isEmpty(topDocs.scoreDocs)) {
			return null; //XXX null object pattern?
		}
		
		final Document doc = service.document(branchPath, topDocs.scoreDocs[0].doc, ID_LABEL_FIELD_TO_LOAD);
		
		return new ComponentIdAndLabel(
				Preconditions.checkNotNull(doc.get(CommonIndexConstants.COMPONENT_LABEL), "Component label was null for component. CDO ID: " + storageKey),
				Preconditions.checkNotNull(doc.get(CommonIndexConstants.COMPONENT_ID), "Component ID was null for component. CDO ID: " + storageKey)); 
		
	}
	
	/**
	 * Returns with the application specific unique component type of a component given with its unique storage key.
	 * This method will return with {@link CoreTerminologyBroker#UNSPECIFIED_NUMBER_SHORT unspecified} if the component does not 
	 * exist on the given branch or does not have any associated concrete component type.
	 * @param branchPath the branch path.
	 * @param storageKey the storage key of the component.
	 * @return the component type as a short.
	 */
	public short getComponentType(final IBranchPath branchPath, final long storageKey) {
		
		final Query query = new TermQuery(IndexUtils.getStorageKeyTerm(storageKey));
		
		final TopDocs topDocs = service.search(checkNotNull(branchPath, "branchPath"), query, 1);
		
		if (null == topDocs || CompareUtils.isEmpty(topDocs.scoreDocs)) {
			return CoreTerminologyBroker.UNSPECIFIED_NUMBER_SHORT;
		}
		
		final Document doc = service.document(branchPath, topDocs.scoreDocs[0].doc, COMPONENT_TYPE_FIELD_TO_LOAD);
		
		final IndexableField field = doc.getField(CommonIndexConstants.COMPONENT_TYPE);
		return null == field ? CoreTerminologyBroker.UNSPECIFIED_NUMBER_SHORT : IndexUtils.getShortValue(field);
		
	}
	
	public boolean isTerminologyAvailable(final IBranchPath branchPath) {
		final BooleanQuery notVersionQuery = new BooleanQuery(true);
		
		final PrefixQuery versionIdQuery = new PrefixQuery(new Term(TerminologyRegistryIndexConstants.VERSION_VERSION_ID));
		versionIdQuery.setRewriteMethod(MultiTermQuery.CONSTANT_SCORE_FILTER_REWRITE);
		notVersionQuery.add(versionIdQuery, Occur.MUST_NOT);
		
		final PrefixQuery codeSystemShortNameQuery = new PrefixQuery(new Term(TerminologyRegistryIndexConstants.SYSTEM_SHORT_NAME));
		codeSystemShortNameQuery.setRewriteMethod(MultiTermQuery.CONSTANT_SCORE_FILTER_REWRITE);
		notVersionQuery.add(codeSystemShortNameQuery, Occur.MUST_NOT);
		
		notVersionQuery.add(new MatchAllDocsQuery(), Occur.MUST);
		
		return service.getHitCount(branchPath, notVersionQuery, null) > 0;
	}

	@Override
	public Collection<ICodeSystem> getCodeSystems(final IBranchPath branchPath) {
		return new TerminologyRegistryServiceWrapper(service).getCodeSystems(checkNotNull(branchPath, "branchPath"));
	}

	@Override
	public Collection<ICodeSystemVersion> getCodeSystemVersions(final IBranchPath branchPath, final String codeSystemShortName) {
		return new TerminologyRegistryServiceWrapper(service).getCodeSystemVersions(branchPath, codeSystemShortName);
	}

	@Override
	public Collection<ICodeSystemVersion> getCodeSystemVersionsFromRepositoryWithInitVersion(final IBranchPath branchPath, final String repositoryUuid) {
		return new TerminologyRegistryServiceWrapper(service).getCodeSystemVersionsFromRepositoryWithInitVersion(branchPath, repositoryUuid);
	}
	
	@Override
	public Collection<ICodeSystemVersion> getCodeSystemVersionsFromRepository(final IBranchPath branchPath, final String repositoryUuid) {
		return new TerminologyRegistryServiceWrapper(service).getCodeSystemVersionsFromRepository(branchPath, repositoryUuid);
	}
	
	@Override
	public ICodeSystem getCodeSystemByShortName(final IBranchPath branchPath, final String codeSystemShortName) {
		return new TerminologyRegistryServiceWrapper(service).getCodeSystemByShortName(branchPath, codeSystemShortName);
	}

	@Override
	public ICodeSystem getCodeSystemByOid(final IBranchPath branchPath, final String codeSystemOid) {
		return new TerminologyRegistryServiceWrapper(service).getCodeSystemByOid(branchPath, codeSystemOid);
	}

	@Override
	public Map<String, ICodeSystem> getTerminologyComponentIdCodeSystemMap(final IBranchPath branchPath) {
		return new TerminologyRegistryServiceWrapper(service).getTerminologyComponentIdCodeSystemMap(branchPath);
	}

	@Override
	public Map<String, Collection<ICodeSystem>> getTerminologyComponentIdWithMultipleCodeSystemsMap(final IBranchPath branchPath) {
		return new TerminologyRegistryServiceWrapper(service).getTerminologyComponentIdWithMultipleCodeSystemsMap(branchPath);
	}

	@Override
	public String getTerminologyComponentIdByShortName(final IBranchPath branchPath, final String codeSystemShortName) {
		return new TerminologyRegistryServiceWrapper(service).getTerminologyComponentIdByShortName(branchPath, codeSystemShortName);
	}

	@Override
	public String getVersionId(final IBranchPath branchPath, final ICodeSystem codeSystem) {
		return new TerminologyRegistryServiceWrapper(service).getVersionId(branchPath, codeSystem);
	}
	
	/**
	 * Creates a single {@link IComponent component} from the index search results passed in.
	 * @param docs
	 * @return the component created from the first search result
	 */
	protected E createSingleResultObject(final IBranchPath branchPath, final TopDocs docs) {
		if (CompareUtils.isEmpty(docs.scoreDocs)) {
			return null;
		}
		return createResultObject(branchPath, service.document(branchPath, docs.scoreDocs[0].doc, getFieldNamesToLoad()));
	}

	/**
	 * Creates multiple {@link IComponent components} from the index search results passed in.
	 * @param docs
	 * @return the list of components created from the search results
	 */
	protected List<E> createResultObjects(final IBranchPath branchPath, final TopDocs docs) {
		if (CompareUtils.isEmpty(docs.scoreDocs)) {
			return Collections.emptyList();
		}
		final Builder<E> builder = ImmutableList.builder();
		for (final ScoreDoc scoreDoc : docs.scoreDocs) {
			final Document doc = service.document(branchPath, scoreDoc.doc, getFieldNamesToLoad());
			final E component = createResultObject(branchPath, doc);
			builder.add(component);
		}
		return builder.build();
	}
	
	/**
	 * Creates multiple {@link IComponent components} from the index search results passed in.
	 * @param iterator the document ID iterator to use
	 * @return
	 */
	protected List<E> createResultObjects(final IBranchPath branchPath, final DocIdsIterator iterator) {
		final Builder<E> builder = ImmutableList.builder();
		while (iterator.next()) {
			final Document doc = service.document(branchPath, iterator.getDocID(), getFieldNamesToLoad());
			final E component = createResultObject(branchPath, doc);
			builder.add(component);
		}
		return builder.build();
	}
	
	/**
	 * Template method for creating a {@link IComponent component} from a index {@link Document document}.
	 * 
	 * @param doc the index document
	 * @param branchPath the branch path.
	 * @return the component created from the index document
	 */
	protected abstract E createResultObject(final IBranchPath branchPath, final Document doc);
	
	/**
	 * Template method for returning the names of the fields, that need to be loaded from the index to build the {@link IComponent components}.
	 * 
	 * @return the names of the index fields to load
	 */
	protected abstract Set<String> getFieldNamesToLoad();

	protected int getQueryResultCount(final IBranchPath branchPath, final Query query) throws IOException {
		return service.getTotalHitCount(branchPath, query);
	}
	
	/**Returns with the term identifying the component ID. Field name is {@link CommonIndexConstants#COMPONENT_ID} value is the given string.
	 *<br><b>NOTE:&nbsp;</b>clients may override this if e.g. unique component ID field is stored as numeric value and component ID
	 *string has to be prefixcoded into the proper {@link BytesRef}.*/
	protected Term getIdTerm(final String componentId) {
		return new Term(CommonIndexConstants.COMPONENT_ID, componentId);
	}
	
	@SuppressWarnings("unchecked")
	private IndexServerService<E> getService(final IIndexService<?> service) {
		return ClassUtils.checkAndCast(service, IndexServerService.class);
	}
}