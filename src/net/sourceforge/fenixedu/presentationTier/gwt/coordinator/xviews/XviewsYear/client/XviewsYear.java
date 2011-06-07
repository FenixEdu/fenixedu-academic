package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.constants.CatConstants;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.FenixLoadingScreenWidget;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.averageByCurricularYears.CompositeAverageByCurricularYears;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.courseAnalysis.CourseAnalysis;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.detailedBarPopup.DetailPopupCanvas;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.detailedInarPopup.DetailedInarPopup;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarByCurricularYears.CompositeInarByCurricularYears;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarCaption.InarCaption;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.problematicCoursesTree.CompositeProblematicCourses;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.problematicCoursesTree.MultiChoiceSwitcher;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Grid;
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
    
    private final native void addBlockSpan(int height) /*-{
        try{
            var divH;
            if($wnd.document.getElementById('gwt_content').pixelHeight) {
                divH = $wnd.document.getElementById('gwt_content').pixelHeight;
            } else {
                divH = $wnd.document.getElementById('gwt_content').clientHeight;
            }
            divH = divH + height;
            var divHeight = divH + 'px';
            $wnd.document.getElementById('gwt_content').style.height = divHeight;
        } catch(e) {
            alert(e);
        }
    }-*/;
    
    private final native void removeBlockSpan(int height) /*-{
        try{
            var divH;
            if($wnd.document.getElementById('gwt_content').pixelHeight) {
                divH = $wnd.document.getElementById('gwt_content').pixelHeight;
            } else {
                divH = $wnd.document.getElementById('gwt_content').clientHeight;
            }
            divH = divH - height;
            var divHeight = divH + 'px';
            $wnd.document.getElementById('gwt_content').style.height = divHeight;
        } catch(e) {
            alert(e);
        }
    }-*/;
    
    
    public RootPanel content;
    public RootPanel shader;
    public RootPanel overlay;
    private CatConstants bundle;
    public FenixLoadingScreenWidget loadingScreen;
    public InarWidget inarWidget;
    public InarCaption inarCaption;
    public CompositeInarByCurricularYears inarByYear;
    public CompositeAverageByCurricularYears averageByYear;
    public DetailPopupCanvas popupCanvas;
    public DetailedInarPopup popupInar;
    public CompositeProblematicCourses coursesRootWidget;
    public Label problematicCoursesTitle;
    public int blocksHeight;
    public MultiChoiceSwitcher criteriaSwitcher;
    public int criteriaSwitcherState = 0;
    public String[] choices;
    
    public VerticalPanel vPanel;
    public HorizontalPanel rowOne;
    public HorizontalPanel rowTwo;
    public VerticalPanel rowThree;
    public int widgetCnt;
    
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
    
    public CatConstants getBundle() {
	return bundle;
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
	removeBlockSpan(blocksHeight);
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
		notifyServiceFailure();

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
    }
    
    public void backProblematicCourses() {
	initCriteriaSwitcher();
	rowThree.clear();
	addBlockSpan(blocksHeight);
	rowThree.add(problematicCoursesTitle);
	criteriaSwitcher.setStyleName("ExecutiveYear-MultiChoiceSwitcher");
	rowThree.add(criteriaSwitcher);
	rowThree.add(coursesRootWidget);
    }
    
    private void initCriteriaSwitcher() {
	criteriaSwitcher = new MultiChoiceSwitcher(this, 600, 75, choices, criteriaSwitcherState);
    }
    
    public void addBlock(int offsetHeight) {
	blocksHeight += offsetHeight;
	addBlockSpan(offsetHeight);
    }
    
    public void removeBlock(int offsetHeight) {
	blocksHeight -= offsetHeight;
	removeBlockSpan(offsetHeight);
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
	coursesRootWidget = new CompositeProblematicCourses(this, 400, 300, eyId, dcpId, heuristic, inarService);
	rowThree.insert(coursesRootWidget, index);
	removeBlockSpan(blocksHeight);
	blocksHeight = 0;
    }
    
    public void widgetReady() {
	if(--widgetCnt == 0)
	    attachAllWidgets();
    }
    
    private void initInarService() {
	inarService = (InarServiceAsync) GWT.create(InarService.class);
	ServiceDefTarget endpoint = (ServiceDefTarget) inarService;
	String moduleRelativeURL = GWT.getModuleBaseURL() + "InarService.gwt";
	endpoint.setServiceEntryPoint(moduleRelativeURL);
    }
    
    public void notifyServiceFailure() {
	Window.alert(bundle.serviceError());
	overlay.clear();
	shader.clear();
	content.clear();	
    }

    @Override
    public void onModuleLoad() {
	
	content = RootPanel.get("gwt_content");
	shader = RootPanel.get("gwt_shader");
	overlay = RootPanel.get("gwt_overlay");
	
	bundle = (CatConstants)GWT.create(CatConstants.class);
	choices = new String[3];
	choices[0] = bundle.showAll();
	choices[1] = bundle.AB50();
	choices[2] = bundle.FO30();
	
        if (content != null && overlay != null && shader != null) {
            
            defocus();
            
            widgetCnt = 5;
            eyId = getArgs("eyId");
            dcpId = getArgs("dcpId");
            blocksHeight = 0;
            initInarService();
            
            loadingScreen = new FenixLoadingScreenWidget(800,600,bundle.loadingCaption(),bundle.loadingSubcaption());
            content.add(loadingScreen);
            
            inarWidget = new InarWidget(this,600,150,eyId,dcpId,inarService);
            inarCaption = new InarCaption(150, bundle);
            
            inarByYear = new CompositeInarByCurricularYears(this, 400, 250, eyId, dcpId, inarService);
            averageByYear = new CompositeAverageByCurricularYears(this, 400, 250, eyId, dcpId, inarService);
            
            criteriaSwitcher = new MultiChoiceSwitcher(this, 600, 75, choices, criteriaSwitcherState);
            coursesRootWidget = new CompositeProblematicCourses(this, 400, 300, eyId, dcpId, "ShowAll", inarService);
            
        }
    }
    
    private void attachAllWidgets() {
	rowOne = new HorizontalPanel();
        rowOne.insert(inarWidget, 0);
        rowOne.insert(inarCaption,1);
        
        
        rowTwo = new HorizontalPanel();
        rowTwo.setSpacing(50);
        rowTwo.insert(inarByYear, 0);
        rowTwo.insert(averageByYear, 1);
        
        
        rowThree = new VerticalPanel();
        rowThree.setSpacing(20);
        problematicCoursesTitle = new Label(bundle.curricularCourseAnalysis());
        problematicCoursesTitle.setStyleName("ExecutionYear-ProblematicCoursesTitle");
        rowThree.add(problematicCoursesTitle);
        criteriaSwitcher.setStyleName("ExecutiveYear-MultiChoiceSwitcher");
        rowThree.add(criteriaSwitcher);
        rowThree.add(coursesRootWidget);
        
        
        vPanel = new VerticalPanel();
        vPanel.setSpacing(20);
        vPanel.add(rowOne);
        vPanel.add(rowTwo);
        vPanel.add(rowThree);
        
        content.remove(loadingScreen);
        content.add(vPanel);
    }

}
