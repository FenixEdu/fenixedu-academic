package Util.renderer;

import java.awt.Paint;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.XYStepRenderer;
import org.jfree.data.XYDataset;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import Util.renderer.container.RequestEntry;
import Util.renderer.tools.OutputUtils;

public class TimeLineChart extends Chart {

    public TimeLineChart(SortedSet requestEntries, String fileName) {
        super("Execution Time");

        Paint[] colors = new Paint[requestEntries.size()];
        int i = 0;
        TimeSeriesCollection collection = new TimeSeriesCollection();
        Iterator iterator = requestEntries.iterator();
        while (iterator.hasNext()) {
            RequestEntry requestEntry = (RequestEntry) iterator.next();
            TimeSeries series = createTimeSeries(requestEntry, i);
            collection.addSeries(series);
            colors[i] = OutputUtils.getStringColor(series.getName());
            i++;
        }
        createLOCChart(collection, colors, "title");
        createChart();
        saveChart(500, 500, fileName);
    }

    private TimeSeries createTimeSeries(RequestEntry requestEntry, int requestClassification) {
        TimeSeries result = new TimeSeries(new Integer(requestClassification + 1).toString(),
                Millisecond.class);
        for (int i = 0; i < requestEntry.getExecutionTimes().size(); i++) {
            Integer executionTime = (Integer) requestEntry.getExecutionTimes().get(i);
            Date logTime = (Date) requestEntry.getLogTimes().get(i);
            try {
                result.add(new Millisecond(logTime), executionTime.doubleValue());
            } catch (Exception ex) {
                // Ignore duplicate entries
            }
        }
        return result;
    }

    private void createLOCChart(TimeSeriesCollection collection, Paint[] colors, String title) {
        XYDataset data = collection;
        boolean legend = (collection.getSeriesCount() > 1);
        setChart(ChartFactory.createTimeSeriesChart("Requests", "Log Time", "Execution Time", data,
                legend, false, false));

        // getChart().getPlot().setSeriesPaint(colors);
        XYPlot plot = getChart().getXYPlot();
        for (int i = 0; i < colors.length; i++) {
            plot.getRenderer().setSeriesPaint(0, colors[i]);
        }
        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        domainAxis.setVerticalTickLabels(true);
        ValueAxis valueAxis = plot.getRangeAxis();
        valueAxis.setLowerBound(0);
        plot.setRenderer(new XYStepRenderer());
    }

}