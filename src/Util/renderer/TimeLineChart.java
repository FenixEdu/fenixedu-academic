package Util.renderer;

import java.awt.Color;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.XYStepRenderer;
import org.jfree.data.XYDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import Util.renderer.container.RequestEntry;

public class TimeLineChart extends Chart {

	public TimeLineChart(SortedSet requestEntries, String fileName) {

		super(fileName);

		//Paint[] colors = new Paint[1];
		//colors[0] = Color.blue;

		TimeSeriesCollection collection = new TimeSeriesCollection();
		Iterator iterator = requestEntries.iterator();
		while (iterator.hasNext()) {
			RequestEntry requestEntry = (RequestEntry) iterator.next();
			collection.addSeries(createTimeSeries(requestEntry));
		}

		//String range = timeLine.getRangeLabel();
		//String domain = Messages.getString("DOMAIN_TIME");

		XYDataset data = collection;
		setChart(ChartFactory.createTimeSeriesChart("ProjectName", "domain", "range", data, false, false, false));

		XYPlot plot = (XYPlot) getChart().getPlot();
		plot.getRenderer().setSeriesPaint(0, Color.blue);
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setVerticalTickLabels(true);
		plot.setRenderer(new XYStepRenderer());

		createChart();
		saveChart(500, 300, fileName);
	}

	private TimeSeries createTimeSeries(RequestEntry requestEntry) {
		TimeSeries result = new TimeSeries(requestEntry.getRequestPath(), Second.class);
		for (int i = 0; i < requestEntry.getExecutionTimes().size(); i++) {
			Integer executionTime = (Integer) requestEntry.getExecutionTimes().get(i);
			Date logTime = (Date) requestEntry.getLogTimes().get(i);
			result.add(new Second(logTime), executionTime.doubleValue());
		}
		return result;
	}
}