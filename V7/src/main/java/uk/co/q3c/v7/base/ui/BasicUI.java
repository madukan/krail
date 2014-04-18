package uk.co.q3c.v7.base.ui;

import uk.co.q3c.v7.base.navigate.V7Navigator;
import uk.co.q3c.v7.base.push.Broadcaster;
import uk.co.q3c.v7.base.push.PushMessageRouter;

import com.google.inject.Inject;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.converter.ConverterFactory;
import com.vaadin.server.ErrorHandler;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.VerticalLayout;

// @PreserveOnRefresh
@Theme("chameleon")
public class BasicUI extends ScopedUI {

	@Inject
	protected BasicUI(V7Navigator navigator, ErrorHandler errorHandler, ConverterFactory converterFactory,
			Broadcaster broadcaster, PushMessageRouter pushMessageRouter) {
		super(navigator, errorHandler, converterFactory, broadcaster, pushMessageRouter);

	}

	@Override
	protected AbstractOrderedLayout screenLayout() {
		return new VerticalLayout(getViewDisplayPanel());
	}

	@Override
	protected String pageTitle() {
		return "V7 base";
	}

	@Override
	protected void processBroadcastMessage(String group, String message) {
		// TODO Auto-generated method stub

	}

}