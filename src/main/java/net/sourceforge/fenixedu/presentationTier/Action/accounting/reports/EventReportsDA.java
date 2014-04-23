package net.sourceforge.fenixedu.presentationTier.Action.accounting.reports;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob;
import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJobBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class EventReportsDA extends FenixDispatchAction {

    public ActionForward listReports(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        List<EventReportQueueJob> doneJobs = readDoneReports();
        List<EventReportQueueJob> pendingOrCancelledJobs = readPendingOrCancelledJobs();

        request.setAttribute("doneJobs", doneJobs);
        request.setAttribute("pendingOrCancelledJobs", pendingOrCancelledJobs);

        return mapping.findForward("listReports");
    }

    protected abstract List<EventReportQueueJob> readPendingOrCancelledJobs();

    protected abstract List<EventReportQueueJob> readDoneReports();

    public ActionForward prepareCreateReportRequest(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        EventReportQueueJobBean bean = createEventReportQueueJobBean();

        request.setAttribute("bean", bean);

        return mapping.findForward("createReportRequest");
    }

    protected abstract EventReportQueueJobBean createEventReportQueueJobBean();

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
