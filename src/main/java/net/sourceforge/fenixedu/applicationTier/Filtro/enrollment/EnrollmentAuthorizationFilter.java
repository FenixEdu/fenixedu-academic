package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.Filtro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class EnrollmentAuthorizationFilter extends Filtro {

    public static final EnrollmentAuthorizationFilter instance = new EnrollmentAuthorizationFilter();

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

    protected String hasPrevilege(IUserView userView, Integer executionDegreeId, Registration registration) {
        if (userView.hasRoleType(RoleType.STUDENT)) {

            if (registration != null) {
                return checkStudentInformation(userView, registration);
            }

            return checkStudentInformation(userView);

        } else {
            if (userView.hasRoleType(RoleType.COORDINATOR) && executionDegreeId != null) {
                return checkCoordinatorInformation(userView, executionDegreeId, registration);

            } else if (userView.hasRoleType(RoleType.TEACHER)) {
                return checkTeacherInformation(userView, executionDegreeId, registration);

            } else if (userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE)
                    || userView.hasRoleType(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER)) {

                return checkDegreeAdministrativeOfficeInformation(registration);

            } else {
                return "noAuthorization";
            }
        }
    }

    protected String checkDegreeAdministrativeOfficeInformation(Registration registration) {

        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
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

    protected String checkTeacherInformation(IUserView userView, Integer executionDegreeId, Registration registration) {

        final Teacher teacher = readTeacher(userView);
        if (teacher == null) {
            return "noAuthorization";
        }

        if (registration == null) {
            return "noAuthorization";
        }

        if (registration.getActiveTutorship() == null || !registration.getActiveTutorship().getTeacher().equals(teacher)) {
            return "error.enrollment.notStudentTutor+" + registration.getNumber().toString();
        }

        return null;
    }

    private String checkCoordinatorInformation(IUserView userView, Integer executionDegreeId, Registration registration) {
        if (!verifyCoordinator(userView.getPerson(), executionDegreeId, registration)) {
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

        if (!registration.isActive()) {
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

    protected Teacher readTeacher(IUserView id) {
        return id.getPerson().getTeacher();
    }

    protected boolean verifyCoordinator(Person person, Integer executionDegreeId, Registration registration) {

        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(executionDegreeId);
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

        final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
        if (studentCurricularPlan == null) {
            return false;
        }
        return studentCurricularPlan.getDegreeCurricularPlan().equals(coordinator.getExecutionDegree().getDegreeCurricularPlan());
    }

    public void execute(Integer executionDegreeId, Registration registration) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        String messageException = hasPrevilege(id, executionDegreeId, registration);

        if ((id != null && id.getRoleTypes() != null && !containsRoleType(id.getRoleTypes()))
                || (id != null && id.getRoleTypes() != null && messageException != null) || (id == null)
                || (id.getRoleTypes() == null)) {
            throw new NotAuthorizedException(messageException);
        }
    }
}