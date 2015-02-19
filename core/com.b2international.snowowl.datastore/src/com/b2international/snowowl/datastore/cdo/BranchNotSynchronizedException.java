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
package com.b2international.snowowl.datastore.cdo;

import com.b2international.snowowl.core.api.SnowowlServiceException;

/**
 * Exception to indicate that a branch is not synchronized to its parent.
 * 
 */
public class BranchNotSynchronizedException extends SnowowlServiceException {

	// generated
	private static final long serialVersionUID = 7223728699915037360L;

	public BranchNotSynchronizedException(String message, Throwable exception) {
		super(message, exception);
	}

	public BranchNotSynchronizedException(String message) {
		super(message);
	}

	public BranchNotSynchronizedException(Throwable exception) {
		super(exception);
	}

}