/**
 * Nov 21, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDomainExecutionPeriodByOID extends Service {

    public ExecutionSemester run(final Integer executionPeriodID) {
	return rootDomainObject.readExecutionSemesterByOID(executionPeriodID);
    }

}
