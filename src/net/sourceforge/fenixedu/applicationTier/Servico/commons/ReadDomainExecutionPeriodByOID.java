/**
 * Nov 21, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDomainExecutionPeriodByOID extends Service {

    public ExecutionPeriod run(final Integer executionPeriodID) throws ExcepcaoPersistencia {
        return (ExecutionPeriod) persistentObject.readByOID(ExecutionPeriod.class, executionPeriodID);
    }
}
