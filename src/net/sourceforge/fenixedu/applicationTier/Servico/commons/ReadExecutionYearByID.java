package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

public class ReadExecutionYearByID extends Service {

    public InfoExecutionYear run(final Integer executionYearId) throws ExcepcaoPersistencia {
        final IPersistentExecutionYear executionYearDAO = persistentSupport.getIPersistentExecutionYear();

        final ExecutionYear executionYear = (ExecutionYear) executionYearDAO.readByOID(ExecutionYear.class, executionYearId);

        return  (executionYear != null) ? InfoExecutionYear.newInfoFromDomain(executionYear) : null;
    }

}