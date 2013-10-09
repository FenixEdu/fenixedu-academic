package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteHoliday {

    @Atomic
    public static void run(final Holiday holiday) {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        holiday.delete();
    }

}