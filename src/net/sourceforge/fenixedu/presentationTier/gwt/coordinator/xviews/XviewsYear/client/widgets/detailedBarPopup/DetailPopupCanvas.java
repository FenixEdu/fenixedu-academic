package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.detailedBarPopup;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael.Text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DetailPopupCanvas extends Raphael {

    private XviewsYear window;
    private DetailPopup detailedBar;

    public DetailPopupCanvas(XviewsYear window, int width, int height) {
	super(width, height);
	this.window = window;
    }

    public void loadDetailedBar(double cx, double cy, double w, double h, String color, String uText, String lText) {
	double _w = w;
	double _h = h*2.8;
	double x = cx - (_w/2.0);
	double y = cy - (_h/2.0);
	
	final Rect background = new Rect(x-5.0,y-5.0,_w+10.0,_h+10.0);
	background.attr("fill", "white");
	background.attr("stroke-width", 0.00001);
	
	final Rect frame = new Rect(x-2.0,y-2.0, _w+4.0, _h+4.0);
	frame.attr("stroke", color);
	frame.attr("stroke-width", 1);
	
	detailedBar = new DetailPopup(x, y, _w, _h, color, uText, lText);
	detailedBar.attr("fill", color);
	detailedBar.attr("stroke-width", 0.00001);
	detailedBar.attr("cursor","pointer");
    }

    public void onLoad() {
	if (detailedBar != null) {
	    detailedBar.onLoad();
	    setPopupHandler();
	}
    }

    public void setPopupHandler() {
	detailedBar.addClickHandler(new ClickHandler() {
	    public void onClick(ClickEvent event) {
		window.defocus();
	    }
	});
    }

    private class DetailPopup extends Raphael.Rect implements HasClickHandlers {
	double absoluteX;
	double absoluteY;
	double width;
	double height;
	double cx;
	double cy;
	double uLH;
	double lLH;

	String color;
	String uText;
	String lText;

	ClickableText upperLine;
	ClickableText lowerLine;

	private DetailPopup(double x, double y, double w, double h) {
	    super(x, y, w, h);
	}

	public DetailPopup(double x, double y, double w, double h, String color, String uText, String lText) {
	    this(x, y, w, h);
	    absoluteX = x;
	    absoluteY = y;
	    width = w;
	    height = h;
	    cx = x + (w / 2.0);
	    cy = y + (h / 2.0);
	    uLH = y + (h * 0.35);
	    lLH = y + (h * 0.75);

	    this.color = color;
	    this.uText = uText;
	    this.lText = lText;

	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
	    upperLine.addClickHandler(handler);
	    lowerLine.addClickHandler(handler);
	    return this.addDomHandler(handler, ClickEvent.getType());
	}

	public void onLoad() {
	    upperLine = new ClickableText(cx - (width * 0.4), uLH, uText);
	    upperLine.attr("font-size", getFontSize(6)+"px");
	    upperLine.attr("font-family", "sans-serif");
	    upperLine.attr("fill", "#FFFFFF");
	    upperLine.attr("text-anchor", "start");
	    upperLine.attr("cursor","pointer");

	    lowerLine = new ClickableText(cx - (width * 0.4), lLH, lText);
	    lowerLine.attr("font-size", getFontSize(6)+"px");
	    lowerLine.attr("font-family", "sans-serif");
	    lowerLine.attr("font-weight", "bold");
	    lowerLine.attr("fill", "#FFFFFF");
	    lowerLine.attr("text-anchor", "start");
	    lowerLine.attr("cursor","pointer");
	}

	private class ClickableText extends Text implements HasClickHandlers {

	    public ClickableText(double x, double y, String str) {
		super(x, y, str);
	    }

	    public HandlerRegistration addClickHandler(ClickHandler handler) {
		return this.addDomHandler(handler, ClickEvent.getType());
	    }

	}
	
	private int getFontSize(int reference) {
		return (int) (Math.sqrt((width * width) + (height * height)) * (reference * 1.0) / 100.0);
	    }
    }

}
