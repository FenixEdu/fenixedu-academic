/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class DeleteGlossaryEntry {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(String entryId) {
        FenixFramework.<GlossaryEntry> getDomainObject(entryId).delete();
    }

}