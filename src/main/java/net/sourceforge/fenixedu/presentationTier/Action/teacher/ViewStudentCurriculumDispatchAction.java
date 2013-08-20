package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Fernanda Quitério 05/Fev/2004
 * 
 */
public class ViewStudentCurriculumDispatchAction extends FenixDispatchAction {

    public ActionForward prepareView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        getExecutionDegreeId(request);

        if (request.getParameter("degreeCurricularPlanID") != null) {
            String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        return mapping.findForward("prepareViewStudentCurriculumChooseStudent");
    }

    private void getExecutionDegreeId(HttpServletRequest request) {
        String executionDegreeId = request.getParameter("executionDegreeId");
        if (executionDegreeId == null) {
            executionDegreeId = (String) request.getAttribute("executionDegreeId");
        }
        request.setAttribute("executionDegreeId", executionDegreeId);
    }
}