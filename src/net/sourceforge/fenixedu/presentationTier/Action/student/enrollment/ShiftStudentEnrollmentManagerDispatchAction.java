package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ShiftToEnrol;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.commons.TransactionalDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.ExecutionDegreesFormat;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ShiftStudentEnrollmentManagerDispatchAction extends TransactionalDispatchAction {

    public ActionForward prepareStartViewWarning(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return mapping.findForward("prepareEnrollmentViewWarning");
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	super.createToken(request);
	return prepareShiftEnrollment(mapping, form, request, response);
    }

    public ActionForward prepareShiftEnrollment(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final String classID = request.getParameter("classId");
	if (classID != null) {
	    request.setAttribute("studentId", Integer.valueOf(request.getParameter("studentId")));
	    request.setAttribute("classId", Integer.valueOf(classID));
	    return mapping.findForward("showEnrollmentPage");
	}

	final IUserView userView = getUserView(request);
	final Registration registration = getStudent(userView);
	final ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();
	request.setAttribute("student", registration);

	if (readAndSetSelectCoursesParameter(request) == null) {
	    return prepareShiftEnrolmentInformation(mapping, request, userView, registration,
		    executionPeriod);

	} else {
	    return prepareSelectCoursesInformation(mapping, actionForm, request, registration,
		    executionPeriod);
	}
    }

    private ActionForward prepareSelectCoursesInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, final Registration registration,
	    final ExecutionPeriod executionPeriod) {

	final DynaActionForm form = (DynaActionForm) actionForm;

	final List<ExecutionDegree> executionDegrees = executionPeriod.getExecutionYear()
		.getExecutionDegreesFor(DegreeType.DEGREE);
	executionDegrees.addAll(executionPeriod.getExecutionYear()
		.getExecutionDegreesFor(DegreeType.BOLONHA_DEGREE));
	executionDegrees.addAll(executionPeriod.getExecutionYear()
		.getExecutionDegreesFor(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE));

	if (executionDegrees.isEmpty()) {
	    addActionMessage(request, "errors.impossible.operation");
	    return mapping.getInputForward();
	}

	final ExecutionDegree selectedExecutionDegree = getSelectedExecutionDegree(form, registration,
		executionPeriod, executionDegrees);
	if (selectedExecutionDegree == null) {
	    addActionMessage(request, "errors.impossible.operation");
	    return mapping.getInputForward();
	}

	request.setAttribute("selectedExecutionDegree", selectedExecutionDegree);
	form.set("degree", selectedExecutionDegree.getIdInternal());

	sortExecutionDegreesByDegreeName(executionDegrees);
	request.setAttribute("executionDegrees", ExecutionDegreesFormat
		.buildLabelValueBeansForExecutionDegree(executionDegrees, getResources(request,
			"ENUMERATION_RESOURCES"), request));

	request.setAttribute("attendingExecutionCourses", registration
		.getAttendingExecutionCoursesFor(executionPeriod));
	request.setAttribute("executionCoursesFromExecutionDegree", selectedExecutionDegree
		.getDegreeCurricularPlan().getExecutionCoursesByExecutionPeriod(executionPeriod));

	return mapping.findForward("selectCourses");
    }

    private void sortExecutionDegreesByDegreeName(List<ExecutionDegree> result) {
	Collections.sort(result, new BeanComparator("degree.name"));
    }

    private ActionForward prepareShiftEnrolmentInformation(ActionMapping mapping,
	    HttpServletRequest request, final IUserView userView, final Registration registration,
	    final ExecutionPeriod executionPeriod) throws FenixFilterException {

	try {

	    final List<ShiftToEnrol> shiftsToEnrol = (List<ShiftToEnrol>) ServiceManagerServiceFactory
		    .executeService(userView, "ReadShiftsToEnroll", new Object[] { registration });

	    request.setAttribute("numberOfExecutionCoursesHavingNotEnroledShifts", registration
		    .getNumberOfExecutionCoursesHavingNotEnroledShiftsFor(executionPeriod));

	    request.setAttribute("shiftsToEnrolFromEnroledExecutionCourses",
		    getShiftsToEnrolByEnroledState(shiftsToEnrol, true));
	    request.setAttribute("shiftsToEnrolFromUnenroledExecutionCourses",
		    getShiftsToEnrolByEnroledState(shiftsToEnrol, false));

	    final List<Shift> studentShifts = registration.getShiftsFor(executionPeriod);
	    request.setAttribute("studentShifts", studentShifts);
	    sortStudentShifts(studentShifts);

	    return mapping.findForward("showShiftsEnrollment");

	} catch (FenixServiceException e) {
	    addActionMessage(request, e.getMessage());
	    return mapping.getInputForward();
	}
    }

    private void sortStudentShifts(List<Shift> studentShifts) {
	final ComparatorChain chain = new ComparatorChain();
	chain.addComparator(new BeanComparator("disciplinaExecucao.nome"));
	chain.addComparator(new BeanComparator("tipo"));
	Collections.sort(studentShifts, chain);
    }

    private List<ShiftToEnrol> getShiftsToEnrolByEnroledState(final List<ShiftToEnrol> shiftsToEnrol,
	    boolean enroled) {
	List<ShiftToEnrol> result = new ArrayList<ShiftToEnrol>();
	for (final ShiftToEnrol shiftToEnrol : shiftsToEnrol) {
	    if (shiftToEnrol.isEnrolled() == enroled) {
		result.add(shiftToEnrol);
	    }
	}
	return result;
    }

    private ExecutionDegree getSelectedExecutionDegree(final DynaActionForm form,
	    final Registration registration, final ExecutionPeriod executionPeriod,
	    final List<ExecutionDegree> executionDegrees) {

	final Integer executionDegreeIdChosen = (Integer) form.get("degree");
	final ExecutionDegree executionDegreeChosen = rootDomainObject
		.readExecutionDegreeByOID(executionDegreeIdChosen);
	if (executionDegreeChosen != null
		&& executionDegreeChosen.getExecutionYear() == executionPeriod.getExecutionYear()) {
	    return executionDegreeChosen;
	} else {
	    return searchForExecutionDegreeInStudent(registration, executionPeriod);
	}
    }

    private ExecutionDegree searchForExecutionDegreeInStudent(final Registration registration,
	    final ExecutionPeriod executionPeriod) {
	final StudentCurricularPlan studentCurricularPlan = registration
		.getActiveStudentCurricularPlan();
	if (studentCurricularPlan == null) {
	    return null;
	}
	for (final ExecutionDegree executionDegree : studentCurricularPlan.getDegreeCurricularPlan()
		.getExecutionDegreesSet()) {
	    if (executionDegree.getExecutionYear() == executionPeriod.getExecutionYear()) {
		return executionDegree;
	    }
	}
	for (final DegreeCurricularPlan degreeCurricularPlan : studentCurricularPlan.getDegree()
		.getDegreeCurricularPlansSet()) {
	    for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
		if (executionDegree.getExecutionYear() == executionPeriod.getExecutionYear()) {
		    return executionDegree;
		}
	    }
	}
	return null;
    }

    private Registration getStudent(final IUserView userView) {
	Registration registration = userView.getPerson().getStudentByUsername();
	if (registration == null) {
	    registration = userView.getPerson().getStudentByType(DegreeType.DEGREE);
	}
	return registration;
    }

    private String readAndSetSelectCoursesParameter(final HttpServletRequest request) {
	final String selectCourses = request.getParameter("selectCourses");
	if (selectCourses != null) {
	    request.setAttribute("selectCourses", selectCourses);
	}
	return selectCourses;
    }

    public ActionForward unEnroleStudentFromShift(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final IUserView userView = getUserView(request);
	final Integer shiftId = Integer.valueOf(request.getParameter("shiftId"));
	final String executionCourseID = request.getParameter("executionCourseID");
	if (!StringUtils.isEmpty(executionCourseID)) {
	    request.setAttribute("executionCourseID", executionCourseID);
	}

	try {
	    ServiceManagerServiceFactory.executeService(userView, "UnEnrollStudentFromShift",
		    new Object[] { getStudent(userView), shiftId });

	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	return start(mapping, form, request, response);
    }

}