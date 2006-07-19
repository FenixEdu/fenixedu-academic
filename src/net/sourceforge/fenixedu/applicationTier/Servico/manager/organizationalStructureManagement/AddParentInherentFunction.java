package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class AddParentInherentFunction extends Service {

    public void run(Integer functionID, Integer parentInherentFunctionID) throws ExcepcaoPersistencia,
            FenixServiceException, DomainException {

        Function parentInherentFunction = (Function) rootDomainObject
                .readAccountabilityTypeByOID(parentInherentFunctionID);

        if (parentInherentFunction == null) {
            throw new FenixServiceException("error.no.parentInherentFunction");
        }

        Function function = (Function) rootDomainObject.readAccountabilityTypeByOID(functionID);
        if (function == null) {
            throw new FenixServiceException("error.noFunction");
        }

        function.addParentInherentFunction(parentInherentFunction);
    }
}
