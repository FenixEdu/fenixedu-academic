package pt.utl.ist.scripts.process.report;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

import net.sourceforge.fenixedu.util.ConnectionManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.bennu.scheduler.CronTask;
import pt.utl.ist.fenix.tools.util.StringAppender;

public class TransactionStatistics extends CronTask {

    private static class Statistics {
        long reports;
        long reads;
        long writes;
        long aborts;
        long conflicts;
    }

    final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    final String totalQueryPrefix =
            "select sum(NUM_REPORT), sum(NUM_READS), sum(NUM_WRITES), sum(NUM_ABORTS), sum(NUM_CONFLICTS) from FF$TRANSACTION_STATISTICS";
    final String averageQueryPrefix =
            "select avg(NUM_REPORT), avg(NUM_READS), avg(NUM_WRITES), avg(NUM_ABORTS), avg(NUM_CONFLICTS) from FF$TRANSACTION_STATISTICS";
    final String sinceConditionPrefix = " where STATS_WHEN > '";
    final String endConditionPrefix = " and STATS_WHEN < '";
    final String conditionPostfix = "'";

    @Override
    public void runTask() {
        final StringBuilder stringBuilder = new StringBuilder();
        writeHeader(stringBuilder);

        final Connection connection = ConnectionManager.getCurrentSQLConnection(); //Transaction.getNewJdbcConnection();

        try {
            report(stringBuilder, connection, totalQueryPrefix, null, true);
            report(stringBuilder, connection, averageQueryPrefix, null, false);
        } catch (SQLException e) {
            throw new Error(e);
        }

        taskLog(stringBuilder.toString());

        try {
            generateReport("OneWeek", new DateTime().minusWeeks(1), connection);
            generateReport("OneMonth", new DateTime().minusMonths(1), connection);
        } catch (SQLException e) {
            throw new Error(e);
        } catch (IOException e) {
            throw new Error(e);
        }

    }

    private void generateReport(final String filename, final DateTime since, final Connection connection) throws SQLException,
            IOException {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (DateTime dateTime = since; !dateTime.isAfterNow(); dateTime = dateTime.plusDays(1)) {
            final Statistics statistics = getStatisticsForDay(connection, dateTime);
            dataset.addValue(statistics.reports, "Reports", dateTime);
            dataset.addValue(statistics.reads, "Reads", dateTime);
            dataset.addValue(statistics.writes, "Writes", dateTime);
            dataset.addValue(statistics.aborts, "Aborts", dateTime);
            dataset.addValue(statistics.conflicts, "Conflicts", dateTime);
        }

        buildChart(filename, dataset);
    }

    private Statistics getStatisticsForDay(final Connection connection, final DateTime dateTime) throws SQLException {
        final Statement statement = connection.createStatement();
        final String query = makeQuery(totalQueryPrefix, dateTime, dateTime.plusDays(1));
        final ResultSet resultSet = statement.executeQuery(query);
        final Statistics statistics = new Statistics();
        if (resultSet.next()) {
            statistics.reports = resultSet.getLong(1);
            statistics.reads = resultSet.getLong(2);
            statistics.writes = resultSet.getLong(3);
            statistics.aborts = resultSet.getLong(4);
            statistics.conflicts = resultSet.getLong(5);
        }
        return statistics;
    }

    private void buildChart(final String filename, final CategoryDataset dataset) throws IOException {
        final PlotOrientation plotOrientation = PlotOrientation.VERTICAL;
        final JFreeChart jfreeChart =
                ChartFactory.createLineChart("Title", "categoryAxisLabel", "valueAxisLabel", dataset, plotOrientation, true,
                        true, true);
        final BufferedImage bufferedImage = jfreeChart.createBufferedImage(1000, 1000);
        final FileOutputStream outputStream = new FileOutputStream(filename + ".jpeg");
        ImageIO.write(bufferedImage, "jpg", outputStream);
        outputStream.close();

    }

    private void writeHeader(final StringBuilder stringBuilder) {
        stringBuilder.append("                               \t");
        stringBuilder.append("Reports\t");
        stringBuilder.append("Reads\t");
        stringBuilder.append("Writes\t");
        stringBuilder.append("Aborts\t");
        stringBuilder.append("Conflicts\n");
    }

    private void report(final StringBuilder stringBuilder, final Connection connection, final String queryPrefix,
            final DateTime lastInvocationStartTime, final boolean isTotal) throws SQLException {
        final String query = makeQuery(queryPrefix, lastInvocationStartTime, null);
        presentResults(stringBuilder, connection, query, lastInvocationStartTime, isTotal);
    }

    private void presentResults(final StringBuilder stringBuilder, final Connection connection, final String query,
            final DateTime lastInvocationStartTime, final boolean isTotal) throws SQLException {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            stringBuilder.append(isTotal ? "Total" : "Avg. ");
            stringBuilder.append(" since ");
            stringBuilder.append(lastInvocationStartTime == null ? "always             " : dateTimeFormatter
                    .print(lastInvocationStartTime));
            stringBuilder.append('\t');
            stringBuilder.append(resultSet.getString(1));
            stringBuilder.append('\t');
            stringBuilder.append(resultSet.getString(2));
            stringBuilder.append('\t');
            stringBuilder.append(resultSet.getString(3));
            stringBuilder.append('\t');
            stringBuilder.append(resultSet.getString(4));
            stringBuilder.append('\t');
            stringBuilder.append(resultSet.getString(5));
            stringBuilder.append('\n');
        }
    }

    private String makeQuery(final String queryPrefix, final DateTime conditionValue, final DateTime endConfitionValue) {
        return conditionValue == null ? queryPrefix : makeFullQuery(queryPrefix, conditionValue, endConfitionValue);
    }

    private String makeFullQuery(final String queryPrefix, final DateTime conditionValue, final DateTime endConfitionValue) {
        return endConfitionValue == null ? StringAppender.append(queryPrefix, sinceConditionPrefix,
                dateTimeFormatter.print(conditionValue), conditionPostfix) : StringAppender.append(queryPrefix,
                sinceConditionPrefix, dateTimeFormatter.print(conditionValue), conditionPostfix, endConditionPrefix,
                dateTimeFormatter.print(endConfitionValue), conditionPostfix);
    }

}
