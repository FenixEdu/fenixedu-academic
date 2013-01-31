package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.courseAnalysis;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar.TotalsLabel;

public class CourseInarTotals extends TotalsLabel {

	public CourseInarTotals(int width, int height, int totals) {
		super(width, height, totals);
	}

	@Override
	protected int getFontSize() {
		return (int) (Math.sqrt((width * width) + (height * height)) * 4.0 / 100.0);
	}

}
