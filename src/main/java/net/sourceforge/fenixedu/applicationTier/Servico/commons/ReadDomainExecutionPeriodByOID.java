/**
 * Nov 21, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;


import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDomainExecutionPeriodByOID {

    @Service
    public static ExecutionSemester run(final Integer executionPeriodID) {
        return RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodID);
    }

}