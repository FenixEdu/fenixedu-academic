package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadNotClosedPublicExecutionPeriods extends Service {

    public List run() throws ExcepcaoPersistencia {
        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : ExecutionPeriod.readNotClosedPublicExecutionPeriods()) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        }
        return result;
    }
}