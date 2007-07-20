package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class DegreeCurricularPlanEquivalencePlanPredicates {

    public static final AccessControlPredicate<DegreeCurricularPlanEquivalencePlan> checkPermissionsToCreate = new AccessControlPredicate<DegreeCurricularPlanEquivalencePlan>() {
	public boolean evaluate(DegreeCurricularPlanEquivalencePlan degreeCurricularPlanEquivalencePlan) {
	    return hasRoleType(RoleType.SCIENTIFIC_COUNCIL) || hasRoleType(RoleType.MANAGER);
	}
    };

    private static boolean hasRoleType(final RoleType roleType) {
	return AccessControl.getPerson().hasRole(roleType);
    }
}
