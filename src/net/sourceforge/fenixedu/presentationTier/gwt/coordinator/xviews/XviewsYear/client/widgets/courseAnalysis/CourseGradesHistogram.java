package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.courseAnalysis;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.InarServiceAsync;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.server.InarServiceImpl;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CourseGradesHistogram extends Composite {
    
    private InarServiceAsync inarService;
    private String ecId;
    private int[] gradesDistribution;
    
    private XviewsYear window;
    
    private HorizontalPanel mainPanel;
    
    private Widget gradesHistogramCanvas;
    private int gradesHistogramCanvaslWidth;
    private int gradesHistogramCanvasHeight;
    
    public CourseGradesHistogram(XviewsYear window, int width, int height, String ecId, InarServiceAsync inarService) {
	this.window = window;
	this.ecId = ecId;
	this.inarService = inarService;
	this.gradesHistogramCanvaslWidth = width;
	this.gradesHistogramCanvasHeight = height;
	
	mainPanel = new HorizontalPanel();
	mainPanel.setSpacing(0);
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
    
    private void loadGradesHistogram(int[] gradesDistribution) {
	gradesHistogramCanvas = new GradesHistogramCanvas(window, gradesHistogramCanvaslWidth, gradesHistogramCanvasHeight, gradesDistribution);
	mainPanel.add(gradesHistogramCanvas);
	/*int padding = ((int) Math.floor(gradesDistribution.length * 0.5)) * 2;
	for(int i = 0; i<gradesDistribution.length; i++) {
	    int label = padding + i;
	    Label proxy = new Label(label + " - " + gradesDistribution[i]);
	    mainPanel.add(proxy);
	}*/
    }

}
