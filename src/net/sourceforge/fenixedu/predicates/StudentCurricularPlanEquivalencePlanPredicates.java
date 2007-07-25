package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class StudentCurricularPlanEquivalencePlanPredicates {

    public static final AccessControlPredicate<StudentCurricularPlanEquivalencePlan> checkPermissionsToCreate = new AccessControlPredicate<StudentCurricularPlanEquivalencePlan>() {
	public boolean evaluate(StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan) {
	    return hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE) || hasRoleType(RoleType.MANAGER)
		    || hasRoleType(RoleType.COORDINATOR);

	}
    };

    private static boolean hasRoleType(final RoleType roleType) {
	return AccessControl.getPerson().hasRole(roleType);
    }
}
