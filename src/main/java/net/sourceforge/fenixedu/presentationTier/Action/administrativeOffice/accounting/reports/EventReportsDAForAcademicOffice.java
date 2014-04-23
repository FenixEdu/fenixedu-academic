package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.accounting.reports;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob;
import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJobBean;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.accounting.reports.EventReportsDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/eventReports", module = "academicAdministration")
@Forwards({ @Forward(name = "listReports", path = "/academicAdminOffice/accounting/reports/events/listReports.jsp"),
        @Forward(name = "createReportRequest", path = "/academicAdminOffice/accounting/reports/events/createReportRequest.jsp"),
        @Forward(name = "viewRequest", path = "/academicAdminOffice/accounting/reports/events/viewRequest.jsp") })
public class EventReportsDAForAcademicOffice extends EventReportsDA {

    @Override
    protected List<EventReportQueueJob> readPendingOrCancelledJobs() {
        return EventReportQueueJob.readPendingOrCancelledJobs(getOffices());
    }

    @Override
    protected List<EventReportQueueJob> readDoneReports() {
        return EventReportQueueJob.readDoneReports(getOffices());
    }

    @Override
    protected EventReportQueueJobBean createEventReportQueueJobBean() {
        return EventReportQueueJobBean.createBeanForAdministrativeOffice();
    }

    private Set<AdministrativeOffice> getOffices() {
        return AcademicAuthorizationGroup.getOfficesForOperation(AccessControl.getPerson(),
                AcademicOperationType.MANAGE_EVENT_REPORTS);
    }
}
