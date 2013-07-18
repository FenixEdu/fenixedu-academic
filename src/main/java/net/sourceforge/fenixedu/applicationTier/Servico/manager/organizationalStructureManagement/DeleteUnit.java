/*
 * Created on Nov 22, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteUnit {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer unitID) throws FenixServiceException {
        Unit unit = (Unit) RootDomainObject.getInstance().readPartyByOID(unitID);
        if (unit == null) {
            throw new FenixServiceException("error.noUnit");
        }
        unit.delete();
    }

}