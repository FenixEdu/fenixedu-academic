/*
 * Created on 2004/12/03
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
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

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();

        IExecutionCourse executionCourseDestination = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseDestinationId);
        IExecutionCourse executionCourseSource = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseSourceId);

        long now = System.currentTimeMillis();
        if (executionCourseDestination.getExecutionPeriod().getBeginDate().getTime() < now
                || executionCourseSource.getExecutionPeriod().getBeginDate().getTime() < now) {
            throw new NotAuthorizedException();
        }
    }

}