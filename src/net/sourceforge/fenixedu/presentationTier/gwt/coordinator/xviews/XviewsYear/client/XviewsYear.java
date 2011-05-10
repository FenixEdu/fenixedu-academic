package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.averageByCurricularYears.CompositeAverageByCurricularYears;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.courseAnalysis.CourseAnalysis;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.detailedBarPopup.DetailPopupCanvas;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.detailedInarPopup.DetailedInarPopup;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarByCurricularYears.CompositeInarByCurricularYears;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarByCurricularYears.InarByCurricularYears;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarCaption.InarCaption;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.problematicCoursesTree.CompositeProblematicCourses;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.problematicCoursesTree.MultiChoiceSwitcher;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class XviewsYear implements EntryPoint {
    
    private final native String getArgs(String paramId) /*-{
        try{
            var myArg = $wnd.document.getElementById(paramId).value;
            return myArg;
        } catch(e) {
            alert(e);
        }
    }-*/;
    
    
    public RootPanel content;
    public RootPanel shader;
    public RootPanel overlay;
    public DetailPopupCanvas popupCanvas;
    public DetailedInarPopup popupInar;
    public CompositeProblematicCourses coursesRootWidget;
    public Label problematicCoursesTitle;
    public MultiChoiceSwitcher criteriaSwitcher;
    public int criteriaSwitcherState = 0;
    public String[] choices = {"Show All", "Approvals ratio below 50%", "Flunks ratio over 30%"};
    
    public VerticalPanel vPanel;
    public HorizontalPanel rowOne;
    public HorizontalPanel rowTwo;
    public VerticalPanel rowThree;
    
    public String dcpId;
    public String eyId;
    public InarServiceAsync inarService;
    
    public void focus() {
	shader.setVisible(true);
	overlay.setVisible(true);
    }
    
    public void defocus() {
	overlay.setVisible(false);
	shader.setVisible(false);
    }
    
    public void loadDetailedSlicePopup(double x, double y, double w, double h, String color, String uText, String lText) {
	if(overlay != null && shader != null) {
	    if(popupCanvas != null) {
		overlay.remove(popupCanvas);
	    }
	    if(popupInar != null) {
		overlay.remove(popupInar);
	    }
	    int canvasWidth = ((int) (x+(2.0*w)));
	    int canvasHeight = ((int) (y+(2.0*h)));
	    popupCanvas = new DetailPopupCanvas(this,canvasWidth,canvasHeight);
	    popupCanvas.loadDetailedBar(x, y, w, h, color, uText, lText);
	    overlay.add(popupCanvas);
	    defocus();
	}
    }
    
    public void loadDetailedInarPopup(double cx, double cy, String shortYear, int curricularYear, int[] inar) {
	if(overlay != null && shader != null) {
	    if(popupCanvas != null) {
		overlay.remove(popupCanvas);
	    }
	    if(popupInar != null) {
		overlay.remove(popupInar);
	    }
	    int canvasWidth = ((int) (cx+500));
	    int canvasHeight = ((int) (cy+500));
	    popupInar = new DetailedInarPopup(this, canvasWidth, canvasHeight);
	    popupInar.loadDetailedInar(cx, cy, shortYear, curricularYear, inar);
	    overlay.add(popupInar);
	    defocus();
	}	    
    }
    
    public void showDetailedCourse(String ecId) {
	rowThree.clear();
	VerticalPanel vPanel = new VerticalPanel();
	Grid titleGrid = new Grid(2,3);
	titleGrid.getRowFormatter().setVerticalAlign(0, HasVerticalAlignment.ALIGN_MIDDLE);
	titleGrid.getCellFormatter().setStyleName(0, 2, "ExecutionYear-CourseTitleCell");
	titleGrid.getCellFormatter().setStyleName(1, 2, "ExecutiveYear-CourseTitleUnderlying");
	titleGrid.getCellFormatter().setWidth(0, 1, "20px");
	titleGrid.getCellFormatter().setWidth(1, 2, "200px");
	titleGrid.getCellFormatter().setHeight(1, 2, "50px");
	final Image backButton = new Image("../images/left30.png");
	backButton.setStyleName("ExecutionYear-CoursesListIcons");
	final Label courseTitleLabel = new Label();
	AsyncCallback<String> nameRetriever = new AsyncCallback<String>() {

	    @Override
	    public void onFailure(Throwable caught) {
		Window.alert(caught.getMessage());

	    }

	    @Override
	    public void onSuccess(String result) {
		courseTitleLabel.setText(result);

	    }

	};
	inarService.getCourseName(ecId, nameRetriever);
	
	ClickHandler handler = new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		backProblematicCourses();
		
	    }
	};
	backButton.addClickHandler(handler);
	backButton.addStyleName("ExecutionYear-BackIcon");
	courseTitleLabel.addStyleName("ExecutionYear-CourseTitleLabel");
	titleGrid.setWidget(0, 0, backButton);
	titleGrid.setWidget(0, 2, courseTitleLabel);
	vPanel.add(titleGrid);
	final CourseAnalysis courseAnalysis = new CourseAnalysis(this, 1000, 250, ecId, inarService);
	courseAnalysis.setStyleName("ExecutionYear-CourseAnalysisPanel");
	vPanel.add(courseAnalysis);
	rowThree.add(vPanel);
	
	//add a VPanel: 0.Back button; 1.Widget zone
	//on 1: add a HPanel: 0.InarForCourse; 1.HistogramForCourse
    }
    
    public void backProblematicCourses() {
	initCriteriaSwitcher();
	rowThree.clear();
	rowThree.add(problematicCoursesTitle);
	rowThree.add(criteriaSwitcher);
	rowThree.add(coursesRootWidget);
    }
    
    private void initCriteriaSwitcher() {
	criteriaSwitcher = new MultiChoiceSwitcher(this, 600, 75, choices, criteriaSwitcherState);
    }
    
    public void switchCriteria(int newCriteria) {
	criteriaSwitcherState = newCriteria;
	int index = rowThree.getWidgetIndex(coursesRootWidget);
	String heuristic;
	switch(criteriaSwitcherState) {
	case 0:
	    heuristic = "ShowAll";
	    break;
	case 1:
	    heuristic = "AB50";
	    break;
	case 2:
	    heuristic = "FO30";
	    break;
	default: heuristic = "ShowAll";
	}
	
	rowThree.remove(index);
	coursesRootWidget = new CompositeProblematicCourses(this, 400, 1200, eyId, dcpId, heuristic, inarService);
	rowThree.insert(coursesRootWidget, index);
    }
    
    private void initInarService() {
	inarService = (InarServiceAsync) GWT.create(InarService.class);
	ServiceDefTarget endpoint = (ServiceDefTarget) inarService;
	String moduleRelativeURL = GWT.getModuleBaseURL() + "InarService.gwt";
	endpoint.setServiceEntryPoint(moduleRelativeURL);
    }

    @Override
    public void onModuleLoad() {
	
	content = RootPanel.get("gwt_content");
	shader = RootPanel.get("gwt_shader");
	overlay = RootPanel.get("gwt_overlay");
	
        if (content != null && overlay != null && shader != null) {
            
            defocus();
            
            eyId = getArgs("eyId");
            dcpId = getArgs("dcpId");
            initInarService();
            
            rowOne = new HorizontalPanel();
            InarWidget inarWidget = new InarWidget(this,600,150,eyId,dcpId,inarService);
            rowOne.insert(inarWidget, 0);
            
            InarCaption inarCaption = new InarCaption(150);
            rowOne.insert(inarCaption,1);
            
            
            rowTwo = new HorizontalPanel();
            rowTwo.setSpacing(50);
            CompositeInarByCurricularYears inarByYear = new CompositeInarByCurricularYears(this, 400, 250, eyId, dcpId, inarService);
            rowTwo.insert(inarByYear, 0);
            CompositeAverageByCurricularYears averageByYear = new CompositeAverageByCurricularYears(this, 400, 250, eyId, dcpId, inarService);
            rowTwo.insert(averageByYear, 1);
            
            
            rowThree = new VerticalPanel();
            rowThree.setSpacing(20);
            problematicCoursesTitle = new Label("Problematic Courses");
            problematicCoursesTitle.setStyleName("ExecutionYear-ProblematicCoursesTitle");
            rowThree.add(problematicCoursesTitle);
            MultiChoiceSwitcher switcher = new MultiChoiceSwitcher(this, 600, 75, choices, criteriaSwitcherState);
            criteriaSwitcher = switcher;
            rowThree.add(criteriaSwitcher);
            CompositeProblematicCourses problematicCourses = new CompositeProblematicCourses(this, 400, 1200, eyId, dcpId, "ShowAll", inarService);
            coursesRootWidget = problematicCourses;
            rowThree.add(coursesRootWidget);
            
            
            vPanel = new VerticalPanel();
            vPanel.setSpacing(20);
            vPanel.add(rowOne);
            vPanel.add(rowTwo);
            vPanel.add(rowThree);
            
            content.add(vPanel);
        }
    }

}
