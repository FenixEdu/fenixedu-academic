package ServidorApresentacao.Action.degreeAdministrativeOffice.withoutRules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoObject;
import DataBeans.InfoStudent;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.ExecutionDegreesFormat;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author David Santos in Mar 5, 2004
 */

public class OptionalCoursesEnrollmentManagerDispatchAction extends DispatchAction
{
	private static final int MAX_CURRICULAR_YEARS = 5;
	private static final int MAX_CURRICULAR_SEMESTERS = 2;

	public ActionForward chooseStudentAndExecutionYear(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ActionErrors errors = new ActionErrors();

		String degreeType = request.getParameter("degreeType");

		if (degreeType == null)
		{
			degreeType = (String) request.getAttribute("degreeType");
			if (degreeType == null)
			{
				DynaActionForm dForm = (DynaActionForm) form;
				degreeType = (String) dForm.get("degreeType");
			}
		}

		request.setAttribute("degreeType", degreeType);

		List executionYears = null;

		try
		{
			executionYears = (List) ServiceManagerServiceFactory.executeService(null, "ReadNotClosedExecutionYears", null);
		} catch (FenixServiceException e)
		{
			errors.add("noExecutionYears", new ActionError("error.impossible.operations"));
			saveErrors(request, errors);
			return mapping.findForward("exit");
		}

		if (executionYears == null || executionYears.size() <= 0)
		{
			errors.add("noExecutionYears", new ActionError("error.impossible.operations"));
			saveErrors(request, errors);
			return mapping.findForward("exit");
		}

		ComparatorChain comparator = new ComparatorChain();
		comparator.addComparator(new BeanComparator("year"), true);
		Collections.sort(executionYears, comparator);

		List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
		request.setAttribute("executionYears", executionYearLabels);

		return mapping.findForward("chooseStudentAndExecutionYear");
	}

