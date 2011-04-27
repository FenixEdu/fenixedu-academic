package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;
import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael.Text;

/**
 * Each slice of the bar will be a clickable rectangle with 2 text areas
 * associated (cardinal and percentage)
 * 
 * @author kurt
 * 
 */
public class InarBar extends Raphael {

    private XviewsYear window;
    
    private int width;
    private int height;
    private int inarBarOX;
    private int inarBarOY;
    private int cx;
    private int cy;
    private int absoluteLeft;
    private int absoluteTop;

    private Raphael.Rect outline;
    private BarSlice IBar;
    private BarSlice NBar;
    private BarSlice ABar;
    private BarSlice RBar;

    private int[] ianrt;

    private InarBar(int width, int height) {
	super(width, height);
    }

    public InarBar(XviewsYear window, int width, int height, int[] inar) {
	this(width, height);
	this.window = window;
	int _width = width - 2;
	int _height = height - 2;

	double proportion = (_width * 1.0) / (_height * 1.0);
	if (proportion <= 7.0) {
	    this.width = _width;
	    this.height = (int) ((this.width) * (1.0 / 7.0));
	} else {
	    this.height = (int) (_height * 0.5);
	    this.width = (int) (this.height * 7.0);
	}

	inarBarOX = (int) ((_width - this.width) / 2.0);
	inarBarOY = (int) ((_height - this.height) / 2.0);

	cx = _width / 2;
	cy = _height / 2;

	this.ianrt = inar;

	initInarBar();
    }

    @Override
    public void onLoad() {
	super.onLoad();

	if (ianrt[0] != 0) {
	    IBar.initText();
	    IBar.setBarHandlers();
	}

	if (ianrt[1] != 0) {
	    ABar.initText();
	    ABar.setBarHandlers();
	}

	if (ianrt[2] != 0) {
	    NBar.initText();
	    NBar.setBarHandlers();
	}
	if (ianrt[3] != 0) {
	    RBar.initText();
	    RBar.setBarHandlers();
	}
    }
    
    public int getThisLeft() {
	return this.getAbsoluteLeft();
    }
    public int getThisTop() {
	return this.getAbsoluteTop();
    }

    private class BarSlice extends Raphael.Rect implements HasClickHandlers {

	private double originX;
	private double originY;
	private double thisWidth;
	private double thisHeight;

	private double thisCX;
	private double thisCY;
	private double percCY;

	private int flavour;
	
	private InarBar parent;
	
	private ClickableText nominalLabel;
	private ClickableText percLabel;
	

	private BarSlice(double x, double y, double w, double h) {
	    super(x, y, w, h);
	}

	public BarSlice(double sliceOX, double sliceOY, double sliceWidth, double sliceHeight, int flavour, InarBar parent) {
	    this(sliceOX, sliceOY, sliceWidth, sliceHeight);
	    originX = sliceOX;
	    originY = sliceOY;
	    thisWidth = sliceWidth;
	    thisHeight = sliceHeight;
	    thisCX = originX + (thisWidth / 2.0);
	    thisCY = originY + (thisHeight / 2.0);
	    percCY = originY - (0.2 * thisHeight);

	    this.flavour = flavour;
	    
	    this.parent = parent;

	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
	    if(nominalLabel != null)
		nominalLabel.addClickHandler(handler);
	    if(percLabel != null)
		percLabel.addClickHandler(handler);
	    return this.addDomHandler(handler, ClickEvent.getType());
	}

	public void initText() {

	    if (((pickCardinal(flavour) * 1.0) / (ianrt[4] * 1.0)) > 0.1) {
		nominalLabel = new ClickableText(thisCX, thisCY, Integer.toString(pickCardinal(flavour)));
		nominalLabel.attr("font-size", getFontSize(4) + "px");
		nominalLabel.attr("font-weight", "bold");
		nominalLabel.attr("font-family", "sans-serif");
		nominalLabel.attr("fill", "#FFFFFF");
		nominalLabel.attr("cursor","pointer");
	    }

	    percLabel = new ClickableText(thisCX, percCY, getPerc(flavour));
	    percLabel.attr("font-size", getFontSize(3) + "px");
	    percLabel.attr("font-family", "sans-serif");
	    percLabel.attr("fill", "#000000");
	    percLabel.attr("cursor","pointer");
	    
	}
	
	public int getThisLeft() {
	    return parent.getThisLeft()+((int)originX);
	}
	public int getThisTop() {
	    return parent.getThisTop()+((int)originY);
	}

	public void setBarHandlers() {
	    this.addClickHandler(new ClickHandler() {

		public void onClick(ClickEvent event) {
		    window.loadDetailedSlicePopup(getThisLeft()+(thisWidth / 2.0), getThisTop()+(thisHeight / 2.0), width, height, pickColor(flavour), pickULabel(flavour), pickLLabel(flavour));
		    window.focus();
		}

	    });
	}
	
