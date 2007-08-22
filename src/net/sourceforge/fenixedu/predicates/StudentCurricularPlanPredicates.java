package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class StudentCurricularPlanPredicates {

    public static final AccessControlPredicate<StudentCurricularPlan> enrol = new AccessControlPredicate<StudentCurricularPlan>() {

	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    return isStudentOrAcademicAdminOfficeOrManager();
	}

    };

    public static final AccessControlPredicate<StudentCurricularPlan> enrolInAffinityCycle = new AccessControlPredicate<StudentCurricularPlan>() {
	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    return isStudentOrAcademicAdminOfficeOrManager();
	}

    };

    public static final AccessControlPredicate<StudentCurricularPlan> moveCurriculumLines = new AccessControlPredicate<StudentCurricularPlan>() {

	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();
	    return person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE) || person.hasRole(RoleType.MANAGER);
	}

    };

    private static boolean isStudentOrAcademicAdminOfficeOrManager() {
	final Person person = AccessControl.getPerson();
	return person.hasRole(RoleType.STUDENT) || person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		|| person.hasRole(RoleType.MANAGER);
    }

}
