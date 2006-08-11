package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadNotClosedExecutionPeriods extends Service {

    public List<InfoExecutionPeriod> run() throws FenixServiceException, ExcepcaoPersistencia {
        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : ExecutionPeriod.readNotClosedExecutionPeriods()) {
        	result.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        }
        return result;
    }
}