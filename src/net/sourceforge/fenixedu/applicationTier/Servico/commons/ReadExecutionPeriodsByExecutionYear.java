package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionPeriodsByExecutionYear extends Service {

    public List run(Integer executionYearId) throws ExcepcaoPersistencia {
        final ExecutionYear executionYear = executionYearId != null ?
                rootDomainObject.readExecutionYearByOID(executionYearId)
                : ExecutionYear.readCurrentExecutionYear();

        final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
            infoExecutionPeriods.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        }
        return infoExecutionPeriods;
    }
}