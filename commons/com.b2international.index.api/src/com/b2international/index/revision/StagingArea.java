/*
 * Copyright 2018 B2i Healthcare Pte Ltd, http://b2i.sg
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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.b2international.commons.ClassUtils;
import com.b2international.index.mapping.DocumentMapping;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flipkart.zjsonpatch.DiffFlags;
import com.flipkart.zjsonpatch.JsonDiff;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

/**
 * A place that stores information about what will go into your next commit.
 * 
 * @since 6.6
 * @see RevisionIndex#prepareCommit()
 */
public final class StagingArea {

	private static final EnumSet<DiffFlags> DIFF_FLAGS = EnumSet.of(DiffFlags.ADD_ORIGINAL_VALUE_ON_REPLACE, DiffFlags.OMIT_COPY_OPERATION, DiffFlags.OMIT_MOVE_OPERATION);
	
	private final DefaultRevisionIndex index;
	private final String branchPath;
	private final ObjectMapper mapper;

	private Map<String, Object> newDocuments;
	private Map<String, Object> changedDocuments;
	private Map<String, RevisionDiff> changedRevisions;
	private Map<String, Object> removedDocuments;
	
	StagingArea(DefaultRevisionIndex index, String branchPath, ObjectMapper mapper) {
		this.index = index;
		this.branchPath = branchPath;
		this.mapper = mapper;
		reset();
	}
	
	/**
	 * Commits the changes so far staged to the staging area.
	 *  
	 * @param timestamp
	 * @param author
	 * @param commitComment
	 * @return
	 */
	public Commit commit(long timestamp, String author, String commitComment) {
		return commit(null, timestamp, author, commitComment);
	}
	
	/**
	 * Commits the changes so far staged to the staging area.
	 *  
	 * @param commitGroupId - can be used to connect multiple commits and consider them as a single commit
	 * @param timestamp - long timestamp when the commit happened
	 * @param author - the author of the changes
	 * @param commitComment - short text about the changes
	 * @return
	 */
	public Commit commit(String commitGroupId, long timestamp, String author, String commitComment) {
		return index.write(branchPath, timestamp, writer -> {
			Commit.Builder commit = Commit.builder();

			final Multimap<String, String> newComponentsByContainer = HashMultimap.create();
			final Multimap<String, String> changedComponentsByContainer = HashMultimap.create();
			final Multimap<String, String> removedComponentsByContainer = HashMultimap.create();
			final Multimap<Class<?>, String> deletedIdsByType = HashMultimap.create();
			
			removedDocuments.forEach((key, value) -> {
				deletedIdsByType.put(value.getClass(), key);
				if (value instanceof Revision) {
					removedComponentsByContainer.put(((Revision) value).getContainerId(), key);
				}
			});
			
			// apply removals first
			for (Class<?> type : deletedIdsByType.keySet()) {
				final ImmutableSet<String> deletedDocIds = ImmutableSet.copyOf(deletedIdsByType.get(type));
				writer.remove(type, deletedDocIds);
			}
			
			// then new documents and revisions
			for (Entry<String, Object> doc : newDocuments.entrySet()) {
				if (!removedDocuments.containsKey(doc.getKey())) {
					Object document = doc.getValue();
					writer.put(doc.getKey(), document);
					if (document instanceof Revision) {
						Revision rev = (Revision) document;
						newComponentsByContainer.put(checkNotNull(rev.getContainerId(), "Missing containerId for revision: " + rev), rev.getId());
					}
				}
			}
			
			// and changed documents
			for (Entry<String, Object> doc : changedDocuments.entrySet()) {
				if (!removedDocuments.containsKey(doc.getKey())) {
					Object document = doc.getValue();
					writer.put(doc.getKey(), document);
				}
			}
			
			final Multimap<ObjectNode, String> revisionsByChange = HashMultimap.create();
			
			// and changed revisions
			for (Entry<String, RevisionDiff> changedRevision : changedRevisions.entrySet()) {
				if (!removedDocuments.containsKey(changedRevision.getKey())) {
					RevisionDiff revisionDiff = changedRevision.getValue();
					final Revision rev = revisionDiff.newRevision;
					// XXX temporal coupling between writer.put() and revisionDiff.diff(mapper) call
					// first put the new revision into the writer so that created and revised fields will get their values properly
					// then call the diff method to calculate the diff and serialize the new node into a JsonNode with _changes and created, revised fields
					writer.put(changedRevision.getKey(), rev);
					String containerId = checkNotNull(rev.getContainerId(), "Missing containerId for revision: " + rev);
					if (!Revision.ROOT.equals(containerId)) { // XXX register only sub-components in the changed objects
						changedComponentsByContainer.put(containerId, rev.getId());
					}
					revisionDiff.diff(mapper).forEach(node -> {
						if (node instanceof ObjectNode) {
							revisionsByChange.put((ObjectNode) node, rev.getId());
						}
					});
				}
			}
			
			final List<CommitDetail> details = newArrayList();
			
			// collect property changes
			revisionsByChange.asMap().forEach((change, objects) -> {
				final String prop = change.get("path").asText().substring(1); // XXX removes the forward slash from the beginning
				final String from = change.get("fromValue").asText(); 
				final String to = change.get("value").asText();
				details.add(CommitDetail.changed()
						.propertyChange(prop, from, to, objects)
						.build());
			});
			
			if (!newComponentsByContainer.isEmpty()) {
				CommitDetail.Builder addedObjects = CommitDetail.added();
				// collect hierarchical changes and register them by container ID
				newComponentsByContainer.asMap().forEach((container, components) -> addedObjects.putObjects(container, components));
				details.add(addedObjects.build());
			}

			if (!changedComponentsByContainer.isEmpty()) {
				CommitDetail.Builder changedObjects = CommitDetail.changed();
				changedComponentsByContainer.asMap().forEach((container, components) -> changedObjects.putObjects(container, components));
				details.add(changedObjects.build());
			}

			if (!removedComponentsByContainer.isEmpty()) {
				CommitDetail.Builder removedObjects = CommitDetail.removed();
				removedComponentsByContainer.asMap().forEach((container, components) -> removedObjects.putObjects(container, components));
				details.add(removedObjects.build());
			}
			
			// free up memory before committing 
			reset();
			newComponentsByContainer.clear();
			changedComponentsByContainer.clear();
			removedComponentsByContainer.clear();
			deletedIdsByType.clear();
			
			// generate a commit entry that marks the end of the commit and contains all changes in a details property
			Commit commitDoc = commit
					.id(UUID.randomUUID().toString())
					.groupId(commitGroupId)
					.author(author)
					.branch(branchPath)
					.comment(commitComment)
					.timestamp(timestamp)
					.details(details)
					.build();
			writer.put(commitDoc.getId(), commitDoc);
			writer.commit();
			return commitDoc;
		});
	}

