package net.sourceforge.fenixedu.presentationTier.Action.manager.accounting.reports;

import java.util.List;

import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob;
import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJobBean;
import net.sourceforge.fenixedu.presentationTier.Action.accounting.reports.EventReportsDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/eventReports", module = "manager")
@Forwards({ @Forward(name = "listReports", path = "/manager/accounting/reports/events/listReports.jsp"),
        @Forward(name = "createReportRequest", path = "/manager/accounting/reports/events/createReportRequest.jsp"),
        @Forward(name = "viewRequest", path = "/manager/accounting/reports/events/viewRequest.jsp"),
        @Forward(name = "viewErrors", path = "/manager/accounting/reports/events/viewErrors.jsp") })
public class EventReportsDAForManager extends EventReportsDA {

    @Override
    protected List<EventReportQueueJob> readPendingOrCancelledJobs() {
        return EventReportQueueJob.readPendingOrCancelledJobs(null);
    }

    @Override
    protected List<EventReportQueueJob> readDoneReports() {
        return EventReportQueueJob.readDoneReports(null);
    }

    @Override
    protected EventReportQueueJobBean createEventReportQueueJobBean() {
        return EventReportQueueJobBean.createBeanForManager();
    }
}
