package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.averageByCurricularYears;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael.Rect;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael.Text;

public class AverageByCurricularYears extends Raphael {
    
    private double[] averageData;
    private int numberOfCurricularYears;
    private String yearTag;

    private XviewsYear window;
    private double ox;
    private double oy;
    private double width;
    private double height;
    private double[][] textCoords;
    private double titleSpacing;

    private AverageByCurricularYears(int width, int height) {
	super(width, height);
    }
    
    public AverageByCurricularYears(XviewsYear window, int width, int height, int numberOfCurricularYears, double[] averageData, String yearTag) {
	this(width,height+50);
	this.window = window;
	ox = 0.0;
	oy = 0.0;
	this.width = width;
	this.height = height;
	this.numberOfCurricularYears = numberOfCurricularYears;
	this.averageData = averageData;
	this.yearTag = yearTag;
	titleSpacing = 50.0;
	
	//drawOutline();
	
	textCoords = new double[numberOfCurricularYears][4];

	double xRule = ox;
	double yRule = oy+titleSpacing;
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
    
    @Override
    public void onLoad() {
	super.onLoad();
	final Text title = new Text(160,25,"Average Grade per Curricular Year");
	title.attr("font-size", getFontSize(4) + "px");
	title.attr("font-family", "sans-serif");
	title.attr("fill", "#000000");
	
	if (1 <= numberOfCurricularYears) {
	    final Text firstYear = new Text(textCoords[0][0], textCoords[0][1], "1st Year");
	    firstYear.attr("font-size", getFontSize(3) + "px");
	    firstYear.attr("font-weight", "bold");
	    firstYear.attr("font-family", "sans-serif");
	    firstYear.attr("fill", "#000000");
	    
	    final Text firstTotals = new Text(textCoords[0][2], textCoords[0][3], (averageData[0] == 0.0 ? "" : Double.toString(averageData[0])));
	    firstTotals.attr("font-size", getFontSize(3) + "px");
	    firstTotals.attr("font-weight", "bold");
	    firstTotals.attr("font-family", "sans-serif");
	    firstTotals.attr("fill", "#000000");
	}

	if (2 <= numberOfCurricularYears) {
	    final Text secondYear = new Text(textCoords[1][0], textCoords[1][1], "2nd Year");
	    secondYear.attr("font-size", getFontSize(3) + "px");
	    secondYear.attr("font-weight", "bold");
	    secondYear.attr("font-family", "sans-serif");
	    secondYear.attr("fill", "#000000");
	    
	    final Text secondTotals = new Text(textCoords[1][2], textCoords[1][3], (averageData[1] == 0.0 ? "" : Double.toString(averageData[1])));
	    secondTotals.attr("font-size", getFontSize(3) + "px");
	    secondTotals.attr("font-weight", "bold");
	    secondTotals.attr("font-family", "sans-serif");
	    secondTotals.attr("fill", "#000000");
	}

	if (3 <= numberOfCurricularYears) {
	    final Text thirdYear = new Text(textCoords[2][0], textCoords[2][1], "3rd Year");
	    thirdYear.attr("font-size", getFontSize(3) + "px");
	    thirdYear.attr("font-weight", "bold");
	    thirdYear.attr("font-family", "sans-serif");
	    thirdYear.attr("fill", "#000000");
	    
	    final Text thirdTotals = new Text(textCoords[2][2], textCoords[2][3], (averageData[2] == 0.0 ? "" : Double.toString(averageData[2])));
	    thirdTotals.attr("font-size", getFontSize(3) + "px");
	    thirdTotals.attr("font-weight", "bold");
	    thirdTotals.attr("font-family", "sans-serif");
	    thirdTotals.attr("fill", "#000000");
	}

	if (4 <= numberOfCurricularYears) {
	    final Text fourthYear = new Text(textCoords[3][0], textCoords[3][1], "4th Year");
	    fourthYear.attr("font-size", getFontSize(3) + "px");
	    fourthYear.attr("font-weight", "bold");
	    fourthYear.attr("font-family", "sans-serif");
	    fourthYear.attr("fill", "#000000");
	    
	    final Text fourthTotals = new Text(textCoords[3][2], textCoords[3][3], (averageData[3] == 0.0 ? "" : Double.toString(averageData[3])));
	    fourthTotals.attr("font-size", getFontSize(3) + "px");
	    fourthTotals.attr("font-weight", "bold");
	    fourthTotals.attr("font-family", "sans-serif");
	    fourthTotals.attr("fill", "#000000");
	}

	if (5 <= numberOfCurricularYears) {
	    final Text fifthYear = new Text(textCoords[4][0], textCoords[4][1], "5th Year");
	    fifthYear.attr("font-size", getFontSize(3) + "px");
	    fifthYear.attr("font-weight", "bold");
	    fifthYear.attr("font-family", "sans-serif");
	    fifthYear.attr("fill", "#000000");
	    
	    final Text fifthTotals = new Text(textCoords[4][2], textCoords[4][3], (averageData[4] == 0.0 ? "" : Double.toString(averageData[4])));
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
	    firstYear.attr("stroke-width", 1);
	    textCoords[0][0] = (xRule + (labelWidth / 2.0));
	    textCoords[0][1] = (topAligment + (labelHeight / 2.0));
	}

	if (2 <= numberOfCurricularYears) {
	    topAligment += bracketSize;
	    final Rect secondYear = new Rect(xRule, topAligment, labelWidth, labelHeight);
	    secondYear.attr("stroke", "white");
	    secondYear.attr("stroke-width", 1);
	    textCoords[1][0] = (xRule + (labelWidth / 2.0));
	    textCoords[1][1] = (topAligment + (labelHeight / 2.0));
	}

	if (3 <= numberOfCurricularYears) {
	    topAligment += bracketSize;
	    final Rect thirdYear = new Rect(xRule, topAligment, labelWidth, labelHeight);
	    thirdYear.attr("stroke", "white");
	    thirdYear.attr("stroke-width", 1);
	    textCoords[2][0] = (xRule + (labelWidth / 2.0));
	    textCoords[2][1] = (topAligment + (labelHeight / 2.0));
	}

	if (4 <= numberOfCurricularYears) {
	    topAligment += bracketSize;
	    final Rect fourthYear = new Rect(xRule, topAligment, labelWidth, labelHeight);
	    fourthYear.attr("stroke", "white");
	    fourthYear.attr("stroke-width", 1);
	    textCoords[3][0] = (xRule + (labelWidth / 2.0));
	    textCoords[3][1] = (topAligment + (labelHeight / 2.0));
	}

	if (5 <= numberOfCurricularYears) {
	    topAligment += bracketSize;
	    final Rect fifthYear = new Rect(xRule, topAligment, labelWidth, labelHeight);
	    fifthYear.attr("stroke", "white");
	    fifthYear.attr("stroke-width", 1);
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
	    final Rect firstYear = new Rect(leftAlignment, topAligment, 0.0, labelHeight);
	    firstYear.attr("stroke", "black");
	    firstYear.attr("stroke-width", 1);
	    firstYear.attr("fill","0-#000-"+getHexColorCode(1));
	    JSONObject animeParams = new JSONObject();
	    animeParams.put("width", new JSONString(Double.toString(labelWidth*0.8*getRelativeBarWidth(1))));
	    firstYear.animate(animeParams, ((int) (2000 * getRelativeBarWidth(1))));
	    
	    textCoords[0][2] = (leftAlignment + (labelWidth * (getRelativeBarWidth(1) * 0.8)) + (labelWidth * 0.1));
	    textCoords[0][3] = (topAligment + (labelHeight / 2.0));
	}

	if (2 <= numberOfCurricularYears) {
	    topAligment += bracketSize;
	    final Rect secondYear = new Rect(leftAlignment, topAligment, 0.0, labelHeight);
	    secondYear.attr("stroke", "black");
	    secondYear.attr("stroke-width", 1);
	    secondYear.attr("fill","0-#000-"+getHexColorCode(2));
	    JSONObject animeParams = new JSONObject();
	    animeParams.put("width", new JSONString(Double.toString(labelWidth*0.8*getRelativeBarWidth(2))));
	    secondYear.animate(animeParams, ((int) (2000 * getRelativeBarWidth(2))));
	    
	    textCoords[1][2] = (leftAlignment + (labelWidth * (getRelativeBarWidth(2) * 0.8)) + (labelWidth * 0.1));
	    textCoords[1][3] = (topAligment + (labelHeight / 2.0));
	}

	if (3 <= numberOfCurricularYears) {
	    topAligment += bracketSize;
	    final Rect thirdYear = new Rect(leftAlignment, topAligment, 0.0, labelHeight);
	    thirdYear.attr("stroke", "black");
	    thirdYear.attr("stroke-width", 1);
	    thirdYear.attr("fill","0-#000-"+getHexColorCode(3));
	    JSONObject animeParams = new JSONObject();
	    animeParams.put("width", new JSONString(Double.toString(labelWidth*0.8*getRelativeBarWidth(3))));
	    thirdYear.animate(animeParams, ((int) (2000 * getRelativeBarWidth(3))));
	    
	    textCoords[2][2] = (leftAlignment + (labelWidth * (getRelativeBarWidth(3) * 0.8)) + (labelWidth * 0.1));
	    textCoords[2][3] = (topAligment + (labelHeight / 2.0));
	}

	if (4 <= numberOfCurricularYears) {
	    topAligment += bracketSize;
	    final Rect fourthYear = new Rect(leftAlignment, topAligment, 0.0, labelHeight);
	    fourthYear.attr("stroke", "black");
	    fourthYear.attr("stroke-width", 1);
	    fourthYear.attr("fill","0-#000-"+getHexColorCode(4));
	    JSONObject animeParams = new JSONObject();
	    animeParams.put("width", new JSONString(Double.toString(labelWidth*0.8*getRelativeBarWidth(4))));
	    fourthYear.animate(animeParams, ((int) (2000 * getRelativeBarWidth(4))));
	    
	    textCoords[3][2] = (leftAlignment + (labelWidth * (getRelativeBarWidth(4) * 0.8)) + (labelWidth * 0.1));
	    textCoords[3][3] = (topAligment + (labelHeight / 2.0));
	}

	if (5 <= numberOfCurricularYears) {
	    topAligment += bracketSize;
	    final Rect fifthYear = new Rect(leftAlignment, topAligment, 0.0, labelHeight);
	    fifthYear.attr("stroke", "black");
	    fifthYear.attr("stroke-width", 1);
	    fifthYear.attr("fill","0-#000-"+getHexColorCode(5));
	    JSONObject animeParams = new JSONObject();
	    animeParams.put("width", new JSONString(Double.toString(labelWidth*0.8*getRelativeBarWidth(5))));
	    fifthYear.animate(animeParams, ((int) (2000 * getRelativeBarWidth(5))));
	    
	    textCoords[4][2] = (leftAlignment + (labelWidth * (getRelativeBarWidth(5) * 0.8)) + (labelWidth * 0.1));
	    textCoords[4][3] = (topAligment + (labelHeight / 2.0));
	}
    }
    
    private double getRelativeBarWidth(int year) {
	return (averageData[year-1] / 20.0);
    }
    
    private String getHexColorCode(int year) {
	double ratio = getRelativeBarWidth(year);
	int colorIndex = ((int) (ratio * 16.0));
	String color = "#" + Integer.toHexString(colorIndex) + Integer.toHexString(colorIndex) + Integer.toHexString(colorIndex);
	return color;
    }
    
    private int getFontSize(int reference) {
	return (int) (Math.sqrt((width * width) + (height * height)) * (reference * 1.0) / 100.0);
    }

}
