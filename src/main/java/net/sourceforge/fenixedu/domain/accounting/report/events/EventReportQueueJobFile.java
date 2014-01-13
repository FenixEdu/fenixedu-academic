package net.sourceforge.fenixedu.domain.accounting.report.events;

public class EventReportQueueJobFile extends EventReportQueueJobFile_Base {

    public EventReportQueueJobFile(byte[] contents, String filename) {
        super();
        init(filename, filename, contents, null);
    }

    @Override
    public void delete() {
        setEventReportQueueJobForExemptions(null);
        setEventReportQueueJobForTransactions(null);
        setEventReportQueueJobForDebts(null);
        super.delete();
    }

}
