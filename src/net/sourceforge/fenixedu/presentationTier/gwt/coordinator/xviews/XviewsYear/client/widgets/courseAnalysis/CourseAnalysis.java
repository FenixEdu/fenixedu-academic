package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.courseAnalysis;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.InarServiceAsync;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CourseAnalysis extends Composite {
    
    private int width;
    private int height;
    private InarServiceAsync inarService;
    private String ecId;
    
    private XviewsYear window;
    
    private Grid mainPanel;
    
    public CourseAnalysis(XviewsYear window, int width, int height, String ecId, InarServiceAsync inarService) {
	this.width = width;
	this.height = height;
	this.window = window;
	this.ecId = ecId;
	this.inarService = inarService;
	
	mainPanel = new Grid(2,2);
	initWidget(mainPanel);
	loadCourseAnalysisWidgets();
    }
    
    private void loadCourseAnalysisWidgets() {
	final Label inarLabel = new Label(window.getBundle().enrollmentsStatus());
	mainPanel.getCellFormatter().setStyleName(0, 0, "ExecutionYear-CourseWidgetTitleCell");
	mainPanel.setWidget(0, 0, inarLabel);
	
	final Label gradesDistLabel = new Label(window.getBundle().gradesDistribution());
	mainPanel.getCellFormatter().setStyleName(0, 1, "ExecutionYear-CourseWidgetTitleCell");
	mainPanel.setWidget(0, 1, gradesDistLabel);
	
	CourseInar courseInar = new CourseInar(window, 375, height, ecId, inarService);
	mainPanel.getCellFormatter().setStyleName(1, 0, "ExecutionYear-CourseWidgetCell");
	mainPanel.setWidget(1, 0, courseInar);
	
	CourseGradesHistogram gradesHistogram = new CourseGradesHistogram(window, 375, height, ecId, inarService);	
	mainPanel.getCellFormatter().setStyleName(1, 1, "ExecutionYear-CourseWidgetCell");
	mainPanel.setWidget(1, 1, gradesHistogram);
    }

}