	public ActionForward showAvailableOptionalCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
		return showAvailableCourses(mapping, form, request, response);
	}
	
	public ActionForward showAvailableExecutionDegrees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm optionalCoursesEnrolmentForm = (DynaActionForm) form;

		Integer studentNumber = Integer.valueOf((String) optionalCoursesEnrolmentForm.get("studentNumber"));
		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(studentNumber);

		String executionYear = (String) optionalCoursesEnrolmentForm.get("executionYear");
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear(executionYear);

		String degreeTypeCode = (String) optionalCoursesEnrolmentForm.get("degreeType");
		TipoCurso degreeType = new TipoCurso();
		degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));

		Integer executionDegreeID = (Integer) optionalCoursesEnrolmentForm.get("executionDegreeID");

		Object args[] = {infoStudent, degreeType, executionDegreeID, infoExecutionYear};
		List executionDegreesList = null;
		InfoExecutionDegree selectedExecutionDegree = null;
		try
		{
			executionDegreesList = (List) ServiceManagerServiceFactory.executeService(userView, "PrepareDegreesListByStudentNumber",
				args);
			if (executionDegreesList == null || executionDegreesList.size() < 0)
			{
				throw new FenixServiceException();
			}
		} catch (FenixServiceException e)
		{
			return setAcurateErrorMessage(mapping, request, e, errors, null);
		}

		selectedExecutionDegree = (InfoExecutionDegree) executionDegreesList.get(0);
		executionDegreesList = executionDegreesList.subList(1, executionDegreesList.size() - 1);

		Collections.sort(executionDegreesList, new ComparatorByNameForInfoExecutionDegree());
		List executionDegreeLabels = ExecutionDegreesFormat.buildExecutionDegreeLabelValueBean(executionDegreesList);
		request.setAttribute(SessionConstants.DEGREE_LIST, executionDegreeLabels);

		List listOfChosenCurricularYears = getListOfChosenCurricularYears();
		List listOfChosenCurricularSemesters = getListOfChosenCurricularSemesters();
		request.setAttribute(SessionConstants.ENROLMENT_YEAR_LIST_KEY, listOfChosenCurricularYears);
		request.setAttribute(SessionConstants.ENROLMENT_SEMESTER_LIST_KEY, listOfChosenCurricularSemesters);

		optionalCoursesEnrolmentForm.set("executionDegreeID", selectedExecutionDegree.getIdInternal());
		keepFromElementsValues(optionalCoursesEnrolmentForm, request);

		return mapping.findForward("showAvailableExecutionDegrees");
	}
	
	public ActionForward showChosenDegreeCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm optionalCoursesEnrolmentForm = (DynaActionForm) form;

		Integer studentNumber = Integer.valueOf((String) optionalCoursesEnrolmentForm.get("studentNumber"));
		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(studentNumber);

		String executionYear = (String) optionalCoursesEnrolmentForm.get("executionYear");
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setYear(executionYear);

		String degreeTypeCode = (String) optionalCoursesEnrolmentForm.get("degreeType");
		TipoCurso degreeType = new TipoCurso();
		degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));

		Integer executionDegreeID = (Integer) optionalCoursesEnrolmentForm.get("executionDegreeID");

		Integer[] curricularYears = (Integer[]) optionalCoursesEnrolmentForm.get("curricularYears");
		List curricularYearsList = Arrays.asList(curricularYears);

		Integer[] curricularSemesters = (Integer[]) optionalCoursesEnrolmentForm.get("curricularSemesters");
		List curricularSemestersList = Arrays.asList(curricularSemesters);

		Object args[] = {infoStudent, degreeType, infoExecutionYear, executionDegreeID, curricularYearsList, curricularSemestersList};
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = null;
		try
		{
			infoStudentEnrolmentContext = (InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(userView,
				"ReadCurricularCoursesToEnroll", args);

			InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
			infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
			infoStudentEnrolmentContext.setInfoExecutionPeriod(infoExecutionPeriod);
		} catch (NotAuthorizedException e)
		{
			return setAcurateErrorMessage(mapping, request, e, errors, null);
		} catch (FenixServiceException e)
		{
			List errorMessageAttributes = new ArrayList();
			errorMessageAttributes.set(0, studentNumber);
			return setAcurateErrorMessage(mapping, request, e, errors, errorMessageAttributes);
		}

		optionalCoursesEnrolmentForm.set("curricularCourseID", ((InfoCurricularCourse) infoStudentEnrolmentContext
			.getFinalInfoCurricularCoursesWhereStudentCanBeEnrolled().get(0)).getIdInternal());
		keepFromElementsValues(optionalCoursesEnrolmentForm, request);

		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);

		return mapping.findForward("showChosenDegreeCourses");
	}
	
	public ActionForward unenrollFromCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
		ActionErrors errors = new ActionErrors();
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm optionalCoursesEnrolmentForm = (DynaValidatorForm) form;

		Integer[] enrolledCurricularCoursesBefore = (Integer[]) optionalCoursesEnrolmentForm.get("enrolledCurricularCoursesBefore");
		Integer[] enrolledCurricularCoursesAfter = (Integer[]) optionalCoursesEnrolmentForm.get("enrolledCurricularCoursesAfter");
		Integer executionDegreeID = (Integer) optionalCoursesEnrolmentForm.get("executionDegreeID");
		Integer studentCurricularPlanID = (Integer) optionalCoursesEnrolmentForm.get("studentCurricularPlanID");

		List enrollmentsBefore = Arrays.asList(enrolledCurricularCoursesBefore);
		List enrollmentsAfter = Arrays.asList(enrolledCurricularCoursesAfter);
		List toUnenroll = (List) CollectionUtils.subtract(enrollmentsBefore, enrollmentsAfter);

		if (toUnenroll.size() == 1)
		{
			Object[] args = {executionDegreeID, studentCurricularPlanID, (Integer) toUnenroll.get(0)};
			try
			{
				ServiceManagerServiceFactory.executeService(userView, "DeleteEnrolment", args);
			} catch (NotAuthorizedException e)
			{
				return setAcurateErrorMessage(mapping, request, e, errors, null);
			} catch (FenixServiceException e)
			{
				return setAcurateErrorMessage(mapping, request, e, errors, null);
			}
		}

		return showAvailableCourses(mapping, form, request, response);
	}
	
	public ActionForward enrollInCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
		ActionErrors errors = new ActionErrors();
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm optionalCoursesEnrolmentForm = (DynaValidatorForm) form;

		Integer[] curricularCoursesToEnroll = (Integer[]) optionalCoursesEnrolmentForm.get("unenrolledCurricularCourses");
		Integer executionDegreeID = (Integer) optionalCoursesEnrolmentForm.get("executionDegreeID");
		Integer studentCurricularPlanID = (Integer) optionalCoursesEnrolmentForm.get("studentCurricularPlanID");
		Integer chosenCurricularCourseID = (Integer) optionalCoursesEnrolmentForm.get("curricularCourseID");
		String executionYear = (String) optionalCoursesEnrolmentForm.get("executionYear");
		String degreeTypeCode = (String) optionalCoursesEnrolmentForm.get("degreeType");
		TipoCurso degreeType = new TipoCurso();
		degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));

		List toEnroll = Arrays.asList(curricularCoursesToEnroll);

		if (toEnroll.size() == 1)
		{
			Object[] args = {executionDegreeID, studentCurricularPlanID, toEnroll.get(0), chosenCurricularCourseID,
				executionYear, degreeType};
			try
			{
				ServiceManagerServiceFactory.executeService(userView, "WriteEnrolmentInOptionalCourse", args);
			} catch (NotAuthorizedException e)
			{
				return setAcurateErrorMessage(mapping, request, e, errors, null);
			} catch (FenixServiceException e)
			{
				return setAcurateErrorMessage(mapping, request, e, errors, null);
			}
		}
		return showAvailableCourses(mapping, form, request, response);
	}

	public ActionForward enrollmentConfirmation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
		ActionErrors errors = new ActionErrors();
		IUserView userView = SessionUtils.getUserView(request);
		DynaValidatorForm optionalCoursesEnrolmentForm = (DynaValidatorForm) form;

		Integer executionDegreeID = (Integer) optionalCoursesEnrolmentForm.get("executionDegreeID");
		Integer studentCurricularPlanID = (Integer) optionalCoursesEnrolmentForm.get("studentCurricularPlanID");

		showAvailableCourses(mapping, form, request, response);

		List curriculum = null;
		Object args[] = {executionDegreeID, studentCurricularPlanID};
		try
		{
			curriculum = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
				"ReadStudentCurriculumForEnrollmentConfirmation", args);
		} catch (NotAuthorizedException e)
		{
			return setAcurateErrorMessage(mapping, request, e, errors, null);
		}

		InfoStudentEnrolmentContext infoStudentEnrolmentContext = (InfoStudentEnrolmentContext) request
			.getAttribute("infoStudentEnrolmentContext");
		
		Collections.sort(infoStudentEnrolmentContext.getStudentInfoEnrollmentsWithStateEnrolled(), new BeanComparator(
			"infoCurricularCourse.name"));

		sortCurriculum(curriculum);

		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);
		request.setAttribute("curriculum", curriculum);

		return mapping.findForward("enrollmentConfirmation");
	}

	public ActionForward exit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		return mapping.findForward("exit");
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws Exception
	 */
	private ActionForward showAvailableCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws Exception
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		DynaActionForm optionalCoursesEnrolmentForm = (DynaActionForm) form;

		Integer studentNumber = Integer.valueOf((String) optionalCoursesEnrolmentForm.get("studentNumber"));
		InfoStudent infoStudent = new InfoStudent();
		infoStudent.setNumber(studentNumber);

		String executionYear = (String) optionalCoursesEnrolmentForm.get("executionYear");

		String degreeTypeCode = (String) optionalCoursesEnrolmentForm.get("degreeType");
		TipoCurso degreeType = new TipoCurso();
		degreeType.setTipoCurso(Integer.valueOf(degreeTypeCode));

		Object[] args = {infoStudent, degreeType, executionYear};
		InfoStudentEnrolmentContext infoStudentEnrolmentContext = null;

		try
		{
			infoStudentEnrolmentContext = (InfoStudentEnrolmentContext) ServiceManagerServiceFactory.executeService(userView,
				"PrepareStudentEnrolmentContextForOptionalCoursesEnrolment", args);
		} catch (NotAuthorizedException e)
		{
			return setAcurateErrorMessage(mapping, request, e, errors, null);
		} catch (FenixServiceException e)
		{
			List errorMessageAttributes = new ArrayList();
			errorMessageAttributes.set(0, studentNumber);
			return setAcurateErrorMessage(mapping, request, e, errors, errorMessageAttributes);
		}

		Collections.sort(infoStudentEnrolmentContext.getStudentInfoEnrollmentsWithStateEnrolled(), new BeanComparator(
			"infoCurricularCourse.name"));
		Collections.sort(infoStudentEnrolmentContext.getFinalInfoCurricularCoursesWhereStudentCanBeEnrolled(), new BeanComparator(
			"name"));

		Integer[] enrolledIn = buildArrayForForm(infoStudentEnrolmentContext.getStudentInfoEnrollmentsWithStateEnrolled());

		setFromElementsValues(optionalCoursesEnrolmentForm, enrolledIn, enrolledIn, studentNumber.toString(), executionYear,
			degreeTypeCode, infoStudentEnrolmentContext.getInfoStudentCurricularPlan().getIdInternal(), null, null, null, null, null);

		request.setAttribute("infoStudentEnrolmentContext", infoStudentEnrolmentContext);

		return mapping.findForward("showAvailableOptionalCourses");
	}

	/**
	 * @param listToTransform
	 * @return Integer[]
	 */
	private Integer[] buildArrayForForm(List listToTransform)
	{
		List newList = new ArrayList();
		newList = (List) CollectionUtils.collect(listToTransform, new Transformer()
		{
			public Object transform(Object arg0)
			{
				InfoObject infoObject = (InfoObject) arg0;
				return infoObject.getIdInternal();
			}
		});

		Integer[] array = new Integer[newList.size()];
		for (int i = 0; i < array.length; i++)
		{
			array[i] = (Integer) newList.get(i);
		}
		
		return array;
	}

	/**
	 * @param mapping
	 * @param request
	 * @param e
	 * @param errors
	 * @param errorMessageAttributes
	 * @return ActionForward
	 */
	private ActionForward setAcurateErrorMessage(ActionMapping mapping, HttpServletRequest request, Exception e,
		ActionErrors errors, List errorMessageAttributes)
	{
		e.printStackTrace();

		if (e instanceof NotAuthorizedException)
		{
			errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
		} else if (e instanceof FenixServiceException)
		{
			if (errorMessageAttributes != null)
			{
				String studentNumber = (String) errorMessageAttributes.get(0);

				if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans"))
				{
					errors.add("noStudentCurricularPlan", new ActionError(e.getMessage(), studentNumber));
				} else if (e.getMessage() != null && e.getMessage().endsWith("not.from.chosen.execution.year"))
				{
					errors.add("notFromChosenExecutionYear", new ActionError(e.getMessage(), studentNumber));
				} else if (e.getMessage() != null && e.getMessage().endsWith("error.degree.noData"))
				{
					errors.add("notFromChosenExecutionYear", new ActionError(e.getMessage()));
				} else
				{
					errors.add("noResult", new ActionError("error.impossible.operations"));
				}
			} else
			{
				errors.add("noResult", new ActionError("error.impossible.operations"));
			}
		}

		saveErrors(request, errors);
		return mapping.getInputForward();
	}

	/**
	 * @param infoExecutionYears
	 * @return List
	 */
	private List buildLabelValueBeanForJsp(List infoExecutionYears)
	{
		List executionYearLabels = new ArrayList();
		CollectionUtils.collect(infoExecutionYears, new Transformer()
		{
			public Object transform(Object arg0)
			{
				InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

				LabelValueBean executionYear = new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear());
				return executionYear;
			}
		}, executionYearLabels);
		return executionYearLabels;
	}

	/**
	 * @return List
	 */
	private List getListOfChosenCurricularYears()
	{
		List result = new ArrayList();

		for (int i = 1; i <= MAX_CURRICULAR_YEARS; i++)
		{
			result.add(new Integer(i));
		}
		return result;
	}

	/**
	 * @return List
	 */
	private List getListOfChosenCurricularSemesters()
	{
		List result = new ArrayList();

		for (int i = 1; i <= MAX_CURRICULAR_SEMESTERS; i++)
		{
			result.add(new Integer(i));
		}
		return result;
	}

	/**
	 * @param form
	 */
	private void keepFromElementsValues(DynaActionForm form, HttpServletRequest request)
	{
		form.set("studentNumber", form.get("studentNumber"));
		form.set("executionYear", form.get("executionYear"));
		form.set("degreeType", form.get("degreeType"));
		form.set("studentCurricularPlanID", form.get("studentCurricularPlanID"));
		form.set("executionDegreeID", form.get("executionDegreeID"));
		form.set("curricularCourseID", form.get("curricularCourseID"));

		Integer[] enrolledCurricularCoursesBefore = (Integer[]) form.get("enrolledCurricularCoursesBefore");
		Integer[] enrolledCurricularCoursesAfter = (Integer[]) form.get("enrolledCurricularCoursesAfter");
		Integer[] unenrolledCurricularCourses = (Integer[]) form.get("unenrolledCurricularCourses");
		Integer[] curricularYears = (Integer[]) form.get("curricularYears");
		Integer[] curricularSemesters = (Integer[]) form.get("curricularSemesters");

		List enrolledCurricularCoursesBeforeList = null;
		List enrolledCurricularCoursesAfterList = null;
		List unenrolledCurricularCoursesList = null;
		List curricularYearsList = null;
		List curricularSemestersList = null;

		if (enrolledCurricularCoursesBefore != null)
		{
			enrolledCurricularCoursesBeforeList = Arrays.asList(enrolledCurricularCoursesBefore);
		} else
		{
			enrolledCurricularCoursesBeforeList = new ArrayList();
		}

		if (enrolledCurricularCoursesAfter != null)
		{
			enrolledCurricularCoursesAfterList = Arrays.asList(enrolledCurricularCoursesAfter);
		} else
		{
			enrolledCurricularCoursesAfterList = new ArrayList();
		}

		if (unenrolledCurricularCourses != null)
		{
			unenrolledCurricularCoursesList = Arrays.asList(unenrolledCurricularCourses);
		} else
		{
			unenrolledCurricularCoursesList = new ArrayList();
		}

		if (curricularYears != null)
		{
			curricularYearsList = Arrays.asList(curricularYears);
		} else
		{
			curricularYearsList = new ArrayList();
		}

		if (curricularSemesters != null)
		{
			curricularSemestersList = Arrays.asList(curricularSemesters);
		} else
		{
			curricularSemestersList = new ArrayList();
		}

		request.setAttribute("enrolledCurricularCoursesBeforeList", enrolledCurricularCoursesBeforeList);
		request.setAttribute("enrolledCurricularCoursesAfterList", enrolledCurricularCoursesAfterList);
		request.setAttribute("unenrolledCurricularCoursesList", unenrolledCurricularCoursesList);
		request.setAttribute("curricularYearsList", curricularYearsList);
		request.setAttribute("curricularSemestersList", curricularSemestersList);
	}

	/**
	 * @param form
	 * @param enrolledCurricularCoursesBefore
	 * @param enrolledCurricularCoursesAfter
	 * @param studentNumber
	 * @param executionYear
	 * @param degreeType
	 * @param studentCurricularPlanID
	 * @param executionPeriodID
	 * @param executionDegreeID
	 * @param unenrolledCurricularCourses
	 * @param curricularYears
	 * @param curricularSemesters
	 */
	private void setFromElementsValues(DynaActionForm form, Integer[] enrolledCurricularCoursesBefore,
		Integer[] enrolledCurricularCoursesAfter, String studentNumber, String executionYear, String degreeType,
		Integer studentCurricularPlanID, Integer executionDegreeID, Integer curricularCourseID,
		Integer[] unenrolledCurricularCourses, Integer[] curricularYears, Integer[] curricularSemesters)
	{
		form.set("enrolledCurricularCoursesBefore", enrolledCurricularCoursesBefore);
		form.set("enrolledCurricularCoursesAfter", enrolledCurricularCoursesAfter);
		form.set("studentNumber", studentNumber);
		form.set("executionYear", executionYear);
		form.set("degreeType", degreeType);
		form.set("studentCurricularPlanID", studentCurricularPlanID);
		form.set("executionDegreeID", executionDegreeID);
		form.set("curricularCourseID", curricularCourseID);
		form.set("unenrolledCurricularCourses", unenrolledCurricularCourses);
		form.set("curricularYears", curricularYears);
		form.set("curricularSemesters", curricularSemesters);
	}

	/**
	 * @param curriculum
	 */
	private void sortCurriculum(List curriculum)
	{
		BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
		BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
		ComparatorChain chainComparator = new ComparatorChain();
		chainComparator.addComparator(courseName);
		chainComparator.addComparator(executionYear);

		Collections.sort(curriculum, chainComparator);
	}
}