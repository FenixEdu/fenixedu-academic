package net.sourceforge.fenixedu.applicationTier.Servico.research;


import net.sourceforge.fenixedu.applicationTier.Filtro.ManageUnitPersistentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import pt.ist.fenixWebFramework.services.Service;

public class DeletePersistentGroup {

    protected void run(PersistentGroupMembers group) {
        group.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeletePersistentGroup serviceInstance = new DeletePersistentGroup();

    @Service
    public static void runDeletePersistentGroup(PersistentGroupMembers group) throws NotAuthorizedException {
        ManageUnitPersistentGroup.instance.execute(group.getUnit());
        serviceInstance.run(group);
    }

}