package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar.ExecutionYearLabel;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar.InarBar;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarBar.TotalsLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class InarWidget extends Composite {

    private int frameWidth;
    private int frameHeight;
    private String dcpId;
    private String eyId;
    private InarServiceAsync inarService;
    private String rawYearLabel;
    private int[] rawInar;

    private XviewsYear window;
    private HorizontalPanel mainPanel;

    private Widget yearCanvas;
    private int yearCanvasWidth;
    private int yearCanvasHeight;

    private Widget inarBarCanvas;
    private int inarBarCanvasWidth;
    private int inarBarCanvasHeight;

    private Widget totalsCanvas;
    private int totalsCanvasWidth;
    private int totalsCanvasHeight;

    public InarWidget(XviewsYear window, int width, int height, String eyId, String dcpId, InarServiceAsync inarService) {
	this.window = window;
	this.frameWidth = width;
	this.frameHeight = height;
	this.dcpId = dcpId;
	this.eyId = eyId;
	this.inarService = inarService;

	mainPanel = new HorizontalPanel();
	initWidget(mainPanel);
	cropCanvas();
	
	loadYear();

    }

    private void cropCanvas() {

	inarBarCanvasWidth = (int) (frameWidth * 0.75);
	yearCanvasWidth = (frameWidth - inarBarCanvasWidth) / 2;
	totalsCanvasWidth = frameWidth - inarBarCanvasWidth - yearCanvasWidth;

	yearCanvasHeight = frameHeight;
	inarBarCanvasHeight = frameHeight;
	totalsCanvasHeight = frameHeight;

    }

    private void loadYear() {
	inarService.getExecutionYear(eyId, new AsyncCallback<String>() {

	    @Override
	    public void onFailure(Throwable caught) {
		window.notifyServiceFailure();
		
	    }

	    @Override
	    public void onSuccess(String result) {
		rawYearLabel = result;
		loadYearLabelWidget();
		
	    }
	    
	});
	
    }
    
    private void loadYearLabelWidget() {
	yearCanvas = new ExecutionYearLabel(yearCanvasWidth, yearCanvasHeight, rawYearLabel);
	mainPanel.insert(yearCanvas, 0);
	loadInar();
    }
    
    private void loadInar() {
	inarService.getInar(eyId, dcpId, new AsyncCallback<int[]>() {

	    @Override
	    public void onFailure(Throwable caught) {
		window.notifyServiceFailure();
	    }

	    @Override
	    public void onSuccess(int[] result) {
		rawInar = result;
		loadInarAndTotalsWidgets();
		
	    }
	    
	});
	
    }
    
    private void loadInarAndTotalsWidgets() {
	inarBarCanvas = new InarBar(window, inarBarCanvasWidth, inarBarCanvasHeight, rawInar);
	mainPanel.insert(inarBarCanvas,1);
	totalsCanvas = new TotalsLabel(totalsCanvasWidth, totalsCanvasHeight, rawInar[4]);
	mainPanel.insert(totalsCanvas,2);
	window.widgetReady();
    }
    
}
