package net.sourceforge.fenixedu.stm;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

class StatisticsThread extends Thread {
    private static final long SECONDS_BETWEEN_REPORTS = 5 * 60;

    private String server;
    private int numReport = 0;
	
    StatisticsThread() {
        this.server = Util.getServerName();
        
        setDaemon(true);
    }

    public void run() {
        while (true) {
            try {
                sleep(SECONDS_BETWEEN_REPORTS * 1000);
            } catch (InterruptedException ie) {
                // ignore exception
            }
            reportStatistics();
        }
    }
    
    private void reportStatistics() {
        PersistenceBroker broker = null;
        
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            
            Connection conn = broker.serviceConnectionManager().getConnection();
            Statement stmt = conn.createStatement();

            TransactionStatistics stats = Transaction.STATISTICS.cloneAndReset();
            numReport++;
            
            // insert statistics for this server
            stmt.executeUpdate("INSERT INTO TRANSACTION_STATISTICS VALUES ('" 
                               + server 
                               + "',"
                               + numReport
                               + ","
                               + stats.getNumReads()
                               + ","
                               + stats.getNumWrites()
                               + ","
                               + stats.getNumAborts()
                               + ","
                               + stats.getNumConflicts()
                               + ","
                               + SECONDS_BETWEEN_REPORTS
                               // this last null is the timestamp
                               + ",null)");
            
            broker.commitTransaction();
        } catch (Exception e) {
            // the statistics are not crucial, if anything goes wrong, that's ok
            // issue just a warning
            System.out.println("WARNING: Couldn't insert the statistics data");
        } finally {
            if (broker != null) {
                broker.close();
            }
        }
    }
}
