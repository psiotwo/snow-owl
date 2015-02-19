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
package com.b2international.snowowl.api.task.exception;

import com.b2international.snowowl.api.exception.BadRequestException;

/**
 * Thrown when an editing task can not be written because it is already promoted and closed for modification.
 * 
 */
public class TaskAlreadyPromotedException extends BadRequestException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception instance with the specified message.
	 * 
	 * @param message the exception message
	 */
	public TaskAlreadyPromotedException(final String message) {
		super(message);
	}
}