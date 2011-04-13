package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.courseAnalysis;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.InarServiceAsync;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.server.InarServiceImpl;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class CourseGradesHistogram extends Composite {
    
    private InarServiceAsync inarService;
    private String ecId;
    
    private XviewsYear window;
    
    private HorizontalPanel mainPanel;
    
    public CourseGradesHistogram(XviewsYear window, String ecId, InarServiceAsync inarService) {
	this.window = window;
	this.ecId = ecId;
	this.inarService = inarService;
	
	mainPanel = new HorizontalPanel();
	mainPanel.setSpacing(10);
	initWidget(mainPanel);
	loadGradesHistogramData();
    }
    
    private void loadGradesHistogramData() {
	inarService.getGradesDistribution(ecId, new AsyncCallback<int[]>() {

	    @Override
	    public void onFailure(Throwable caught) {
		Window.alert(caught.getMessage());
	    }

	    @Override
	    public void onSuccess(int[] result) {
		loadGradesHistogram(result);
	    }
	    
	});
    }
    
    private void loadGradesHistogram(int[] gradeDistribution) {
	int padding = ((int) Math.floor(gradeDistribution.length * 0.5)) * 2;
	for(int i = 0; i<gradeDistribution.length; i++) {
	    int label = padding + i;
	    Label proxy = new Label(label + " - " + gradeDistribution[i]);
	    mainPanel.add(proxy);
	}
	//Label proxy = new Label("GRADES_HISTOGRAM");
	//mainPanel.add(proxy);
    }

}
