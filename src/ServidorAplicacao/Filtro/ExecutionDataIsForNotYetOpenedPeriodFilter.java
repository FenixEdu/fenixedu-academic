/*
 * Created on 2004/12/03
 *  
 */
package ServidorAplicacao.Filtro;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 *  
 */
public class ExecutionDataIsForNotYetOpenedPeriodFilter extends Filtro {

    public void execute(ServiceRequest request, ServiceResponse response) throws FilterException, Exception {
        Object[] serviceArgs = getServiceCallArguments(request);
        Integer executionCourseDestinationId = (Integer) serviceArgs[0];
        Integer executionCourseSourceId = (Integer) serviceArgs[1];

        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
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