/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.support.FAQEntry;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class DeleteFAQEntry {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(String entryId) {
        FenixFramework.<FAQEntry> getDomainObject(entryId).delete();
    }

}