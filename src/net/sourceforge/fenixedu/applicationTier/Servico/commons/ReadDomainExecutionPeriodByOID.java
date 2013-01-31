/**
 * Nov 21, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDomainExecutionPeriodByOID extends FenixService {

	@Service
	public static ExecutionSemester run(final Integer executionPeriodID) {
		return rootDomainObject.readExecutionSemesterByOID(executionPeriodID);
	}

}