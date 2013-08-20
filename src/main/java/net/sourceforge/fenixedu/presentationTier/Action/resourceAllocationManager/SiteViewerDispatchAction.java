package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ExecutionCourseSiteComponentService;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SiteViewerDispatchAction extends FenixDispatchAction {

    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        ISiteComponent firstPageComponent = new InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses();

        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }

        if (readSiteView(request, firstPageComponent, objectCodeString, null) == true) {
            return mapping.findForward("sucess");
        }
        return mapping.findForward("erro");
    }

    private boolean readSiteView(HttpServletRequest request, ISiteComponent firstPageComponent, String infoExecutionCourseCode,
            Integer sectionIndex) throws FenixActionException {

        String objectCodeString = null;
        if (infoExecutionCourseCode == null) {
            objectCodeString = request.getParameter("objectCode");
            if (objectCodeString == null) {
                objectCodeString = (String) request.getAttribute("objectCode");

            }
        }

        ISiteComponent commonComponent = new InfoSiteCommon();

        boolean result = true;
        try {
            ExecutionCourseSiteView siteView =
                    ExecutionCourseSiteComponentService.runExecutionCourseSiteComponentService(commonComponent,
                            firstPageComponent, objectCodeString, infoExecutionCourseCode, sectionIndex, null);
            request.setAttribute("objectCode", objectCodeString);
            if (siteView == null) {
                result = false;

                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("StudentNotEnroled", new ActionError("error.nonExisting.AssociatedCurricularCourses"));
                saveErrors(request, actionErrors);

            } else {
                request.setAttribute("siteView", siteView);
                request.setAttribute("executionCourseCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse()
                        .getExternalId());
                request.setAttribute("executionPeriodCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse()
                        .getInfoExecutionPeriod().getExternalId());

            }
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A disciplina", e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return result;
    }

}