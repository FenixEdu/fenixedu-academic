package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;


import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteExpectationEvaluationGroup {

    @Atomic
    public static void run(ExpectationEvaluationGroup group) {
        check(RolePredicates.DEPARTMENT_ADMINISTRATIVE_OFFICE_PREDICATE);
        if (group != null) {
            group.delete();
        }
    }

}