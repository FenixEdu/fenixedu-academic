package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;

public class EventReportQueueJobFile extends EventReportQueueJobFile_Base {

    public EventReportQueueJobFile(byte[] contents, String filename) {
        super();
        init(filename, filename, contents, new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_EVENT_REPORTS));
    }

    @Override
    public void delete() {
        setEventReportQueueJobForExemptions(null);
        setEventReportQueueJobForTransactions(null);
        setEventReportQueueJobForDebts(null);
        super.delete();
    }

}
