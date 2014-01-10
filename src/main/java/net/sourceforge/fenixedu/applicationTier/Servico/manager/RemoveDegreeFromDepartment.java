package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class RemoveDegreeFromDepartment {

    @Atomic
    public static void run(final Department department, final Degree degree) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        if (department != null && degree != null) {
            department.removeDegrees(degree);
        }
    }

}