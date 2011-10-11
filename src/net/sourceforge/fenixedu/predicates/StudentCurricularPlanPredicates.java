package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class StudentCurricularPlanPredicates {

    static public final AccessControlPredicate<StudentCurricularPlan> ENROL = new AccessControlPredicate<StudentCurricularPlan>() {

	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();
	    return person.hasRole(RoleType.STUDENT)
		    || person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		    || person.hasRole(RoleType.MANAGER)
		    || person.hasRole(RoleType.INTERNATIONAL_RELATION_OFFICE)
		    /*
		     * used in PhdIndividualProgramProcess enrolments management
		     */
		    || person.isCoordinatorFor(studentCurricularPlan.getDegreeCurricularPlan(),
			    ExecutionYear.readCurrentExecutionYear());
	}
    };

    static public final AccessControlPredicate<StudentCurricularPlan> ENROL_IN_AFFINITY_CYCLE = new AccessControlPredicate<StudentCurricularPlan>() {
	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();

	    if (person.hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    if (!studentCurricularPlan.isConclusionProcessed()) {
		return person.hasRole(RoleType.STUDENT) || person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE);
	    }

	    if (!person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)) {
		return false;
	    }

	    final AdministrativeOfficePermission permission = getUpdateRegistrationAfterConclusionProcessPermission(person);
	    if (permission == null || !permission.isAppliable(studentCurricularPlan)) {
		return true;
	    }

	    return permission.isMember(person);
	}
    };

    static public AccessControlPredicate<StudentCurricularPlan> ENROL_WITHOUT_RULES = new AccessControlPredicate<StudentCurricularPlan>() {

	@Override
	public boolean evaluate(final StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();

	    if (person.hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    if (!(person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE) || person
		    .hasRole(RoleType.INTERNATIONAL_RELATION_OFFICE))) {
		return false;
	    }

	    final AdministrativeOfficePermission permission = getEnrolmentWithoutRulesPermission(person);
	    if (permission == null || !permission.isAppliable(studentCurricularPlan)) {
		return true;
	    }

	    return permission.isMember(person);
	}
    };

    static public final AccessControlPredicate<StudentCurricularPlan> MOVE_CURRICULUM_LINES = new AccessControlPredicate<StudentCurricularPlan>() {

	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();
	    return person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE) || person.hasRole(RoleType.MANAGER);
	}
    };

    static public AccessControlPredicate<StudentCurricularPlan> MOVE_CURRICULUM_LINES_WITHOUT_RULES = new AccessControlPredicate<StudentCurricularPlan>() {

	@Override
	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();

	    if (person.hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    if (!person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)) {
		return false;
	    }

	    final AdministrativeOfficePermission permission = getMoveCurriculumLinesWithoutRulesPermission(person);
	    if (permission == null || !permission.isAppliable(studentCurricularPlan)) {
		return true;
	    }

	    return permission.isMember(person);

	}

    };

    static public AccessControlPredicate<StudentCurricularPlan> SET_EVALUATIONS = new AccessControlPredicate<StudentCurricularPlan>() {

	@Override
	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();
	    return person != null
		    && (person.hasRole(RoleType.MANAGER) || person.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE));
	}
    };

    static private AdministrativeOfficePermission getPermission(final Person person, final PermissionType type) {
	return person.getEmployeeAdministrativeOffice().getPermission(type, person.getEmployeeCampus());
    }

    static private AdministrativeOfficePermission getUpdateRegistrationAfterConclusionProcessPermission(final Person person) {
	return getPermission(person, PermissionType.UPDATE_REGISTRATION_AFTER_CONCLUSION);
    }

    static private AdministrativeOfficePermission getEnrolmentWithoutRulesPermission(final Person person) {
	return getPermission(person, PermissionType.ENROLMENT_WITHOUT_RULES);
    }

    static private AdministrativeOfficePermission getMoveCurriculumLinesWithoutRulesPermission(Person person) {
	return getPermission(person, PermissionType.MOVE_CURRICULUM_LINES_WITHOUT_RULES);
    }

}
