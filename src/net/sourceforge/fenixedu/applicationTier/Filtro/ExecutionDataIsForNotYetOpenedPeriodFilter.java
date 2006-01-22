/*
 * Created on 2004/12/03
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Luis Cruz
 *  
 */
public class ExecutionDataIsForNotYetOpenedPeriodFilter extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        Object[] serviceArgs = getServiceCallArguments(request);
        Integer executionCourseDestinationId = (Integer) serviceArgs[0];
        Integer executionCourseSourceId = (Integer) serviceArgs[1];

        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();

        ExecutionCourse executionCourseDestination = (ExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseDestinationId);
        ExecutionCourse executionCourseSource = (ExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseSourceId);

        long now = System.currentTimeMillis();
        if (executionCourseDestination.getExecutionPeriod().getBeginDate().getTime() < now
                || executionCourseSource.getExecutionPeriod().getBeginDate().getTime() < now) {
            throw new NotAuthorizedException();
        }
    }

}