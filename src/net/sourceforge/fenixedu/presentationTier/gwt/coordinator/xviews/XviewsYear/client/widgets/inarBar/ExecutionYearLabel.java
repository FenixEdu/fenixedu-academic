package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar;

import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;

public class ExecutionYearLabel extends Raphael {

	protected int width;
	protected int height;
	protected int cx;
	protected int cy;
	protected Text yearLabel;
	protected String yearAcronym;

	protected ExecutionYearLabel(int width, int height) {
		super(width, height);
	}

	public ExecutionYearLabel(int width, int height, String yearAcronym) {
		this(width, height);
		this.width = width;
		this.height = height;
		this.yearAcronym = yearAcronym;

		cx = width / 2;
		cy = height / 2;
	}

	@Override
	public void onLoad() {
		super.onLoad();

		yearLabel = new Text(cx, cy, yearAcronym);
		yearLabel.attr("font-size", getFontSize() + "px");
		yearLabel.attr("font-weight", "bold");
		yearLabel.attr("font-family", "sans-serif");
		yearLabel.attr("fill", "#000000");

	}

	protected int getFontSize() {
		return (int) (Math.sqrt((width * width) + (height * height)) * 12.0 / 100.0);
	}

}
