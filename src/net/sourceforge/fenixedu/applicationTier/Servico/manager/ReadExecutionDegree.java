/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

/**
 * @author lmac1
 */

public class ReadExecutionDegree extends FenixService {

	public InfoExecutionDegree run(Integer idInternal) throws FenixServiceException {
		final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(idInternal);

		if (executionDegree == null) {
			throw new NonExistingServiceException();
		}

		return InfoExecutionDegree.newInfoFromDomain(executionDegree);
	}

}