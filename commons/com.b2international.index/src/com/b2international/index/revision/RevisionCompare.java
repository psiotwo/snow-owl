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
package com.b2international.index.revision;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.b2international.commons.collections.Collections3;
import com.google.common.collect.ImmutableList;

/**
 * @since 5.0
 */
public final class RevisionCompare {

	static Builder builder(RevisionBranchRef base, RevisionBranchRef compare) {
		return new Builder(base, compare);
	}
	
	static class Builder {
		
		private final RevisionBranchRef base;
		private final RevisionBranchRef compare;
		private int added;
		private int changed;
		private int removed;
	
		private final Map<String, RevisionCompareDetail> detailsByComponent = newHashMap();
		
		Builder(RevisionBranchRef base, RevisionBranchRef compare) {
			this.base = base;
			this.compare = compare;
		}
		
		public Builder apply(Commit commit) {
			for (CommitDetail detail : commit.getDetails()) {
				List<String> objects = detail.getObjects();
				for (int i = 0; i < objects.size(); i++) {
					String object = objects.get(i);
					final ObjectId objectId = ObjectId.of(detail.getObjectType(), object);
					
					final List<RevisionCompareDetail> details;
					if (detail.isPropertyChange()) {
						// ignore property change if an existing ADD detail has been added for the component
						RevisionCompareDetail existingObjectDetail = detailsByComponent.get(objectId.toString());
						if (existingObjectDetail != null && existingObjectDetail.isAdd()) {
							details = Collections.emptyList();
						} else {
							details = Collections.singletonList(
									RevisionCompareDetail.propertyChange(
											commit.getAuthor(), 
											commit.getTimestamp(), 
											commit.getComment(), 
											detail.getOp(), 
											objectId, 
											detail.getProp(), 
											detail.getFrom(), detail.getTo()));
						}
					} else {
						details = detail.getComponents()
								.get(i)
								.stream()
								.map(component -> RevisionCompareDetail.componentChange(commit.getAuthor(), commit.getTimestamp(), commit.getComment(), detail.getOp(), objectId, ObjectId.of(detail.getComponentType(), component)))
								.collect(Collectors.toList());
					}
					
					details.forEach(compareDetail -> {
						switch (compareDetail.getOp()) {
						case ADD:
							added++;
							break;
						case CHANGE:
							changed++;
							break;
						case REMOVE:
							removed++;
							break;
						}
						detailsByComponent.merge(compareDetail.key(), compareDetail, (oldV, newV) -> oldV.merge(newV));
					});
				}
			}
			return this;
		}
		
		public RevisionCompare build() {
			return new RevisionCompare(
					base, 
					compare,
					ImmutableList.copyOf(detailsByComponent.values()),
					added,
					changed,
					removed);
		}
		
	}
	
	private final RevisionBranchRef base;
	private final RevisionBranchRef compare;
	private final List<RevisionCompareDetail> details;
	private final int totalAdded;
	private final int totalChanged;
	private final int totalRemoved;

	private RevisionCompare(RevisionBranchRef base,	RevisionBranchRef compare, List<RevisionCompareDetail> details, int totalAdded, int totalChanged, int totalRemoved) {
		this.base = base;
		this.compare = compare;
		this.details = Collections3.toImmutableList(details);
		this.totalAdded = totalAdded;
		this.totalChanged = totalChanged;
		this.totalRemoved = totalRemoved;
	}

	public RevisionBranchRef getBase() {
		return base;
	}
	
	public RevisionBranchRef getCompare() {
		return compare;
	}
	
	public List<RevisionCompareDetail> getDetails() {
		return details;
	}
	
	public int getTotalAdded() {
		return totalAdded;
	}
	
	public int getTotalChanged() {
		return totalChanged;
	}
	
	public int getTotalRemoved() {
		return totalRemoved;
	}
	
}
