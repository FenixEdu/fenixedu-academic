package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.inarByCurricularYears;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.InarServiceAsync;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class CompositeInarByCurricularYears extends Composite {

    private InarServiceAsync inarService;
    private String eyId;
    private String dcpId;
    private int[][] inarData;
    private int numberOfCurricularYears;
    private String yearTag;

    private XviewsYear window;
    private int width;
    private int height;

    private HorizontalPanel mainPanel;
    private InarByCurricularYears inarByCurricularYears;

    public CompositeInarByCurricularYears(XviewsYear window, int width, int height, String eyId, String dcpId,
            InarServiceAsync inarService) {
        super();
        this.width = width;
        this.height = height;
        this.eyId = eyId;
        this.dcpId = dcpId;
        this.inarService = inarService;
        this.window = window;

        mainPanel = new HorizontalPanel();
        initWidget(mainPanel);
        loadExecutionYearTag();
    }

    private void loadExecutionYearTag() {
        inarService.getExecutionYear(eyId, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                window.notifyServiceFailure();

            }

            @Override
            public void onSuccess(String result) {
                yearTag = result;
                loadCurricularYears();

            }

        });

    }

    private void loadCurricularYears() {
        inarService.getNumberOfCurricularYears(dcpId, new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
                window.notifyServiceFailure();

            }

            @Override
            public void onSuccess(Integer result) {
                numberOfCurricularYears = result;
                loadInarData();
            }

        });
    }

    private void loadInarData() {
        inarService.getInarByCurricularYears(eyId, dcpId, new AsyncCallback<int[][]>() {

            @Override
            public void onFailure(Throwable caught) {
                window.notifyServiceFailure();

            }

            @Override
            public void onSuccess(int[][] result) {
                inarData = result;
                loadWidget();
            }

        });
    }

    private void loadWidget() {
        inarByCurricularYears = new InarByCurricularYears(window, width, height, numberOfCurricularYears, inarData, yearTag);
        mainPanel.add(inarByCurricularYears);
        window.widgetReady();
    }

}
