package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
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
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
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
		Integer curricularCourseCode = new Integer(getFromRequest("curricularCourseCode", request));
		request.setAttribute("executionYear",executionYear);
		request.setAttribute("curricularCourseCode",curricularCourseCode);
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

		String executionYear = (String) getFromRequest("executionYear", request); 
		String degree = getFromRequest("degree", request);
		String curricularCourse = getFromRequest("curricularCourse", request);

		Integer curricularCourseCode = Integer.valueOf(getFromRequest("curricularCourseCode", request));
		Integer studentNumber = Integer.valueOf(getFromRequest("studentNumber", request));

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
//			
//			
//
//
//			if (!Util.Data.validDate(Integer.valueOf(examDay),Integer.valueOf(examMonth),Integer.valueOf(examYear))){
//
//				ActionErrors actionErrors = new ActionErrors();
//					actionErrors.add(
//							"StudentNotEnroled",
//							new ActionError(
//								"error.student.Mark.NotAvailable"));
//					saveErrors(request, actionErrors);
//					return mapping.findForward("studentMarks");			
//			}
			Locale locale = new Locale("pt", "PT");
			Date examDate = DateFormat.getDateInstance(DateFormat.SHORT, locale).parse(getFromRequest("examDate",request));
			Date gradeAvailableDate = DateFormat.getDateInstance(DateFormat.SHORT, locale).parse(getFromRequest("gradeAvailableDate",request));
//ver isso das datas
////			result = DataIndisponivel.isDataIndisponivel(examDate);
			
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
			request.setAttribute("executionYear", executionYear);
			request.setAttribute("degree", degree);
			request.setAttribute("curricularCourse", curricularCourse);
			request.setAttribute("curricularCourseCode", curricularCourseCode);
			request.setAttribute("scopeCode", curricularCourseCode);
			request.setAttribute("studentNumber", studentNumber);
			List evaluationsWithError = null;
			try {
				IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
				GestorServicos serviceManager = GestorServicos.manager();
				Object args[] = {curricularCourseCode, enrolmentEvaluationCode,infoEnrolmentEvaluation,infoTeacher.getTeacherNumber(), userView};
				InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;
				evaluationsWithError = (List) serviceManager.executar(userView, "AlterStudentEnrolmentEvaluation", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException(teacherNumber.toString(), e);
			}
			request.setAttribute("Label.MarkChange", "Registo  Alterado");
			
//			check for invalid marks
			ActionErrors actionErrors = null;
			actionErrors = checkForErrors(evaluationsWithError);
			if (actionErrors != null) {
				saveErrors(request, actionErrors);
				return mapping.getInputForward();
			}

			
		}
			return mapping.findForward("editStudentMarkChanged");
		
	}
	
	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}
	
	private ActionErrors checkForErrors(List evaluationsWithError) {
			ActionErrors actionErrors = null;

			if (evaluationsWithError != null && evaluationsWithError.size() > 0) {
				actionErrors = new ActionErrors();
				Iterator iterator = evaluationsWithError.listIterator();
				while (iterator.hasNext()) {
					InfoEnrolmentEvaluation infoEnrolmentEvaluation = (InfoEnrolmentEvaluation) iterator.next();

					actionErrors.add(
						"invalidGrade",
						new ActionError(
							"errors.invalidMark",
							infoEnrolmentEvaluation.getGrade(),
							String.valueOf(
								infoEnrolmentEvaluation
									.getInfoEnrolment()
									.getInfoStudentCurricularPlan()
									.getInfoStudent()
									.getNumber()
									.intValue())));
				}
			}
			return actionErrors;
		}
}
