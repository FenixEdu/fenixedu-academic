/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DisassociateParentUnit {

    @Atomic
    public static void run(String accountabilityID) throws FenixServiceException {
        check(RolePredicates.MANAGER_PREDICATE);
        Accountability accountability = FenixFramework.getDomainObject(accountabilityID);
        if (accountability == null) {
            throw new FenixServiceException("error.inexistent.accountability");
        }
        accountability.delete();
    }
}