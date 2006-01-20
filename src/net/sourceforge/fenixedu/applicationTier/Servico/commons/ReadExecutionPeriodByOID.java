package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionPeriodByOID extends Service {

    public InfoExecutionPeriod run(final Integer executionPeriodID) throws ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentObject
        		.readByOID(ExecutionPeriod.class, executionPeriodID);

        return InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod);
    }

}