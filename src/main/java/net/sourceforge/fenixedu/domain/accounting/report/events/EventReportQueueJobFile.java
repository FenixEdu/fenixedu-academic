package net.sourceforge.fenixedu.domain.accounting.report.events;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class EventReportQueueJobFile extends EventReportQueueJobFile_Base {

    private static final String ROOT_DIR_DESCRIPTION = "Events, Exemptions and Transactions";
    private static final String ROOT_DIR = "EventReportQueueJobFile";

    public EventReportQueueJobFile(byte[] contents, String filename) {
        super();
        init(getVirtualPath(), filename, filename, null, contents, null);
    }

    protected VirtualPath getVirtualPath() {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));

        filePath.addNode(new VirtualPathNode(this.getExternalId(), this.getExternalId()));

        return filePath;
    }

    @Override
    public void delete() {
        setEventReportQueueJobForExemptions(null);
        setEventReportQueueJobForTransactions(null);
        setEventReportQueueJobForDebts(null);
        super.delete();
    }

}
