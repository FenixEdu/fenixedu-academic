package net.sourceforge.fenixedu.stm;

class TransactionStatistics {
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

    private TransactionStatistics(int reads, int writes, int aborts, int conflicts) {
        numReadTxs = reads;
        numWriteTxs = writes;
        numAborts = aborts;
        numConflicts = conflicts;
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

    public synchronized TransactionStatistics cloneAndReset() {
        TransactionStatistics stats = new TransactionStatistics(numReadTxs, numWriteTxs, numAborts, numConflicts);
        numReadTxs = 0;
        numWriteTxs = 0;
        numAborts = 0;
        numConflicts = 0;

        return stats;
    }

    public int getNumReads() {
        return numReadTxs;
    }

    public int getNumWrites() {
        return numWriteTxs;
    }

    public int getNumAborts() {
        return numAborts;
    }

    public int getNumConflicts() {
        return numConflicts;
    }
}
