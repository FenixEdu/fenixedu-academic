package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionPeriod extends Service {

    public InfoExecutionPeriod run(final String name, final InfoExecutionYear infoExecutionYear)
            throws ExcepcaoPersistencia {

        final ExecutionSemester executionSemester = ExecutionSemester.readByNameAndExecutionYear(name, infoExecutionYear.getYear());
        return (executionSemester != null) ? InfoExecutionPeriod.newInfoFromDomain(executionSemester) : null;
    }

}