package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.TeacherAdministrationSiteComponentService;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

/**
 * @author Tânia Pousão
 * 
 */
public class ShowMarksListOptionsAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = getUserView(request);

        String executionCourseCodeString = request.getParameter("objectCode");
        if (executionCourseCodeString == null) {
            executionCourseCodeString = request.getAttribute("objectCode").toString();
        }

        String evaluationCodeString = request.getParameter("evaluationCode");
        if (evaluationCodeString == null) {
            evaluationCodeString = request.getAttribute("evaluationCode").toString();
        }

        ISiteComponent commonComponent = new InfoSiteCommon();

        TeacherAdministrationSiteView siteView = null;
        try {
            siteView =
                    TeacherAdministrationSiteComponentService.runTeacherAdministrationSiteComponentService(
                            executionCourseCodeString, commonComponent, null, null, null);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse().getExternalId());
        request.setAttribute("evaluationCode", evaluationCodeString);

        return mapping.findForward("showMarksListOptions");
    }
}