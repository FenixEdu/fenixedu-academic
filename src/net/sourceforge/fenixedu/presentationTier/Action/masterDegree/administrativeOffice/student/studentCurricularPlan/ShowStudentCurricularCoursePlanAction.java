package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan.ReadPosGradStudentCurricularPlanById;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Tânia Pousão Created on 6/Out/2003
 */
public class ShowStudentCurricularCoursePlanAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	Integer studentCurricularPlanId = null;
	String studentCurricularPlanIdString = request.getParameter("studentCurricularPlanId");
	if (studentCurricularPlanIdString == null) {
	    studentCurricularPlanIdString = (String) request.getAttribute("studentCurricularPlanId");
	}
	studentCurricularPlanId = new Integer(studentCurricularPlanIdString);

	IUserView userView = getUserView(request);

	InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) ReadPosGradStudentCurricularPlanById
		.run(studentCurricularPlanId);

	request.setAttribute("student", infoStudentCurricularPlan.getInfoStudent());
	request.setAttribute("studentCurricularPlan", infoStudentCurricularPlan);

	return mapping.findForward("ShowStudentCurricularCoursePlan");
    }
}