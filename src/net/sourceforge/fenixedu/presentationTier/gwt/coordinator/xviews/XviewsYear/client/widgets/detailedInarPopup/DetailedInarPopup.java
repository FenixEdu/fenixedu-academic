package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.detailedInarPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;

public class DetailedInarPopup extends Raphael {

    private XviewsYear window;
    private double width;
    private double height;
    private ClickHandler onClick;
    double cx;
    double cy;
    String shortYear;
    int curricularYear;
    int[] inar;
    int maxInar;

    public DetailedInarPopup(XviewsYear xviewsYear, int width, int height) {
	super(width, height);
	this.window = xviewsYear;
	this.width = width;
	this.height = height;
	
	onClick = new ClickHandler() {
	    public void onClick(ClickEvent event) {
		window.defocus();
	    }
	};

	// you have the bricks already, now arrange them.
    }
    
    public void onLoad() {
	drawTitle(cx, cy, shortYear, curricularYear);
	drawCaptions(cx, cy);
    }

    public void loadDetailedInar(double cx, double cy, String shortYear, int curricularYear, int[] inar) {
	this.cx = cx;
	this.cy = cy;
	this.shortYear = shortYear;
	this.curricularYear = curricularYear;
	this.inar = inar;
	getInarMax();
	
	drawBackPanel(cx, cy);
	drawInarBars(cx, cy, inar);
    }

    public void drawBackPanel(double cx, double cy) {
	final ClickableBox backPanel = new ClickableBox(cx - 300, cy - 200, 600, 400, 20);
	backPanel.attr("fill", "#FFFFFF");
	backPanel.attr("stroke-width", 1);
	backPanel.attr("cursor", "pointer");
	backPanel.addClickHandler(onClick);
    }
    
    public void drawTitle(double cx, double cy, String shortYear, int curricularYear) {
	final ClickableText title = new ClickableText((cx-260), (cy-160), shortYear + getCurrYearTag(curricularYear));
	title.attr("font-size", getFontSize(4)+"px");
	title.attr("font-family", "sans-serif");
	title.attr("font-weight","bold");
	title.attr("fill", "#000000");
	title.attr("text-anchor", "start");
	title.attr("cursor","pointer");
	title.addClickHandler(onClick);
    }
    
    public void drawCaptions(double cx, double cy) {
	final ClickableText inFrequency = new ClickableText((cx-80),(cy-60),window.getBundle().inFrequency());
	inFrequency.attr("font-size", getFontSize(3)+"px");
	inFrequency.attr("font-family", "sans-serif");
	inFrequency.attr("fill", "#000000");
	inFrequency.attr("text-anchor", "end");
	inFrequency.attr("cursor","pointer");
	inFrequency.addClickHandler(onClick);
	
	final ClickableText freqTotal = new ClickableText((cx-50+getWidth(0)),(cy-60),Integer.toString(inar[0]));
	freqTotal.attr("font-size", getFontSize(3)+"px");
	freqTotal.attr("font-weight","bold");
	freqTotal.attr("font-family", "sans-serif");
	freqTotal.attr("fill", "#000000");
	freqTotal.attr("text-anchor", "start");
	freqTotal.attr("cursor","pointer");
	freqTotal.addClickHandler(onClick);
	
	
	
	final ClickableText approved = new ClickableText((cx-80),(cy),window.getBundle().approved());
	approved.attr("font-size", getFontSize(3)+"px");
	approved.attr("font-family", "sans-serif");
	approved.attr("fill", "#000000");
	approved.attr("text-anchor", "end");
	approved.attr("cursor","pointer");
	approved.addClickHandler(onClick);
	
	final ClickableText appTotal = new ClickableText((cx-50+getWidth(1)),(cy),Integer.toString(inar[1]));
	appTotal.attr("font-size", getFontSize(3)+"px");
	appTotal.attr("font-weight","bold");
	appTotal.attr("font-family", "sans-serif");
	appTotal.attr("fill", "#000000");
	appTotal.attr("text-anchor", "start");
	appTotal.attr("cursor","pointer");
	appTotal.addClickHandler(onClick);
	
	
	
	final ClickableText nonEvaluated = new ClickableText((cx-80),(cy+60),window.getBundle().nonEvaluated());
	nonEvaluated.attr("font-size", getFontSize(3)+"px");
	nonEvaluated.attr("font-family", "sans-serif");
	nonEvaluated.attr("fill", "#000000");
	nonEvaluated.attr("text-anchor", "end");
	nonEvaluated.attr("cursor","pointer");
	nonEvaluated.addClickHandler(onClick);
	
	final ClickableText nEvTotal = new ClickableText((cx-50+getWidth(2)),(cy+60),Integer.toString(inar[2]));
	nEvTotal.attr("font-size", getFontSize(3)+"px");
	nEvTotal.attr("font-weight","bold");
	nEvTotal.attr("font-family", "sans-serif");
	nEvTotal.attr("fill", "#000000");
	nEvTotal.attr("text-anchor", "start");
	nEvTotal.attr("cursor","pointer");
	nEvTotal.addClickHandler(onClick);
	
	
	
	final ClickableText flunked = new ClickableText((cx-80),(cy+120),window.getBundle().flunked());
	flunked.attr("font-size", getFontSize(3)+"px");
	flunked.attr("font-family", "sans-serif");
	flunked.attr("fill", "#000000");
	flunked.attr("text-anchor", "end");
	flunked.attr("cursor","pointer");
	flunked.addClickHandler(onClick);
	
	final ClickableText flunkTotal = new ClickableText((cx-50+getWidth(3)),(cy+120),Integer.toString(inar[3]));
	flunkTotal.attr("font-size", getFontSize(3)+"px");
	flunkTotal.attr("font-weight","bold");
	flunkTotal.attr("font-family", "sans-serif");
	flunkTotal.attr("fill", "#000000");
	flunkTotal.attr("text-anchor", "start");
	flunkTotal.attr("cursor","pointer");
	flunkTotal.addClickHandler(onClick);
    }
    
