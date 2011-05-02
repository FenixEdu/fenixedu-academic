package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.courseAnalysis;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.PathBuilder;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;

public class GradesHistogramCanvas extends Raphael {
    
    private double width;
    private double height;
    private double ox;
    private double oy;
    private int[] gradesDist;
    private double innerProd;
    private double xAxisPosPad;
    private double[] xAxisPos;
    private double[] yAxisPos;
    private double rulersOX;
    private double rulersOY;
    private Spotter[] spotters;
    private String xLabel;
    private String yLabel;

    private GradesHistogramCanvas(int width, int height) {
	super(width, height);
    }
    
    public GradesHistogramCanvas(XviewsYear window, int width, int height, int[] gradesDistribution, String xLabel, String yLabel) {
	this(width, height);
	this.width = width;
	this.height = height;
	ox = 0.0;
	oy = 0.0;
	this.gradesDist = gradesDistribution;
	innerProd = Math.sqrt((width*width) + (height*height));
	xAxisPos = new double[gradesDist.length];
	yAxisPos = new double[gradesDist.length];
	spotters = new Spotter[gradesDist.length];
	this.xLabel = xLabel;
	this.yLabel = yLabel;
	//drawOutline();
	drawAxis();
	drawChart();
	drawBubbles();
	drawLabels();
    }
    
    private void drawOutline() {
	final Rect outline = new Rect(ox, oy, width-1, height-1);
	outline.attr("stroke", "black");
	outline.attr("stroke-width", 1);
    }
    
    private void drawAxis() {
	final double xRuleOX = (ox*1.0) + (width*0.25); //0.2
	final double xRuleOY = (oy*1.0) + (height*0.75); //0.8
	final double xRuleW = width * 0.65; //0.7
	final double xRuleH = innerProd * 0.01;
	final Rect xAxis = new Rect(xRuleOX, xRuleOY, xRuleW, xRuleH);
	xAxis.attr("fill","#35697C");
	xAxis.attr("stroke-width", 0);
	
	final double yRuleOX = (ox*1.0) + (width*0.2); //0.15
	final double yRuleOY = (oy*1.0) + (height*0.1); //0.1
	final double yRuleW = innerProd * 0.01;
	final double yRuleH = height * 0.6; //0.65
	final Rect yAxis = new Rect(yRuleOX, yRuleOY, yRuleW, yRuleH);
	yAxis.attr("fill","#35697C");
	yAxis.attr("stroke-width", 0);
	
	
	double scaleOX = xRuleOX;
	double scaleOY = xRuleOY + xRuleH;
	double scaleW = xRuleW * 0.01;
	double scaleH = xRuleH * 0.75;
	double xAdvance = (xRuleW/(gradesDist.length - 1)) - ((gradesDist.length * scaleW) / (gradesDist.length - 1)) + scaleW;
	xAxisPosPad = xAdvance;
	int startIndex = 10;
	int endIndex = 20;
	if(gradesDist.length == 3) {
	    startIndex = 3;
	    endIndex = 5;
	}
	rulersOY = xRuleOY;
	
	for(int i = startIndex; i <= endIndex; i++) {
	    final Rect scale = new Rect(scaleOX, scaleOY, scaleW, scaleH);
	    scale.attr("fill","#35697C");
	    scale.attr("stroke-width", 0);
	    
	    final Text scaleLabel = new Text(scaleOX+(scaleW/2), scaleOY+(4*scaleH), Integer.toString(i));
	    scaleLabel.attr("font-size", getFontSize(2) + "px");
	    scaleLabel.attr("font-family", "sans-serif");
	    scaleLabel.attr("font-weight", "bold");
	    
	    xAxisPos[i-startIndex] = scaleOX+(scaleW/2);
	    
	    scaleOX += xAdvance;
	}
	
	
	startIndex = 0;
	endIndex = 5;
	int maxGradeDist = gradesDist[0];
	int minGradeDist = gradesDist[0];
	for(int j = 1; j < gradesDist.length; j++) {
	    if(maxGradeDist < gradesDist[j])
		maxGradeDist = gradesDist[j];
	    if(minGradeDist > gradesDist[j])
		minGradeDist = gradesDist[j];
	}
	int range = maxGradeDist - minGradeDist;
	int bottom = minGradeDist;
	int steps = (range + (endIndex - (range % endIndex))) / endIndex;
	int top = bottom + range + (endIndex - (range % endIndex));
	
	setYAxisPos(yRuleH, yRuleOY, bottom, top);
	
	scaleW = xRuleH * 0.75;
	scaleH = xRuleW * 0.01;
	scaleOX = yRuleOX - scaleW;
	scaleOY = yRuleOY + yRuleH - scaleH;
	double yAdvance = (yRuleH/(endIndex*1.0)) - (((endIndex+1.0) * scaleH) / (endIndex*1.0)) + scaleH;
	rulersOX = yRuleOX+yRuleW;
	
	for(int k = startIndex; k <= endIndex; k++) {
	    final Rect scale = new Rect(scaleOX, scaleOY, scaleW, scaleH);
	    scale.attr("fill","#35697C");
	    scale.attr("stroke-width", 0);
	    
	    final Text scaleLabel = new Text(scaleOX-(4*scaleW), scaleOY+(2*scaleH), Integer.toString(bottom + k*steps));
	    scaleLabel.attr("font-size", getFontSize(2) + "px");
	    scaleLabel.attr("font-family", "sans-serif");
	    scaleLabel.attr("font-weight", "bold");
	    
	    if(k!=0) {
		final Rect background = new Rect(xRuleOX, scaleOY+(scaleH/2), xRuleW, yAdvance);
		String colorCode = "#EDF5F7";
		if(k%2 == 1) {
		    colorCode = "#E4F0F5";
		}
		background.attr("fill", colorCode);
		background.attr("stroke-width", 0);
	    }
	    scaleOY -= yAdvance;
	}
    }
    
