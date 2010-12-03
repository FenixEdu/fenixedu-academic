package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar;

import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;

public class ExecutionYearLabel extends Raphael{
    
    private int width;
    private int height;
    private int cx;
    private int cy;
    private Text yearLabel;
    private String yearAcronym;

    private ExecutionYearLabel(int width, int height) {
	super(width, height);
    }
    
    public ExecutionYearLabel(int width, int height, String yearAcronym) {
	this(width, height);
	this.width = width;
	this.height = height;
	this.yearAcronym = yearAcronym;
	
	cx = width/2;
	cy = height/2;
    }
    
    @Override
    public void onLoad() {
      super.onLoad();
      
      yearLabel = new Text(cx, cy, yearAcronym);
      yearLabel.attr("font-size", getFontSize()+"px");
      yearLabel.attr("font-weight","bold");
      yearLabel.attr("font-family","sans-serif");
      yearLabel.attr("fill","#000000");
      
    }
    
    private int getFontSize() {
	return (int) (Math.sqrt((width*width) + (height*height)) * 12.0/100.0);
    }

}
