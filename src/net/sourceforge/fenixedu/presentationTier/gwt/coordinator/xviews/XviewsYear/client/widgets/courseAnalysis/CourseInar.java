package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.courseAnalysis;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.InarServiceAsync;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar.ExecutionYearLabel;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar.InarBar;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar.TotalsLabel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CourseInar extends Composite {
    
    private int frameWidth;
    private int frameHeight;
    private InarServiceAsync inarService;
    private String ecId;
    private int[] ecInar;
    
    private XviewsYear window;
    
    private HorizontalPanel mainPanel;
    
    private Widget courseInarLabel;
    private int courseInarLabelWidth;
    private int courseInarLabelHeight;
    
    private Widget courseInar;
    private int courseInarWidth;
    private int courseInarHeight;
    
    private Widget courseInarTotals;
    private int courseInarTotalsWidth;
    private int courseInarTotalsHeight;
    
    public CourseInar(XviewsYear window, int width, int height, String ecId, InarServiceAsync inarService) {
	this.window = window;
	this.ecId = ecId;
	this.inarService = inarService;
	this.frameWidth = width;
	this.frameHeight = height;
	
	mainPanel = new HorizontalPanel();
	mainPanel.setSpacing(10);
	initWidget(mainPanel);
	cropCanvas();
	arrangeWidgets();
	loadCourseInarLabelData();
	loadCourseInarData();
    }
    
    private void cropCanvas() {

	courseInarWidth = (int) (frameWidth * 0.75);
	courseInarLabelWidth = (frameWidth - courseInarWidth) / 2;
	courseInarTotalsWidth = frameWidth - courseInarWidth - courseInarLabelWidth;

	courseInarLabelHeight = frameHeight;
	courseInarHeight = frameHeight;
	courseInarTotalsHeight = frameHeight;

    }
    
    private void arrangeWidgets() {
	courseInarLabel = new Label();
	mainPanel.add(courseInarLabel);
	
	courseInar = new Label();
	mainPanel.add(courseInar);
	
	courseInarTotals = new Label();
	mainPanel.add(courseInarTotals);
    }
    
    private void loadCourseInarLabelData() {
	inarService.getCourseInarLabel(ecId, new AsyncCallback<String[]>() {

	    @Override
	    public void onFailure(Throwable caught) {
		Window.alert(caught.getMessage());
	    }

	    @Override
	    public void onSuccess(String[] result) {
		loadCourseInarLabel(result);
	    }
	    
	});
    }
    
    private void loadCourseInarLabel(String[] label) {
	mainPanel.remove(courseInarLabel);
	courseInarLabel = new CourseInarLabel(courseInarLabelWidth, courseInarLabelHeight, label[0], label[1]);
	mainPanel.insert(courseInarLabel, 0);
    }
    
    private void loadCourseInarData() {
	inarService.getInarByExecutionCourse(ecId, new AsyncCallback<int[]>() {

	    @Override
	    public void onFailure(Throwable caught) {
		Window.alert(caught.getMessage());
	    }

	    @Override
	    public void onSuccess(int[] result) {
		ecInar = result;
		loadCourseInar();
		loadCourseInarTotals();
	    }
	    
	});
    }
    
    private void loadCourseInar() {
	mainPanel.remove(courseInar);
	courseInar = new InarBar(window, courseInarWidth, courseInarHeight, ecInar);
	mainPanel.insert(courseInar, 1);
    }
    
    private void loadCourseInarTotals() {
	mainPanel.remove(courseInarTotals);
	courseInarTotals = new CourseInarTotals(courseInarTotalsWidth, courseInarTotalsHeight, ecInar[4]);
	mainPanel.insert(courseInarTotals, 2);
    }

}
