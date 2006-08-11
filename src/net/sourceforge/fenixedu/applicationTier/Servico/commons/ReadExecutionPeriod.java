package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionPeriod extends Service {

    public InfoExecutionPeriod run(final String name, final InfoExecutionYear infoExecutionYear)
            throws ExcepcaoPersistencia {

        final ExecutionPeriod executionPeriod = ExecutionPeriod.readByNameAndExecutionYear(name, infoExecutionYear.getYear());
        return (executionPeriod != null) ? InfoExecutionPeriod.newInfoFromDomain(executionPeriod) : null;
    }

}