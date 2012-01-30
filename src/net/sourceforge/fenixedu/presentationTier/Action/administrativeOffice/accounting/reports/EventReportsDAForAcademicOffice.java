package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.accounting.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob;
import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJobBean;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.accounting.reports.EventReportsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/eventReports", module = "academicAdminOffice")
@Forwards({ @Forward(name = "listReports", path = "/academicAdminOffice/accounting/reports/events/listReports.jsp"),
	@Forward(name = "createReportRequest", path = "/academicAdminOffice/accounting/reports/events/createReportRequest.jsp"),
	@Forward(name = "viewRequest", path = "/academicAdminOffice/accounting/reports/events/viewRequest.jsp") })
public class EventReportsDAForAcademicOffice extends EventReportsDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	AdministrativeOfficeType administrativeOfficeType = getAdministrativeOfficeType();

	request.setAttribute("administrativeOfficeType", administrativeOfficeType);

	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected List<EventReportQueueJob> readPendingOrCancelledJobs() {
	AdministrativeOfficeType administrativeOfficeType = getAdministrativeOfficeType();
	return EventReportQueueJob.readPendingOrCancelledJobs(administrativeOfficeType);
    }

    @Override
    protected List<EventReportQueueJob> readDoneReports() {
	AdministrativeOfficeType administrativeOfficeType = getAdministrativeOfficeType();

	return EventReportQueueJob.readDoneReports(administrativeOfficeType);
    }

    private AdministrativeOfficeType getAdministrativeOfficeType() {
	Employee employee = AccessControl.getPerson().getEmployee();
	AdministrativeOffice administrativeOffice = employee.getAdministrativeOffice();
	AdministrativeOfficeType administrativeOfficeType = administrativeOffice.getAdministrativeOfficeType();
	return administrativeOfficeType;
    }

    @Override
    protected EventReportQueueJobBean createEventReportQueueJobBean() {
	AdministrativeOfficeType administrativeOfficeType = getAdministrativeOfficeType();
	
	if(administrativeOfficeType == AdministrativeOfficeType.DEGREE) {
	    return EventReportQueueJobBean.createBeanForDegreeAdministrativeOffice();
	} else if(administrativeOfficeType == AdministrativeOfficeType.MASTER_DEGREE) {
	    return EventReportQueueJobBean.createBeanForMasterDegreeAdministrativeOffcie();
	}

	return null;
    }

}
