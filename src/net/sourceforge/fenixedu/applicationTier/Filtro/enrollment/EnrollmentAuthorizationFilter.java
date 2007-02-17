package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EnrollmentAuthorizationFilter extends AuthorizationByManyRolesFilter {

    private static final int LEIC_OLD_DCP = 10;

    @Override
    protected Collection<RoleType> getNeededRoleTypes() {
	final List<RoleType> roles = new ArrayList<RoleType>();
	roles.add(RoleType.COORDINATOR);
	roles.add(RoleType.TEACHER);
	roles.add(RoleType.STUDENT);
	roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
	roles.add(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
	return roles;
    }

    protected String hasPrevilege(IUserView userView, Object[] arguments) {
	if (userView.hasRoleType(RoleType.STUDENT)) {

	    for (int i = 0; i < arguments.length; i++) {
		Object object = arguments[i];
		if (object instanceof Registration) {
		    return checkStudentInformation(userView, (Registration) object);
		}
	    }

	    return checkStudentInformation(userView);

	} else {
	    if (userView.hasRoleType(RoleType.COORDINATOR) && arguments[0] != null) {
		return checkCoordinatorInformation(userView, arguments);

	    } else if (userView.hasRoleType(RoleType.TEACHER)) {
		return checkTeacherInformation(userView, arguments);

	    } else if (userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
		    || userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {

		return checkDegreeAdministrativeOfficeInformation(arguments);

	    } else {
		return "noAuthorization";
	    }
	}
    }

    protected String checkDegreeAdministrativeOfficeInformation(Object[] args) {

	final StudentCurricularPlan studentCurricularPlan = readStudent(args)
		.getActiveStudentCurricularPlan();
	if (studentCurricularPlan == null) {
	    return "noAuthorization";
	}
	/*
         * if (insideEnrollmentPeriod(studentCurricularPlan)) { final Tutor
         * tutor = studentCurricularPlan.getAssociatedTutor(); if (tutor !=
         * null) { return "error.enrollment.student.withTutor+" +
         * tutor.getTeacher().getTeacherNumber().toString() + "+" +
         * tutor.getTeacher().getPerson().getNome(); } }
         */
	return null;
    }

    protected String checkTeacherInformation(IUserView userView, Object[] arguments) {

	final Teacher teacher = readTeacher(userView);
	if (teacher == null) {
	    return "noAuthorization";
	}

	final Registration registration = readStudent(arguments);
	if (registration == null) {
	    return "noAuthorization";
	}

	if (registration.getAssociatedTutor() == null
		|| !registration.getAssociatedTutor().getTeacher().equals(teacher)) {
	    return "error.enrollment.notStudentTutor+" + registration.getNumber().toString();
	}

	return null;
    }

    private String checkCoordinatorInformation(IUserView userView, Object[] arguments) {
	if (!verifyCoordinator(userView.getPerson(), arguments)) {
	    return "noAuthorization";
	}

	return null;
    }

    private String checkStudentInformation(IUserView userView) {
	Registration registration = readStudent(userView);
	return checkStudentInformation(userView, registration);
    }

    private String checkStudentInformation(IUserView userView, Registration registration) {

	if (readStudent(userView) == null) {
	    return "noAuthorization";
	}

	if (!registration.isInRegisteredState()) {
	    return "error.message.not.in.registered.state";
	}

	if (!registration.getPayedTuition()) {
	    if (!registration.getInterruptedStudies()) {
		return "error.message.tuitionNotPayed";
	    }
	}

	if (registration.getRequestedChangeDegree() == null || registration.getRequestedChangeDegree()) {
	    return "error.message.requested.change.degree";
	}

	if (registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan().getIdInternal() == LEIC_OLD_DCP) {

	    return "error.message.oldLeicStudent";
	}

	return null;
    }

    protected boolean insideEnrollmentPeriod(StudentCurricularPlan studentCurricularPlan) {
	return (studentCurricularPlan.getDegreeCurricularPlan().getActualEnrolmentPeriod() != null);
    }

    protected Registration readStudent(IUserView userView) {
	Registration registration = userView.getPerson().getStudentByUsername();
	if (registration == null) {
	    return userView.getPerson().getStudentByType(DegreeType.DEGREE);
	}
	return registration;
    }

    protected Registration readStudent(Object[] arguments) {
	return (arguments[1] != null) ? (Registration) arguments[1] : null;
    }

    protected Teacher readTeacher(IUserView id) {
	return id.getPerson().getTeacher();
    }

    protected boolean verifyCoordinator(Person person, Object[] args) {

	final ExecutionDegree executionDegree = rootDomainObject
		.readExecutionDegreeByOID((Integer) args[0]);
	if (executionDegree == null) {
	    return false;
	}

	final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(person);
	if (coordinator == null) {
	    return false;
	}

	// check if is LEEC coordinator
	if (!coordinator.getExecutionDegree().getDegreeCurricularPlan().getName().equals("LEEC 2003")) {
	    return false;
	}

	final StudentCurricularPlan studentCurricularPlan = readStudent(args)
		.getActiveStudentCurricularPlan();
	if (studentCurricularPlan == null) {
	    return false;
	}
	return studentCurricularPlan.getDegreeCurricularPlan().equals(
		coordinator.getExecutionDegree().getDegreeCurricularPlan());
    }
}