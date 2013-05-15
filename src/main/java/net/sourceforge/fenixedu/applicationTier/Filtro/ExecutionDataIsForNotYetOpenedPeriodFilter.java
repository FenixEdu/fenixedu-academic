/*
 * Created on 2004/12/03
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Luis Cruz
 * 
 */
public class ExecutionDataIsForNotYetOpenedPeriodFilter extends Filtro {

    @Override
    public void execute(Object[] parameters) throws Exception {
        Object[] serviceArgs = parameters;
        Integer executionCourseDestinationId = (Integer) serviceArgs[0];
        Integer executionCourseSourceId = (Integer) serviceArgs[1];

        ExecutionCourse executionCourseDestination = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseDestinationId);
        ExecutionCourse executionCourseSource = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseSourceId);

        long now = System.currentTimeMillis();
        if (executionCourseDestination.getExecutionPeriod().getBeginDate().getTime() < now
                || executionCourseSource.getExecutionPeriod().getBeginDate().getTime() < now) {
            throw new NotAuthorizedException();
        }
    }

}