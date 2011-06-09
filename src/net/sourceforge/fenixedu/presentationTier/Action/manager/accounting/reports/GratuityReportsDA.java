package net.sourceforge.fenixedu.presentationTier.Action.manager.accounting.reports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.accounting.report.GratuityReportBean;
import net.sourceforge.fenixedu.domain.accounting.report.GratuityReportQueueJob;
import net.sourceforge.fenixedu.domain.accounting.report.GratuityReportQueueJobLaunchService;
import net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/gratuityReports", module = "manager")
@Forwards( { 
 @Forward(name = "prepare-generate-report", path = "/manager/accounting/reports/gratuity/prepareGenerateReport.jsp"),
	@Forward(name = "list-reports", path = "/manager/accounting/reports/gratuity/listReports.jsp")
})
public class GratuityReportsDA extends FenixDispatchAction {

    public ActionForward listReports(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	GratuityReportBean bean = getGratuityReportBean();

	if (bean == null) {
	    bean = new GratuityReportBean(ExecutionYear.readCurrentExecutionYear());
	}

	RenderUtils.invalidateViewState("gratuity.report.bean");

	request.setAttribute("gratuityReportBean", bean);
	request.setAttribute("generatedReports", GratuityReportQueueJob.retrieveAllGeneratedReports(bean.getExecutionYear()));
	request.setAttribute("eventReports", EventReportQueueJob.retrieveAllGeneratedReports());
	request.setAttribute("notGeneratedReports", GratuityReportQueueJob.retrieveNotGeneratedReports(bean.getExecutionYear()));
	request.setAttribute("canRequestReportGeneration", GratuityReportQueueJob.canRequestReportGeneration());

	return mapping.findForward("list-reports");
    }

    private GratuityReportBean getGratuityReportBean() {
	return getRenderedObject("gratuity.report.bean");
    }

    public ActionForward prepareGenerateReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	GratuityReportBean bean = new GratuityReportBean(ExecutionYear.readCurrentExecutionYear());

	request.setAttribute("gratuityReportBean", bean);

	return mapping.findForward("prepare-generate-report");
    }

    public ActionForward prepareGenerateReportInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	GratuityReportBean bean = getGratuityReportBean();
	request.setAttribute("gratuityReportBean", bean);

	RenderUtils.invalidateViewState("gratuity.report.bean");

	return mapping.findForward("prepare-generate-report");
    }

    public ActionForward generateReportPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	GratuityReportBean bean = getGratuityReportBean();
	request.setAttribute("gratuityReportBean", bean);

	RenderUtils.invalidateViewState("gratuity.report.bean");

	return mapping.findForward("prepare-generate-report");
    }

    public ActionForward generateReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	GratuityReportBean bean = getGratuityReportBean();
	GratuityReportQueueJobLaunchService.launchJob(bean);

	return listReports(mapping, form, request, response);
    }

    public ActionForward cancelQueueJob(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	QueueJob job = getDomainObject(request, "queueJobId");
	job.cancel();

	return listReports(mapping, form, request, response);
    }

}
