package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.courseAnalysis;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.InarServiceAsync;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class CourseAnalysis extends Composite {
    
    private int width;
    private int height;
    private InarServiceAsync inarService;
    private String ecId;
    
    private XviewsYear window;
    
    private HorizontalPanel mainPanel;
    
    public CourseAnalysis(XviewsYear window, int width, int height, String ecId, InarServiceAsync inarService) {
	this.width = width;
	this.height = height;
	this.window = window;
	this.ecId = ecId;
	this.inarService = inarService;
	
	mainPanel = new HorizontalPanel();
	mainPanel.setSpacing(50);
	initWidget(mainPanel);
	loadCourseAnalysisWidgets();
    }
    
    private void loadCourseAnalysisWidgets() {
	// this.width = courseInarW + histoW + spacing
	CourseInar courseInar = new CourseInar(window, 375, height, ecId, inarService);
	mainPanel.add(courseInar);
	CourseGradesHistogram gradesHistogram = new CourseGradesHistogram(window, 375, height, ecId, inarService);
	mainPanel.add(gradesHistogram);
    }

}
