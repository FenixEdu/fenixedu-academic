package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class LocalizationTest extends Composite {

	private final HorizontalPanel panel = new HorizontalPanel();
	private Label label;

	public LocalizationTest(final int width, final int height, final String args) {

		initWidget(panel);
		String labelStr = "Client window width x height: " + width + " x " + height + "\n\n" + args;
		label = new Label(labelStr);
		panel.add(label);

	}

}