	/**
	 * Reset staging area to empty.
	 */
	private void reset() {
		newDocuments = newHashMap();
		changedDocuments = newHashMap();
		changedRevisions = newHashMap();
		removedDocuments = newHashMap();
	}

	public StagingArea stageNew(Revision newRevision) {
		return stageNew(newRevision.getId(), newRevision);
	}
	
	public StagingArea stageNew(String key, Object newDocument) {
		newDocuments.put(key, newDocument);
		return this;
	}
	
	public StagingArea stageChange(Revision oldRevision, Revision changedRevision) {
		checkArgument(Objects.equals(oldRevision.getId(), changedRevision.getId()), "IDs of oldRevision and changedRevision must match");
		changedRevisions.put(changedRevision.getId(), new RevisionDiff(oldRevision, changedRevision));
		return this;
	}
	
	public StagingArea stageChange(String key, Object changed) {
		checkArgument(!(changed instanceof Revision), "Use the other stageChange method properly track changes for revision documents.");
		changedDocuments.put(key, changed);
		return this;
	}
	
	public StagingArea stageRemove(Revision removedRevision) {
		return stageRemove(removedRevision.getId(), removedRevision);
	}

	public StagingArea stageRemove(String key, Object removed) {
		removedDocuments.put(key, removed);
		return this;
	}
	
	private class RevisionDiff {
		
		final Revision oldRevision;
		final Revision newRevision;
		
		public RevisionDiff(Revision oldRevision, Revision newRevision) {
			this.oldRevision = oldRevision;
			this.newRevision = newRevision;
		}

		public ArrayNode diff(ObjectMapper mapper) {
			final DocumentMapping mapping = index.admin().mappings().getMapping(newRevision.getClass());
			final Set<String> diffFields = mapping.getHashedFields();
			if (diffFields.isEmpty()) {
				return null; // in case of no hash fields, do NOT try to compute the diff
			}
			oldRevision._source = mapper.valueToTree(oldRevision);
			newRevision._source = mapper.valueToTree(newRevision);
			final JsonNode diff = JsonDiff.asJson(oldRevision._source, newRevision._source, DIFF_FLAGS);
			// remove revision specific fields from diff
			final ArrayNode diffNode = ClassUtils.checkAndCast(diff, ArrayNode.class);
			final ArrayNode changes = mapper.createArrayNode();
			final Iterator<JsonNode> elements = diffNode.elements();
			while (elements.hasNext()) {
				JsonNode node = elements.next();
				final ObjectNode change = ClassUtils.checkAndCast(node, ObjectNode.class);
				final String property = change.get("path").asText().replaceFirst("/", "");
				if (diffFields.contains(property)) {
					changes.add(change);
				}
			}
			return changes;
		}
		
	}

}