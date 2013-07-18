package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeletePersistentGroup {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(PersistentGroupMembers groupMembers) {
        if (groupMembers != null) {
            groupMembers.delete();
        }
    }
}