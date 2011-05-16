package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.problematicCoursesTree;

import java.util.Arrays;
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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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
    private Grid fallList;
    private Boolean[] fallChecks;
    private Label springTerm;
    private Grid springList;
    private Boolean[] springChecks;

    public YearBlock(int year, Map<Integer, List<String>> data, XviewsYear window, InarServiceAsync inarService) {
	this.year = year;
	detailed = false;
	grid = new Grid(1, 4);
	this.data = data;
	this.window = window;
	this.inarService = inarService;
	
	if(data.get(1) != null) {
	    fallChecks = new Boolean[data.get(1).size()];
	    Arrays.fill(fallChecks, Boolean.FALSE);
	}
	if(data.get(2) != null) {
	    springChecks = new Boolean[data.get(2).size()];
	    Arrays.fill(springChecks, Boolean.FALSE);
	}

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
	window.removeBlock(grid.getOffsetHeight());
	if(data.get(1) != null)
	    Arrays.fill(fallChecks, Boolean.FALSE);
	if(data.get(2) != null)
	    Arrays.fill(springChecks, Boolean.FALSE);
	grid.resizeRows(1);
	setHeader(false);
    }
    
    private boolean dataHasLoaded() {
	boolean fallCheck = true;
	if(fallChecks != null) {
	    for(int index=0; index<fallChecks.length; index++) {
		if(!fallChecks[index]) {
		    fallCheck = false;
		    break;
		}
	    }
	}
	boolean springCheck = true;
	if(springChecks != null) {
	    for(int index=0; index<springChecks.length; index++) {
		if(!springChecks[index]) {
		    springCheck = false;
		    break;
		}
	    }
	}
	return fallCheck && springCheck;
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
	int rows = 1;
	if(data.containsKey(1)) {
	    rows = data.get(1).size()/2;
	    rows += data.get(1).size()%2 == 0 ? 0 : 1;
	}
	fallList = new Grid(rows,2);
	grid.setWidget(1, 3, fallList);
	if (data.containsKey(1)) {
	    int tickets = 0;
	    int row = 0;
	    int column = 0;
	    for (String ecId : data.get(1)) {
		final int ticket = tickets;
		tickets++;
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
			fallChecks[ticket] = true;
			
			if(dataHasLoaded()) {
			    window.addBlock(grid.getOffsetHeight());
			}
		    }

		};
		inarService.getCourseName(ecId, nameRetriever);
		anchor.addClickHandler(provideAnchorHandler(ecId));

		if(rows == 1) {
		    fallList.getCellFormatter().setStyleName(row, column, "ExecutionYear-CourseListCellSingle");
		} else if(row == 0) {
		    fallList.getCellFormatter().setStyleName(row, column, "ExecutionYear-CourseListCellTop");  
		} else if(row == rows-1) {
		    fallList.getCellFormatter().setStyleName(row, column, "ExecutionYear-CourseListCellBottom");
		} else {
		    fallList.getCellFormatter().setStyleName(row, column, "ExecutionYear-CourseListCellMiddle");
		}
		fallList.getCellFormatter().setHorizontalAlignment(row, column, HasHorizontalAlignment.ALIGN_CENTER);
		fallList.setWidget(row, column, anchor);
		if(++column == 2) {
		    column = 0;
		    row++;
		}
	    }
	}
    }

    private void showSpringDetails() {
	springTerm = new Label("Spring Term");
	grid.getCellFormatter().setStyleName(2, 2, "ExecutionYear-TermTags");
	grid.setWidget(2, 2, springTerm);
	int rows = 1;
	if(data.containsKey(2)) {
	    rows = data.get(2).size()/2;
	    rows += data.get(2).size()%2 == 0 ? 0 : 1;
	}
	springList = new Grid(rows,2);
	grid.setWidget(2, 3, springList);
	if (data.containsKey(2)) {
	    int tickets = 0;
	    int row = 0;
	    int column = 0;
	    for (String ecId : data.get(2)) {
		final int ticket = tickets;
		tickets++;
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
			springChecks[ticket] = true;
			
			if(dataHasLoaded()) {
			    window.addBlock(grid.getOffsetHeight());
			}
		    }

		};
		inarService.getCourseName(ecId, nameRetriever);
		anchor.addClickHandler(provideAnchorHandler(ecId));

		if(rows == 1) {
		    springList.getCellFormatter().setStyleName(row, column, "ExecutionYear-CourseListCellSingle");
		} else if(row == 0) {
		    springList.getCellFormatter().setStyleName(row, column, "ExecutionYear-CourseListCellTop");  
		} else if(row == rows-1) {
		    springList.getCellFormatter().setStyleName(row, column, "ExecutionYear-CourseListCellBottom");
		} else {
		    springList.getCellFormatter().setStyleName(row, column, "ExecutionYear-CourseListCell");
		}
		springList.getCellFormatter().setHorizontalAlignment(row, column, HasHorizontalAlignment.ALIGN_CENTER);
		springList.setWidget(row, column, anchor);
		if(++column == 2) {
		    column = 0;
		    row++;
		}
	    }
	}
    }
}
