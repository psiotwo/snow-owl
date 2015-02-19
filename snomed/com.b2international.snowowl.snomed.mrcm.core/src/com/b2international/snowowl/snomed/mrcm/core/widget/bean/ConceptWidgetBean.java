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
package com.b2international.snowowl.snomed.mrcm.core.widget.bean;

import static com.b2international.commons.StringUtils.isEmpty;
import static com.b2international.snowowl.core.ApplicationContext.getServiceForClass;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.b2international.snowowl.core.api.IComponent;
import com.b2international.snowowl.core.api.NullComponent;
import com.b2international.snowowl.snomed.datastore.SnomedClientTerminologyBrowser;
import com.b2international.snowowl.snomed.mrcm.core.widget.model.ConceptWidgetModel;

/**
 * Represents the root element of the backing bean tree, forming the simplified representation of a SNOMED CT concept.
 * 
 */
public class ConceptWidgetBean extends ModeledWidgetBean implements Serializable {

	private static final long serialVersionUID = 6737791419471322048L;
	
	private DescriptionContainerWidgetBean descriptions;
	private ContainerWidgetBean properties;
	private ContainerWidgetBean mappings;
	private String conceptId;
	private boolean active;

	/**
	 * Default constructor for serialization.
	 */
	protected ConceptWidgetBean() {
		super();
	}
	
	public ConceptWidgetBean(final ConceptWidgetModel model, final String conceptId, final boolean active) {
		super(model);
		this.conceptId = conceptId;
		this.active = active;
	}
	
	@Override
	public ConceptWidgetModel getModel() {
		return (ConceptWidgetModel) super.getModel();
	}
	
	/**
	 * Returns with the unique ID of the SNOMED&nbsp;CT concept.
	 * @return the SNOMED&nbsp;CT concept. Can have temporary CDO ID and NEW state.
	 */
	public String getConceptId() {
		return conceptId;
	}

	/**
	 * Returns with the status of the SNOMED&nbsp;CT concept.
	 * @return {@code true} if active, otherwise {@code false}.
	 */
	public boolean isActive() {
		return active;
	}
	
	public ContainerWidgetBean getProperties() {
		return properties;
	}

	public void setProperties(final ContainerWidgetBean properties) {
		this.properties = properties;
	}

	public DescriptionContainerWidgetBean getDescriptions() {
		return descriptions;
	}
	
	public void setDescriptions(final DescriptionContainerWidgetBean descriptions) {
		this.descriptions = descriptions;
	}
	
	public ContainerWidgetBean getMappings() {
		return mappings;
	}
	
	public void setMappings(ContainerWidgetBean mappings) {
		this.mappings = mappings;
	}

	@Override
	public ConceptWidgetBean getConcept() {
		return this;
	}
	
	@Override
	public String toString() {
		return String.format("ConceptWidgetBean [descriptions=%s, properties=%s, concept ID=%s, active=%s]",
				descriptions, properties, conceptId, active);
	}
	
	public void add(final String id) {
		if (!isEmpty(id)) {
			synchronized (componentMapMutex) {
				componentMap.put(id, NullComponent.<String>getNullImplementation());
			}
		}
	}

	public void add(final IComponent<String> component) {
		if (!NullComponent.isNullComponent(component) && !isEmpty(component.getId())) {
			synchronized (componentMapMutex) {
				componentMap.put(component.getId(), component);
			}
		}
	}
	
	public IComponent<String> getComponent(final String id) {
		if (isEmpty(id)) {
			return NullComponent.getNullImplementation();
		}
		
		synchronized (componentMapMutex) {
			
			IComponent<String> component = componentMap.get(id);
			if (NullComponent.isNullComponent(component)) {
				
				final Collection<String> unresolvedComponentIds = newHashSet();
				for (final Entry<String, IComponent<String>> entry : componentMap.entrySet()) {
					if (NullComponent.isNullComponent(entry.getValue())) {
						unresolvedComponentIds.add(entry.getKey());
					}
				}
				
				for (final IComponent<String> concept : getServiceForClass(SnomedClientTerminologyBrowser.class).getConcepts(unresolvedComponentIds)) {
					componentMap.put(concept.getId(), concept);
				}
				
				return componentMap.get(id);
				
			} else {
				return component;
			}
		}
	}
	
	private final Map<String, IComponent<String>> componentMap = newHashMap();
	private final Object componentMapMutex = UUID.randomUUID();
}	