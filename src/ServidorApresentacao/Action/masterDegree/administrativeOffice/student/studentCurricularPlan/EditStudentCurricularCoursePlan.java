package ServidorApresentacao.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoEnrolment;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.StudentCurricularPlanState;

/**
 * @author Angela
 * Created on 8/Out/2003
 */
public class EditStudentCurricularCoursePlan extends DispatchAction {
	
	
	public ActionForward prepare(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response)
				throws Exception {
				HttpSession session = request.getSession();
				DynaActionForm editStudentCurricularPlanForm = (DynaActionForm) form;
				Integer studentCurricularPlanId = new Integer(getFromRequest("studentCurricularPlanId", request));
				UserView userView = (UserView) session.getAttribute("UserView");

				Object args[] = { studentCurricularPlanId };

				GestorServicos gestor = GestorServicos.manager();

				InfoStudentCurricularPlan infoStudentCurricularPlan = null;
				try {
					infoStudentCurricularPlan = (InfoStudentCurricularPlan) gestor.executar(userView, "ReadPosGradStudentCurricularPlanById", args);
				} catch (FenixServiceException e) {
					throw new FenixActionException(e);
				}
			
				//put request			
				request.setAttribute(SessionConstants.STATE,StudentCurricularPlanState.toArrayList());
				request.setAttribute("student", infoStudentCurricularPlan.getInfoStudent());
				request.setAttribute("studentCurricularPlan", infoStudentCurricularPlan);
				editStudentCurricularPlanForm.set("currentState",infoStudentCurricularPlan.getCurrentState().toString());
				editStudentCurricularPlanForm.set("credits",String.valueOf(infoStudentCurricularPlan.getGivenCredits ()));

			
				return mapping.findForward("editStudentCurricularCoursePlan");
				}
	
	
	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();
		Integer studentCurricularPlanId = null;
		DynaActionForm editStudentCurricularPlanForm = (DynaActionForm) form;
		
		
		String studentCurricularPlanIdString = request.getParameter("studentCurricularPlanId");
		
		String[] courseType = request.getParameterValues("courseType");
		StudentCurricularPlanState studentCurricularPlanState = new StudentCurricularPlanState();
		String currentState = (String)editStudentCurricularPlanForm.get("currentState");
		Double credits = Double.valueOf( (String)editStudentCurricularPlanForm.get("credits"));

		if (studentCurricularPlanIdString == null) {
			studentCurricularPlanIdString = (String) request.getAttribute("studentCurricularPlanId");
		}
		studentCurricularPlanId = new Integer(studentCurricularPlanIdString);
		UserView userView = (UserView) session.getAttribute("UserView");
		
		InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
		InfoEnrolment infoEnrolment = new InfoEnrolment();
		
		infoStudentCurricularPlan.setIdInternal(new Integer(studentCurricularPlanIdString));
		
		infoStudentCurricularPlan.setCurrentState(new StudentCurricularPlanState(currentState));
		infoStudentCurricularPlan.setGivenCredits(credits);
		
		
		
		
		Object args[] = {userView,studentCurricularPlanIdString,currentState,credits, courseType };

		GestorServicos gestor = GestorServicos.manager();

		try {
			infoStudentCurricularPlan = (InfoStudentCurricularPlan) gestor.executar(userView, "EditPosGradStudentCurricularPlans", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("student", infoStudentCurricularPlan.getInfoStudent());
		request.setAttribute("studentCurricularPlan", infoStudentCurricularPlan);

		return mapping.findForward("ShowStudentCurricularCoursePlan");
	}
	
	private String getFromRequest(String parameter, HttpServletRequest request) {
			String parameterString = request.getParameter(parameter);
			if (parameterString == null) {
				parameterString = (String) request.getAttribute(parameter);
			}
			return parameterString;
	}
}
