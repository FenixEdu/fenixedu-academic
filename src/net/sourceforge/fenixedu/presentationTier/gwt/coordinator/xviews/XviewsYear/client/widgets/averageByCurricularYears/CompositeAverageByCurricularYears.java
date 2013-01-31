package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.averageByCurricularYears;

import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.InarServiceAsync;
import net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.XviewsYear;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class CompositeAverageByCurricularYears extends Composite {

	private InarServiceAsync inarService;
	private String eyId;
	private String dcpId;
	private double[] averageData;
	private int numberOfCurricularYears;
	private String yearTag;

	private XviewsYear window;
	private int width;
	private int height;

	private HorizontalPanel mainPanel;
	private AverageByCurricularYears averageByCurricularYears;

	public CompositeAverageByCurricularYears(XviewsYear window, int width, int height, String eyId, String dcpId,
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
				loadAverageData();
			}

		});
	}

	private void loadAverageData() {
		inarService.getAverageByCurricularYears(eyId, dcpId, new AsyncCallback<double[]>() {

			@Override
			public void onFailure(Throwable caught) {
				window.notifyServiceFailure();

			}

			@Override
			public void onSuccess(double[] result) {
				averageData = result;
				loadWidget();
			}

		});
	}

	private void loadWidget() {
		averageByCurricularYears =
				new AverageByCurricularYears(window, width, height, numberOfCurricularYears, averageData, yearTag);
		mainPanel.add(averageByCurricularYears);
		window.widgetReady();
	}

}
