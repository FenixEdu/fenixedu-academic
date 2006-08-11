/*
 * Created on 18/07/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ReadPublicExecutionPeriods extends Service {

	public List<InfoExecutionPeriod> run() throws FenixServiceException, ExcepcaoPersistencia {
		final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
		for (final ExecutionPeriod executionPeriod : ExecutionPeriod.readPublicExecutionPeriods()) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        }
		return result;
	}
}