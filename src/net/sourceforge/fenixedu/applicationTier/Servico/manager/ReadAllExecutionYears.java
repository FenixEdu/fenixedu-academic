package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadAllExecutionYears extends Service {

    public List<InfoExecutionYear> run() throws FenixServiceException, ExcepcaoPersistencia {
        List<ExecutionYear> executionYears = (List<ExecutionYear>) persistentObject.readAll(ExecutionYear.class);
        if (executionYears == null || executionYears.isEmpty()) {
            return new ArrayList<InfoExecutionYear>();
        }

        List<InfoExecutionYear> result = new ArrayList<InfoExecutionYear>(executionYears.size());
        for (ExecutionYear executionYear : executionYears) {
            result.add(InfoExecutionYear.newInfoFromDomain(executionYear));
        }

        return result;
    }

}
