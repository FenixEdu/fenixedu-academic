package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.problematicCoursesTree;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;

public class MultiChoiceSwitcher extends Raphael{
    
    private double width;
    private double height;
    private double ox;
    private double oy;
    private double innerProd;
    private XviewsYear window;
    private String[] choices;
    private int startingState;
    
    private Rect slide;
    private Slider slider;
    private SliderMechanics sliderMechs;
    private SliderGhost[] ghosts;

    private MultiChoiceSwitcher(int width, int height) {
	super(width, height);
    }
    
    public MultiChoiceSwitcher(XviewsYear window, int width, int height, String[] choices, int startingState) {
	this(width, height);
	this.width = width;
	this.height = height;
	ox = 0.0;
	oy = 0.0;
	innerProd = Math.sqrt((width*width) + (height*height));
	this.window = window;
	this.choices = choices;
	this.startingState = startingState;
	ghosts = new SliderGhost[choices.length];
	//drawOutline();
	drawWidget();
	initState();
	window.widgetReady();
    }
    
    private void drawOutline() {
	final Rect outline = new Rect(ox, oy, width-1, height-1);
	outline.attr("stroke", "black");
	outline.attr("stroke-width", 1);
    }
    
    private void drawWidget() {
	final double slideOX = ox + width * 0.05;
	final double slideOY = oy + height * 0.75;
	final double slideW = width * 0.9;
	final double slideH = height * 0.2;
	final double slideR = slideH * 0.6;
	final double slideStroke = 2.0;
	slide = new Rect(slideOX, slideOY, slideW, slideH, slideR);
	slide.attr("stroke", "#143541");
	slide.attr("stroke-width", 2.0);
	slide.attr("fill","#EFEFEF");
	
	final double ghostStroke = 1.0;
	final double ghostPadding = ghostStroke * 1;
	
	final double sliderStroke = 0.0;
	final double sliderOX = slideOX + slideStroke;
	final double sliderOY = slideOY + slideStroke;
	final double sliderW = ((slideW - slideStroke*2.0 - (choices.length*2*ghostPadding)) / choices.length);
	final double sliderH = slideH - slideStroke * 2.0;
	final double sliderR = slideH * 0.4;
	slider = new Slider(sliderOX, sliderOY, sliderW, sliderH, sliderR, sliderStroke, slideOX+slideW-slideStroke);
	sliderMechs = new SliderMechanics(sliderOX, sliderOY, sliderW, sliderH, sliderR, sliderStroke, slideOX+slideW-slideStroke);
	
	final double ghostOX = slideOX + slideStroke;
	final double ghostOY = slideOY + slideStroke;
	final double ghostW = ((slideW - slideStroke*2.0 - (choices.length*2*ghostPadding)) / choices.length);
	final double ghostH = slideH - slideStroke * 2.0;
	for(int i = 0; i < choices.length; i++) {
	    final SliderGhost ghost = new SliderGhost(ghostOX+(ghostW*i)+((2*i+1)*ghostPadding), ghostOY, ghostW, ghostH, ghostStroke, ghostPadding, i);
	    ghost.label.addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
		    swapState(ghost);
		}
		
	    });
	    ghosts[i] = ghost;
	}
	slider.sliderToFront();
	sliderMechs.sliderToFront();
    }
    
    private void initState() {
	slider.moveSlider(ghosts[startingState]);
	sliderMechs.moveSliderMechs(ghosts[startingState]);
	ghosts[startingState].label.onFocus();
    }
    
    private int getFontSize(int reference) {
	return (int) (innerProd * (reference * 1.0) / 100.0);
    }
    
    private void determineDropZone() {
	for(int i=0; i<ghosts.length; i++) {
	    if(slider.cx >= ghosts[i].ghostOX && slider.cx < (ghosts[i].ghostOX + ghosts[i].ghostW)) {
		swapState(ghosts[i]);
	    }
	}
    }
    
    private void swapState(SliderGhost ghost) {
	for(int k=0; k<ghosts.length; k++) {
	    ghosts[k].label.deFocus();
	}
	slider.animateSlider(ghost);
	sliderMechs.moveSliderMechs(ghost);
	ghost.label.onFocus();
	window.switchCriteria(ghost.relOrder);
    }
    
    private class Slider extends Rect {
	
	double sliderOX;
	double sliderOY;
	double sliderW;
	double sliderH;
	double sliderR;
	double sliderStroke;
	double leftMargin;
	double rightMargin;
	
	double cx;
	double cy;
	
	Circle center;
	Circle centerBowl;
	
	public Slider(double x, double y, double w, double h, double r, double sliderStroke, double rightMargin) {
	    super(x, y, w, h, r);
	    this.sliderOX = x;
	    this.leftMargin = x;
	    this.sliderOY = y;
	    this.sliderW = w;
	    this.sliderH = h;
	    this.sliderR= r;
	    this.sliderStroke = sliderStroke;
	    this.rightMargin = rightMargin;
	    
	    cx = sliderOX + sliderW/2;
	    cy = sliderOY + sliderH/2;
	    
	    this.attr("fill","90-#58ADCC-#AEE0F2");
	    this.attr("stroke", "#CFCFCF");
	    this.attr("stroke-width", sliderStroke);
	    this.attr("cursor", "pointer");
	    
	    centerBowl = new Circle(cx,cy, sliderH*0.4);
	    centerBowl.attr("fill","90-#AEE0F2-#58ADCC");
	    centerBowl.attr("stroke-width", 0.00001);
	    centerBowl.attr("cursor", "pointer");
	    
	    center = new Circle(cx,cy, sliderH*0.15);
	    center.attr("fill","#58ADCC");
	    center.attr("stroke-width", 0.00001);
	    center.attr("cursor", "pointer");
	}
	
	public void animateSlider(SliderGhost ghost) {
	    sliderOX = ghost.ghostOX;
	    cx = sliderOX + sliderW/2;
	    JSONObject sliderAnimeParams = new JSONObject();
	    sliderAnimeParams.put("x", new JSONString(Double.toString(sliderOX)));
	    JSONObject centerSliderAnimeParams = new JSONObject();
	    centerSliderAnimeParams.put("cx", new JSONString(Double.toString(ghost.ghostOX+sliderW/2)));
	    
	    this.animate(sliderAnimeParams, 250);
	    center.animate(centerSliderAnimeParams, 250);
	    centerBowl.animate(centerSliderAnimeParams, 250);
	}
	
	public void moveSlider(SliderGhost ghost) {
	    sliderOX = ghost.ghostOX;
	    cx = sliderOX + sliderW/2;
	    this.attr("x", sliderOX);
	    center.attr("cx", ghost.ghostOX+sliderW/2);
	    centerBowl.attr("cx", ghost.ghostOX+sliderW/2);
	}
	
	public void sliderToFront() {
	    this.toFront();
	    centerBowl.toFront();
	    center.toFront();
	}
    }
    
    private class SliderMechanics extends Rect implements HasMouseDownHandlers, HasMouseUpHandlers, HasMouseMoveHandlers, HasMouseOutHandlers{
	
	double sliderOX;
	double sliderOY;
	double sliderW;
	double sliderH;
	double sliderR;
	double sliderStroke;
	double leftMargin;
	double rightMargin;
	
	MouseDownHandler mouseDown;
	HandlerRegistration downReg;
	
	MouseUpHandler mouseUp;
	HandlerRegistration upReg;
	
	MouseMoveHandler mouseMove;
	HandlerRegistration moveReg;
	
	MouseOutHandler mouseOut;
	HandlerRegistration outReg;
	
	double originX;
	double destX;
	
	public SliderMechanics(double x, double y, double w, double h, double r, double sliderStroke, double rightMargin) {
	    super(x, y, w, h, r);
	    this.sliderOX = x;
	    this.leftMargin = x;
	    this.sliderOY = y;
	    this.sliderW = w;
	    this.sliderH = h;
	    this.sliderR= r;
	    this.sliderStroke = sliderStroke;
	    this.rightMargin = rightMargin;
	    
	    this.attr("fill","#FFF");
	    this.attr("stroke", "#FFF");
	    this.attr("stroke-width", sliderStroke);
	    this.attr("cursor", "pointer");
	    this.attr("opacity", 0.0);
	    
	    initMouseHandlers();
	    downReg = addMouseDownHandler(mouseDown);
	}
	
	public void moveSliderMechs(SliderGhost ghost) {
	    sliderOX = ghost.ghostOX;
	    this.attr("x", sliderOX);
	}
	
	public void sliderToFront() {
	    this.toFront();
	}
	
	private void initMouseHandlers() {
	    mouseDown = new MouseDownHandler() {
		@Override
		public void onMouseDown(MouseDownEvent event) {
		    event.preventDefault();
		    sliderMechs.attr("cursor", "move");
		    slider.attr("cursor", "move");
		    slider.center.attr("cursor", "move");
		    slider.centerBowl.attr("cursor", "move");
		    originX = event.getClientX();
		    
		    upReg = addMouseUpHandler(mouseUp);
		    moveReg = addMouseMoveHandler(mouseMove);
		    outReg = addMouseOutHandler(mouseOut);
		}
	    };
	    
	    mouseUp = new MouseUpHandler() {
		@Override
		public void onMouseUp(MouseUpEvent event) {
		    outReg.removeHandler();
		    moveReg.removeHandler();
		    upReg.removeHandler();
		    determineDropZone();
		    sliderMechs.attr("cursor", "pointer");
		    slider.attr("cursor", "pointer");
		    slider.center.attr("cursor", "pointer");
		    slider.centerBowl.attr("cursor", "pointer");
		    
		}
	    };
	    
	    mouseMove = new MouseMoveHandler() {
		@Override
		public void onMouseMove(MouseMoveEvent event) {
		    event.preventDefault();
		    destX = event.getClientX();
		    double distance = destX - originX;
		    if((sliderMechs.sliderOX+distance) < sliderMechs.leftMargin) {
			distance = sliderMechs.leftMargin - sliderMechs.sliderOX;
		    } else if((sliderMechs.sliderOX+distance) > (rightMargin - sliderW)) {
			distance = (rightMargin - sliderW) - sliderMechs.sliderOX;
		    }
		    originX = destX;
		    sliderMechs.sliderOX += distance;
		    slider.sliderOX += distance;
		    slider.cx = slider.sliderOX + slider.sliderW/2;
		    sliderMechs.attr("x", sliderOX);
		    slider.attr("x", sliderOX);
		    slider.center.attr("cx", sliderOX+sliderW/2);
		    slider.centerBowl.attr("cx", sliderOX+sliderW/2);
		}
	    };
	    
	    mouseOut = new MouseOutHandler() {
		@Override
		public void onMouseOut(MouseOutEvent event) {
		    outReg.removeHandler();
		    moveReg.removeHandler();
		    upReg.removeHandler();
		    determineDropZone();
		    sliderMechs.attr("cursor", "pointer");
		    slider.attr("cursor", "pointer");
		    slider.center.attr("cursor", "pointer");
		    slider.centerBowl.attr("cursor", "pointer");
		}
	    };
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
	    return addDomHandler(handler, MouseDownEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
	    return addDomHandler(handler, MouseUpEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
	    return addDomHandler(handler, MouseMoveEvent.getType());
	}
	
	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
	    return addDomHandler(handler, MouseOutEvent.getType());
	}
    }
    
    private class SliderGhost extends Rect {
	
	double ghostOX;
	double ghostOY;
	double ghostW;
	double ghostH;
	double ghostStroke;
	double ghostPadding;
	int relOrder;
	
	ChoiceLabel label;
	
	public SliderGhost(double x, double y, double w, double h, double ghostStroke, double ghostPadding, int relOrder) {
	    super(x, y, w, h);
	    this.ghostOX = x;
	    this.ghostOY = y;
	    this.ghostW = w;
	    this.ghostH = h;
	    this.relOrder = relOrder;
	    
	    this.attr("stroke-width", ghostStroke);
	    this.attr("fill", "#CFCFCF");
	    this.attr("opacity", 0.0);
	    
	    label = new ChoiceLabel(ghostOX+ghostW/2, oy+height*0.4, choices[relOrder]);
	}
    }
    
    private class ChoiceLabel extends Text implements HasClickHandlers{

	public ChoiceLabel(double cx, double cy, String str) {
	    super(cx, cy, str);
	    this.attr("font-size", getFontSize(2) + "px");
	    this.attr("font-family", "sans-serif");
	    this.attr("cursor", "pointer");
	}
	
	public void onFocus() {
	    this.attr("font-weight","bold");
	}
	public void deFocus() {
	    this.attr("font-weight","normal");
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
	    return addDomHandler(handler, ClickEvent.getType());
	}
	
    }

}
