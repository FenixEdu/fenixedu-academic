package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllExecutionYears extends Service {

    public List<InfoExecutionYear> run() throws FenixServiceException{
        List<InfoExecutionYear> result = new ArrayList<InfoExecutionYear>();

        for (ExecutionYear executionYear : rootDomainObject.getExecutionYears()) {
            result.add(InfoExecutionYear.newInfoFromDomain(executionYear));
        }

        return result;
    }

}
