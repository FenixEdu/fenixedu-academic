package net.sourceforge.fenixedu.applicationTier.Servico;


import net.sourceforge.fenixedu.domain.GenericEvent;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class DeleteGenericEvent {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static void run(GenericEvent genericEvent) {
        if (genericEvent != null) {
            genericEvent.delete();
        }
    }
}