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
package com.b2international.snowowl.snomed.datastore;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Represents an <b>ALWAYS</b> map rule SNOMED CT complex map reference set attribute.
 * <br><br><b>Example:</b> ALWAYS F31.1 
 */
public class AlwalysComplexMapAttribute implements IComplexMapAttribute {

	private final Map<Object, Object> cache = Maps.newHashMap();

	/* (non-Javadoc)
	 * @see com.b2international.snowowl.snomed.datastore.IComplexMapAttribute#setAttribute(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setAttribute(final Object key, final Object value) {
		cache.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final Object value = cache.get(ICD_10_ID);
		final String label = null == value ? ICD_10_ID : String.valueOf(value); 
		return "ALWAYS " + label;
	}

}