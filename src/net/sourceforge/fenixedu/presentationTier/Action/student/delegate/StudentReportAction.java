/*
 * Created on Feb 20, 2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.delegate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoUniversity;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseHistoric;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoSiteStudentCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoStudentCourseReport;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.University;
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
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class StudentReportAction extends DispatchAction {
    private String getReadService() {
        return "ReadStudentCourseReport";
    }

    private String getEditService() {
        return "EditStudentCourseReport";
    }

    private String getReadCourseHistoricService() {
        return "ReadCurricularCourseHistoric";
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
        InfoStudentCourseReport infoStudentCourseReport = getInfoStudentCourseReportFromForm(form);
        Object[] args = { infoStudentCourseReport.getIdInternal(), infoStudentCourseReport };
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
    protected InfoStudentCourseReport getInfoStudentCourseReportFromForm(ActionForm form)
            throws FenixActionException {
        try {
            DynaActionForm dynaForm = (DynaActionForm) form;
            InfoStudentCourseReport infoStudentCourseReport = new InfoStudentCourseReport();
            BeanUtils.copyProperties(infoStudentCourseReport, dynaForm);
            Integer curricularCourseId = (Integer) dynaForm.get("curricularCourseId");
            Integer degreeCurricularPlanId = (Integer) dynaForm.get("degreeCurricularPlanId");
            final DegreeCurricularPlan degreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanId);
            Integer universityId = (Integer) dynaForm.get("universityId");
            final University university = RootDomainObject.getInstance().readUniversityByOID(universityId);

            InfoUniversity infoUniversity = new InfoUniversity(university);

            InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan(degreeCurricularPlan);

            final CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().
    				readDegreeModuleByOID(curricularCourseId);
            InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse(curricularCourse);
            infoCurricularCourse.setInfoUniversity(infoUniversity);
            infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

            infoStudentCourseReport.setInfoCurricularCourse(infoCurricularCourse);

            return infoStudentCourseReport;
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
    protected void populateFormFromInfoSiteStudentCourseReport(ActionMapping mapping,
            InfoSiteStudentCourseReport infoSiteStudentCourseReport, ActionForm form,
            HttpServletRequest request) {
        try {
            DynaActionForm dynaForm = (DynaActionForm) form;
            InfoStudentCourseReport infoStudentCourseReport = infoSiteStudentCourseReport
                    .getInfoStudentCourseReport();
            BeanUtils.copyProperties(form, infoStudentCourseReport);

            InfoCurricularCourse infoCurricularCourse = infoStudentCourseReport
                    .getInfoCurricularCourse();
            dynaForm.set("curricularCourseId", infoCurricularCourse.getIdInternal());
            dynaForm.set("universityId", infoCurricularCourse.getInfoUniversity().getIdInternal());
            dynaForm.set("degreeCurricularPlanId", infoCurricularCourse.getInfoDegreeCurricularPlan()
                    .getIdInternal());
            dynaForm.set("degreeId", infoCurricularCourse.getInfoDegreeCurricularPlan().getInfoDegree()
                    .getIdInternal());
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
        InfoSiteStudentCourseReport infoSiteStudentCourseReport = readInfoSiteStudentCourseReport(
                mapping, form, request);
        InfoSiteCourseHistoric infoSiteCourseHistoric = readInfoSiteCourseHistoric(mapping, form,
                request);

        if (!hasErrors(request) && infoSiteStudentCourseReport != null) {
            populateFormFromInfoSiteStudentCourseReport(mapping, infoSiteStudentCourseReport, form,
                    request);
        }
        setInfoSiteStudentCourseReportToRequest(request, infoSiteStudentCourseReport, mapping);
        setInfoSiteCourseHistoricToRequest(request, infoSiteCourseHistoric, mapping);
        return mapping.findForward("show-form");
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @return
     */
    private InfoSiteCourseHistoric readInfoSiteCourseHistoric(ActionMapping mapping, ActionForm form,
            HttpServletRequest request) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        String curricularCourseId = request.getParameter("curricularCourseId");

        Object[] args = { new Integer(curricularCourseId) };
        return (InfoSiteCourseHistoric) ServiceUtils.executeService(userView,
                getReadCourseHistoricService(), args);
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @return
     */
    private InfoSiteStudentCourseReport readInfoSiteStudentCourseReport(ActionMapping mapping,
            ActionForm form, HttpServletRequest request) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);
        String curricularCourseId = request.getParameter("curricularCourseId");

        Object[] args = { new Integer(curricularCourseId) };
        return (InfoSiteStudentCourseReport) ServiceUtils.executeService(userView, getReadService(),
                args);
    }

    /**
     * @param request
     * @param infoSiteCourseHistoric
     * @param mapping
     */
    private void setInfoSiteCourseHistoricToRequest(HttpServletRequest request,
            InfoSiteCourseHistoric infoSiteCourseHistoric, ActionMapping mapping) {
        if (infoSiteCourseHistoric != null) {
            request.setAttribute("infoSiteCourseHistoric", infoSiteCourseHistoric);
        }
    }

    /**
     * @param request
     * @param infoStudentCourseReport
     * @param mapping
     */
    private void setInfoSiteStudentCourseReportToRequest(HttpServletRequest request,
            InfoSiteStudentCourseReport infoSiteStudentCourseReport, ActionMapping mapping) {
        if (infoSiteStudentCourseReport != null) {
            request.setAttribute("infoSiteStudentCourseReport", infoSiteStudentCourseReport);
        }
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
        InfoSiteStudentCourseReport infoSiteStudentCourseReport = readInfoSiteStudentCourseReport(
                mapping, form, request);
        InfoSiteCourseHistoric infoSiteCourseHistoric = readInfoSiteCourseHistoric(mapping, form,
                request);

        setInfoSiteStudentCourseReportToRequest(request, infoSiteStudentCourseReport, mapping);
        setInfoSiteCourseHistoricToRequest(request, infoSiteCourseHistoric, mapping);
        return mapping.findForward("successfull-read");
    }
}