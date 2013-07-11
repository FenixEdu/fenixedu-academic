package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;


import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class DeleteExpectationEvaluationGroup {

    @Checked("RolePredicates.DEPARTMENT_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Atomic
    public static void run(ExpectationEvaluationGroup group) {
        if (group != null) {
            group.delete();
        }
    }

}