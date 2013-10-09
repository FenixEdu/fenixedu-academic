package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class AddParentInherentFunction {

    @Atomic
    public static void run(String functionID, String parentInherentFunctionID) throws FenixServiceException, DomainException {
        check(RolePredicates.MANAGER_PREDICATE);

        Function parentInherentFunction = (Function) FenixFramework.getDomainObject(parentInherentFunctionID);

        if (parentInherentFunction == null) {
            throw new FenixServiceException("error.no.parentInherentFunction");
        }

        Function function = (Function) FenixFramework.getDomainObject(functionID);
        if (function == null) {
            throw new FenixServiceException("error.noFunction");
        }

        function.addParentInherentFunction(parentInherentFunction);
    }
}