    public void drawInarBars(double cx, double cy, int[] inar) {
	final ClickableBox blue = new ClickableBox((cx-60), (cy-82), getWidth(0), 30);
	blue.attr("fill", "#1888B8");
	blue.attr("stroke-width", 0.00001);
	blue.attr("cursor","pointer");
	blue.addClickHandler(onClick);
	
	final ClickableBox green = new ClickableBox((cx-60), (cy-24), getWidth(1), 30);
	green.attr("fill", "#39B54A");
	green.attr("stroke-width", 0.00001);
	green.attr("cursor","pointer");
	green.addClickHandler(onClick);
	
	
	final ClickableBox yellow = new ClickableBox((cx-60), (cy+36), getWidth(2), 30);
	yellow.attr("fill", "#FBB03B");
	yellow.attr("stroke-width", 0.00001);
	yellow.attr("cursor","pointer");
	yellow.addClickHandler(onClick);
	
	final ClickableBox red = new ClickableBox((cx-60), (cy+96), getWidth(3), 30);
	red.attr("fill", "#ED1C24");
	red.attr("stroke-width", 0.00001);
	red.attr("cursor","pointer");
	red.addClickHandler(onClick);
    }

    private class ClickableText extends Raphael.Text implements HasClickHandlers {

	public ClickableText(double x, double y, String str) {
	    super(x, y, str);
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
	    return this.addDomHandler(handler, ClickEvent.getType());
	}

    }

    private class ClickableBox extends Raphael.Rect implements HasClickHandlers {

	public ClickableBox(double x, double y, double w, double h) {
	    super(x, y, w, h);
	}
	
	public ClickableBox(double x, double y, double w, double h, double r) {
	    super(x, y, w, h, r);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
	    return this.addDomHandler(handler, ClickEvent.getType());
	}

    }
    
    private void getInarMax() {
	int max = inar[0];
	for(int i=1; i<4; i++) {
	    if(max < inar[i])
		max = inar[i];
	}
	maxInar = max;
    }
    
    private double getWidth(int index) {
	 return (((inar[index]*1.0) / (maxInar*1.0)) * 250.0);
    }

    private int getFontSize(int reference) {
	return (int) (Math.sqrt((600 * 600) + (400 * 400)) * (reference * 1.0) / 100.0);
    }
    
    private String getCurrYearTag(int curricularYear) {
	switch(curricularYear) {
	case 1: return " - " + window.getBundle()._1stYear();
	case 2: return " - " + window.getBundle()._2ndYear();
	case 3: return " - " + window.getBundle()._3rdYear();
	case 4: return " - " + window.getBundle()._4thYear();
	case 5: return " - " + window.getBundle()._5thYear();
	default: return " - " + window.getBundle().generalException();
	}
    }

}
