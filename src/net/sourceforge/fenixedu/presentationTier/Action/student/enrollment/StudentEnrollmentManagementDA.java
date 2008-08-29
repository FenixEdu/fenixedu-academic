package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.CycleEnrolmentBean;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class StudentEnrollmentManagementDA extends FenixDispatchAction {

    public ActionForward showWelcome(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return mapping.findForward("welcome");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final Student student = getLoggedStudent(request);
	if (!student.hasPersonDataAuthorizationChoiseForCurrentExecutionYear()) {
	    request.setAttribute("student", student);
	    return mapping.findForward("choosePersonalDataAuthorizationChoice");
	}
	
	if(student.hasInquiriesToRespond()){
	    addActionMessage(request, "message.student.cannotEnroll.inquiriesNotAnswered");
	    return mapping.findForward("enrollmentCannotProceed");
	}

	final List<Registration> registrationsToEnrol = getRegistrationsToEnrolByStudent(request);
	if (registrationsToEnrol.size() == 1) {
	    final Registration registration = registrationsToEnrol.iterator().next();
	    return getActionForwardForRegistration(mapping, request, registration);
	}

	request.setAttribute("registrationsToEnrol", registrationsToEnrol);
	request.setAttribute("registrationsToChooseSecondCycle", getRegistrationsToChooseSecondCycle(student));

	return mapping.findForward("chooseRegistration");
    }

    // TODO: refactor this method
    private List<Registration> getRegistrationsToChooseSecondCycle(final Student student) {
	final List<Registration> result = new ArrayList<Registration>();

	for (final Registration registration : student.getRegistrations()) {

	    if (!registration.isBolonha() || !registration.isConcluded()) {
		continue;
	    }

	    final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
	    if (studentCurricularPlan.getDegreeType() != DegreeType.BOLONHA_MASTER_DEGREE) {

		final CycleCurriculumGroup firstCycle = studentCurricularPlan.getFirstCycle();
		if (firstCycle != null && firstCycle.isConcluded()
			&& !studentCurricularPlan.hasAnyRegistrationWithFirstCycleAffinity()) {
		    result.add(registration);
		}
	    }
	}

	return result;
    }

    private ActionForward getActionForwardForRegistration(ActionMapping mapping, HttpServletRequest request,
	    final Registration registration) {

	final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
	final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();

	//----------------------------------------------------------------------
	// ---------------------------------------------
	// TODO: refactor this code, should be more generic
	//----------------------------------------------------------------------
	// ---------------------------------------------

	if (!studentCurricularPlan.isActive() && !studentCurricularPlan.getRegistration().isConcluded()) {
	    request.setAttribute("registrationsToEnrol", Collections.singletonList(registration));
	    addActionMessage(request, "error.studentCurricularPlan.is.not.active.or.concluded");
	    return mapping.findForward("chooseRegistration");
	}

	if (studentCurricularPlan.getDegreeType() == DegreeType.BOLONHA_MASTER_DEGREE) {
	    request.setAttribute("registration", registration);
	    return mapping.findForward("proceedToEnrolment");

	} else {
	    final CycleCurriculumGroup firstCycle = studentCurricularPlan.getFirstCycle();

	    if (firstCycle == null || !firstCycle.isConcluded()) {
		request.setAttribute("registration", registration);
		return mapping.findForward("proceedToEnrolment");

	    } else {
		final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
		if (secondCycle == null) {
		    return prepareSelectAffinityToEnrol(mapping, request, studentCurricularPlan, executionSemester);

		} else if (secondCycle.isExternal()) {
		    final Student student = studentCurricularPlan.getRegistration().getStudent();
		    final Registration newRegistration = student.getRegistrationFor(secondCycle
			    .getDegreeCurricularPlanOfDegreeModule());

		    if (newRegistration != null) {
			request.setAttribute("registration", newRegistration);
			return mapping.findForward("proceedToEnrolment");
		    }

		    return showAffinityToEnrol(mapping, request, studentCurricularPlan, executionSemester, secondCycle);

		} else {
		    request.setAttribute("registration", registration);
		    return mapping.findForward("proceedToEnrolment");
		}
	    }
	}

	//----------------------------------------------------------------------
	// ---------------------------------------------
	// TODO: refactor this code, should be more generic
	//----------------------------------------------------------------------
	// ---------------------------------------------

    }

    private ActionForward prepareSelectAffinityToEnrol(final ActionMapping mapping, final HttpServletRequest request,
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester) {

	if (!canContinueToEnrolment(request, studentCurricularPlan, executionSemester)) {
	    return mapping.findForward("enrollmentCannotProceed");
	}

	request.setAttribute("cycleEnrolmentBean", new CycleEnrolmentBean(studentCurricularPlan, executionSemester,
		CycleType.FIRST_CYCLE, CycleType.SECOND_CYCLE));

	return mapping.findForward("selectAffinityToEnrol");
    }

    private ActionForward showAffinityToEnrol(final ActionMapping mapping, final HttpServletRequest request,
	    final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
	    final CycleCurriculumGroup curriculumGroup) {

	if (!canContinueToEnrolment(request, studentCurricularPlan, executionSemester)) {
	    return mapping.findForward("enrollmentCannotProceed");
	}

	request.setAttribute("cycleEnrolmentBean", new CycleEnrolmentBean(studentCurricularPlan, executionSemester,
		curriculumGroup.getCycleCourseGroup()));

	return mapping.findForward("showAffinityToEnrol");
    }

    public ActionForward showAffinityToEnrol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final CycleEnrolmentBean bean = getCycleEnrolmentBeanFromViewState();

	if (!canContinueToEnrolment(request, bean.getStudentCurricularPlan(), bean.getExecutionPeriod())) {
	    return mapping.findForward("enrollmentCannotProceed");
	}

	request.setAttribute("cycleEnrolmentBean", bean);
	return mapping.findForward("showAffinityToEnrol");
    }

    private boolean canContinueToEnrolment(final HttpServletRequest request, final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester) {

	if (isFromSpecialSeason(studentCurricularPlan, executionSemester)) {
	    if (studentCurricularPlan.getDegreeCurricularPlan().getActualEnrolmentPeriodInCurricularCoursesSpecialSeason() == null) {
		addOutOfPeriodMessage(request, studentCurricularPlan.getDegreeCurricularPlan()
			.getNextEnrolmentPeriodInCurricularCoursesSpecialSeason());
		return false;
	    }
	} else {
	    if (!studentCurricularPlan.getDegreeCurricularPlan().hasActualEnrolmentPeriodInCurricularCourses()) {
		addOutOfPeriodMessage(request, studentCurricularPlan.getDegreeCurricularPlan().getNextEnrolmentPeriod());
		return false;
	    }
	}

	if (studentCurricularPlan.getRegistration().getStudent().isAnyTuitionInDebt()) {
	    addActionMessage(request, "error.message.tuitionNotPayed");
	    return false;
	}

	return true;
    }

    private boolean isFromSpecialSeason(final StudentCurricularPlan activeStudentCurricularPlan,
	    final ExecutionSemester executionSemester) {
	return activeStudentCurricularPlan.hasSpecialSeasonOrHasSpecialSeasonInTransitedStudentCurricularPlan(executionSemester);
    }

    private void addOutOfPeriodMessage(HttpServletRequest request, final EnrolmentPeriod nextEnrollmentPeriod) {
	if (nextEnrollmentPeriod != null) {
	    addActionMessage(request, "message.out.curricular.course.enrolment.period", nextEnrollmentPeriod
		    .getStartDateDateTime().toString(DateFormatUtil.DEFAULT_DATE_FORMAT), nextEnrollmentPeriod
		    .getEndDateDateTime().toString(DateFormatUtil.DEFAULT_DATE_FORMAT));
	} else {
	    addActionMessage(request, "message.out.curricular.course.enrolment.period.default");
	}
    }

    public ActionForward enrolInAffinityCycle(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CycleEnrolmentBean cycleEnrolmentBean = getCycleEnrolmentBeanFromViewState();

	try {
	    final Registration registration = (Registration) executeService("EnrolInAffinityCycle", getLoggedPerson(request),
		    cycleEnrolmentBean.getStudentCurricularPlan(), cycleEnrolmentBean.getCycleCourseGroupToEnrol(),
		    cycleEnrolmentBean.getExecutionPeriod());

	    request.setAttribute("registration", registration);

	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    request.setAttribute("cycleEnrolmentBean", cycleEnrolmentBean);
	    return mapping.findForward("showAffinityToEnrol");
	}

	return mapping.findForward("proceedToEnrolment");
    }

    public ActionForward chooseRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Registration registration = getRegistration(request);
	if (!registrationBelongsToRegistrationsToEnrol(request, registration)) {
	    return mapping.findForward("notAuthorized");
	}

	return getActionForwardForRegistration(mapping, request, registration);
    }

    private boolean registrationBelongsToRegistrationsToEnrol(HttpServletRequest request, final Registration registration) {
	return getRegistrationsToEnrolByStudent(request).contains(registration);
    }

    private Registration getRegistration(final HttpServletRequest request) {
	return getRegistrationFrom(request, "registrationId");
    }

    private Registration getRegistrationFrom(final HttpServletRequest request, final String parameterName) {
	return rootDomainObject.readRegistrationByOID(getRequestParameterAsInteger(request, parameterName));
    }

    public ActionForward choosePersonalDataAuthorizationChoice(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepare(mapping, form, request, response);
    }

    private List<Registration> getRegistrationsToEnrolByStudent(final HttpServletRequest request) {
	return getLoggedStudent(request).getRegistrationsToEnrolByStudent();
    }

    private Student getLoggedStudent(final HttpServletRequest request) {
	return getLoggedPerson(request).getStudent();
    }

    private CycleEnrolmentBean getCycleEnrolmentBeanFromViewState() {
	return (CycleEnrolmentBean) getRenderedObject();
    }
}
