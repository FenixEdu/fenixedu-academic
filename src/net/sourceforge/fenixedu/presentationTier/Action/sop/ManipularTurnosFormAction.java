package net.sourceforge.fenixedu.presentationTier.Action.sop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author tfc130
 */

public class ManipularTurnosFormAction extends
        FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.execute(mapping, form, request, response);

        HttpSession sessao = request.getSession(false);
        if (sessao != null) {

            String parameter = request.getParameter(new String("operation"));

            return mapping.findForward(parameter);

        }
        throw new Exception();
    }

}