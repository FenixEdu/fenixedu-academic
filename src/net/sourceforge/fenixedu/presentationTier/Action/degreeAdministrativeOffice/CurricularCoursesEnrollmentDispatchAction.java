package net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedBranchChangeException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.Data;
import net.sourceforge.fenixedu.util.SecretaryEnrolmentStudentReason;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

public class CurricularCoursesEnrollmentDispatchAction extends TransactionalDispatchAction {

	private Integer readAndSetExecutionDegree(HttpServletRequest request) {
		Integer executionDegreeId = null;

		String executionDegreeIdString = request.getParameter("executionDegreeId");
		if (executionDegreeIdString == null) {
			executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
		}
		if (executionDegreeIdString != null) {
			executionDegreeId = Integer.valueOf(executionDegreeIdString);
		}
		request.setAttribute("executionDegreeId", executionDegreeId);

		return executionDegreeId;
	}

	public ActionForward prepareEnrollmentChooseStudent(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) {

		readAndSetExecutionDegree(request);
		final DynaValidatorForm form = (DynaValidatorForm) actionForm;

		if (!StringUtils.isEmpty(request.getParameter("degreeCurricularPlanID"))) {
			request.setAttribute("degreeCurricularPlanID", Integer.valueOf(request
					.getParameter("degreeCurricularPlanID")));
			form.set("degreeCurricularPlanID", Integer.valueOf(request
					.getParameter("degreeCurricularPlanID")));
		}
		return mapping.findForward("prepareEnrollmentChooseStudent");
	}

	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		super.createToken(request);
		return prepareEnrollmentChooseCurricularCourses(mapping, form, request, response);
	}

	private ActionForward prepareEnrollmentChooseCurricularCourses(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		final DynaValidatorForm form = (DynaValidatorForm) actionForm;

		readAndSetDegreeCurricularPlanID(request, form);
		StudentCurricularPlan studentCurricularPlan = null;

		try {
			final IUserView userView = getUserView(request);
			final Object[] args = { readAndSetExecutionDegree(request), getStudent(userView, form) };

			if (checkIfUserHasAdministrativeRoles(userView)) {

				studentCurricularPlan = (StudentCurricularPlan) ServiceManagerServiceFactory
						.executeService(userView, "ReadStudentCurricularPlanForEnrollments", args);

			} else {

				studentCurricularPlan = (StudentCurricularPlan) ServiceManagerServiceFactory
						.executeService(userView, "ReadStudentCurricularPlanWithRulesForEnrollments",
								args);
			}

			prepareEnrollmentChooseCurricularCoursesInformation(request, studentCurricularPlan);

		} catch (NotAuthorizedFilterException e) {
			if (e.getMessage() != null) {
				addAuthorizationErrors(request, e);
			} else {
				addActionMessage(request, "error.exception.notAuthorized.student.warningTuition");
			}

		} catch (ExistingServiceException e) {
			if (e.getMessage().equals("student")) {
				addActionMessage(request, "error.no.student.in.database");
			} else if (e.getMessage().equals("studentCurricularPlan")) {
				addActionMessage(request, "error.student.curricularPlan.nonExistent");
			}

		} catch (OutOfCurricularCourseEnrolmentPeriod e) {
			final String startDate = (e.getStartDate() != null) ? Data.format2DayMonthYear(e
					.getStartDate()) : "";
			final String endDate = (e.getEndDate() != null) ? Data.format2DayMonthYear(e.getEndDate())
					: "";
			addActionMessage(request, e.getMessageKey(), startDate, endDate);

		} catch (FenixDomainException e) {
			addActionMessage(request, SecretaryEnrolmentStudentReason.getEnum(e.getErrorType())
					.getName());

		} catch (FenixServiceException e) {
			if (e.getMessage().equals("degree")) {
				addActionMessage(request, "error.student.degreeCurricularPlan.LEEC");
			} else if (e.getMessage().equals("enrolmentPeriod")) {
				addActionMessage(request, "error.student.enrolmentPeriod");
			} else if (getActionMessages(request).isEmpty()) {
				throw new FenixActionException(e);
			}
		}
		if (!getActionMessages(request).isEmpty()) {
			return mapping.getInputForward();
		}

		if (checkIfStudentBelongsToLERCI0304(studentCurricularPlan)) {
			request.setAttribute("warnings", Collections.singletonList("warning.lerci"));
		}

		return mapping.findForward("prepareEnrollmentChooseCurricularCourses");
	}

	private boolean checkIfStudentBelongsToLERCI0304(StudentCurricularPlan studentCurricularPlan) {
		return studentCurricularPlan.getDegreeCurricularPlan().getIdInternal().equals(90);
	}

	private void prepareEnrollmentChooseCurricularCoursesInformation(HttpServletRequest request,
			final StudentCurricularPlan studentCurricularPlan) throws FenixDomainException {

		final ExecutionPeriod actualExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();

		request.setAttribute("studentNumber", studentCurricularPlan.getStudent().getNumber());
		request.setAttribute("studentCurricularPlan", studentCurricularPlan);
		request.setAttribute("executionPeriod", actualExecutionPeriod);

		final List<Enrolment> allStudentEnrolledEnrollmentsInExecutionPeriod = studentCurricularPlan
				.getAllStudentEnrolledEnrollmentsInExecutionPeriod(actualExecutionPeriod);
		Collections.sort(allStudentEnrolledEnrollmentsInExecutionPeriod, new BeanComparator(
				"curricularCourse.name"));
		request.setAttribute("studentCurrentSemesterEnrollments",
				allStudentEnrolledEnrollmentsInExecutionPeriod);

		final List<CurricularCourse2Enroll> curricularCoursesToEnroll = studentCurricularPlan
				.getCurricularCoursesToEnroll(actualExecutionPeriod);
		Collections.sort(curricularCoursesToEnroll, new BeanComparator("curricularYear.year"));
		request.setAttribute("curricularCourses2Enroll", curricularCoursesToEnroll);

	}

	private Integer readAndSetDegreeCurricularPlanID(final HttpServletRequest request,
			final DynaValidatorForm form) {
		Integer degreeCurricularPlanID = (Integer) form.get("degreeCurricularPlanID");
		if (degreeCurricularPlanID == null && request.getParameter("degreeCurricularPlanID") != null) {
			degreeCurricularPlanID = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
		}
		request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
		return degreeCurricularPlanID;
	}

	private boolean checkIfUserHasAdministrativeRoles(final IUserView userView) {
		return userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
				|| userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
	}

	private boolean checkIfUserHasTeacherRole(final IUserView userView) {
		return userView.hasRoleType(RoleType.TEACHER);
	}

	private void addAuthorizationErrors(HttpServletRequest request, NotAuthorizedFilterException e) {
		String messageException = e.getMessage();
		String message = null;
		String arg1 = null;
		String arg2 = null;
		if (messageException.indexOf("+") != -1) {
			message = messageException.substring(0, messageException.indexOf("+"));
			String newMessage = messageException.substring(messageException.indexOf("+") + 1);
			if (newMessage.indexOf("+") != -1) {
				arg1 = newMessage.substring(0, newMessage.indexOf("+"));
				arg2 = newMessage.substring(newMessage.indexOf("+") + 1);
			} else {
				arg1 = newMessage;
			}
		} else {
			message = messageException;
		}
		addActionMessage(request, message, arg1, arg2);
	}

	public ActionForward prepareEnrollmentPrepareChooseAreas(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		super.createToken(request);

		final DynaValidatorForm form = (DynaValidatorForm) actionForm;

		readAndSetDegreeCurricularPlanID(request, form);
		final Integer studentNumber = Integer.valueOf(request.getParameter("studentNumber"));
		form.set("studentNumber", studentNumber.toString());

		request.setAttribute("executionPeriod", request.getParameter("executionPeriod"));
		request.setAttribute("executionYear", request.getParameter("executionYear"));

		try {
			final IUserView userView = getUserView(request);
			final Object[] args = { readAndSetExecutionDegree(request), getStudent(userView, form) };
			final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) ServiceManagerServiceFactory
					.executeService(userView, "ReadStudentCurricularPlanForEnrollments", args);

			prepareEnrollmentPrepareChooseAreasInformation(request, form, studentCurricularPlan);

		} catch (NotAuthorizedFilterException e) {
			if (e.getMessage() != null) {
				addAuthorizationErrors(request, e);
			} else {
				addActionMessage(request, "error.exception.notAuthorized");
			}
		} catch (ExistingServiceException e) {
			if (e.getMessage().equals("student")) {
				addActionMessage(request, "error.no.student.in.database", studentNumber.toString());

			} else if (e.getMessage().equals("studentCurricularPlan")) {
				addActionMessage(request, "error.student.curricularPlan.nonExistent");
			}
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		if (!getActionMessages(request).isEmpty()) {
			return mapping.findForward("beginTransaction");
		}

		return mapping.findForward("prepareEnrollmentChooseAreas");
	}

	private void prepareEnrollmentPrepareChooseAreasInformation(final HttpServletRequest request,
			final DynaValidatorForm form, final StudentCurricularPlan studentCurricularPlan) {

		request.setAttribute("studentCurricularPlan", studentCurricularPlan);

		final List<Branch> secondaryAreas = studentCurricularPlan.getDegreeCurricularPlan()
				.getSecundaryAreas();
		Collections.sort(secondaryAreas, new BeanComparator("name"));
		request.setAttribute("secondaryAreas", secondaryAreas);

		final List<Branch> specializationAreas = studentCurricularPlan.getDegreeCurricularPlan()
				.getSpecializationAreas();
		Collections.sort(specializationAreas, new BeanComparator("name"));
		request.setAttribute("specializationAreas", specializationAreas);

		if (studentCurricularPlan.hasBranch()) {
			form.set("specializationArea", studentCurricularPlan.getBranch().getIdInternal());
		}
		if (studentCurricularPlan.hasSecundaryBranch()) {
			form.set("secondaryArea", studentCurricularPlan.getSecundaryBranch().getIdInternal());
		}
	}

	public ActionForward prepareEnrollmentChooseAreas(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, actionForm, mapping, "error.transaction.enrollment");

		final DynaValidatorForm form = (DynaValidatorForm) actionForm;

		final Integer specializationArea = (Integer) form.get("specializationArea");
		final Integer secondaryArea = (Integer) form.get("secondaryArea");

		readAndSetDegreeCurricularPlanID(request, form);

		try {
			final IUserView userView = getUserView(request);
			final Object[] args = { readAndSetExecutionDegree(request), getStudent(userView, form),
					specializationArea, secondaryArea };
			ServiceManagerServiceFactory.executeService(userView, "WriteStudentAreas", args);

		} catch (NotAuthorizedFilterException e) {
			if (e.getMessage() != null) {
				addAuthorizationErrors(request, e);
			} else {
				addActionMessage(request, "error.exception.notAuthorized");
			}

		} catch (BothAreasAreTheSameServiceException e) {
			addActionMessage(request, "error.student.enrollment.AreasNotEqual");

		} catch (DomainException e) {
			addActionMessage(request, "error.student.enrollment.incompatibleAreas");

		} catch (ExistingServiceException e) {
			addActionMessage(request, "error.student.curricularPlan.nonExistent");

		} catch (InvalidArgumentsServiceException e) {
			addActionMessage(request, "error.areas.choose");

		} catch (NotAuthorizedBranchChangeException e) {
			addActionMessage(request, "error.areas.notAuthorized");

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (!getActionMessages(request).isEmpty()) {
			request.setAttribute("executionPeriod", request.getParameter("executionPeriod"));
			request.setAttribute("executionYear", request.getParameter("executionYear"));
			return mapping.findForward("prepareChooseAreas");
		}

		return prepareEnrollmentChooseCurricularCourses(mapping, actionForm, request, response);
	}

	public ActionForward enrollInCurricularCourse(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, actionForm, mapping, "error.transaction.enrollment");

		final IUserView userView = getUserView(request);
		final DynaValidatorForm form = (DynaValidatorForm) actionForm;

		final String curricularCourseToEnroll = form.getString("curricularCourse");
		final Integer toEnroll = Integer.valueOf(curricularCourseToEnroll.split("-")[0]);
		readAndSetDegreeCurricularPlanID(request, form);

		final CurricularCourseEnrollmentType enrollmentType = CurricularCourseEnrollmentType
				.valueOf(curricularCourseToEnroll.split("-")[1]);

		final String courseType = form.getString("courseType");
		try {
			final Object[] args = { readAndSetExecutionDegree(request), getStudent(userView, form),
					toEnroll, null, enrollmentType, Integer.valueOf(courseType), userView };
			ServiceManagerServiceFactory.executeService(userView, "WriteEnrollment", args);

		} catch (NotAuthorizedFilterException e) {
			if (e.getMessage() != null) {
				addAuthorizationErrors(request, e);
			} else {
				addActionMessage(request, "error.exception.notAuthorized");
			}
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return prepareEnrollmentChooseCurricularCourses(mapping, actionForm, request, response);
	}

	public ActionForward unenrollFromCurricularCourse(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		super.validateToken(request, actionForm, mapping, "error.transaction.enrollment");

		final DynaValidatorForm form = (DynaValidatorForm) actionForm;
		final Integer unenroll = Integer.valueOf(form.getString("curricularCourse"));
		readAndSetDegreeCurricularPlanID(request, form);

		try {
			final IUserView userView = getUserView(request);
			final Object[] args = { readAndSetExecutionDegree(request), getStudent(userView, form),
					unenroll };
			ServiceManagerServiceFactory.executeService(userView, "DeleteEnrolment", args);

		} catch (NotAuthorizedFilterException e) {
			if (e.getMessage() != null) {
				addAuthorizationErrors(request, e);
			} else {
				addActionMessage(request, "error.exception.notAuthorized");
			}
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		return prepareEnrollmentChooseCurricularCourses(mapping, actionForm, request, response);
	}

	public ActionForward enrollmentConfirmation(ActionMapping mapping, ActionForm actionform,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		final IUserView userView = getUserView(request);
		final DynaValidatorForm form = (DynaValidatorForm) actionform;

		readAndSetDegreeCurricularPlanID(request, form);

		final Registration registration = getStudent(userView, form);
		setStudentCurrentSemesterEnrolments(request, userView, registration);

		if (!getActionMessages(request).isEmpty()) {
			return prepareEnrollmentChooseCurricularCourses(mapping, actionform, request, response);
		}

		return mapping.findForward("enrollmentConfirmation");
	}

	private void setStudentCurrentSemesterEnrolments(HttpServletRequest request,
			final IUserView userView, final Registration student) throws FenixFilterException,
			FenixActionException {

		try {
			StudentCurricularPlan studentCurricularPlan = null;
			final Object[] args = { readAndSetExecutionDegree(request), student };
			if (checkIfUserHasAdministrativeRoles(userView) || checkIfUserHasTeacherRole(userView)) {

				studentCurricularPlan = (StudentCurricularPlan) ServiceManagerServiceFactory
						.executeService(userView, "ReadStudentCurricularPlanForEnrollments", args);
			} else {
				studentCurricularPlan = (StudentCurricularPlan) ServiceManagerServiceFactory
						.executeService(userView, "ReadStudentCurricularPlanWithRulesForEnrollments",
								args);
			}

			final ExecutionPeriod actualExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();

			request.setAttribute("studentCurricularPlan", studentCurricularPlan);
			request.setAttribute("executionPeriod", actualExecutionPeriod);

			final List<Enrolment> allStudentEnrolledEnrollmentsInExecutionPeriod = studentCurricularPlan
					.getAllStudentEnrolledEnrollmentsInExecutionPeriod(actualExecutionPeriod);
			Collections.sort(allStudentEnrolledEnrollmentsInExecutionPeriod, new BeanComparator(
					"curricularCourse.name"));

			request.setAttribute("studentCurrentSemesterEnrollments",
					allStudentEnrolledEnrollmentsInExecutionPeriod);

			final List<Enrolment> curriculum = new ArrayList<Enrolment>(studentCurricularPlan
					.getEnrolmentsSet());
			sortCurriculum(curriculum);
			request.setAttribute("curriculum", curriculum);

		} catch (NotAuthorizedFilterException e) {
			if (e.getMessage() != null) {
				addAuthorizationErrors(request, e);
			} else {
				addActionMessage(request, "error.exception.notAuthorized");
			}

		} catch (ExistingServiceException e) {
			if (e.getMessage().equals("student")) {
				addActionMessage(request, "error.no.student.in.database", student.getNumber().toString());

			} else if (e.getMessage().equals("studentCurricularPlan")) {
				addActionMessage(request, "error.student.curricularPlan.nonExistent");
			}

		} catch (OutOfCurricularCourseEnrolmentPeriod e) {
			addActionMessage(request, e.getMessageKey(), Data.format2DayMonthYear(e.getStartDate()),
					Data.format2DayMonthYear(e.getEndDate()));

		} catch (FenixServiceException e) {
			if (e.getMessage().equals("degree")) {
				addActionMessage(request, "error.student.degreeCurricularPlan.LEEC");
			}
			if (e.getMessage().equals("enrolmentPeriod")) {
				addActionMessage(request, "error.student.enrolmentPeriod");
			}
			throw new FenixActionException(e);
		}
	}

	private void sortCurriculum(List curriculum) {
		final ComparatorChain chainComparator = new ComparatorChain();
		chainComparator.addComparator(new BeanComparator("curricularCourse.name"));
		chainComparator.addComparator(new BeanComparator("executionPeriod.executionYear.year"));
		Collections.sort(curriculum, chainComparator);
	}

	protected Registration getStudent(IUserView userView, DynaActionForm form) {
		final Integer studentNumber = Integer.valueOf(form.getString("studentNumber"));
		return Registration.readStudentByNumberAndDegreeType(studentNumber, DegreeType.DEGREE);
	}
}