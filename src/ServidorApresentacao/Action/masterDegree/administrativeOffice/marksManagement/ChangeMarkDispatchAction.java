package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Fernanda Quitério
 * 30/06/2003
 * 
 */

public class ChangeMarkDispatchAction extends DispatchAction {

	public ActionForward prepareChangeMark(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
	
		String executionYear = getFromRequest("executionYear", request);
		String degree = getFromRequest("degree", request);
		String curricularCourse = getFromRequest("curricularCourse", request);
		Integer scopeCode = new Integer(getFromRequest("curricularCourseCode", request));
		request.setAttribute("executionYear",executionYear);
		request.setAttribute("scopeCode",scopeCode);
		request.setAttribute("curricularCourse", curricularCourse);
		request.setAttribute("degree", degree);


		return mapping.findForward("editStudentNumber");
	}

	public ActionForward chooseStudentMarks(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		MessageResources messages = getResources(request);

		String executionYear = (String) request.getParameter("executionYear");
		String degree = request.getParameter("degree");
		String curricularCourse = request.getParameter("curricularCourse");
		Integer curricularCourseCode = new Integer(request.getParameter("scopeCode"));
		Integer studentNumber = new Integer(request.getParameter("studentNumber"));

		// Get mark student List			
		Object args[] = { curricularCourseCode,studentNumber };
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;
		List infoSiteEnrolmentEvaluations = null;
		request.setAttribute("executionYear", executionYear);
		request.setAttribute("degree", degree);
		request.setAttribute("curricularCourse", curricularCourse);
		request.setAttribute("curricularCourseCode", curricularCourseCode);
		request.setAttribute("scopeCode", curricularCourseCode);
		try {
			infoSiteEnrolmentEvaluations =
				(List) serviceManager.executar(userView, "ReadStudentsAndMarksByCurricularCourse", args);
		} catch (ExistingServiceException e) {
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
					"StudentNotExist",
					new ActionError(
						"error.student.notExist"));
			saveErrors(request, actionErrors);
			return mapping.findForward("editStudentNumber");
			
		}
		
		
		if (infoSiteEnrolmentEvaluations.size() == 0){
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
					"StudentNotEnroled",
					new ActionError(
						"error.student.Enrolment.invalid"));
			saveErrors(request, actionErrors);
			return mapping.findForward("editStudentNumber");
		}
		request.setAttribute("studentNumber",studentNumber);	
		request.setAttribute("executionYear", executionYear);
		request.setAttribute("degree", degree);
		request.setAttribute("curricularCourse", curricularCourse);
		request.setAttribute("curricularCourseCode", curricularCourseCode);
		request.setAttribute("name",((InfoEnrolmentEvaluation)(((InfoSiteEnrolmentEvaluation) infoSiteEnrolmentEvaluations.get(0)).getEnrolmentEvaluations()).get(0)).getInfoEnrolment().getInfoStudentCurricularPlan().getInfoStudent().getInfoPerson().getNome());
		request.setAttribute("infoSiteEnrolmentEvaluation", infoSiteEnrolmentEvaluations);
		InfoSiteEnrolmentEvaluation SiteEnrolmentEvaluation = (InfoSiteEnrolmentEvaluation)infoSiteEnrolmentEvaluations.get(0);
		InfoEnrolmentEvaluation EnrolmentEvaluation = (InfoEnrolmentEvaluation) SiteEnrolmentEvaluation.getEnrolmentEvaluations().get(0);
		return mapping.findForward("studentMarks");
	}

	public ActionForward chooseStudentEvaluation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
			Integer studentEvaluationCode = Integer.valueOf(request.getParameter("studentPosition"));
			String executionYear = (String) request.getParameter("executionYear");
			String degree = request.getParameter("degree");
			String curricularCourse = request.getParameter("curricularCourse");
			Integer curricularCourseCode = Integer.valueOf(request.getParameter("curricularCourseCode"));

			//			Get student evaluation 
			Object args[] = {studentEvaluationCode};
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();
			InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;
			try {
				infoSiteEnrolmentEvaluation = (InfoSiteEnrolmentEvaluation) serviceManager.executar(userView, "ReadStudentEnrolmentEvaluation", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
			request.setAttribute("executionYear", executionYear);
			request.setAttribute("degree", degree);
			request.setAttribute("curricularCourse", curricularCourse);
			request.setAttribute("curricularCourseCode", curricularCourseCode);
System.out.println("--------------" + infoSiteEnrolmentEvaluation.getEnrolmentEvaluations().get(0));
			return mapping.findForward("changeStudentMark");
		
	}

	public ActionForward getStudentsAndMarksByCurricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {

			String executionYear = (String) session.getAttribute(SessionConstants.EXECUTION_YEAR);

			DynaActionForm chooseExecutionCourseForm = (DynaActionForm) form;
			String executionCourse = (String) chooseExecutionCourseForm.get("executionCourse");

			// Get 			
			Object args[] = { executionCourse };
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();
			ArrayList executionCourseList = null;
			try {
				executionCourseList = (ArrayList) serviceManager.executar(userView, "ReadExecutionCoursesByMasterDegree", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
			request.setAttribute("useCase", request.getAttribute("useCase"));
			request.setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, executionCourseList);

			return mapping.findForward("ChooseExecutionCourse");
		} else
			throw new Exception();
	}
	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}
}
