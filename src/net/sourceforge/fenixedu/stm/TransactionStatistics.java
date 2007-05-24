package net.sourceforge.fenixedu.stm;

public class TransactionStatistics {
    private int numReadTxs = 0;
    private int numWriteTxs = 0;
    private int numAborts = 0;
    private int numConflicts = 0;

    TransactionStatistics() {
        numReadTxs = 0;
        numWriteTxs = 0;
        numAborts = 0;
        numConflicts = 0;
    }

    public synchronized void incReads() {
        numReadTxs++;
    }

    public synchronized void incWrites() {
        numWriteTxs++;
    }

    public synchronized void incAborts() {
        numAborts++;
    }

    public synchronized void incConflicts() {
        numConflicts++;
    }

    public synchronized Report getReportAndReset() {
        Report report = new Report(numReadTxs, numWriteTxs, numAborts, numConflicts);
        numReadTxs = 0;
        numWriteTxs = 0;
        numAborts = 0;
        numConflicts = 0;

        return report;
    }

    public static class Report {
        public final int numReads;
        public final int numWrites;
        public final int numAborts;
        public final int numConflicts;

        public Report(int numReads, int numWrites, int numAborts, int numConflicts) {
            this.numReads = numReads;
            this.numWrites = numWrites;
            this.numAborts = numAborts;
            this.numConflicts = numConflicts;
        }
    }
}
