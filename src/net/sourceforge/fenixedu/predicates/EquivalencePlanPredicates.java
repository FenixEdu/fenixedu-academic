package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class EquivalencePlanPredicates {

    public static final AccessControlPredicate<CurricularCourseEquivalencePlanEntry> isCoordinator = new AccessControlPredicate<CurricularCourseEquivalencePlanEntry>() {
	public boolean evaluate(CurricularCourseEquivalencePlanEntry curricularCourseEquivalencePlanEntry) {
	    curricularCourseEquivalencePlanEntry.checkPermissions(AccessControl.getPerson());
	    return true;
	}
    };

}
