package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Tânia Pousão
 *  
 */
public class ShowMarksListOptionsAction extends FenixDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = getUserView(request);

        Integer executionCourseCode = null;
        String executionCourseCodeString = request.getParameter("objectCode");
        if (executionCourseCodeString == null) {
            executionCourseCodeString = request.getAttribute("objectCode").toString();
        }
        executionCourseCode = new Integer(executionCourseCodeString);

        Integer evaluationCode = null;
        String evaluationCodeString = request.getParameter("evaluationCode");
        if (evaluationCodeString == null) {
            evaluationCodeString = request.getAttribute("evaluationCode").toString();
        }
        evaluationCode = new Integer(evaluationCodeString);

        ISiteComponent commonComponent = new InfoSiteCommon();
        Object[] args = { executionCourseCode, commonComponent, null, null, null, null };

        TeacherAdministrationSiteView siteView = null;
        try {
            siteView = (TeacherAdministrationSiteView) ServiceUtils.executeService(userView,
                    "TeacherAdministrationSiteComponentService", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("siteView", siteView);
        request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent())
                .getExecutionCourse().getIdInternal());
        request.setAttribute("evaluationCode", evaluationCode);

        return mapping.findForward("showMarksListOptions");
    }
}