package net.sourceforge.fenixedu.stm;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeTableXYDataset;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.utl.ist.fenix.tools.util.StringAppender;
import sun.awt.image.codec.JPEGImageEncoderImpl;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class TransactionReport implements Serializable {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private YearMonthDay startOfReport;
    private YearMonthDay endOfReport;
    private String server;
    private TransactionAction transactionAction;

    private TimeTableXYDataset dataset = null;

    public TransactionReport(final YearMonthDay startOfReport, final YearMonthDay endOfReport,
	    final TransactionAction transactionAction, final String server) {
        setStartOfReport(startOfReport);
        setEndOfReport(endOfReport);
        setTransactionAction(transactionAction);
        setServer(server);
    }

    public YearMonthDay getEndOfReport() {
        return endOfReport;
    }

    public void setEndOfReport(YearMonthDay endOfReport) {
        this.endOfReport = endOfReport;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public YearMonthDay getStartOfReport() {
        return startOfReport;
    }

    public void setStartOfReport(YearMonthDay startOfReport) {
        this.startOfReport = startOfReport;
    }

    public TransactionAction getTransactionAction() {
        return transactionAction;
    }

    public void setTransactionAction(TransactionAction transactionAction) {
        this.transactionAction = transactionAction;
    }

    public void report() {
        PersistenceBroker broker = null;
        ResultSet resultSet = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            final Connection connection = broker.serviceConnectionManager().getConnection();
            final Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(mekeQueryString());
            process(resultSet);
        } catch (Exception ex) {
            throw new Error(ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    // nothing can be done at this point
                }
            }
            if (broker != null) {
                broker.close();
            }
        }
    }

    public byte[] getChart() throws ImageFormatException, IOException {
        //final PlotOrientation plotOrientation = PlotOrientation.VERTICAL;
        //final JFreeChart jfreeChart = ChartFactory.createLineChart("Title", "categoryAxisLabel", "valueAxisLabel", dataset, plotOrientation, true, true, true);
        final JFreeChart jfreeChart = ChartFactory.createTimeSeriesChart("", "", "", dataset, true, true, true);
        final BufferedImage bufferedImage = jfreeChart.createBufferedImage(1000, 500);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final JPEGImageEncoder imageEncoder = new JPEGImageEncoderImpl(outputStream);
        imageEncoder.encode(bufferedImage);
        bufferedImage.flush();
        outputStream.close();
        return outputStream.toByteArray();
    }

    private void process(final ResultSet resultSet) throws SQLException {
        //dataset = new DefaultCategoryDataset();
	dataset = new TimeTableXYDataset();

        while (resultSet.next()) {
            final String server = resultSet.getString(1);
            if (this.server == null || this.server.equals(server)) {
        	final long reads = resultSet.getLong(2);
        	final long writes = resultSet.getLong(3);
        	final long aborts = resultSet.getLong(4);
        	final long conflicts = resultSet.getLong(5);
        	final DateTime until = new DateTime(resultSet.getTimestamp(6));
        	try {
        	    final Second second = new Second(until.toDate());
        	    if (transactionAction == null) {
        		dataset.add(second, reads, "Reads");
        	    } else if (transactionAction == TransactionAction.READS) {
        		dataset.add(second, reads, server);
        	    }
        	    if (transactionAction == null) {
        		dataset.add(second, writes, "Writes");
        	    } else if (transactionAction == TransactionAction.WRITES) {
        		dataset.add(second, writes, server);
        	    }
        	    if (transactionAction == null) {
        		dataset.add(second, aborts, "Aborts");
        	    } else if (transactionAction == TransactionAction.ABORTS) {
        		dataset.add(second, aborts, server);
        	    }
        	    if (transactionAction == null) {
        		dataset.add(second, conflicts, "Conflicts");
        	    } else if (transactionAction == TransactionAction.CONFLICTS) {
        		dataset.add(second, conflicts, server);
        	    }
        	} catch (IllegalArgumentException ex) {
        	    System.out.println(ex.getMessage());
        	}
            }
        }
    }

    private String mekeQueryString() {
        return StringAppender.append("select left(SERVER, locate(':', SERVER) - 1), NUM_READS, NUM_WRITES, NUM_ABORTS, NUM_CONFLICTS, STATS_WHEN from TRANSACTION_STATISTICS ",
                "where STATS_WHEN >='",
                dateTimeFormatter.print(startOfReport.toDateMidnight()), "' and STATS_WHEN < '",
                dateTimeFormatter.print(endOfReport.toDateMidnight()), "'");
    }

    public TreeSet<String> getServers() {
	final TreeSet<String> servers = new TreeSet<String>();
        PersistenceBroker broker = null;
        ResultSet resultSet = null;
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            final Connection connection = broker.serviceConnectionManager().getConnection();
            final Statement statement = connection.createStatement();
            final String query = "select distinct(left(TRANSACTION_STATISTICS.SERVER, locate(':', TRANSACTION_STATISTICS.SERVER) - 1)) from TRANSACTION_STATISTICS";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                servers.add(resultSet.getString(1));
            }
        } catch (Exception ex) {
            throw new Error(ex);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    // nothing can be done at this point
                }
            }
            if (broker != null) {
                broker.close();
            }
        }
        return servers;
    }

}
