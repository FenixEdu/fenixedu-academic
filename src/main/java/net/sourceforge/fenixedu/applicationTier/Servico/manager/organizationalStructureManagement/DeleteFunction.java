package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteFunction {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer functionID) throws FenixServiceException {
        Function function = (Function) AbstractDomainObject.fromExternalId(functionID);
        if (function == null) {
            throw new FenixServiceException("error.noFunction");
        }
        function.delete();
    }

}