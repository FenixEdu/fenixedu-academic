package net.sourceforge.fenixedu.util.renderer;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.Spacer;

public class Chart {

    private JFreeChart chart;

    private String title;

    private Font font = new Font("SansSerif", Font.PLAIN, 12);

    public Chart(String title) {
        this.title = title;
    }

    public void createChart() {
        addTitles();
        chart.setBackgroundPaint(new Color(255, 255, 255));
    }

    public void saveChart(int imageWidth, int imageHeight, String filename) {
        try {
            ChartUtilities.saveChartAsPNG(new File(filename), chart, imageWidth, imageHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addTitles() {
        TextTitle title2 = new TextTitle(title, font);
        title2.setSpacer(new Spacer(Spacer.RELATIVE, 0.05, 0.05, 0.05, 0.0));
        chart.addSubtitle(title2);
    }

    public JFreeChart getChart() {
        return chart;
    }

    public void setChart(JFreeChart chart) {
        this.chart = chart;
    }

}