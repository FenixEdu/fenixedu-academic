package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ExpectationEvaluationGroup;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class CreateExpectationEvaluationGroup {

    @Atomic
    public static void run(Teacher appraiser, Teacher evaluated, ExecutionYear executionYear) {
        check(RolePredicates.DEPARTMENT_ADMINISTRATIVE_OFFICE_PREDICATE);
        new ExpectationEvaluationGroup(appraiser, evaluated, executionYear);
    }
}