    private void drawChart() {
	final double halfAScale = width * 0.0035;
	final double chartOX = (ox*1.0) + (width*0.25) + halfAScale;
	double difY;
	
	PathBuilder chartBuilder = new PathBuilder();
	chartBuilder.M(chartOX, yAxisPos[0]);
	for(int i=1; i<gradesDist.length; i++) {
	    difY = yAxisPos[i] - yAxisPos[i-1];
	    chartBuilder.l(xAxisPosPad, difY);
	}
	
	final Path chart = new Path(chartBuilder);
	chart.attr("stroke", "black");
    }
    
    private void drawBubbles() {
	for(int i=0; i<gradesDist.length; i++) {
	    final InteractiveCircle core = new InteractiveCircle(xAxisPos[i], yAxisPos[i], innerProd*0.006);
	    core.attr("stroke-width",0);
	    core.attr("fill","#35697C");
	    core.attr("opacity", 0.0);
	    
	    final InteractiveCircle bubble = new InteractiveCircle(xAxisPos[i], yAxisPos[i], 0.0);
	    bubble.attr("stroke","#35697C");
	    bubble.attr("stroke-width",innerProd*0.005);
	    bubble.attr("stroke-dasharray",".");
	    bubble.attr("opacity", 0.0);
	    
	    final Spotter spotter = new Spotter(xAxisPos[i], yAxisPos[i], innerProd*0.02, i);
	    spotter.attr("opacity", 0.0);
	    spotter.attr("fill", "black");
	    spotter.attr("stroke-width", 0);
	    spotter.setCore(core);
	    spotter.setAureola(bubble);
	    spotter.setAureolaRadius(innerProd*0.02);
	    spotter.addMouseOverHandler(new MouseOverHandler() {
	        
	        @Override
	        public void onMouseOver(MouseOverEvent event) {
	            JSONObject coreAnimeParams = new JSONObject();
	            coreAnimeParams.put("opacity", new JSONString("1.0"));
	            spotter.getCore().animate(coreAnimeParams, 100);
	            
	            spotter.getAureola().attr("opacity", 1.0);
	            
	            JSONObject bubbleAnimeParams = new JSONObject();
	            bubbleAnimeParams.put("r", new JSONString(Double.toString(spotter.getAureolaRadius())));
	            bubbleAnimeParams.put("opacity", new JSONString("0.3"));
	            spotter.getAureola().animate(bubbleAnimeParams, 100);
	        }
	    });
	    spotter.addMouseOutHandler(new MouseOutHandler() {
	        
	        @Override
	        public void onMouseOut(MouseOutEvent event) {
	            if(!spotter.isFocus) {
	        	JSONObject coreAnimeParams = new JSONObject();
		        coreAnimeParams.put("opacity", new JSONString("0.0"));
		        spotter.getCore().animate(coreAnimeParams, 1);
	            }
	            
	            JSONObject bubbleAnimeParams = new JSONObject();
	            bubbleAnimeParams.put("r", new JSONString("0.0"));
	            bubbleAnimeParams.put("opacity", new JSONString("0.0"));
	            spotter.getAureola().animate(bubbleAnimeParams, 1);
	        }
	    });
	    spotter.addClickHandler(new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
		    spotter.toggleOnClickFocus();
		    //Window.alert(Integer.toString(gradesDist[spotter.relOrder]));
		}
		
	    });
	    spotter.attr("cursor","pointer");
	    spotters[i] = spotter;
	}
    }
    
    private void setYAxisPos(double yRuleH, double topPadding, int bottom, int top) {
	int relGrade;
	double relPos;
	for(int i=0; i<gradesDist.length; i++) {
	    relGrade = gradesDist[i] - bottom;
	    relPos = (relGrade*1.0)/(top*1.0);
	    yAxisPos[i] = yRuleH - (relPos * yRuleH) + topPadding;
	}
    }
    
    private int getFontSize(int reference) {
	return (int) (innerProd * (reference * 1.0) / 100.0);
    }
    
    private double getRulersOX() {
	return rulersOX;
    }
    
    private double getRulersOY() {
	return rulersOY;
    }
    
    private void drawLabels() {
	final double yLabelCX = (0.0) + (width*0.15) * 0.5;
	final double yLabelCY = (0.0) + (height*0.1) + (height * 0.325);
	final Text yAxisLabel = new Text(yLabelCX, yLabelCY, yLabel);
	yAxisLabel.attr("font-size", getFontSize(3) + "px");
	yAxisLabel.attr("font-family", "sans-serif");
	
	final double xLabelCX = ((ox*1.0) + (width*0.575));
	final double xLabelCY = (0.0) + (height*0.9);
	final Text xAxisLabel = new Text(xLabelCX, xLabelCY, xLabel);
	xAxisLabel.attr("font-size", getFontSize(3) + "px");
	xAxisLabel.attr("font-family", "sans-serif");
    }
    
    private class Spotter extends Circle implements HasMouseOverHandlers, HasMouseOutHandlers, HasClickHandlers {
	
	double cx;
	double cy;
	int relOrder;
	boolean isFocus;
	InteractiveCircle core;
	InteractiveCircle aureola;
	Path hRule;
	Path vRule;
	Rect label;
	Text labelText;
	double aureolaRadius;

	public Spotter(double x, double y, double r, int relOrder) {
	    super(x, y, r);
	    this.cx = x;
	    this.cy = y;
	    this.relOrder = relOrder;
	    this.isFocus = false;
	    drawOnClickFocus();
	}
	
	public InteractiveCircle getCore() {
	    return core;
	}
	
	public void setCore(InteractiveCircle core) {
	    this.core = core;
	}
	
	public InteractiveCircle getAureola() {
	    return aureola;
	}
	
	public void setAureola(InteractiveCircle aureola) {
	    this.aureola = aureola;
	}
	
	public double getAureolaRadius() {
	    return aureolaRadius;
	}
	
	public void setAureolaRadius(double radius) {
	    this.aureolaRadius = radius;
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
	    return addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
	    core.addMouseOutHandler(handler);
	    aureola.addMouseOutHandler(handler);
	    return addDomHandler(handler, MouseOutEvent.getType());
	}
	
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
	    return addDomHandler(handler, ClickEvent.getType());
	}
	
	public void drawOnClickFocus() {
	    final PathBuilder hRuleBuilder = new PathBuilder();
	    hRuleBuilder.M(getRulersOX(), yAxisPos[relOrder]);
	    hRuleBuilder.L(cx, yAxisPos[relOrder]);
	    hRule = new Path(hRuleBuilder);
	    hRule.attr("stroke","#35697C");
	    hRule.attr("stroke-dasharray","-");
	    hRule.attr("opacity","0.0");
	    
	    final PathBuilder vRuleBuilder = new PathBuilder();
	    vRuleBuilder.M(xAxisPos[relOrder], getRulersOY());
	    vRuleBuilder.L(xAxisPos[relOrder], cy);
	    vRule = new Path(vRuleBuilder);
	    vRule.attr("stroke","#35697C");
	    vRule.attr("stroke-dasharray","-");
	    vRule.attr("opacity","0.0");
	    
	    label = new Rect(cx - innerProd*0.03, cy - innerProd*0.07, innerProd*0.06, innerProd*0.04, innerProd*0.012);
	    label.attr("fill", "#F5DCA6");
	    label.attr("stroke", "#F5CE78");
	    label.attr("stroke-width", innerProd*0.01);
	    label.attr("opacity","0.0");
	    
	    labelText = new Text(cx, cy - innerProd*0.04, Integer.toString(gradesDist[relOrder]));
	    labelText.attr("font-size", getFontSize(3) + "px");
	    labelText.attr("font-family", "sans-serif");
	    labelText.attr("stroke","#35697C");
	    labelText.attr("opacity","0.0");
	}
	
	public void hideOnClickFocus() {
	    core.attr("opacity","0.0");
	    hRule.attr("opacity","0.0");
	    vRule.attr("opacity","0.0");
	    label.attr("opacity","0.0");
	    labelText.attr("opacity","0.0");
	}
	
	public void toggleOnClickFocus() {
	    for(int i=0; i<gradesDist.length; i++) {
		if(i != relOrder) {
		    spotters[i].hideOnClickFocus();
		    spotters[i].isFocus = false;
		}
	    }
	    if(isFocus) {
		hideOnClickFocus();
	    } else {
		hRule.attr("opacity","1.0");
		vRule.attr("opacity","1.0");
		label.attr("opacity","1.0");
		labelText.attr("opacity","1.0");
	    }
	    isFocus = !isFocus;
	}
	
    }
    
    private class InteractiveCircle extends Circle implements HasMouseOverHandlers, HasMouseOutHandlers  {

	public InteractiveCircle(double x, double y, double r) {
	    super(x, y, r);
	}
	
	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
	    return addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
	    return addDomHandler(handler, MouseOutEvent.getType());
	}
	
    }
    
    /*
     * COLOR CODES - Fenix's Petrol Blues
     * 
     * Darker
     * 	#143541
     * 	#35697C
     * 	#58ADCC
     * 	#AEE0F2
     * 	#C7E7F2
     * 	#E4F0F5
     * 	#EDF5F7
     * Lighter
     * 
     */

}
