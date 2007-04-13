package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.YearMonthDay;

public class EditFunction extends Service {

    public void run(Integer functionID, String functionName, YearMonthDay begin, YearMonthDay end,
            FunctionType type) throws ExcepcaoPersistencia, FenixServiceException, DomainException {

        Function function = (Function) rootDomainObject.readAccountabilityTypeByOID(functionID);
        if (function == null) {
            throw new FenixServiceException("error.noFunction");
        }
       
        function.edit(functionName, begin, end, type);
    }
}