	private class ClickableText extends Text implements HasClickHandlers {

	    public ClickableText(double x, double y, String str) {
		super(x, y, str);
	    }

	    public HandlerRegistration addClickHandler(ClickHandler handler) {
		return this.addDomHandler(handler, ClickEvent.getType());
	    }

	}

    }

    private void initInarBar() {

	double outlineStroke = 1;
	double padding = (width * (1.0 / 100.0));

	int validSlots = 0;
	for (int i = 0; i < 5; i++) {
	    if (ianrt[i] != 0)
		validSlots++;
	}

	double innerRemainingW = (width * 1.0) - ((validSlots * 1.0) * padding);
	double remainingH = ((height * 1.0) - (inarBarOY * 1.0)) - (2.0 * padding);

	double sliceOY = ((height * 1.0) - remainingH - padding);
	double sliceHeight = remainingH + inarBarOY;

	outline = new Rect(inarBarOX, inarBarOY, width, height);
	outline.attr("stroke", "#000000");
	outline.attr("stroke-width", outlineStroke);

	double sliceOX = ((inarBarOX * 1.0) + padding);
	double sliceWidth;

	if (ianrt[0] != 0) {
	    sliceWidth = (((ianrt[0] * 1.0) / (ianrt[4] * 1.0)) * innerRemainingW);
	    IBar = new BarSlice(sliceOX, sliceOY, sliceWidth, sliceHeight, 0, this);
	    IBar.attr("fill", "#1888B8");
	    IBar.attr("stroke-width", 0);
	    IBar.attr("cursor","pointer");
	    sliceOX += sliceWidth + padding;
	}

	if (ianrt[1] != 0) {
	    sliceWidth = (((ianrt[1] * 1.0) / (ianrt[4] * 1.0)) * innerRemainingW);
	    ABar = new BarSlice(sliceOX, sliceOY, sliceWidth, sliceHeight, 1, this);
	    ABar.attr("fill", "#39B54A");
	    ABar.attr("stroke-width", 0);
	    ABar.attr("cursor","pointer");
	    sliceOX += sliceWidth + padding;
	}

	if (ianrt[2] != 0) {
	    sliceWidth = (((ianrt[2] * 1.0) / (ianrt[4] * 1.0)) * innerRemainingW);
	    NBar = new BarSlice(sliceOX, sliceOY, sliceWidth, sliceHeight, 2, this);
	    NBar.attr("fill", "#FBB03B");
	    NBar.attr("stroke-width", 0);
	    NBar.attr("cursor","pointer");
	    sliceOX += sliceWidth + padding;
	}

	if (ianrt[3] != 0) {
	    sliceWidth = (((ianrt[3] * 1.0) / (ianrt[4] * 1.0)) * innerRemainingW);
	    RBar = new BarSlice(sliceOX, sliceOY, sliceWidth, sliceHeight, 3, this);
	    RBar.attr("fill", "#ED1C24");
	    RBar.attr("stroke-width", 0);
	    RBar.attr("cursor","pointer");
	    sliceOX += sliceWidth + padding;
	}

    }

    private int getFontSize(int reference) {
	return (int) (Math.sqrt((width * width) + (height * height)) * (reference * 1.0) / 100.0);
    }

    private String getPerc(int flavour) {
	final double abs = ((pickCardinal(flavour) * 1.0) / (ianrt[4] * 1.0)) * 1000.0;
	final double round = abs % 10;
	int perc = (int) (((pickCardinal(flavour) * 1.0) / (ianrt[4] * 1.0)) * 100.0);
	if(round > 4)
	    perc++;
	return (Integer.toString(perc) + "%");
    }

    private String pickColor(int flavour) {
	switch (flavour) {
	case 0:
	    return "#1888B8";
	case 1:
	    return "#39B54A";
	case 2:
	    return "#FBB03B";
	case 3:
	    return "#ED1C24";
	default:
	    return "#000000";
	}
    }
    
    private int pickCardinal(int flavour) {
	return ianrt[flavour];
    }
    
    private String pickULabel(int flavour) {
	switch(flavour) {
	case 0:
	    return "In Frequency:";
	case 1:
	    return "Approved:";
	case 2:
	    return "Non-Evaluated:";
	case 3:
	    return "Flunked:";
	default:
	    return "Kaboom...";
	}
    }
    
    private String pickLLabel(int flavour) {
	switch(flavour) {
	case 0:
	    return (ianrt[0] + " out of " + ianrt[4] + " (" + getPerc(flavour) + ")");
	case 1:
	    return (ianrt[1] + " out of " + ianrt[4] + " (" + getPerc(flavour) + ")");
	case 2:
	    return (ianrt[2] + " out of " + ianrt[4] + " (" + getPerc(flavour) + ")");
	case 3:
	    return (ianrt[3] + " out of " + ianrt[4] + " (" + getPerc(flavour) + ")");
	default:
	    return "It just blew up in your hands :(";
	}
    }

}
