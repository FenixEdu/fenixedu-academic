/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.support.FAQEntry;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 */
public class DeleteFAQEntry {

    @Atomic
    public static void run(String entryId) {
        check(RolePredicates.MANAGER_PREDICATE);
        FenixFramework.<FAQEntry> getDomainObject(entryId).delete();
    }

}