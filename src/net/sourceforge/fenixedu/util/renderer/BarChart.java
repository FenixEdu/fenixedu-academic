package net.sourceforge.fenixedu.util.renderer;

import java.awt.Color;
import java.util.Iterator;
import java.util.SortedSet;

import net.sourceforge.fenixedu.util.renderer.container.RequestEntry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DefaultCategoryDataset;

public class BarChart extends Chart {

    public BarChart(SortedSet requestEntries, String title, String filename, int numElementsToShow) {

        super(title);

        DefaultCategoryDataset data = new DefaultCategoryDataset();
        Iterator iterator = requestEntries.iterator();
        while (iterator.hasNext() && data.getColumnCount() < numElementsToShow) {
            RequestEntry requestEntry = (RequestEntry) iterator.next();
            data.addValue(requestEntry.getAverageExecutionTime(), "Top " + numElementsToShow
                    + " Requests", "" + (data.getColumnCount() + 1));
        }

        JFreeChart chart = ChartFactory.createBarChart("Top " + numElementsToShow + " Requests", "",
                "Time (ms)", data, PlotOrientation.VERTICAL, false, true, true);
        setChart(chart);

        CategoryPlot plot = getChart().getCategoryPlot();
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        plot.getRenderer().setSeriesPaint(0, Color.blue);

        createChart();
        saveChart(500, 300, filename);
    }
}