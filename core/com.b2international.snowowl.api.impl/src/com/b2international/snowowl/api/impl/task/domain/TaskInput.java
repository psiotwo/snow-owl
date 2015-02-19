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
package com.b2international.snowowl.api.impl.task.domain;

import com.b2international.snowowl.api.task.domain.ITaskInput;

/**
 */
public class TaskInput implements ITaskInput {

	private String description;
	private String taskId;

	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public String getTaskId() {
		return taskId;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("TaskInput [description=");
		builder.append(description);
		builder.append("]");
		return builder.toString();
	}
}