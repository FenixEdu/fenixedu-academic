package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.EnrolmentEvaluationType;
import Util.FormataData;

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
		if (session != null){
	
		String executionYear = getFromRequest("executionYear", request);
		String degree = getFromRequest("degree", request);
		String curricularCourse = getFromRequest("curricularCourse", request);
		Integer scopeCode = new Integer(getFromRequest("curricularCourseCode", request));
		request.setAttribute("executionYear",executionYear);
		request.setAttribute("scopeCode",scopeCode);
		request.setAttribute("curricularCourse", curricularCourse);
		request.setAttribute("degree", degree);
		} else
		throw new Exception();

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
						"error.student.Enrolment.invalid",String.valueOf(studentNumber),curricularCourse));
			saveErrors(request, actionErrors);
			return mapping.findForward("chooseCurricularCourse");
		}
		if (((InfoSiteEnrolmentEvaluation)infoSiteEnrolmentEvaluations.get(0)).getEnrolmentEvaluations().size() == 0){
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
					"StudentNotEnroled",
					new ActionError(
						"error.student.Mark.NotAvailable",String.valueOf(studentNumber)));
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
			request.setAttribute(SessionConstants.ENROLMENT_EVALUATION_TYPE_LIST, new EnrolmentEvaluationType().toArrayList());
			request.setAttribute("infoSiteEnrolmentEvaluation",infoSiteEnrolmentEvaluation);
			return mapping.findForward("editStudentMark");
		
	}

	public ActionForward studentMarkChanged(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {
		
// get input
			String executionYear = getFromRequest("executionYear", request);
			String degree = getFromRequest("degree", request);
			String curricularCourse = getFromRequest("curricularCourse", request);
			Integer curricularCourseCode = new Integer(getFromRequest("curricularCourseCode", request));	
			Integer enrolmentEvaluationCode = Integer.valueOf(getFromRequest("idInternal",request));
			String grade = getFromRequest("grade",request);	
			Integer evaluationType = Integer.valueOf(getFromRequest("enrolmentEvaluationType.type",request));
			EnrolmentEvaluationType enrolmentEvaluationType = new EnrolmentEvaluationType(evaluationType);
			Integer teacherNumber = Integer.valueOf(getFromRequest("teacherNumber",request));
			String examDay = FormataData.getDay(getFromRequest("examDate",request));
			String examMonth = FormataData.getMonth(getFromRequest("examDate",request));
			String examYear = FormataData.getYear(getFromRequest("examDate",request));	
			String observation = getFromRequest("observation",request);	
			Integer studentNumber = Integer.valueOf(getFromRequest("studentNumber",request));	
			
//			boolean result = true;
//			result = Data.validDate(Integer.valueOf(examDay),Integer.valueOf(examMonth),Integer.valueOf(examYear));
//
//
//			if (!result){
//				ActionErrors actionErrors = new ActionErrors();
//					actionErrors.add(
//							"StudentNotEnroled",
//							new ActionError(
//								"error.student.Mark.NotAvailable"));
//					saveErrors(request, actionErrors);
//					return mapping.findForward("studentMarks");
//				
//			}
			Locale locale = new Locale("pt", "PT");
			Date examDate = DateFormat.getDateInstance(DateFormat.SHORT, locale).parse(getFromRequest("examDate",request));
			Date gradeAvailableDate = DateFormat.getDateInstance(DateFormat.SHORT, locale).parse(getFromRequest("gradeAvailableDate",request));
			
			
			InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
			InfoTeacher infoTeacher = new InfoTeacher();
			
			infoTeacher.setTeacherNumber(teacherNumber);
			
			InfoStudent infoStudent = new InfoStudent();
			infoStudent.setNumber(studentNumber);
			
			InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
			infoStudentCurricularPlan.setInfoStudent(infoStudent);
			
			InfoEnrolment infoEnrolment = new InfoEnrolment();
			infoEnrolment.setInfoStudentCurricularPlan(infoStudentCurricularPlan);
			

			infoEnrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);

			infoEnrolmentEvaluation.setGrade(grade);
			infoEnrolmentEvaluation.setExamDate(examDate);
			infoEnrolmentEvaluation.setGradeAvailableDate(gradeAvailableDate);
			infoEnrolmentEvaluation.setObservation(observation);
			infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);
			
			
//			Object args[] = {enrolmentEvaluationCode,infoEnrolmentEvaluation,infoTeacher};
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();
			Object args[] = {curricularCourseCode, enrolmentEvaluationCode,infoEnrolmentEvaluation,infoTeacher.getTeacherNumber(), userView};
			InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;
			try {
				Object resultObj = (Object) serviceManager.executar(userView, "AlterStudentEnrolmentEvaluation", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}


			request.setAttribute("executionYear", executionYear);
			request.setAttribute("degree", degree);
			request.setAttribute("curricularCourse", curricularCourse);
			request.setAttribute("curricularCourseCode", curricularCourseCode);
		}
			return mapping.findForward("studentMarks");
		
	}
	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}
}
