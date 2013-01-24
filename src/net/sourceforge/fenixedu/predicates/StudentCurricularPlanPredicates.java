package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class StudentCurricularPlanPredicates {

    static public final AccessControlPredicate<StudentCurricularPlan> ENROL = new AccessControlPredicate<StudentCurricularPlan>() {

	@Override
	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();
	    return person.hasRole(RoleType.STUDENT)
		    || hasAuthorization(person, AcademicOperationType.STUDENT_ENROLMENTS, studentCurricularPlan.getDegree())
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
	@Override
	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();

	    if (person.hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    if (!studentCurricularPlan.isConclusionProcessed()) {
		return person.hasRole(RoleType.STUDENT)
			|| hasAuthorization(person, AcademicOperationType.STUDENT_ENROLMENTS, studentCurricularPlan.getDegree())
			|| person.hasRole(RoleType.INTERNATIONAL_RELATION_OFFICE);
	    }

	    if (studentCurricularPlan.isEmptyDegree())
		return true;

	    return hasAuthorization(person, AcademicOperationType.UPDATE_REGISTRATION_AFTER_CONCLUSION,
		    studentCurricularPlan.getDegree());
	}
    };

    static public AccessControlPredicate<StudentCurricularPlan> ENROL_WITHOUT_RULES = new AccessControlPredicate<StudentCurricularPlan>() {

	@Override
	public boolean evaluate(final StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();

	    if (person.hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    return hasAuthorization(person, AcademicOperationType.ENROLMENT_WITHOUT_RULES, studentCurricularPlan.getDegree());
	}
    };

    static public final AccessControlPredicate<StudentCurricularPlan> MOVE_CURRICULUM_LINES = new AccessControlPredicate<StudentCurricularPlan>() {

	@Override
	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();
	    return hasAuthorization(person, AcademicOperationType.STUDENT_ENROLMENTS, studentCurricularPlan.getDegree())
		    || person.hasRole(RoleType.MANAGER);
	}
    };

    static public AccessControlPredicate<StudentCurricularPlan> MOVE_CURRICULUM_LINES_WITHOUT_RULES = new AccessControlPredicate<StudentCurricularPlan>() {

	@Override
	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    final Person person = AccessControl.getPerson();

	    if (person.hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    return hasAuthorization(person, AcademicOperationType.MOVE_CURRICULUM_LINES_WITHOUT_RULES,
		    studentCurricularPlan.getDegree());
	}

    };

    static private boolean hasAuthorization(Party party, AcademicOperationType operation, AcademicProgram program) {
	return AcademicAuthorizationGroup.getProgramsForOperation(party, operation).contains(program);
    }

}
