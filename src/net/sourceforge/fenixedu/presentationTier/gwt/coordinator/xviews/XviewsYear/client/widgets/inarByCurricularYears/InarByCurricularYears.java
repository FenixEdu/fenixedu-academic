package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarByCurricularYears;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class InarByCurricularYears extends Raphael {

	private int[][] inarData;
	private int numberOfCurricularYears;
	private String yearTag;

	private XviewsYear window;
	private double ox;
	private double oy;
	private double width;
	private double height;
	private double[][] textCoords;
	private double titleSpacing;

	private InarByCurricularYears(int width, int height) {
		super(width, height);
	}

	public InarByCurricularYears(XviewsYear window, int width, int height, int numberOfCurricularYears, int[][] inarData,
			String yearTag) {
		this(width, height + 50);
		this.window = window;
		ox = 0.0;
		oy = 0.0;
		this.width = width;
		this.height = height;
		this.numberOfCurricularYears = numberOfCurricularYears;
		this.inarData = inarData;
		this.yearTag = yearTag;
		titleSpacing = 50.0;

		// drawOutline();

		textCoords = new double[numberOfCurricularYears][4];

		double xRule = ox;
		double yRule = oy + titleSpacing;
		double segmentWidth = (width * 2.0) / 10.0;
		double segmentHeight = height;
		double paddingW = (width * 1.0) / 100.0;

		drawLabels(xRule + paddingW, yRule, segmentWidth - (2.0 * paddingW), segmentHeight);

		xRule += segmentWidth;
		segmentWidth = (width * 0.3) / 10.0;
		drawSeparator(xRule + paddingW, yRule, segmentWidth - (2.0 * paddingW), segmentHeight);

		xRule += segmentWidth;
		segmentWidth = (width * 7.7) / 10.0;
		drawBars(xRule + paddingW, yRule, segmentWidth - (2.0 * paddingW), segmentHeight);

	}

	public int getThisLeft() {
		return this.getAbsoluteLeft();
	}

	public int getThisTop() {
		return this.getAbsoluteTop();
	}

	@Override
	public void onLoad() {
		super.onLoad();
		final Text title = new Text(20, 25, window.getBundle().ianfPerCurricularYear());
		title.attr("text-anchor", "start");
		title.attr("font-size", getFontSize(4) + "px");
		title.attr("font-family", "sans-serif");
		title.attr("fill", "#000000");

		if (1 <= numberOfCurricularYears) {
			final Text firstYear = new Text(textCoords[0][0], textCoords[0][1], window.getBundle()._1stYear());
			firstYear.attr("font-size", getFontSize(3) + "px");
			firstYear.attr("font-weight", "bold");
			firstYear.attr("font-family", "sans-serif");
			firstYear.attr("fill", "#000000");

			final Text firstTotals = new Text(textCoords[0][2], textCoords[0][3], Integer.toString(getTotalsForYear(1)));
			firstTotals.attr("font-size", getFontSize(3) + "px");
			firstTotals.attr("font-weight", "bold");
			firstTotals.attr("font-family", "sans-serif");
			firstTotals.attr("fill", "#000000");
		}

		if (2 <= numberOfCurricularYears) {
			final Text secondYear = new Text(textCoords[1][0], textCoords[1][1], window.getBundle()._2ndYear());
			secondYear.attr("font-size", getFontSize(3) + "px");
			secondYear.attr("font-weight", "bold");
			secondYear.attr("font-family", "sans-serif");
			secondYear.attr("fill", "#000000");

			final Text secondTotals = new Text(textCoords[1][2], textCoords[1][3], Integer.toString(getTotalsForYear(2)));
			secondTotals.attr("font-size", getFontSize(3) + "px");
			secondTotals.attr("font-weight", "bold");
			secondTotals.attr("font-family", "sans-serif");
			secondTotals.attr("fill", "#000000");
		}

		if (3 <= numberOfCurricularYears) {
			final Text thirdYear = new Text(textCoords[2][0], textCoords[2][1], window.getBundle()._3rdYear());
			thirdYear.attr("font-size", getFontSize(3) + "px");
			thirdYear.attr("font-weight", "bold");
			thirdYear.attr("font-family", "sans-serif");
			thirdYear.attr("fill", "#000000");

			final Text thirdTotals = new Text(textCoords[2][2], textCoords[2][3], Integer.toString(getTotalsForYear(3)));
			thirdTotals.attr("font-size", getFontSize(3) + "px");
			thirdTotals.attr("font-weight", "bold");
			thirdTotals.attr("font-family", "sans-serif");
			thirdTotals.attr("fill", "#000000");
		}

		if (4 <= numberOfCurricularYears) {
			final Text fourthYear = new Text(textCoords[3][0], textCoords[3][1], window.getBundle()._4thYear());
			fourthYear.attr("font-size", getFontSize(3) + "px");
			fourthYear.attr("font-weight", "bold");
			fourthYear.attr("font-family", "sans-serif");
			fourthYear.attr("fill", "#000000");

			final Text fourthTotals = new Text(textCoords[3][2], textCoords[3][3], Integer.toString(getTotalsForYear(4)));
			fourthTotals.attr("font-size", getFontSize(3) + "px");
			fourthTotals.attr("font-weight", "bold");
			fourthTotals.attr("font-family", "sans-serif");
			fourthTotals.attr("fill", "#000000");
		}

		if (5 <= numberOfCurricularYears) {
			final Text fifthYear = new Text(textCoords[4][0], textCoords[4][1], window.getBundle()._5thYear());
			fifthYear.attr("font-size", getFontSize(3) + "px");
			fifthYear.attr("font-weight", "bold");
			fifthYear.attr("font-family", "sans-serif");
			fifthYear.attr("fill", "#000000");

			final Text fifthTotals = new Text(textCoords[4][2], textCoords[4][3], Integer.toString(getTotalsForYear(5)));
			fifthTotals.attr("font-size", getFontSize(3) + "px");
			fifthTotals.attr("font-weight", "bold");
			fifthTotals.attr("font-family", "sans-serif");
			fifthTotals.attr("fill", "#000000");
		}
	}

	private void drawOutline() {
		final Rect outline = new Rect(ox, oy, width, height);
		outline.attr("stroke", "black");
		outline.attr("stroke-width", 1);
	}

	private void drawLabels(double xRule, double yRule, double segmentWidth, double segmentHeight) {
		double maxNumberOfYears = 5.0;
		double labelWidth = segmentWidth;
		double bracketSize = (segmentHeight / maxNumberOfYears);
		double labelHeight = bracketSize * 0.6;
		double topAligment = (segmentHeight / (2.0 * maxNumberOfYears)) - (labelHeight / 2.0) + titleSpacing;

		if (1 <= numberOfCurricularYears) {
			final Rect firstYear = new Rect(xRule, topAligment, labelWidth, labelHeight);
			firstYear.attr("stroke", "white");
			firstYear.attr("stroke-width", 0.00001);
			textCoords[0][0] = (xRule + (labelWidth / 2.0));
			textCoords[0][1] = (topAligment + (labelHeight / 2.0));
		}

		if (2 <= numberOfCurricularYears) {
			topAligment += bracketSize;
			final Rect secondYear = new Rect(xRule, topAligment, labelWidth, labelHeight);
			secondYear.attr("stroke", "white");
			secondYear.attr("stroke-width", 0.00001);
			textCoords[1][0] = (xRule + (labelWidth / 2.0));
			textCoords[1][1] = (topAligment + (labelHeight / 2.0));
		}

		if (3 <= numberOfCurricularYears) {
			topAligment += bracketSize;
			final Rect thirdYear = new Rect(xRule, topAligment, labelWidth, labelHeight);
			thirdYear.attr("stroke", "white");
			thirdYear.attr("stroke-width", 0.00001);
			textCoords[2][0] = (xRule + (labelWidth / 2.0));
			textCoords[2][1] = (topAligment + (labelHeight / 2.0));
		}

		if (4 <= numberOfCurricularYears) {
			topAligment += bracketSize;
			final Rect fourthYear = new Rect(xRule, topAligment, labelWidth, labelHeight);
			fourthYear.attr("stroke", "white");
			fourthYear.attr("stroke-width", 0.00001);
			textCoords[3][0] = (xRule + (labelWidth / 2.0));
			textCoords[3][1] = (topAligment + (labelHeight / 2.0));
		}

		if (5 <= numberOfCurricularYears) {
			topAligment += bracketSize;
			final Rect fifthYear = new Rect(xRule, topAligment, labelWidth, labelHeight);
			fifthYear.attr("stroke", "white");
			fifthYear.attr("stroke-width", 0.00001);
			textCoords[4][0] = (xRule + (labelWidth / 2.0));
			textCoords[4][1] = (topAligment + (labelHeight / 2.0));
		}

	}

	private void drawSeparator(double xRule, double yRule, double segmentWidth, double segmentHeight) {
		double paddingH = segmentHeight * 0.01;
		double adjustedHeight = segmentHeight * (numberOfCurricularYears / 5.0);
		final Rect separator = new Rect(xRule, yRule + paddingH, segmentWidth, adjustedHeight - (2.0 * +paddingH));
		separator.attr("fill", "black");
		separator.attr("stroke-width", 0.00001);
	}

	private void drawBars(double xRule, double yRule, double segmentWidth, double segmentHeight) {
		double maxNumberOfYears = 5.0;
		double labelWidth = segmentWidth;
		double bracketSize = (segmentHeight / maxNumberOfYears);
		double labelHeight = bracketSize * 0.6;
		double topAligment = yRule + (segmentHeight / (2.0 * maxNumberOfYears)) - (labelHeight / 2.0);
		double leftAlignment = xRule + (segmentWidth - labelWidth) / 2.0;

		if (1 <= numberOfCurricularYears) {
			final ClickableBox firstYear =
					new ClickableBox(leftAlignment, topAligment, (labelWidth * 0.8 * getRelativeBarWidth(1)), labelHeight,
							(labelWidth * 0.8), this);
			firstYear.attr("stroke", "black");
			firstYear.attr("stroke-width", 2);
			ClickHandler clickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					window.loadDetailedInarPopup(firstYear.getPopupCx(), firstYear.getPopupCy(), yearTag, 1, inarData[0]);
					window.focus();
				}

			};
			firstYear.addClickHandler(clickHandler);

			drawSingleBar(leftAlignment, topAligment, labelWidth, labelHeight, inarData[0], clickHandler);
			textCoords[0][2] = (leftAlignment + (labelWidth * (getRelativeBarWidth(1) * 0.8)) + (labelWidth * 0.1));
			textCoords[0][3] = (topAligment + (labelHeight / 2.0));
		}

		if (2 <= numberOfCurricularYears) {
			topAligment += bracketSize;
			final ClickableBox secondYear =
					new ClickableBox(leftAlignment, topAligment, (labelWidth * 0.8 * getRelativeBarWidth(2)), labelHeight,
							(labelWidth * 0.8), this);
			secondYear.attr("stroke", "black");
			secondYear.attr("stroke-width", 2);
			ClickHandler clickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					window.loadDetailedInarPopup(secondYear.getPopupCx(), secondYear.getPopupCy(), yearTag, 2, inarData[1]);
					window.focus();
				}

			};
			secondYear.addClickHandler(clickHandler);

			drawSingleBar(leftAlignment, topAligment, labelWidth, labelHeight, inarData[1], clickHandler);
			textCoords[1][2] = (leftAlignment + (labelWidth * (getRelativeBarWidth(2) * 0.8)) + (labelWidth * 0.1));
			textCoords[1][3] = (topAligment + (labelHeight / 2.0));
		}

		if (3 <= numberOfCurricularYears) {
			topAligment += bracketSize;
			final ClickableBox thirdYear =
					new ClickableBox(leftAlignment, topAligment, (labelWidth * 0.8 * getRelativeBarWidth(3)), labelHeight,
							(labelWidth * 0.8), this);
			thirdYear.attr("stroke", "black");
			thirdYear.attr("stroke-width", 2);
			ClickHandler clickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					window.loadDetailedInarPopup(thirdYear.getPopupCx(), thirdYear.getPopupCy(), yearTag, 3, inarData[2]);
					window.focus();
				}

			};
			thirdYear.addClickHandler(clickHandler);

			drawSingleBar(leftAlignment, topAligment, labelWidth, labelHeight, inarData[2], clickHandler);
			textCoords[2][2] = (leftAlignment + (labelWidth * (getRelativeBarWidth(3) * 0.8)) + (labelWidth * 0.1));
			textCoords[2][3] = (topAligment + (labelHeight / 2.0));
		}

		if (4 <= numberOfCurricularYears) {
			topAligment += bracketSize;
			final ClickableBox fourthYear =
					new ClickableBox(leftAlignment, topAligment, (labelWidth * 0.8 * getRelativeBarWidth(4)), labelHeight,
							(labelWidth * 0.8), this);
			fourthYear.attr("stroke", "black");
			fourthYear.attr("stroke-width", 2);
			ClickHandler clickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					window.loadDetailedInarPopup(fourthYear.getPopupCx(), fourthYear.getPopupCy(), yearTag, 4, inarData[3]);
					window.focus();
				}

			};
			fourthYear.addClickHandler(clickHandler);

			drawSingleBar(leftAlignment, topAligment, labelWidth, labelHeight, inarData[3], clickHandler);
			textCoords[3][2] = (leftAlignment + (labelWidth * (getRelativeBarWidth(4) * 0.8)) + (labelWidth * 0.1));
			textCoords[3][3] = (topAligment + (labelHeight / 2.0));
		}

		if (5 <= numberOfCurricularYears) {
			topAligment += bracketSize;
			final ClickableBox fifthYear =
					new ClickableBox(leftAlignment, topAligment, (labelWidth * 0.8 * getRelativeBarWidth(5)), labelHeight,
							(labelWidth * 0.8), this);
			fifthYear.attr("stroke", "black");
			fifthYear.attr("stroke-width", 2);
			ClickHandler clickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					window.loadDetailedInarPopup(fifthYear.getPopupCx(), fifthYear.getPopupCy(), yearTag, 5, inarData[4]);
					window.focus();
				}

			};
			fifthYear.addClickHandler(clickHandler);

			drawSingleBar(leftAlignment, topAligment, labelWidth, labelHeight, inarData[4], clickHandler);
			textCoords[4][2] = (leftAlignment + (labelWidth * (getRelativeBarWidth(5) * 0.8)) + (labelWidth * 0.1));
			textCoords[4][3] = (topAligment + (labelHeight / 2.0));
		}
	}

	private void drawSingleBar(double leftAlignment, double topAligment, double labelWidth, double labelHeight, int[] inar,
			ClickHandler clickHandler) {
		double barWidth = labelWidth * 0.8;
		double total = (inar[0] + inar[1] + inar[2] + inar[3]) * 1.0;
		barWidth = barWidth * (total / getMaxBarWidth());
		double xRule = leftAlignment;

		double iWidth = ((inar[0] * 1.0) / (inar[4] * 1.0)) * barWidth;
		final ClickableBox iBar = new ClickableBox(xRule, topAligment, iWidth, labelHeight);
		iBar.attr("fill", "#1888B8");
		iBar.attr("stroke-width", 0.00001);
		iBar.addClickHandler(clickHandler);
		iBar.attr("cursor", "pointer");

		xRule += iWidth;
		double aWidth = ((inar[1] * 1.0) / (inar[4] * 1.0)) * barWidth;
		final ClickableBox aBar = new ClickableBox(xRule, topAligment, aWidth, labelHeight);
		aBar.attr("fill", "#39B54A");
		aBar.attr("stroke-width", 0.00001);
		aBar.addClickHandler(clickHandler);
		aBar.attr("cursor", "pointer");

		xRule += aWidth;
		double nWidth = ((inar[2] * 1.0) / (inar[4] * 1.0)) * barWidth;
		final ClickableBox nBar = new ClickableBox(xRule, topAligment, nWidth, labelHeight);
		nBar.attr("fill", "#FBB03B");
		nBar.attr("stroke-width", 0.00001);
		nBar.addClickHandler(clickHandler);
		nBar.attr("cursor", "pointer");

		xRule += nWidth;
		double rWidth = ((inar[3] * 1.0) / (inar[4] * 1.0)) * barWidth;
		final ClickableBox rBar = new ClickableBox(xRule, topAligment, rWidth, labelHeight);
		rBar.attr("fill", "#ED1C24");
		rBar.attr("stroke-width", 0.00001);
		rBar.addClickHandler(clickHandler);
		rBar.attr("cursor", "pointer");
	}

	private double getMaxBarWidth() {
		int threshold = inarData.length;
		int max = 0;
		int ammount = 0;
		for (int i = 0; i < threshold; i++) {
			ammount = inarData[i][0] + inarData[i][1] + inarData[i][2] + inarData[i][3];
			if (ammount > max) {
				max = ammount;
			}
		}
		return (max * 1.0);
	}

	private int getTotalsForYear(int year) {
		return (inarData[year - 1][0] + inarData[year - 1][1] + inarData[year - 1][2] + inarData[year - 1][3]);
	}

	private double getRelativeBarWidth(int year) {
		return getMaxBarWidth() == 0.0 ? 0.0 : (getTotalsForYear(year) / getMaxBarWidth());
	}

	private int getFontSize(int reference) {
		return (int) (Math.sqrt((width * width) + (height * height)) * (reference * 1.0) / 100.0);
	}

	private class ClickableBox extends Raphael.Rect implements HasClickHandlers {

		private double popupCx;
		private double popupCy;
		private InarByCurricularYears parent;

		public ClickableBox(double x, double y, double w, double h) {
			super(x, y, w, h);
		}

		public ClickableBox(double x, double y, double w, double h, double labelWidth, InarByCurricularYears parent) {
			super(x, y, w, h);
			popupCx = x + (labelWidth / 2.0);
			popupCy = y + (h / 2.0);
			this.parent = parent;
		}

		public double getPopupCx() {
			return popupCx + (parent.getThisLeft());
		}

		public double getPopupCy() {
			return popupCy + (parent.getThisTop());
		}

		@Override
		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return this.addDomHandler(handler, ClickEvent.getType());
		}

	}

}
