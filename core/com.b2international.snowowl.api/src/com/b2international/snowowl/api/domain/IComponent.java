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
package com.b2international.snowowl.api.domain;

/**
 * Represents an identifiable component instance of a code system.
 * 
 */
public interface IComponent {

	/**
	 * Returns the component identifier.
	 * @return the component identifier
	 */
	String getId();
	
	/**
	 * Checks the component's release status.
	 * @return {@code true} if the component has already been released as part of a version (and so it is discouraged to
	 * delete it in later versions), {@code false} otherwise
	 */
	boolean isReleased();
}