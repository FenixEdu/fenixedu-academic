package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoEnrolment;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;
import Util.EnrolmentEvaluationType;
import Util.FormataData;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Angela 30/06/2003 Modified by Fernanda Quitério
 */
public class ChangeMarkDispatchAction extends DispatchAction
{
	InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = null;

	public ActionForward prepareChangeMark(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		String curricularCourseId = MarksManagementDispatchAction.getFromRequest("courseId", request);
		MarksManagementDispatchAction.getFromRequest("objectCode", request);
		MarksManagementDispatchAction.getFromRequest("degreeId", request);

		List listEnrolmentEvaluation = null;
		IUserView userView = (IUserView) SessionUtils.getUserView(request);
		Object args[] = { userView, Integer.valueOf(curricularCourseId), null };
		try
		{
			listEnrolmentEvaluation =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadStudentMarksListByCurricularCourse",
					args);
		}
		catch (NotAuthorizedException e)
		{
			return mapping.findForward("NotAuthorized");
		}
		catch (NonExistingServiceException e)
		{
			errors.add("nonExisting", new ActionError("error.exception.noStudents"));
			saveErrors(request, errors);
			return mapping.findForward("NoStudents");
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		if (listEnrolmentEvaluation.size() == 0)
		{
			errors.add("StudentNotEnroled", new ActionError("error.students.Mark.NotAvailable"));
			saveErrors(request, errors);
			return mapping.findForward("NoStudents");
		}

		InfoEnrolment infoEnrolment = (InfoEnrolment) listEnrolmentEvaluation.get(0);
		request.setAttribute("oneInfoEnrollment", infoEnrolment);

		return mapping.findForward("editStudentNumber");
	}

	public ActionForward chooseStudentMarks(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

		ActionErrors actionErrors = new ActionErrors();

		String curricularCourseId = MarksManagementDispatchAction.getFromRequest("courseId", request);
		MarksManagementDispatchAction.getFromRequest("objectCode", request);
		MarksManagementDispatchAction.getFromRequest("degreeId", request);
		Integer studentNumber = null;
		try
		{
			studentNumber =
				Integer.valueOf(MarksManagementDispatchAction.getFromRequest("studentNumber", request));
		}
		catch (NumberFormatException e)
		{
			actionErrors.add("StudentNumberRequired", new ActionError("error.studentNumber.required"));
			saveErrors(request, actionErrors);
			return prepareChangeMark(mapping, form, request, response);

		}
		// Get mark student List
		String showMarks = MarksManagementDispatchAction.getFromRequest("showMarks", request);

		List infoSiteEnrolmentEvaluations = null;
		IUserView userView = (IUserView) SessionUtils.getUserView(request);
		try
		{
			Object args[] = { Integer.valueOf(curricularCourseId), studentNumber, null };
			infoSiteEnrolmentEvaluations =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadStudentMarksByCurricularCourse",
					args);
		}
		catch (ExistingServiceException e)
		{
			//invalid student number
			actionErrors.add("StudentNotExist", new ActionError("error.student.notExist"));
			saveErrors(request, actionErrors);
			if (showMarks == null)
			{
				return prepareChangeMark(mapping, form, request, response);
			}
			else
			{
				return mapping.findForward("chooseCurricularCourse");
			}
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		if (infoSiteEnrolmentEvaluations.size() == 0)
		{
			actionErrors.add(
				"StudentNotEnroled",
				new ActionError(
					"error.student.Enrolment.curricularCourse.invalid",
					String.valueOf(studentNumber)));
			saveErrors(request, actionErrors);
			return prepareChangeMark(mapping, form, request, response);
		}

		if (((InfoSiteEnrolmentEvaluation) infoSiteEnrolmentEvaluations.get(0))
			.getEnrolmentEvaluations()
			.size()
			== 0)
		{
			actionErrors.add(
				"StudentNotEnroled",
				new ActionError("error.student.Mark.NotAvailable", String.valueOf(studentNumber)));
			saveErrors(request, actionErrors);
			if (showMarks != null)
			{
				return mapping.findForward("chooseCurricularCourse");
			}
			return prepareChangeMark(mapping, form, request, response);
		}

		InfoEnrolment infoEnrolmentTemp =
			((InfoEnrolmentEvaluation) ((InfoSiteEnrolmentEvaluation) infoSiteEnrolmentEvaluations
				.get(0))
				.getEnrolmentEvaluations()
				.get(0))
				.getInfoEnrolment();

		InfoEnrolmentEvaluation newEnrolmentEvaluation = null;
		try
		{
			Object args[] = { userView, infoEnrolmentTemp.getIdInternal()};
			newEnrolmentEvaluation =
				(InfoEnrolmentEvaluation) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadInfoEnrolmentEvaluationByEvaluationOID",
					args);
		}
		catch (ExistingServiceException e)
		{
			throw new ExistingActionException(e);
		}

