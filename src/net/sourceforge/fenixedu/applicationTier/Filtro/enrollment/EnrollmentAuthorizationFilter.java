package net.sourceforge.fenixedu.applicationTier.Filtro.enrollment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByManyRolesFilter;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.SecretaryEnrolmentStudent;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

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
        if (insideEnrollmentPeriod(studentCurricularPlan)) {
            final Tutor tutor = studentCurricularPlan.getAssociatedTutor();
            if (tutor != null) {
                return new String("error.enrollment.student.withTutor+"
                        + tutor.getTeacher().getTeacherNumber().toString() + "+"
                        + tutor.getTeacher().getPerson().getNome());
            }
        }
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

        if (registration.getAssociatedTutor() == null) {
            return new String("error.enrollment.notStudentTutor+" + registration.getNumber().toString());
        }

        return null;
    }

    private String checkCoordinatorInformation(IUserView userView, Object[] arguments) {

        final Teacher teacher = readTeacher(userView);
        if (teacher == null) {
            return "noAuthorization";
        }
        if (!verifyCoordinator(teacher, arguments)) {
            return "noAuthorization";
        }

        return null;
    }

    private String checkStudentInformation(IUserView userView) {

        final Registration registration = readStudent(userView);
        if (registration == null) {
            return "noAuthorization";
        }
        if (registration.getPayedTuition() == null
                || registration.getPayedTuition().equals(Boolean.FALSE)) {
            if (registration.getInterruptedStudies().equals(Boolean.FALSE)) {
                return "error.message.tuitionNotPayed";
            }
        }
        if (registration.getFlunked() == null || registration.getFlunked().equals(Boolean.TRUE)) {
            return "error.message.flunked";
        }
        if (registration.getRequestedChangeDegree() == null
                || registration.getRequestedChangeDegree().equals(Boolean.TRUE)) {
            return "error.message.requested.change.degree";
        }

        /*
         * if (registration.hasAssociatedTutor()) { return new
         * String("error.enrollment.student.withTutor+" +
         * registration.getAssociatedTutor().getTeacher().getTeacherNumber().toString() +
         * "+" +
         * registration.getAssociatedTutor().getTeacher().getPerson().getNome()); }
         */

        // check if the student is in the list of secretary enrolments
        // students
        final SecretaryEnrolmentStudent secretaryEnrolmentStudent = registration
                .getSecretaryEnrolmentStudents().isEmpty() ? null : registration
                .getSecretaryEnrolmentStudents().iterator().next();
        if (secretaryEnrolmentStudent != null) {
            return "error.message.secretaryEnrolmentStudent";
        }

        // check if the student is from old Leic Curricular Plan
        List studentCurricularPlans = (List) CollectionUtils.select(registration
                .getStudentCurricularPlans(), new Predicate() {
            public boolean evaluate(Object arg0) {
                StudentCurricularPlan scp = (StudentCurricularPlan) arg0;
                return scp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE);
            }
        });

        boolean oldLeicStudent = CollectionUtils.exists(studentCurricularPlans, new Predicate() {
            public boolean evaluate(Object arg0) {

                StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) arg0;
                return (studentCurricularPlan.getDegreeCurricularPlan().getIdInternal().intValue() == LEIC_OLD_DCP);

            }
        });
        if (oldLeicStudent) {
            return "error.message.oldLeicStudent";
        }

        return null;
    }

    protected boolean insideEnrollmentPeriod(StudentCurricularPlan studentCurricularPlan) {
        return (studentCurricularPlan.getDegreeCurricularPlan().getActualEnrolmentPeriod() != null);
    }

    protected Registration readStudent(IUserView userView) {
        return userView.getPerson().getStudentByUsername();
    }

    protected Registration readStudent(Object[] arguments) {
        return (arguments[1] != null) ? (Registration) arguments[1] : null;
    }

    protected Teacher readTeacher(IUserView id) {
        return id.getPerson().getTeacher();
    }

    protected boolean verifyCoordinator(Teacher teacher, Object[] args) {

        final ExecutionDegree executionDegree = rootDomainObject
                .readExecutionDegreeByOID((Integer) args[0]);
        if (executionDegree == null) {
            return false;
        }

        final Coordinator coordinator = executionDegree.getCoordinatorByTeacher(teacher);
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