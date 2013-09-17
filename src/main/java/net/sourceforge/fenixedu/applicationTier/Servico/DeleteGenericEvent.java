package net.sourceforge.fenixedu.applicationTier.Servico;


import net.sourceforge.fenixedu.domain.GenericEvent;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class DeleteGenericEvent {

    @Atomic
    public static void run(GenericEvent genericEvent) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        if (genericEvent != null) {
            genericEvent.delete();
        }
    }
}