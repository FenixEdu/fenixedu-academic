/*
 * Created on Nov 22, 2005
 *  by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class RemoveParentInherentFunction {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer functionID) throws FenixServiceException, DomainException {

        Function function = (Function) RootDomainObject.getInstance().readAccountabilityTypeByOID(functionID);
        if (function == null) {
            throw new FenixServiceException("error.noFunction");
        }

        function.removeParentInherentFunction();
    }

}