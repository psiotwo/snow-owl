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
package com.b2international.snowowl.snomed.api.impl.domain.classification;

import java.util.Collections;
import java.util.List;

import com.b2international.snowowl.snomed.api.domain.classification.IRelationshipChange;
import com.b2international.snowowl.snomed.api.domain.classification.IRelationshipChangeList;

/**
 */
public class RelationshipChangeList implements IRelationshipChangeList {

	private List<IRelationshipChange> changes = Collections.emptyList();
	private int total;

	public List<IRelationshipChange> getChanges() {
		return changes;
	}

	public int getTotal() {
		return total;
	}

	public void setChanges(final List<IRelationshipChange> changes) {
		this.changes = changes;
	}

	public void setTotal(final int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("RelationshipChangeList [changes=");
		builder.append(changes);
		builder.append(", total=");
		builder.append(total);
		builder.append("]");
		return builder.toString();
	}
}