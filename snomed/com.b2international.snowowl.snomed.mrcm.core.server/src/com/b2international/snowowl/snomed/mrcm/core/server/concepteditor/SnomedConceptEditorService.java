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
package com.b2international.snowowl.snomed.mrcm.core.server.concepteditor;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;

import com.b2international.collections.PrimitiveSets;
import com.b2international.collections.longs.LongSet;
import com.b2international.snowowl.core.ApplicationContext;
import com.b2international.snowowl.core.api.IBranchPath;
import com.b2international.snowowl.core.domain.IComponent;
import com.b2international.snowowl.eventbus.IEventBus;
import com.b2international.snowowl.snomed.core.domain.SnomedConcepts;
import com.b2international.snowowl.snomed.datastore.SnomedTerminologyBrowser;
import com.b2international.snowowl.snomed.datastore.index.entry.SnomedConceptDocument;
import com.b2international.snowowl.snomed.datastore.request.SnomedRequests;
import com.b2international.snowowl.snomed.datastore.snor.SnomedConstraintDocument;
import com.b2international.snowowl.snomed.mrcm.core.concepteditor.ISnomedConceptEditorService;
import com.b2international.snowowl.snomed.mrcm.core.concepteditor.SnomedConceptDetailsBean;
import com.b2international.snowowl.snomed.mrcm.core.configuration.SnomedSimpleTypeRefSetAttributeConfiguration;
import com.b2international.snowowl.snomed.mrcm.core.extensions.IConceptModelExtension;
import com.b2international.snowowl.snomed.mrcm.core.extensions.IConceptModelExtensionProvider;
import com.b2international.snowowl.snomed.mrcm.core.server.widget.WidgetBeanProvider;
import com.b2international.snowowl.snomed.mrcm.core.server.widget.WidgetBeanProviderFactory;
import com.b2international.snowowl.snomed.mrcm.core.widget.IWidgetModelProvider;
import com.b2international.snowowl.snomed.mrcm.core.widget.bean.ConceptWidgetBean;
import com.b2international.snowowl.snomed.mrcm.core.widget.model.ConceptWidgetModel;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

/**
 * Server-side implementation of the SNOMED CT concept editor service.
 * 
 */
public class SnomedConceptEditorService implements ISnomedConceptEditorService {

	@Override
	public SnomedConceptDetailsBean getConceptDetailsBean(final IBranchPath branchPath, final long conceptId, 
			final SnomedSimpleTypeRefSetAttributeConfiguration configuration, 
			final boolean includeUnsanctioned) {

		checkNotNull(branchPath, "branchPath");
		
		// Retrieve initial model
		final String conceptIdString = Long.toString(conceptId);
		final IWidgetModelProvider widgetModelProvider = ApplicationContext.getServiceForClass(IWidgetModelProvider.class);
		final ConceptWidgetModel widgetModel = widgetModelProvider.createConceptWidgetModel(branchPath, conceptIdString, null);
		
		// Apply concept model extensions
		final IConceptModelExtensionProvider conceptModelExtensionProvider = ApplicationContext.getServiceForClass(IConceptModelExtensionProvider.class);
		final Collection<IConceptModelExtension> extensions = conceptModelExtensionProvider.getModelExtensions(branchPath, conceptId);
		for (final IConceptModelExtension conceptModelExtension : extensions) {
			conceptModelExtension.modifyWidgetModel(widgetModel);
		}

		// Create widget bean
		final WidgetBeanProvider widgetBeanProvider = new WidgetBeanProviderFactory().createProvider(branchPath, conceptIdString, widgetModel, configuration, includeUnsanctioned, true);
		final ConceptWidgetBean widgetBean = widgetBeanProvider.createConceptWidgetBean(new NullProgressMonitor());

		// Retrieve synonym and descendant type IDs
		final Set<String> synonymAndDescendants = SnomedRequests.prepareGetSynonyms()
				.build(branchPath.getPath())
				.execute(ApplicationContext.getServiceForClass(IEventBus.class))
				.then(new Function<SnomedConcepts, Set<String>>() {
					@Override
					public Set<String> apply(SnomedConcepts input) {
						return FluentIterable.from(input).transform(IComponent.ID_FUNCTION).toSet();
					}
				})
				.getSync();
		final LongSet synonymAndDescendantIds = PrimitiveSets.newLongOpenHashSet();
		for (final String synonymAndDescendantId : synonymAndDescendants) {
			synonymAndDescendantIds.add(Long.parseLong(synonymAndDescendantId));
		}

		// Retrieve applicable predicates
		final Collection<SnomedConstraintDocument> predicates = predicateBrowser.getPredicates(branchPath, conceptIdString, null);

		// Create regular index entry
		final SnomedTerminologyBrowser terminologyBrowser = ApplicationContext.getServiceForClass(SnomedTerminologyBrowser.class);
		final SnomedConceptDocument conceptIndexEntry = terminologyBrowser.getConcept(branchPath, conceptIdString);
		
		checkArgument(conceptIndexEntry != null, "Can't find concept '" + conceptId + "'.");

		final SnomedConceptDetailsBean snomedConceptDetailsBean = new SnomedConceptDetailsBean(conceptIndexEntry.getLabel(), 
				Long.parseLong(conceptIndexEntry.getIconId()), 
				widgetBean, 
				synonymAndDescendantIds, 
				configuration,
				predicates);

		return snomedConceptDetailsBean;
	}

}