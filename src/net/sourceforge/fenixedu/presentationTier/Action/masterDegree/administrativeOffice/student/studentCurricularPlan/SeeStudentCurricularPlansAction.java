package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author David Santos 2/Out/2003
 */

public class SeeStudentCurricularPlansAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession();

        String studentId1 = this.getFromRequest("studentId", request);
        Integer studentId2 = null;

        try {
            studentId2 = new Integer(studentId1);
        } catch (NumberFormatException e) {
            throw new FenixActionException(e);
        }

        Object args[] = { studentId2 };
        IUserView userView = getUserView(request);

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