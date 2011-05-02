package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.problematicCoursesTree;

import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.InarServiceAsync;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CompositeProblematicCourses extends Composite{
    
    private InarServiceAsync inarService;
    private String eyId;
    private String dcpId;
    private String heuristic;
    private Map<Integer, Map<Integer, List<String>>> executionCourses;

    private XviewsYear window;
    
    private VerticalPanel mainPanel;

    public CompositeProblematicCourses(XviewsYear window, int width, int height, String eyId, String dcpId, String heuristic, InarServiceAsync inarService) {
	super();
	this.eyId = eyId;
	this.dcpId = dcpId;
	this.heuristic = heuristic;
	this.inarService = inarService;
	this.window = window;
	
	mainPanel = new VerticalPanel();
	mainPanel.setSpacing(35);
	initWidget(mainPanel);
	loadProblematicCourses();
    }
    
    private void loadProblematicCourses() {
	inarService.getDCPCourses(eyId, dcpId, heuristic, new AsyncCallback<Map<Integer, Map<Integer, List<String>>>>() {

	    @Override
	    public void onFailure(Throwable caught) {
		Window.alert(caught.getMessage());
		executionCourses = null;
		
	    }

	    @Override
	    public void onSuccess(Map<Integer, Map<Integer, List<String>>> result) {
		executionCourses = result;
		loadWidget();
		
	    }
	    
	});
	
    }
    
    private void loadWidget() {
	for(int year : executionCourses.keySet()) {
	    YearBlock yearBlock = new YearBlock(year, executionCourses.get(year), window, inarService);
	    mainPanel.add(yearBlock);
	}
    }
}
