package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class StudentCurricularPlanPredicates {

    public static final AccessControlPredicate<StudentCurricularPlan> checkPermissionsToSetExtraCurricularEnrolments = new AccessControlPredicate<StudentCurricularPlan>() {
	public boolean evaluate(StudentCurricularPlan studentCurricularPlan) {
	    return AccessControl.getPerson().isAdministrativeOfficeEmployee();
	}
    };

}