		Locale locale = new Locale("pt", "PT");

		String examDay =
			FormataData.getDay(
				DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
					newEnrolmentEvaluation.getExamDate()));
		String examMonth =
			FormataData.getMonth(
				DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
					newEnrolmentEvaluation.getExamDate()));
		String examYear =
			FormataData.getYear(
				DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
					newEnrolmentEvaluation.getExamDate()));
		String gradeAvailableDay =
			FormataData.getDay(
				DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
					newEnrolmentEvaluation.getGradeAvailableDate()));
		String gradeAvailableMonth =
			FormataData.getMonth(
				DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
					newEnrolmentEvaluation.getGradeAvailableDate()));
		String gradeAvailableYear =
			FormataData.getYear(
				DateFormat.getDateInstance(DateFormat.SHORT, locale).format(
					newEnrolmentEvaluation.getGradeAvailableDate()));

		DynaActionForm studentNumberForm = (DynaActionForm) form;

		studentNumberForm.set("examDateYear", examYear);
		int month = Integer.valueOf(examMonth).intValue() - 1;
		studentNumberForm.set("examDateMonth", String.valueOf(month));
		studentNumberForm.set("examDateDay", new Integer(Integer.parseInt(examDay)).toString());

		studentNumberForm.set("gradeAvailableDateYear", gradeAvailableYear);
		month = Integer.valueOf(gradeAvailableMonth).intValue() - 1;
		studentNumberForm.set("gradeAvailableDateMonth", String.valueOf(month));
		studentNumberForm.set(
			"gradeAvailableDateDay",
			new Integer(Integer.parseInt(gradeAvailableDay)).toString());
		studentNumberForm.set("grade", newEnrolmentEvaluation.getGrade());
		studentNumberForm.set("observation", newEnrolmentEvaluation.getObservation());
		studentNumberForm.set(
			"enrolmentEvaluationType",
			String.valueOf(newEnrolmentEvaluation.getEnrolmentEvaluationType().getType()));
		studentNumberForm.set("studentNumber", studentNumber);
		//		responsible teacher
		InfoTeacher infoTeacher = null;

		try
		{
			Object args[] = { newEnrolmentEvaluation.getInfoPersonResponsibleForGrade().getUsername()};
			infoTeacher =
				(InfoTeacher) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadTeacherByUserName",
					args);
		}
		catch (ExistingServiceException e)
		{
			throw new ExistingActionException(e);
		}
		studentNumberForm.set("teacherNumber", String.valueOf(infoTeacher.getTeacherNumber()));
		request.setAttribute(
			SessionConstants.ENROLMENT_EVALUATION_TYPE_LIST,
			new EnrolmentEvaluationType().toArrayList());
		request.setAttribute(SessionConstants.MONTH_DAYS_KEY, Data.getMonthDays());
		request.setAttribute(SessionConstants.MONTH_LIST_KEY, Data.getMonths());
		request.setAttribute(SessionConstants.YEARS_KEY, Data.getYears());

		request.setAttribute("lastEnrolmentEavluation", newEnrolmentEvaluation);
		request.setAttribute("infoSiteEnrolmentEvaluation", infoSiteEnrolmentEvaluations);

		return mapping.findForward("studentMarks");
	}

	public ActionForward studentMarkChanged(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		String curricularCourseId = MarksManagementDispatchAction.getFromRequest("courseId", request);
		MarksManagementDispatchAction.getFromRequest("objectCode", request);
		MarksManagementDispatchAction.getFromRequest("degreeId", request);

		DynaActionForm studentNumberForm = (DynaActionForm) form;
		// get input
		Integer enrolmentEvaluationCode =
			Integer.valueOf(MarksManagementDispatchAction.getFromRequest("teacherCode", request));
		String grade = MarksManagementDispatchAction.getFromRequest("grade", request);

		Integer evaluation =
			Integer.valueOf(
				MarksManagementDispatchAction.getFromRequest("enrolmentEvaluationType", request));

		EnrolmentEvaluationType enrolmentEvaluationType = new EnrolmentEvaluationType(evaluation);

		Integer teacherNumber = null;

		try
		{
			teacherNumber =
				Integer.valueOf(MarksManagementDispatchAction.getFromRequest("teacherNumber", request));
		}
		catch (NumberFormatException e)
		{
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add("TeacharNumberRequired", new ActionError("error.teacherNumber.required"));
			saveErrors(request, actionErrors);
			return chooseStudentMarks(mapping, form, request, response);

		}

		String observation = MarksManagementDispatchAction.getFromRequest("observation", request);
		Integer studentNumber =
			Integer.valueOf(MarksManagementDispatchAction.getFromRequest("studentNumber", request));
		Calendar examDate = Calendar.getInstance();

		String examMonth = MarksManagementDispatchAction.getFromRequest("examDateMonth", request);
		String examYear = MarksManagementDispatchAction.getFromRequest("examDateYear", request);
		String examDay = MarksManagementDispatchAction.getFromRequest("examDateDay", request);

		String gradeAvailableDateMonth =
			MarksManagementDispatchAction.getFromRequest("gradeAvailableDateMonth", request);
		String gradeAvailableDateYear =
			MarksManagementDispatchAction.getFromRequest("gradeAvailableDateYear", request);
		String gradeAvailableDateDay =
			MarksManagementDispatchAction.getFromRequest("gradeAvailableDateDay", request);
		Integer day = null;
		Integer month = null;
		Integer year = null;

		InfoEnrolmentEvaluation infoEnrolmentEvaluation = new InfoEnrolmentEvaluation();
		InfoTeacher infoTeacher = new InfoTeacher();

		if ((examDay == null)
			|| (examMonth == null)
			|| (examYear == null)
			|| (examDay.length() == 0)
			|| (examMonth.length() == 0)
			|| (examYear.length() == 0))
		{
			//				infoEnrolmentEvaluation.setExamDate(null);
			throw new FenixActionException("error.data.exame.inválida");
		}
		else
		{
			day = new Integer((String) studentNumberForm.get("examDateDay"));
			month = new Integer((String) studentNumberForm.get("examDateMonth"));
			year = new Integer((String) studentNumberForm.get("examDateYear"));

			examDate.set(year.intValue(), month.intValue(), day.intValue());
			infoEnrolmentEvaluation.setExamDate(examDate.getTime());
		}

		day = null;
		month = null;
		year = null;
		examDate = Calendar.getInstance();

		if ((gradeAvailableDateDay == null)
			|| (gradeAvailableDateMonth == null)
			|| (gradeAvailableDateYear == null)
			|| (gradeAvailableDateDay.length() == 0)
			|| (gradeAvailableDateMonth.length() == 0)
			|| (gradeAvailableDateYear.length() == 0))
		{
			//				infoEnrolmentEvaluation.setGradeAvailableDate(null);
			throw new FenixActionException("error.data.lançamento.inválida");
		}
		else
		{
			day = new Integer((String) studentNumberForm.get("gradeAvailableDateDay"));
			month = new Integer((String) studentNumberForm.get("gradeAvailableDateMonth"));
			year = new Integer((String) studentNumberForm.get("gradeAvailableDateYear"));

			examDate.set(year.intValue(), month.intValue(), day.intValue());
			infoEnrolmentEvaluation.setGradeAvailableDate(examDate.getTime());
		}
		infoTeacher.setTeacherNumber(teacherNumber);

		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(studentNumber);

		InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
		infoStudentCurricularPlan.setInfoStudent(infoStudent);

		InfoEnrolment infoEnrolment = new InfoEnrolment();
		infoEnrolment.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

		infoEnrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);

		infoEnrolmentEvaluation.setGrade(grade);
		infoEnrolmentEvaluation.setObservation(observation);
		infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);

		List evaluationsWithError = null;
		try
		{
			IUserView userView = (IUserView) SessionUtils.getUserView(request);
			Object args[] =
				{
					Integer.valueOf(curricularCourseId),
					enrolmentEvaluationCode,
					infoEnrolmentEvaluation,
					infoTeacher.getTeacherNumber(),
					userView };
			evaluationsWithError =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"AlterStudentEnrolmentEvaluation",
					args);
		}
		catch (NonExistingServiceException e)
		{
			throw new NonExistingActionException(teacherNumber.toString(), e);
		}

		//			check for invalid marks
		ActionErrors actionErrors = null;
		actionErrors = checkForErrors(evaluationsWithError);
		if (actionErrors != null)
		{
			saveErrors(request, actionErrors);
			return chooseStudentMarks(mapping, form, request, response);
		}

		request.setAttribute("Label.MarkChange", "Registo  Alterado");

		return mapping.findForward("changeStudentMark");
	}

	private ActionErrors checkForErrors(List evaluationsWithError)
	{
		ActionErrors actionErrors = null;

		if (evaluationsWithError != null && evaluationsWithError.size() > 0)
		{
			actionErrors = new ActionErrors();
			Iterator iterator = evaluationsWithError.listIterator();
			while (iterator.hasNext())
			{
				InfoEnrolmentEvaluation infoEnrolmentEvaluation =
					(InfoEnrolmentEvaluation) iterator.next();

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
