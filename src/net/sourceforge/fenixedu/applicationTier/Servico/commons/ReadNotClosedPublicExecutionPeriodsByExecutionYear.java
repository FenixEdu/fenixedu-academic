package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadNotClosedPublicExecutionPeriodsByExecutionYear extends Service {

    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ExecutionYear executionYear;
        if (infoExecutionYear == null) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = rootDomainObject.readExecutionYearByOID(infoExecutionYear.getIdInternal());
        }

        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : executionYear.readNotClosedPublicExecutionPeriods()) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        }
        return result;
    }
}