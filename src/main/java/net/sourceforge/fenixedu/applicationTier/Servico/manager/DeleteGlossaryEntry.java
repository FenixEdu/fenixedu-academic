/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 */
public class DeleteGlossaryEntry {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer entryId) {
        RootDomainObject.getInstance().readGlossaryEntryByOID(entryId).delete();
    }

}