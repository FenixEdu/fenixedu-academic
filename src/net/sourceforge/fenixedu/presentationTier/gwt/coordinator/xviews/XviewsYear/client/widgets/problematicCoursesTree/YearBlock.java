package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.problematicCoursesTree;

import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.InarServiceAsync;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class YearBlock extends Composite {
    protected int year;
    protected boolean detailed;
    protected Grid grid;
    protected Map<Integer, List<String>> data;
    protected XviewsYear window;
    protected InarServiceAsync inarService;
    protected static final String rightIconURL = "../images/right30.png";
    protected static final String downIconURL = "../images/down30.png";

    private Image ico;
    private Label yearLabel;
    private Label fallTerm;
    private FlowPanel fallList;
    private Label springTerm;
    private FlowPanel springList;

    public YearBlock(int year, Map<Integer, List<String>> data, XviewsYear window, InarServiceAsync inarService) {
	this.year = year;
	detailed = false;
	grid = new Grid(1, 4);
	this.data = data;
	this.window = window;
	this.inarService = inarService;

	initWidget(grid);
	setHeader(detailed);
    }

    private String getYearLabel(int year) {
	switch (year) {
	case 1:
	    return "1st Year";
	case 2:
	    return "2nd Year";
	case 3:
	    return "3rd Year";
	case 4:
	    return "4th Year";
	case 5:
	    return "5th Year";
	default:
	    return "NA";
	}
    }

    private ClickHandler provideGlidingHandler() {
	ClickHandler handler = new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		if (!detailed) {
		    showDetails();
		    detailed = true;
		} else {
		    hideDetails();
		    detailed = false;
		}
	    }

	};
	return handler;
    }

    private ClickHandler provideAnchorHandler(final String ecId) {
	ClickHandler handler = new ClickHandler() {

	    @Override
	    public void onClick(ClickEvent event) {
		window.showDetailedCourse(ecId);

	    }
	};
	return handler;
    }

    private void showDetails() {
	grid.resizeRows(3);
	setHeader(true);
	showFallDetails();
	showSpringDetails();
    }

    private void hideDetails() {
	grid.resizeRows(1);
	setHeader(false);
    }

    private void setHeader(boolean detailed) {
	if (detailed)
	    ico = new Image(downIconURL);
	else
	    ico = new Image(rightIconURL);
	ico.setStyleName("ExecutionYear-CoursesListIcons");
	ico.addClickHandler(provideGlidingHandler());
	grid.setWidget(0, 0, ico);

	yearLabel = new Label(getYearLabel(year));
	yearLabel.addClickHandler(provideGlidingHandler());
	grid.getCellFormatter().setStyleName(0, 1, "ExecutionYear-YearTags");
	grid.setWidget(0, 1, yearLabel);
    }

    private void showFallDetails() {
	fallTerm = new Label("Fall Term");
	grid.getCellFormatter().setStyleName(1, 2, "ExecutionYear-TermTags");
	grid.setWidget(1, 2, fallTerm);
	fallList = new FlowPanel();
	grid.setWidget(1, 3, fallList);
	if (data.containsKey(1)) {
	    for (String ecId : data.get(1)) {
		final Anchor anchor = new Anchor();
		anchor.addStyleName("ExecutionYear-CourseListItem");
		AsyncCallback<String> nameRetriever = new AsyncCallback<String>() {

		    @Override
		    public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());

		    }

		    @Override
		    public void onSuccess(String result) {
			anchor.setText(result);

		    }

		};
		inarService.getCourseName(ecId, nameRetriever);
		anchor.addClickHandler(provideAnchorHandler(ecId));

		fallList.add(anchor);
	    }
	}
    }

    private void showSpringDetails() {
	springTerm = new Label("Spring Term");
	grid.getCellFormatter().setStyleName(2, 2, "ExecutionYear-TermTags");
	grid.setWidget(2, 2, springTerm);
	springList = new FlowPanel();
	grid.setWidget(2, 3, springList);
	if (data.containsKey(2)) {
	    for (String ecId : data.get(2)) {
		final Anchor anchor = new Anchor();
		anchor.addStyleName("ExecutionYear-CourseListItem");
		AsyncCallback<String> nameRetriever = new AsyncCallback<String>() {

		    @Override
		    public void onFailure(Throwable caught) {
			Window.alert(caught.getMessage());

		    }

		    @Override
		    public void onSuccess(String result) {
			anchor.setText(result);

		    }

		};
		inarService.getCourseName(ecId, nameRetriever);
		anchor.addClickHandler(provideAnchorHandler(ecId));

		springList.add(anchor);
	    }
	}
    }
}
