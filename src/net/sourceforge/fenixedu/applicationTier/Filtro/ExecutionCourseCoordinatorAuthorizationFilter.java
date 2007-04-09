/*
 * Created on 19/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 */
public class ExecutionCourseCoordinatorAuthorizationFilter extends CoordinatorAuthorizationFilter{

    @Override
    protected ExecutionYear getSpecificExecutionYear(ServiceRequest request, ServiceResponse response) {
        Object argument = request.getServiceParameters().getParameter(0);
        
        ExecutionCourse executionCourse;
        if (argument instanceof InfoExecutionCourse) {
            executionCourse = rootDomainObject.readExecutionCourseByOID(((InfoExecutionCourse) argument).getIdInternal());
        } else {
            executionCourse = rootDomainObject.readExecutionCourseByOID((Integer) argument);
        }
        
        return executionCourse.getExecutionYear();
    }

}
