package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ShiftToEnrol;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.ExecutionDegreesFormat;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ShiftStudentEnrollmentManagerDispatchAction extends TransactionalDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final Student student = getUserView(request).getPerson().getStudent();
	final List<Registration> toEnrol = student.getRegistrationsToEnrolInShiftByStudent();
	if (toEnrol.size() == 1) {
	    request.setAttribute("registrationOID", toEnrol.get(0).getIdInternal());
	    return prepareStartViewWarning(mapping, form, request, response);
	} else {
	    request.setAttribute("toEnrol", toEnrol);
	    return mapping.findForward("chooseRegistration");
	}
    }

    private Registration getAndSetRegistration(final HttpServletRequest request) {
	final Registration registration = rootDomainObject
		.readRegistrationByOID(getIntegerFromRequest(request, "registrationOID"));
	if (!getUserView(request).getPerson().getStudent().getRegistrationsToEnrolInShiftByStudent().contains(registration)) {
	    return null;
	}

	request.setAttribute("registration", registration);
	request.setAttribute("registrationOID", registration.getIdInternal().toString());
	return registration;
    }

    public ActionForward prepareStartViewWarning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (getAndSetRegistration(request) == null) {
	    addActionMessage(request, "errors.impossible.operation");
	    return mapping.getInputForward();
	} else {
	    return mapping.findForward("prepareEnrollmentViewWarning");
	}
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	super.createToken(request);
	return prepareShiftEnrollment(mapping, form, request, response);
    }

    public ActionForward prepareShiftEnrollment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Registration registration = getAndSetRegistration(request);
	if (registration == null) {
	    addActionMessage(request, "errors.impossible.operation");
	    return mapping.getInputForward();
	}

	final String classID = request.getParameter("classId");
	if (classID != null) {
	    request.setAttribute("classId", Integer.valueOf(classID));
	    return mapping.findForward("showEnrollmentPage");
	}

	final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
	if (readAndSetSelectCoursesParameter(request) == null) {
	    return prepareShiftEnrolmentInformation(mapping, request, registration, executionSemester);
	} else {
	    return prepareSelectCoursesInformation(mapping, actionForm, request, registration, executionSemester);
	}
    }

    private ActionForward prepareSelectCoursesInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, final Registration registration, final ExecutionSemester executionSemester) {

	final DynaActionForm form = (DynaActionForm) actionForm;

	final List<ExecutionDegree> executionDegrees = executionSemester.getExecutionYear().getExecutionDegreesFor(
		DegreeType.DEGREE);
	executionDegrees.addAll(executionSemester.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_DEGREE));
	executionDegrees.addAll(executionSemester.getExecutionYear().getExecutionDegreesFor(
		DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));
	executionDegrees.addAll(executionSemester.getExecutionYear().getExecutionDegreesFor(DegreeType.BOLONHA_MASTER_DEGREE));

	if (executionDegrees.isEmpty()) {
	    addActionMessage(request, "errors.impossible.operation");
	    return mapping.getInputForward();
	}

	final ExecutionDegree selectedExecutionDegree = getSelectedExecutionDegree(form, registration, executionSemester,
		executionDegrees);
	if (selectedExecutionDegree == null) {
	    addActionMessage(request, "errors.impossible.operation");
	    return mapping.getInputForward();
	}

	request.setAttribute("selectedExecutionDegree", selectedExecutionDegree);
	form.set("degree", selectedExecutionDegree.getIdInternal());

	sortExecutionDegreesByDegreeName(executionDegrees);
	request.setAttribute("executionDegrees", ExecutionDegreesFormat.buildLabelValueBeansForExecutionDegree(executionDegrees,
		getResources(request, "ENUMERATION_RESOURCES"), request));

	request.setAttribute("attendingExecutionCourses", registration.getAttendingExecutionCoursesFor(executionSemester));
	request.setAttribute("executionCoursesFromExecutionDegree", selectedExecutionDegree.getDegreeCurricularPlan()
		.getExecutionCoursesByExecutionPeriod(executionSemester));

	return mapping.findForward("selectCourses");
    }

    private void sortExecutionDegreesByDegreeName(List<ExecutionDegree> result) {
	Collections.sort(result, new BeanComparator("degree.name"));
    }

    private ActionForward prepareShiftEnrolmentInformation(ActionMapping mapping, HttpServletRequest request,
	    final Registration registration, final ExecutionSemester executionSemester) throws FenixFilterException {

	try {

	    final List<ShiftToEnrol> shiftsToEnrol = (List<ShiftToEnrol>) ServiceManagerServiceFactory.executeService(
		    "ReadShiftsToEnroll", new Object[] { registration });

	    request.setAttribute("numberOfExecutionCoursesHavingNotEnroledShifts", registration
		    .getNumberOfExecutionCoursesHavingNotEnroledShiftsFor(executionSemester));

	    request.setAttribute("shiftsToEnrolFromEnroledExecutionCourses", getShiftsToEnrolByEnroledState(shiftsToEnrol, true));
	    request.setAttribute("shiftsToEnrolFromUnenroledExecutionCourses", getShiftsToEnrolByEnroledState(shiftsToEnrol,
		    false));

	    final List<Shift> studentShifts = registration.getShiftsFor(executionSemester);
	    request.setAttribute("studentShifts", studentShifts);
	    sortStudentShifts(studentShifts);

	    return mapping.findForward("showShiftsEnrollment");

	} catch (FenixServiceException e) {
	    addActionMessage(request, e.getMessage());
	    return mapping.getInputForward();
	}
    }

    private void sortStudentShifts(List<Shift> studentShifts) {
	Collections.sort(studentShifts, Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS);
    }

    private List<ShiftToEnrol> getShiftsToEnrolByEnroledState(final List<ShiftToEnrol> shiftsToEnrol, boolean enroled) {
	List<ShiftToEnrol> result = new ArrayList<ShiftToEnrol>();
	for (final ShiftToEnrol shiftToEnrol : shiftsToEnrol) {
	    if (shiftToEnrol.isEnrolled() == enroled) {
		result.add(shiftToEnrol);
	    }
	}
	return result;
    }

    private ExecutionDegree getSelectedExecutionDegree(final DynaActionForm form, final Registration registration,
	    final ExecutionSemester executionSemester, final List<ExecutionDegree> executionDegrees) {

	final Integer executionDegreeIdChosen = (Integer) form.get("degree");
	final ExecutionDegree executionDegreeChosen = rootDomainObject.readExecutionDegreeByOID(executionDegreeIdChosen);
	if (executionDegreeChosen != null && executionDegreeChosen.getExecutionYear() == executionSemester.getExecutionYear()) {
	    return executionDegreeChosen;
	} else {
	    return searchForExecutionDegreeInStudent(registration, executionSemester);
	}
    }

    private ExecutionDegree searchForExecutionDegreeInStudent(final Registration registration,
	    final ExecutionSemester executionSemester) {
	final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
	if (studentCurricularPlan == null) {
	    return null;
	}
	for (final ExecutionDegree executionDegree : studentCurricularPlan.getDegreeCurricularPlan().getExecutionDegreesSet()) {
	    if (executionDegree.getExecutionYear() == executionSemester.getExecutionYear()) {
		return executionDegree;
	    }
	}
	for (final DegreeCurricularPlan degreeCurricularPlan : studentCurricularPlan.getDegree().getDegreeCurricularPlansSet()) {
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		if (executionDegree.getExecutionYear() == executionSemester.getExecutionYear()) {
		    return executionDegree;
		}
	    }
	}
	for (final DegreeCurricularPlanEquivalencePlan equivalencePlan : studentCurricularPlan.getDegreeCurricularPlan()
		.getTargetEquivalencePlansSet()) {
	    final DegreeCurricularPlan otherDegreeCurricularPlan = equivalencePlan.getDegreeCurricularPlan();
	    for (final ExecutionDegree executionDegree : otherDegreeCurricularPlan.getExecutionDegreesSet()) {
		if (executionDegree.getExecutionYear() == executionSemester.getExecutionYear()) {
		    return executionDegree;
		}
	    }
	}
	return null;
    }

    private String readAndSetSelectCoursesParameter(final HttpServletRequest request) {
	final String selectCourses = request.getParameter("selectCourses");
	if (selectCourses != null) {
	    request.setAttribute("selectCourses", selectCourses);
	}
	return selectCourses;
    }

    public ActionForward unEnroleStudentFromShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Registration registration = getAndSetRegistration(request);
	if (registration == null) {
	    addActionMessage(request, "errors.impossible.operation");
	    return mapping.getInputForward();
	}

	final Integer shiftId = Integer.valueOf(request.getParameter("shiftId"));
	final String executionCourseID = request.getParameter("executionCourseID");
	if (!StringUtils.isEmpty(executionCourseID)) {
	    request.setAttribute("executionCourseID", executionCourseID);
	}

	try {
	    ServiceManagerServiceFactory.executeService("UnEnrollStudentFromShift", new Object[] { registration, shiftId });

	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	return start(mapping, form, request, response);
    }

}