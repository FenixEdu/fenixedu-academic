/*
 * Created on 17/Fev/2004
 *  
 */
package ServidorApresentacao.Action.degreeAdministrativeOffice.withoutRules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.ExecutionDegreesFormat;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class ExecutionCourseEnrolmentWithoutRulesManagerDispatchAction extends DispatchAction
{
	private static final int MAX_CURRICULAR_YEARS = 5;
	private static final int MAX_CURRICULAR_SEMESTERS = 2;

	public ActionForward readEnrollments(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm prepareEnrolmentForm = (DynaActionForm) form;

		Integer studentNumber = Integer.valueOf((String) prepareEnrolmentForm.get("studentNumber"));
		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(studentNumber);

		String executionYear = (String) prepareEnrolmentForm.get("executionYear");
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear(executionYear);

		String degreeTypeCode = (String) prepareEnrolmentForm.get("degreeType");
		TipoCurso degreeType = new TipoCurso();
		if (degreeTypeCode != null && degreeTypeCode.length() > 0)
		{
			degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));
		}

		Object[] args = { infoStudent, degreeType };
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = null;
		try
		{
			infoStudentEnrolmentContext =
				(InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadEnrollmentsWithStateEnrolledByStudent",
					args);

			//set the execution year choosen in the enrollment context
			InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
			infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
			infoStudentEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();

			errors.add("", new ActionError(e.getMessage()));
			saveErrors(request, errors);

			mapping.getInput();
		}

		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);

		return mapping.findForward("curricularCourseEnrollmentList");
	}

	public ActionForward unEnrollCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		System.out.println("-->unEnroll<-- ");
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm unEnrollForm = (DynaActionForm) form;
		Integer studentNumber = Integer.valueOf((String) unEnrollForm.get("studentNumber"));
		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(studentNumber);
		
		String executionYear = (String) unEnrollForm.get("executionYear");
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear(executionYear);

		String degreeTypeCode = (String) unEnrollForm.get("degreeType");
		TipoCurso degreeType = new TipoCurso();
		if (degreeTypeCode != null && degreeTypeCode.length() > 0)
		{
			degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));
		}
		
		Integer[] unenrollments = (Integer[]) unEnrollForm.get("unenrollments");
		List unenrollmentsList = Arrays.asList(unenrollments);

		Object[] args = { infoStudent, degreeType, unenrollmentsList };
		try
		{
			ServiceManagerServiceFactory.executeService(userView, "DeleteEnrollmentsList", args);
		}
		catch (NotAuthorizedException e)
		{
			e.printStackTrace();

			errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
			saveErrors(request, errors);

			mapping.getInput();
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();

			errors.add("", new ActionError(e.getMessage()));
			saveErrors(request, errors);

			mapping.getInput();
		}

		return mapping.findForward("readCurricularCourseEnrollmentList");
	}

	public ActionForward prepareEnrollmentCourses(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{

		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm prepareEnrolmentForm = (DynaActionForm) form;

		String studentNumber = (String) prepareEnrolmentForm.get("studentNumber");

		String executionYear = (String) prepareEnrolmentForm.get("executionYear");
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear(executionYear);

		String degreeTypeCode = (String) prepareEnrolmentForm.get("degreeType");
		TipoCurso degreeType = new TipoCurso();
		if (degreeTypeCode != null && degreeTypeCode.length() > 0)
		{
			degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));
		}

		//read execution degree
		Object args[] = { infoExecutionYear, degreeType };
		List executionDegreeList = null;
		try
		{
			executionDegreeList =
				(List) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadExecutionDegreesByExecutionYearAndDegreeType",
					args);
		}
		catch (FenixServiceException e)
		{
			errors.add(
				"impossibleOperation",
				new ActionError("error.masterDegree.gratuity.impossible.operation"));
			saveErrors(request, errors);
			return mapping.findForward("choose");
		}
		Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());
		List executionDegreeLabels =
			ExecutionDegreesFormat.buildExecutionDegreeLabelValueBean(executionDegreeList);
		request.setAttribute(SessionConstants.DEGREE_LIST, executionDegreeLabels);

		//read all curricular years and semester
		List listOfChosenCurricularYears = getListOfChosenCurricularYears();
		List listOfChosenCurricularSemesters = getListOfChosenCurricularSemesters();
		request.setAttribute(SessionConstants.ENROLMENT_YEAR_LIST_KEY, listOfChosenCurricularYears);
		request.setAttribute(
			SessionConstants.ENROLMENT_SEMESTER_LIST_KEY,
			listOfChosenCurricularSemesters);

		//maintenance of the form
		DynaActionForm enrollForm = (DynaActionForm) form;
		enrollForm.set("studentNumber", studentNumber);
		enrollForm.set("executionYear", executionYear);
		enrollForm.set("degreeType", degreeTypeCode);

		//maintenance of the Context with the student's number and name and execution year
		InfoStudentEnrolmentContext infoStudentEnrolmentContext =
			maintenanceContext(studentNumber, infoExecutionYear);
		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);

		return mapping.findForward("choosesForEnrollment");
	}

	private List getListOfChosenCurricularYears()
	{
		List result = new ArrayList();

		for (int i = 1; i <= MAX_CURRICULAR_YEARS; i++)
		{
			result.add(new Integer(i));
		}
		return result;
	}

	private List getListOfChosenCurricularSemesters()
	{
		List result = new ArrayList();

		for (int i = 1; i <= MAX_CURRICULAR_SEMESTERS; i++)
		{
			result.add(new Integer(i));
		}
		return result;
	}

	private InfoStudentEnrolmentContext maintenanceContext(
		String studentNumber,
		InfoExecutionYear infoExecutionYear)
	{
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = new InfoStudentEnrolmentContext();

		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(Integer.valueOf(studentNumber));
		InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
		infoStudentCurricularPlan.setInfoStudent(infoStudent);
		infoStudentEnrolmentContext.setInfoStudentCurricularPlan(infoStudentCurricularPlan);

		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
		infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
		infoStudentEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);
		return infoStudentEnrolmentContext;
	}

	public ActionForward readCoursesToEnroll(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		System.out.println("-->enrollmentExecutionCourses<-- ");
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm enrollForm = (DynaActionForm) form;

		Integer studentNumber = Integer.valueOf((String) enrollForm.get("studentNumber"));
		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(studentNumber);

		String executionYear = (String) enrollForm.get("executionYear");
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear(executionYear);

		String degreeTypeCode = (String) enrollForm.get("degreeType");
		TipoCurso degreeType = new TipoCurso();
		if (degreeTypeCode != null && degreeTypeCode.length() > 0)
		{
			degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));
		}

		Integer executionDegreeID = Integer.valueOf((String) enrollForm.get("executionDegree"));

		Integer[] curricularYears = (Integer[]) enrollForm.get("curricularYears");
		List curricularYearsList = Arrays.asList(curricularYears);

		Integer[] curricularSemesters = (Integer[]) enrollForm.get("curricularSemesters");
		List curricularSemestersList = Arrays.asList(curricularSemesters);

		Object args[] =
			{
				infoStudent,
				degreeType,
				infoExecutionYear,
				executionDegreeID,
				curricularYearsList,
				curricularSemestersList };
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = null;
		try
		{
			infoStudentEnrolmentContext =
				(InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(
					userView,
					"ReadCurricularCoursesToEnroll",
					args);
			
			//set the execution year choosen in the enrollment context
			InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
			infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
			infoStudentEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();

			errors.add("", new ActionError(e.getMessage()));
			saveErrors(request, errors);

			mapping.getInput();
		}
		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);
		
		return mapping.findForward("showCurricularCourseToEnroll");
	}
	
	public ActionForward enrollCourses(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception
	{
		System.out.println("-->enrollCourses<-- ");
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm enrollForm = (DynaActionForm) form;
		Integer studentNumber = Integer.valueOf((String) enrollForm.get("studentNumber"));
		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(studentNumber);
		
		String executionYear = (String) enrollForm.get("executionYear");
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear(executionYear);

		String degreeTypeCode = (String) enrollForm.get("degreeType");
		TipoCurso degreeType = new TipoCurso();
		if (degreeTypeCode != null && degreeTypeCode.length() > 0)
		{
			degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));
		}
		
		Integer[] curricularCourses = (Integer[]) enrollForm.get("curricularCourses");
		List curricularCoursesList = Arrays.asList(curricularCourses);
		System.out.println("-->courses: " + curricularCoursesList);
		
		Object[] args = { infoStudent, degreeType, infoExecutionYear, curricularCoursesList };
		try
		{
			ServiceManagerServiceFactory.executeService(userView, "WriteEnrollmentsList", args);
		}
		catch (NotAuthorizedException e)
		{
			e.printStackTrace();

			errors.add("notauthorized", new ActionError("error.exception.notAuthorized"));
			saveErrors(request, errors);

			mapping.getInput();
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();

			errors.add("", new ActionError(e.getMessage()));
			saveErrors(request, errors);

			mapping.getInput();
		}

		return mapping.findForward("readCurricularCourseEnrollmentList");
	}
}
