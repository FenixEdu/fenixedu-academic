package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarCaption;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.constants.CatConstants;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;

public class InarCaption extends Raphael {

	private double ox;
	private double oy;
	private double width;
	private double height;
	private CatConstants bundle;

	private InarCaption(int width, int height) {
		super(width, height);
	}

	public InarCaption(int relativeHeight, CatConstants bundle) {
		this((relativeHeight * 2), relativeHeight);

		height = relativeHeight / 2.0;
		oy = (relativeHeight / 2.0) - (height / 4.0);
		width = height * 2.0;
		ox = 1;
		this.bundle = bundle;

		double bracketSize = height / 4.0;
		double leftAligning = (width * 2.0) / 10.0;

		double miniEdges = height / 5.0;

		/*final Rect frame = new Rect(ox,oy,width,height);
		frame.attr("stroke", "black");
		frame.attr("stroke-width", 1);*/

		double yRule = oy + (bracketSize - miniEdges) / 2.0;
		final Rect blueSquare = new Rect(leftAligning, yRule, miniEdges, miniEdges, 2.0);
		blueSquare.attr("fill", "#1888B8");
		blueSquare.attr("stroke-width", 0.00001);

		yRule += bracketSize;
		final Rect greenSquare = new Rect(leftAligning, yRule, miniEdges, miniEdges, 2.0);
		greenSquare.attr("fill", "#39B54A");
		greenSquare.attr("stroke-width", 0.00001);

		yRule += bracketSize;
		final Rect yellowSquare = new Rect(leftAligning, yRule, miniEdges, miniEdges, 2.0);
		yellowSquare.attr("fill", "#FBB03B");
		yellowSquare.attr("stroke-width", 0.00001);

		yRule += bracketSize;
		final Rect redSquare = new Rect(leftAligning, yRule, miniEdges, miniEdges, 2.0);
		redSquare.attr("fill", "#ED1C24");
		redSquare.attr("stroke-width", 0.00001);
	}

	@Override
	public void onLoad() {
		double bracketSize = height / 4.0;
		double leftAligning = (width * 2.0) / 10.0;

		double miniEdges = height / 6.0;

		leftAligning = (leftAligning * 1.2) + miniEdges;

		double yRule = oy + bracketSize / 2.0;
		final Text blueText = new Text(leftAligning, yRule, bundle.inFrequency());
		blueText.attr("font-size", getFontSize(9) + "px");
		blueText.attr("font-family", "sans-serif");
		blueText.attr("fill", "#000000");
		blueText.attr("text-anchor", "start");

		yRule += bracketSize;
		final Text greenText = new Text(leftAligning, yRule, bundle.approved());
		greenText.attr("font-size", getFontSize(9) + "px");
		greenText.attr("font-family", "sans-serif");
		greenText.attr("fill", "#000000");
		greenText.attr("text-anchor", "start");

		yRule += bracketSize;
		final Text yellowText = new Text(leftAligning, yRule, bundle.nonEvaluated());
		yellowText.attr("font-size", getFontSize(9) + "px");
		yellowText.attr("font-family", "sans-serif");
		yellowText.attr("fill", "#000000");
		yellowText.attr("text-anchor", "start");

		yRule += bracketSize;
		final Text redText = new Text(leftAligning, yRule, bundle.flunked());
		redText.attr("font-size", getFontSize(9) + "px");
		redText.attr("font-family", "sans-serif");
		redText.attr("fill", "#000000");
		redText.attr("text-anchor", "start");

	}

	private int getFontSize(int reference) {
		return (int) (Math.sqrt((width * width) + (height * height)) * (reference * 1.0) / 100.0);
	}

}
