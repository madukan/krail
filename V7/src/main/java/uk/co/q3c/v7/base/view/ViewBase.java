/*
 * Copyright (C) 2013 David Sowerby
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package uk.co.q3c.v7.base.view;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.q3c.v7.base.navigate.V7Navigator;

public abstract class ViewBase implements V7View {

	private static Logger log = LoggerFactory.getLogger(ViewBase.class);
	private final V7Navigator navigator;

	@Inject
	protected ViewBase(V7Navigator navigator) {
		super();
		this.navigator = navigator;
		buildUI();
	}

	protected abstract void buildUI();

	@Override
	public void enter(V7ViewChangeEvent event) {
		log.debug("entered view: " + this.getClass().getSimpleName() + " with uri: "
				+ event.getNewNavigationState().getFragment().getUri());
		processParams(event.getNewNavigationState().getFragment().getParameters());
	}

	protected abstract void processParams(Map<String, String> params);

	public V7Navigator getNavigator() {
		return navigator;
	}

}
