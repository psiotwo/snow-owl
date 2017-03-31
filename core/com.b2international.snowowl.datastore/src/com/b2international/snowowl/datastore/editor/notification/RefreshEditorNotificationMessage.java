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
package com.b2international.snowowl.datastore.editor.notification;

import java.io.Serializable;


/**
 * @since 2.8
 */
public class RefreshEditorNotificationMessage extends NotificationMessage<Serializable> {

	private static final long serialVersionUID = 6148649505602133061L;

	public RefreshEditorNotificationMessage(Serializable data, String message) {
		super(data, message);
	}

}