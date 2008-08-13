package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionYearByID extends Service {

    public InfoExecutionYear run(final Integer executionYearId) {
        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
        return  (executionYear != null) ? InfoExecutionYear.newInfoFromDomain(executionYear) : null;
    }

}