package ServidorApresentacao.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos 2/Out/2003
 */

public class SeeStudentCurricularPlansAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        HttpSession session = request.getSession();

        String studentId1 = this.getFromRequest("studentId", request);
        Integer studentId2 = null;

        try {
            studentId2 = new Integer(studentId1);
        } catch (NumberFormatException e) {
            throw new FenixActionException(e);
        }

        Object args[] = { studentId2 };
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        List studentCurricularPlansList = null;
        try {
            studentCurricularPlansList = (ArrayList) ServiceUtils.executeService(userView,
                    "ReadPosGradStudentCurricularPlans", args);
            if (studentCurricularPlansList != null && !studentCurricularPlansList.isEmpty()) {
                Collections.sort(studentCurricularPlansList);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("studentCurricularPlansList", studentCurricularPlansList);
        request.setAttribute("student", ((InfoStudentCurricularPlan) studentCurricularPlansList.get(0))
                .getInfoStudent());

        return mapping.findForward("viewStudentCurricularPlans");
    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }
}