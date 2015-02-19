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
package com.b2international.snowowl.core.internal;

import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.service.prefs.PreferencesService;

import com.b2international.commons.platform.PlatformUtil;
import com.b2international.snowowl.core.CoreActivator;
import com.b2international.snowowl.core.config.ClientPreferences;
import com.b2international.snowowl.core.config.SnowOwlConfiguration;
import com.b2international.snowowl.core.login.LoginConfiguration;
import com.b2international.snowowl.core.markers.MarkerManager;
import com.b2international.snowowl.core.setup.BootstrapFragment;
import com.b2international.snowowl.core.setup.Environment;

/**
 * @since 3.3
 */
public class SnowOwlApplicationBootstrap implements BootstrapFragment {

	@Override
	public void init(SnowOwlConfiguration configuration, Environment env) {
		final PreferencesService preferences = env.preferences(); 
		
		final MarkerManager markerManager = new MarkerManager();
		env.services().registerService(MarkerManager.class, markerManager);
		
		final ClientPreferences cdoClientConfiguration = new ClientPreferences(preferences);
		env.services().registerService(ClientPreferences.class, cdoClientConfiguration);

		final LoginConfiguration loginConfiguration = new LoginConfiguration(preferences);
		env.services().registerService(LoginConfiguration.class, loginConfiguration);
	}

	@Override
	public void run(SnowOwlConfiguration configuration, Environment environment, IProgressMonitor monitor) {
		if (!environment.isEmbedded() && environment.isClient()) {
			PlatformUtil.enableSystemProxies(CoreActivator.getContext());
		}
	}

}