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
package com.b2international.snowowl.datastore.browser;

import com.b2international.snowowl.core.api.IBranchPath;
import com.b2international.snowowl.core.api.IComponent;
import com.b2international.snowowl.core.api.IStatement;
import com.b2international.snowowl.core.api.browser.IStatementBrowser;

/**
 * {@link AbstractClientStatementBrowser Client statement browser} implementation, which lets you specify the branch path to use. 
 */
public class BranchSpecificClientStatementBrowser<C extends IComponent<K>, R extends IStatement<K>, K> extends AbstractClientStatementBrowser<C, R, K> {

	private final IBranchPath branchPath;

	/**
	 * @param wrappedBrowser the branch-aware service
	 * @param branchPath the branch to use
	 */
	public BranchSpecificClientStatementBrowser(final IStatementBrowser<C, R, K> wrappedBrowser, final IBranchPath branchPath) {
		super(wrappedBrowser);
		this.branchPath = branchPath;
	}

	@Override
	public IBranchPath getBranchPath() {
		return branchPath;
	}
}