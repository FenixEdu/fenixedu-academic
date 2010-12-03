package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar;

import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;

public class TotalsLabel extends Raphael{

    private int width;
    private int height;
    private int cx;
    private int cy;
    private int totals;
    
    private TotalsLabel(int width, int height) {
	super(width, height);
    }
    
    public TotalsLabel(int width, int height, int totals) {
	this(width, height);
	
	this.width = width;
	this.height = height;
	this.totals = totals;
	
	cx = width/2;
	cy = height/2;
    }
    
    @Override
    public void onLoad() {
      super.onLoad();
      
      final Text totalsLabel = new Text(cx, cy, Integer.toString(totals));
      totalsLabel.attr("font-size", getFontSize()+"px");
      totalsLabel.attr("font-weight","bold");
      totalsLabel.attr("font-family","sans-serif");
      totalsLabel.attr("fill","#000000");
      
    }
    
    private int getFontSize() {
	return (int) (Math.sqrt((width*width) + (height*height)) * 14.0/100.0);
    }

}
