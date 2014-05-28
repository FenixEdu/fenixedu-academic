/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.accounting.reports;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob;
import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJobBean;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminPaymentsApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = AcademicAdminPaymentsApp.class, path = "event-reports", titleKey = "title.event.reports",
        accessGroup = "academic(MANAGE_EVENT_REPORTS)")
@Mapping(path = "/eventReports", module = "academicAdministration")
@Forwards({ @Forward(name = "listReports", path = "/academicAdminOffice/accounting/reports/events/listReports.jsp"),
        @Forward(name = "createReportRequest", path = "/academicAdminOffice/accounting/reports/events/createReportRequest.jsp"),
        @Forward(name = "viewRequest", path = "/academicAdminOffice/accounting/reports/events/viewRequest.jsp") })
public class EventReportsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward listReports(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        List<EventReportQueueJob> doneJobs = readDoneReports();
        List<EventReportQueueJob> pendingOrCancelledJobs = readPendingOrCancelledJobs();

        request.setAttribute("doneJobs", doneJobs);
        request.setAttribute("pendingOrCancelledJobs", pendingOrCancelledJobs);

        return mapping.findForward("listReports");
    }

    protected List<EventReportQueueJob> readPendingOrCancelledJobs() {
        return EventReportQueueJob.readPendingOrCancelledJobs(getOffices());
    }

    protected List<EventReportQueueJob> readDoneReports() {
        return EventReportQueueJob.readDoneReports(getOffices());
    }

    protected EventReportQueueJobBean createEventReportQueueJobBean() {
        return EventReportQueueJobBean.createBeanForAdministrativeOffice();
    }

    private Set<AdministrativeOffice> getOffices() {
        return AcademicAuthorizationGroup.getOfficesForOperation(AccessControl.getPerson(),
                AcademicOperationType.MANAGE_EVENT_REPORTS);
    }

    public ActionForward prepareCreateReportRequest(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        EventReportQueueJobBean bean = createEventReportQueueJobBean();

        request.setAttribute("bean", bean);

        return mapping.findForward("createReportRequest");
    }

    public ActionForward createReportRequest(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        EventReportQueueJobBean bean = getRenderedObject("bean");

        try {
            EventReportQueueJob.createRequest(bean);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            return createReportRequestInvalid(mapping, form, request, response);
        }

        return listReports(mapping, form, request, response);
    }

    public ActionForward createReportRequestPostback(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        EventReportQueueJobBean bean = getRenderedObject("bean");

        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();

        return mapping.findForward("createReportRequest");
    }

    public ActionForward createReportRequestInvalid(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        EventReportQueueJobBean bean = getRenderedObject("bean");

        request.setAttribute("bean", bean);

        return mapping.findForward("createReportRequest");
    }

    public ActionForward cancelReportRequest(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        EventReportQueueJob job = readEventReportQueueJob(request);

        try {
            job.cancel();
        } catch (DomainException e) {
            addActionMessage("error", request, e.getKey(), e.getArgs());
            return listReports(mapping, form, request, response);
        }

        return listReports(mapping, form, request, response);
    }

    public ActionForward viewRequest(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        EventReportQueueJob queueJob = readEventReportQueueJob(request);

        request.setAttribute("queueJob", queueJob);

        return mapping.findForward("viewRequest");
    }

    public EventReportQueueJob readEventReportQueueJob(HttpServletRequest request) {
        return getDomainObject(request, "queueJobId");
    }
}
