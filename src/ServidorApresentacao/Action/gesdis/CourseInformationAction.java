/*
 * Created on Nov 15, 2003
 *  
 */
package ServidorApresentacao.Action.gesdis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.SiteView;
import DataBeans.gesdis.InfoCourseReport;
import DataBeans.gesdis.InfoSiteCourseInformation;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class CourseInformationAction extends DispatchAction
{
    private String getReadService()
    {
        return "ReadCourseInformation";
    }

    private String getEditService()
    {
        return "EditCourseInformation";
    }

    /**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward to the action mapping configuration called successfull-edit
	 * @throws Exception
	 */
    public ActionForward edit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        InfoCourseReport infoCourseReport = getInfoCourseReportFromForm(form);
        Object[] args = { infoCourseReport.getIdInternal(), infoCourseReport };
        ServiceUtils.executeService(SessionUtils.getUserView(request), getEditService(), args);
        return read(mapping, form, request, response);
    }

    /**
	 * This method creates an InfoServiceProviderRegime using the form properties.
	 * 
	 * @param form
	 * @return InfoServiceProviderRegime created
	 */
    protected InfoCourseReport getInfoCourseReportFromForm(ActionForm form)
    {
        DynaActionForm dynaForm = (DynaActionForm) form;
        Integer courseReportId = (Integer) dynaForm.get("courseReportId");
        Integer executionCourseId = (Integer) dynaForm.get("executionCourseId");
        Integer executionPeriodId = (Integer) dynaForm.get("executionPeriodId");
        Integer executionYearId = (Integer) dynaForm.get("executionYearId");
        String report = (String) dynaForm.get("report");

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setIdInternal(executionYearId);

        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        infoExecutionPeriod.setIdInternal(executionPeriodId);
        infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        infoExecutionCourse.setIdInternal(executionCourseId);
        infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

        InfoCourseReport infoCourseReport = new InfoCourseReport();
        infoCourseReport.setIdInternal(courseReportId);
        infoCourseReport.setReport(report);
        infoCourseReport.setInfoExecutionCourse(infoExecutionCourse);

        return infoCourseReport;
    }

    /**
	 * Tests if errors are presented.
	 * 
	 * @param request
	 * @return
	 */
    private boolean hasErrors(HttpServletRequest request)
    {

        return request.getAttribute(Globals.ERROR_KEY) != null;
    }

    /**
	 * @param mapping
	 * @param form
	 * @param request
	 */
    protected void populateFormFromInfoCourseReport(
        ActionMapping mapping,
        InfoCourseReport infoCourseReport,
        ActionForm form,
        HttpServletRequest request)
    {
        try
        {
            BeanUtils.copyProperties(form, infoCourseReport);

            DynaActionForm dynaForm = (DynaActionForm) form;

            InfoExecutionCourse infoExecutionCourse = infoCourseReport.getInfoExecutionCourse();
            InfoExecutionPeriod infoExecutionPeriod = infoExecutionCourse.getInfoExecutionPeriod();
            InfoExecutionYear infoExecutionYear = infoExecutionPeriod.getInfoExecutionYear();
            dynaForm.set("courseReportId", infoCourseReport.getIdInternal());
            dynaForm.set("report", infoCourseReport.getReport());
            dynaForm.set("executionCourseId", infoExecutionCourse.getIdInternal());
            dynaForm.set("executionPeriodId", infoExecutionPeriod.getIdInternal());
            dynaForm.set("executionYearId", infoExecutionYear.getIdInternal());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward to the action mapping configuration called show-form
	 * @throws Exception
	 */
    public ActionForward prepareEdit(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        SiteView siteView = readSiteView(mapping, form, request);
        if (!hasErrors(request) && siteView != null)
        {
            InfoSiteCourseInformation infoSiteCourseInformation =
                (InfoSiteCourseInformation) siteView.getComponent();
            populateFormFromInfoCourseReport(
                mapping,
                infoSiteCourseInformation.getInfoCourseReport(),
                form,
                request);
        }
        setSiteViewToRequest(request, siteView, mapping);
        return mapping.findForward("show-form");
    }

    /**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return forward to the action mapping configuration called successfull-read
	 * @throws Exception
	 */
    public ActionForward read(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        SiteView siteView = readSiteView(mapping, form, request);
        setSiteViewToRequest(request, siteView, mapping);
        return mapping.findForward("successfull-read");
    }

    /**
	 * Reads the infoCourseReport using de read service associated to the action
	 * 
	 * @param mapping
	 * @param form
	 * @return
	 */
    private SiteView readSiteView(ActionMapping mapping, ActionForm form, HttpServletRequest request)
        throws FenixServiceException
    {
        IUserView userView = SessionUtils.getUserView(request);
        String executionCourseId = request.getParameter("executionCourseId");

        Object[] args = { new Integer(executionCourseId)};
        return (SiteView) ServiceUtils.executeService(userView, getReadService(), args);
    }

    /**
	 * @param request
	 * @param infoCourseReport
	 */
    private void setSiteViewToRequest(
        HttpServletRequest request,
        SiteView siteView,
        ActionMapping mapping)
    {
        if (siteView != null)
        {
            request.setAttribute("siteView", siteView);
        }
    }
}
