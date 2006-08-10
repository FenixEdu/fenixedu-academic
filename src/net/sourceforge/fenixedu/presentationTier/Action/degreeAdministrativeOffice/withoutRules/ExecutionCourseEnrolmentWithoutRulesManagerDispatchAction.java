package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.withoutRules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.ExecutionDegreesFormat;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

public class ExecutionCourseEnrolmentWithoutRulesManagerDispatchAction extends FenixDispatchAction {

	private static final int MAX_CURRICULAR_YEARS = 5;

	private static final int MAX_CURRICULAR_SEMESTERS = 2;

	public ActionForward exit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("exit");
	}

	public ActionForward prepareEnrollmentChooseStudentAndExecutionYear(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<InfoExecutionPeriod> executionPeriods = null;
		try {
			final Object[] args = { DegreeType.valueOf(readAndSetDegreeType(request,
					(DynaActionForm) form)) };
			executionPeriods = (List) ServiceManagerServiceFactory.executeService(null,
					"ReadExecutionPeriodsEnrollmentFenix", args);

		} catch (FenixServiceException e) {
			addActionMessage(request, "error.impossible.operations");
			return mapping.findForward("globalEnrolment");
		}
		if (executionPeriods == null || executionPeriods.size() <= 0) {
			addActionMessage(request, "error.impossible.operations");
			return mapping.findForward("globalEnrolment");
		}

		sortExecutionPeriods(executionPeriods, (DynaActionForm) form);

		List executionYearLabels = buildLabelValueBeanForJsp(executionPeriods);
		request.setAttribute("executionPeriods", executionYearLabels);

		return mapping.findForward("prepareEnrollmentChooseStudentWithoutRules");
	}

	private String readAndSetDegreeType(HttpServletRequest request, DynaActionForm form) {
		String degreeType = request.getParameter("degreeType");
		if (degreeType == null) {
			degreeType = (String) request.getAttribute("degreeType");
			if (degreeType == null) {
				degreeType = form.getString("degreeType");
			}
		}
		request.setAttribute("degreeType", degreeType);
		return degreeType;
	}

	private void sortExecutionPeriods(List executionPeriods, DynaActionForm form) {
		ComparatorChain comparator = new ComparatorChain();
		comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
		comparator.addComparator(new BeanComparator("semester"), true);
		Collections.sort(executionPeriods, comparator);

		int size = executionPeriods.size();
		for (int i = (size - 1); i >= 0; i--) {
			InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) executionPeriods.get(i);
			if (infoExecutionPeriod.getState().equals(PeriodState.CURRENT)) {
				form.set("executionPeriod", infoExecutionPeriod.getIdInternal().toString());
				break;
			}
		}
	}

	private List buildLabelValueBeanForJsp(List infoExecutionPeriods) {
		List executionPeriodsLabels = new ArrayList();
		CollectionUtils.collect(infoExecutionPeriods, new Transformer() {
			public Object transform(Object arg0) {
				InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;

				LabelValueBean executionYear = new LabelValueBean(infoExecutionPeriod.getName() + " - "
						+ infoExecutionPeriod.getInfoExecutionYear().getYear(), infoExecutionPeriod
						.getIdInternal().toString());
				return executionYear;
			}
		}, executionPeriodsLabels);
		return executionPeriodsLabels;
	}

	public ActionForward readEnrollments(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		final DynaActionForm form = (DynaActionForm) actionForm;

		if (StringUtils.isEmpty(form.getString("studentNumber"))) {
			addActionMessage(request, "error.no.student");
			return mapping.getInputForward();
		}

		final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
		final ExecutionPeriod executionPeriod = getExecutionPeriod(form);

		StudentCurricularPlan studentCurricularPlan = null;
		try {
			final Object[] args = { getStudent(form), getDegreeType(form), executionPeriod };
			studentCurricularPlan = (StudentCurricularPlan) ServiceManagerServiceFactory.executeService(
					getUserView(request), "ReadStudentCurricularPlanForEnrollmentsWithoutRules", args);

		} catch (NotAuthorizedFilterException e) {
			e.printStackTrace();
			addActionMessage(request, "error.exception.notAuthorized2");
			return mapping.getInputForward();

		} catch (FenixServiceException e) {
			e.printStackTrace();

			if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
				addActionMessage(request, e.getMessage(), studentNumber.toString());
			} else if (e.getMessage() != null
					&& e.getMessage().endsWith("not.from.chosen.execution.year")) {
				addActionMessage(request, e.getMessage(), studentNumber.toString());
			} else {
				addActionMessage(request, "error.impossible.operations");
			}
			return mapping.getInputForward();
		}

		checkIfStudentHasPayedTuition(request, studentCurricularPlan);

		request.setAttribute("executionPeriod", executionPeriod);
		request.setAttribute("studentCurricularPlan", studentCurricularPlan);
		request.setAttribute("studentCurrentSemesterEnrollments", studentCurricularPlan
				.getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionPeriod));

		return mapping.findForward("curricularCourseEnrollmentList");
	}

	private ExecutionPeriod getExecutionPeriod(final DynaActionForm form) {
		return rootDomainObject.readExecutionPeriodByOID(Integer.valueOf(form.getString("executionPeriod")));
	}

	private DegreeType getDegreeType(final DynaActionForm form) {
		return DegreeType.valueOf(form.getString("degreeType"));
	}

	private Registration getStudent(final DynaActionForm form) {
		final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
		return Registration.readStudentByNumberAndDegreeType(studentNumber, getDegreeType(form));
	}

	private void checkIfStudentHasPayedTuition(HttpServletRequest request,
			StudentCurricularPlan studentCurricularPlan) {
		if (studentCurricularPlan.getStudent().getPayedTuition().equals(Boolean.FALSE)) {
			addActionMessage(request, "error.message.noTuitonPayed");
		}
	}

	public ActionForward unEnrollCourses(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		final DynaActionForm form = (DynaActionForm) actionForm;
		
		final Integer studentNumber = Integer.valueOf((String) form.get("studentNumber"));
		final List<Integer> unenrollmentsList = Arrays.asList((Integer[]) form.get("unenrollments"));

		try {
			final Object[] args = { getStudent(form), getDegreeType(form), unenrollmentsList };
			ServiceManagerServiceFactory.executeService(getUserView(request), "DeleteEnrollmentsList",
					args);

		} catch (NotAuthorizedFilterException e) {
			addActionMessage(request, "error.exception.notAuthorized2");
			return mapping.getInputForward();

		} catch (DomainException e) {
			addActionMessage(request, e.getMessage());
			return mapping.getInputForward();

		} catch (FenixServiceException e) {
			e.printStackTrace();
			addActionMessage(request, "error.impossible.operations.unenroll", studentNumber.toString());
			return mapping.getInputForward();
		}

		return mapping.findForward("readCurricularCourseEnrollmentList");
	}

	public ActionForward prepareEnrollmentCourses(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		final DynaActionForm form = (DynaActionForm) actionForm;
		final ExecutionPeriod executionPeriod = getExecutionPeriod(form);

		StudentCurricularPlan studentCurricularPlan = null;
		List<ExecutionDegree> result = null;
		try {
			
			final Registration student = getStudent(form);
			studentCurricularPlan = student.getActiveOrConcludedStudentCurricularPlan();
			
			result = (List<ExecutionDegree>) ServiceManagerServiceFactory.executeService(
					getUserView(request), "PrepareDegreesListByStudentNumber", new Object[] {
							student, getDegreeType(form), executionPeriod });

		} catch (NotAuthorizedFilterException e) {
			e.printStackTrace();
			addActionMessage(request, "error.exception.notAuthorized2");
			return mapping.getInputForward();

		} catch (FenixServiceException e) {
			addActionMessage(request, "error.impossible.operations");
			return mapping.findForward("readCurricularCourseEnrollmentList");
		}

		prepareEnrollmentCoursesInformation(request, form, studentCurricularPlan, result);

		// set attributes used by courseEnrollment context
		request.setAttribute("executionPeriod", executionPeriod);
		request.setAttribute("studentCurricularPlan", studentCurricularPlan);

		return mapping.findForward("choosesForEnrollment");
	}

	private void prepareEnrollmentCoursesInformation(final HttpServletRequest request,
			final DynaActionForm form, final StudentCurricularPlan studentCurricularPlan,
			final List<ExecutionDegree> result) {

		setDefaultExecutionDegree(form, studentCurricularPlan, result);

		sortExecutionDegrees(result);

		request.setAttribute(SessionConstants.DEGREE_LIST, ExecutionDegreesFormat
				.buildLabelValueBeansForExecutionDegree(result, getResources(request,
						"ENUMERATION_RESOURCES"), request));

		request.setAttribute(SessionConstants.ENROLMENT_YEAR_LIST_KEY, getListOfChosenCurricularYears());
		request.setAttribute(SessionConstants.ENROLMENT_SEMESTER_LIST_KEY,
				getListOfChosenCurricularSemesters());

	}

	private void setDefaultExecutionDegree(final DynaActionForm form,
			final StudentCurricularPlan studentCurricularPlan,
			final List<ExecutionDegree> executionDegrees) {

		if (StringUtils.isEmpty(form.getString("executionDegree"))) {
			List<ExecutionDegree> intersection = (List<ExecutionDegree>) CollectionUtils.intersection(
					executionDegrees, studentCurricularPlan.getDegreeCurricularPlan()
							.getExecutionDegreesSet());
			form.set("executionDegree", intersection.get(0).getIdInternal().toString());
		}
	}

	private void sortExecutionDegrees(List<ExecutionDegree> result) {
		Collections.sort(result, new Comparator<ExecutionDegree>() {
			public int compare(ExecutionDegree o1, ExecutionDegree o2) {
				final String name = "" + o1.getDegree().getTipoCurso().name() + o1.getDegree().getName();
				final String name2 = "" + o2.getDegree().getTipoCurso().name()
						+ o2.getDegree().getName();
				return name.compareToIgnoreCase(name2);
			}
		});
	}

	private List getListOfChosenCurricularYears() {
		final List<Integer> result = new ArrayList<Integer>();
		for (int i = 1; i <= MAX_CURRICULAR_YEARS; i++) {
			result.add(Integer.valueOf(i));
		}
		return result;
	}

	private List getListOfChosenCurricularSemesters() {
		final List<Integer> result = new ArrayList<Integer>();
		for (int i = 1; i <= MAX_CURRICULAR_SEMESTERS; i++) {
			result.add(Integer.valueOf(i));
		}
		return result;
	}

	public ActionForward readCoursesToEnroll(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		final DynaActionForm form = (DynaActionForm) actionForm;

		final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
		final ExecutionPeriod executionPeriod = getExecutionPeriod(form);
		
		final Integer executionDegreeID = Integer.valueOf(form.getString("executionDegree"));
		final List<Integer> curricularYearsList = Arrays.asList((Integer[]) form.get("curricularYears"));
		final List<Integer> curricularSemesters = Arrays.asList((Integer[]) form.get("curricularSemesters"));

		StudentCurricularPlan studentCurricularPlan = null;
		List<CurricularCourse2Enroll> curricularCourses2Enroll = null;
		try {

			final Integer userType = (Integer) form.get("userType");
			final Registration student = getStudent(form);
			studentCurricularPlan = student.getActiveOrConcludedStudentCurricularPlan();

			if (userType.equals(0)) {
				curricularCourses2Enroll = (List<CurricularCourse2Enroll>) ServiceManagerServiceFactory
						.executeService(getUserView(request), "ReadCurricularCoursesToEnroll",
								new Object[] { student, getDegreeType(form), executionPeriod,
										executionDegreeID, curricularYearsList, curricularSemesters });

			} else {
				curricularCourses2Enroll = (List<CurricularCourse2Enroll>) ServiceManagerServiceFactory
						.executeService(getUserView(request), "ReadCurricularCoursesToEnrollSuperUser",
								new Object[] { student, getDegreeType(form), executionPeriod,
										executionDegreeID, curricularYearsList, curricularSemesters });
			}

		} catch (NotAuthorizedFilterException e) {
			e.printStackTrace();
			addActionMessage(request, "error.exception.notAuthorized2");
			return mapping.getInputForward();

		} catch (FenixServiceException e) {
			e.printStackTrace();
			if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
				addActionMessage(request, e.getMessage(), studentNumber.toString());
			} else if (e.getMessage() != null && !e.getMessage().endsWith("noCurricularPlans")) {
				addActionMessage(request, e.getMessage());
			} else {
				addActionMessage(request, "error.impossible.operations");
			}
			return mapping.getInputForward();
		}

		checkIfStudentHasPayedTuition(request, studentCurricularPlan);

		// set attributes used by courseEnrollment context
		request.setAttribute("executionPeriod", executionPeriod);
		request.setAttribute("studentCurricularPlan", studentCurricularPlan);

		sortCurricularCourses2Enrol(curricularCourses2Enroll);
		request.setAttribute("curricularCourses2Enroll", curricularCourses2Enroll);
		form.set("enrollmentTypes", getInitializedMap(curricularCourses2Enroll));

		return mapping.findForward("showCurricularCourseToEnroll");
	}

	private void sortCurricularCourses2Enrol(List<CurricularCourse2Enroll> curricularCourse2Enroll) {
		Collections.sort(curricularCourse2Enroll, new Comparator<CurricularCourse2Enroll>() {
			public int compare(CurricularCourse2Enroll o1, CurricularCourse2Enroll o2) {
				return o1.getCurricularCourse().getName().compareTo(o2.getCurricularCourse().getName());
			}
		});
	}

	private Object getInitializedMap(List<CurricularCourse2Enroll> curricularCourses2Enroll) {
		final Map result = new HashMap();
		for (final CurricularCourse2Enroll curricularCourse2Enroll : curricularCourses2Enroll) {
			result.put(curricularCourse2Enroll.getCurricularCourse().getIdInternal(), Integer.valueOf(0));
		}
		return result;
	}

	public ActionForward enrollCourses(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		final DynaActionForm form = (DynaActionForm) actionForm;

		final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
		final List<String> curricularCourses = Arrays.asList((String[]) form.get("curricularCourses"));
		final Map optionalEnrollments = (HashMap) form.get("enrollmentTypes");
		
		try {
			ServiceManagerServiceFactory.executeService(getUserView(request), "WriteEnrollmentsList",
					new Object[] { getStudent(form), getDegreeType(form), getExecutionPeriod(form), curricularCourses,
							optionalEnrollments, getUserView(request) });

		} catch (NotAuthorizedFilterException e) {
			e.printStackTrace();
			addActionMessage(request, "error.exception.notAuthorized2");
			return mapping.getInputForward();

		} catch (FenixServiceException e) {
			e.printStackTrace();
			if (e.getMessage() != null && e.getMessage().endsWith("noCurricularPlans")) {
				addActionMessage(request, e.getMessage(), studentNumber.toString());
			} else if (e.getMessage() != null && !e.getMessage().endsWith("noCurricularPlans")) {
				addActionMessage(request, e.getMessage());
			} else {
				addActionMessage(request, "error.impossible.operations");
			}
			return mapping.getInputForward();
		}

		return mapping.findForward("readCurricularCourseEnrollmentList");
	}
}