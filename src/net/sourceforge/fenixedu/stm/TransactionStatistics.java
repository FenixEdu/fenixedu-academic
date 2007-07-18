package net.sourceforge.fenixedu.stm;

public class TransactionStatistics {
    private int numReadTxs = 0;
    private int numWriteTxs = 0;
    private int numAborts = 0;
    private int numConflicts = 0;

    private CounterStats readOnlyReads = new CounterStats();
    private CounterStats readWriteReads = new CounterStats();
    private CounterStats readWriteWrites = new CounterStats();


    TransactionStatistics() {
    }

    public synchronized void incReads(TopLevelTransaction tx) {
        // don't count empty transactions
        if (tx.numBoxReads == 0) {
            return;
        }

        numReadTxs++;

        readOnlyReads.addNewValue(tx.numBoxReads);
    }

    public synchronized void incWrites(TopLevelTransaction tx) {
        numWriteTxs++;

        readWriteReads.addNewValue(tx.numBoxReads);
        readWriteWrites.addNewValue(tx.numBoxWrites);
    }

    public synchronized void incAborts() {
        numAborts++;
    }

    public synchronized void incConflicts() {
        numConflicts++;
    }

    public synchronized Report getReportAndReset() {
        Report report = new Report(numReadTxs, 
                                   numWriteTxs, 
                                   numAborts, 
                                   numConflicts,
                                   readOnlyReads.getAndReset(),
                                   readWriteReads.getAndReset(),
                                   readWriteWrites.getAndReset());
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

        public final CounterStats readOnlyReads;
        public final CounterStats readWriteReads;
        public final CounterStats readWriteWrites;

        public Report(int numReads, 
                      int numWrites, 
                      int numAborts, 
                      int numConflicts, 
                      CounterStats readOnlyReads, 
                      CounterStats readWriteReads, 
                      CounterStats readWriteWrites) {
            this.numReads = numReads;
            this.numWrites = numWrites;
            this.numAborts = numAborts;
            this.numConflicts = numConflicts;
            this.readOnlyReads = readOnlyReads;
            this.readWriteReads = readWriteReads;
            this.readWriteWrites = readWriteWrites;
        }
    }

    public static class CounterStats {
        int minValue = Integer.MAX_VALUE;
        int maxValue = 0;
        long valueSum = 0;

        public void addNewValue(int value) {
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
            valueSum += value;
        }

        public CounterStats getAndReset() {
            CounterStats snapshot = new CounterStats();
            snapshot.minValue = minValue;
            snapshot.maxValue = maxValue;
            snapshot.valueSum = valueSum;

            minValue = Integer.MAX_VALUE;
            maxValue = 0;
            valueSum = 0;

            return snapshot;
        }
    }
}
