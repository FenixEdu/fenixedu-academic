package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadNextExecutionPeriod extends Service {

    public InfoExecutionPeriod run(final Integer oid) throws ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(oid);
        return (executionPeriod != null && executionPeriod.getNextExecutionPeriod() != null) ? InfoExecutionPeriod.newInfoFromDomain(executionPeriod) : null;
    }

}
