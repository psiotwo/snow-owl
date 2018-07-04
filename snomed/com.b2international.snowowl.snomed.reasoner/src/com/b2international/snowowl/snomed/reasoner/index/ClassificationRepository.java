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
package com.b2international.snowowl.snomed.reasoner.index;

import java.util.Map;

import com.b2international.index.Index;
import com.b2international.index.IndexRead;
import com.b2international.index.IndexWrite;
import com.b2international.index.admin.IndexAdmin;

/**
 * @since 7.0
 */
public final class ClassificationRepository implements Index {

	private final Index index;

	public ClassificationRepository(final Index index) {
		this.index = index;
	}

	@Override
	public IndexAdmin admin() {
		return index.admin();
	}

	@Override
	public String name() {
		return index.name();
	}

	@Override
	public <T> T read(final IndexRead<T> read) {
		return index.read(read);
	}

	@Override
	public <T> T write(final IndexWrite<T> write) {
		return index.write(write);
	}

	public void save(final String key, final Object document) {
		write(writer -> {
			writer.put(key, document);
			writer.commit();
			return null;
		});
	}

	public void saveAll(final Map<String, Object> documentsByKey) {
		write(writer -> {
			writer.putAll(documentsByKey);
			writer.commit();
			return null;
		});		
	}

	public <T> void remove(final Class<T> clazz, final String key) {
		write(writer -> {
			writer.remove(clazz, key);
			writer.commit();
			return null;
		});
	}
}