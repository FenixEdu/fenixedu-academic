/*
 * Created on Nov 21, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DisassociateParentUnit {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(String accountabilityID) throws FenixServiceException {
        Accountability accountability = FenixFramework.getDomainObject(accountabilityID);
        if (accountability == null) {
            throw new FenixServiceException("error.inexistent.accountability");
        }
        accountability.delete();
    }
}