/*
 * Created on Fev 10, 2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.gesdis;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class TeachingReportAction extends DispatchAction {
    private String getReadService() {
        return "ReadCourseInformation";
    }

    private String getReadHistoricService() {
        return "ReadCourseHistoric";
    }

    private String getEditService() {
        return "EditCourseInformation";
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return forward to the action mapping configuration called
     *         successfull-edit
     * @throws Exception
     */
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InfoCourseReport infoCourseReport = getInfoCourseReportFromForm(form);
        Object[] args = { infoCourseReport.getIdInternal(), infoCourseReport };
        ServiceUtils.executeService(SessionUtils.getUserView(request), getEditService(), args);
        return read(mapping, form, request, response);
    }

    /**
     * This method creates an InfoServiceProviderRegime using the form
     * properties.
     * 
     * @param form
     * @return InfoServiceProviderRegime created
     */
    protected InfoCourseReport getInfoCourseReportFromForm(ActionForm form) throws FenixActionException {
        try {
            DynaActionForm dynaForm = (DynaActionForm) form;
            InfoCourseReport infoCourseReport = new InfoCourseReport();
            BeanUtils.copyProperties(infoCourseReport, dynaForm);
            Integer executionCourseId = (Integer) dynaForm.get("executionCourseId");
            Integer executionPeriodId = (Integer) dynaForm.get("executionPeriodId");
            Integer executionYearId = (Integer) dynaForm.get("executionYearId");

            InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
            infoExecutionYear.setIdInternal(executionYearId);

            InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
            infoExecutionPeriod.setIdInternal(executionPeriodId);
            infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);

            InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
            infoExecutionCourse.setIdInternal(executionCourseId);
            infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);

            infoCourseReport.setInfoExecutionCourse(infoExecutionCourse);

            return infoCourseReport;
        } catch (Exception e) {
            throw new FenixActionException(e.getMessage());
        }
    }

    /**
     * Tests if errors are presented.
     * 
     * @param request
     * @return
     */
    private boolean hasErrors(HttpServletRequest request) {

        return request.getAttribute(Globals.ERROR_KEY) != null;
    }

    /**
     * @param mapping
     * @param form
     * @param request
     */
    protected void populateFormFromInfoCourseReport(ActionMapping mapping,
            InfoCourseReport infoCourseReport, ActionForm form, HttpServletRequest request) {
        try {
            DynaActionForm dynaForm = (DynaActionForm) form;
            BeanUtils.copyProperties(form, infoCourseReport);

            InfoExecutionCourse infoExecutionCourse = infoCourseReport.getInfoExecutionCourse();
            dynaForm.set("executionCourseId", infoExecutionCourse.getIdInternal());
            dynaForm.set("executionPeriodId", infoExecutionCourse.getInfoExecutionPeriod()
                    .getIdInternal());
            dynaForm.set("executionYearId", infoExecutionCourse.getInfoExecutionPeriod()
                    .getInfoExecutionYear().getIdInternal());
        } catch (Exception e) {
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
    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SiteView siteView = readSiteView(mapping, form, request);
        List infoCoursesHistoric = readCoursesHistoric(mapping, form, request);
        if (!hasErrors(request) && siteView != null) {
            InfoSiteCourseInformation infoSiteCourseInformation = (InfoSiteCourseInformation) siteView
                    .getComponent();
            populateFormFromInfoCourseReport(mapping, infoSiteCourseInformation.getInfoCourseReport(),
                    form, request);
        }
        setSiteViewToRequest(request, siteView, mapping);
        setInfoCoursesHistoric(request, infoCoursesHistoric, mapping);
        return mapping.findForward("show-form");
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return forward to the action mapping configuration called
     *         successfull-read
     * @throws Exception
     */
    public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SiteView siteView = readSiteView(mapping, form, request);
        setSiteViewToRequest(request, siteView, mapping);
        List infoCoursesHistoric = readCoursesHistoric(mapping, form, request);
        setInfoCoursesHistoric(request, infoCoursesHistoric, mapping);
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        return mapping.findForward("successfull-read");
    }

    /**
     * @param request
     * @param infoCoursesHistoric
     * @param mapping
     */
    private void setInfoCoursesHistoric(HttpServletRequest request, List list, ActionMapping mapping)
            throws Exception {
        if (list != null) {
            request.setAttribute("infoCoursesHistoric", list);
        }
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @return
     */
    private List readCoursesHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request)
            throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        String executionCourseId = request.getParameter("executionCourseId");

        Object[] args = { new Integer(executionCourseId) };
        return (List) ServiceUtils.executeService(userView, getReadHistoricService(), args);
    }

    /**
     * Reads the infoCourseReport using de read service associated to the action
     * 
     * @param mapping
     * @param form
     * @return
     */
    private SiteView readSiteView(ActionMapping mapping, ActionForm form, HttpServletRequest request)
            throws FenixServiceException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);
        String executionCourseId = request.getParameter("executionCourseId");

        Object[] args = { new Integer(executionCourseId) };
        return (SiteView) ServiceUtils.executeService(userView, getReadService(), args);
    }

    /**
     * @param request
     * @param infoCourseReport
     */
    private void setSiteViewToRequest(HttpServletRequest request, SiteView siteView,
            ActionMapping mapping) {
        if (siteView != null) {
            request.setAttribute("siteView", siteView);
        }
    }
}