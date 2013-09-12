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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import uk.co.q3c.v7.base.navigate.V7Navigator;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;

public class DefaultLogoutView extends VerticalViewBase implements LogoutView {

	@Inject
	protected DefaultLogoutView(V7Navigator navigator) {
		super(navigator);
	}

	@Override
	public Component getUiComponent() {
		Panel p = new Panel("Logged out");
		p.setSizeFull();
		addComponent(p);
		return this;

	}

	@Override
	protected void processParams(LinkedHashMap<String, String> params) {

	}

	@Override
	public void enter(V7ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
