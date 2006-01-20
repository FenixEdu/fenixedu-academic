package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

public class ReadExecutionPeriod extends Service {

    public InfoExecutionPeriod run(final String name, final InfoExecutionYear infoExecutionYear)
            throws ExcepcaoPersistencia {
        final IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();

        final ExecutionPeriod executionPeriod = executionPeriodDAO.readByNameAndExecutionYear(name, infoExecutionYear.getYear());

        return executionPeriod != null ? InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod) : null;
    }

}