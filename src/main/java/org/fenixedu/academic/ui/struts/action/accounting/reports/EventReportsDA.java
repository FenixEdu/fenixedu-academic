/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.accounting.reports;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accounting.report.events.EventReportQueueJob;
import org.fenixedu.academic.domain.accounting.report.events.EventReportQueueJobBean;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminPaymentsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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
        return AcademicAccessRule.getOfficesAccessibleToFunction(AcademicOperationType.MANAGE_EVENT_REPORTS,
                Authenticate.getUser()).collect(Collectors.toSet());
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
