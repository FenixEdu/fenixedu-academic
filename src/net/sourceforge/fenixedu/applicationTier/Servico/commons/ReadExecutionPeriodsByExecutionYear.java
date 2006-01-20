package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

public class ReadExecutionPeriodsByExecutionYear extends Service {

    public List run(InfoExecutionYear infoExecutionYear) throws ExcepcaoPersistencia {
        final IPersistentExecutionYear executionYearDAO = persistentSupport.getIPersistentExecutionYear();

        final ExecutionYear executionYear = (infoExecutionYear != null) ?
                (ExecutionYear) executionYearDAO.readByOID(ExecutionYear.class, infoExecutionYear.getIdInternal())
                : executionYearDAO.readCurrentExecutionYear();

        final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
            infoExecutionPeriods.add(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod));
        }
        return infoExecutionPeriods;
    }

}