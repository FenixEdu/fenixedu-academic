/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadCourseInformationCoordinatorAuthorizationFilter extends CoordinatorAuthorizationFilter {

    @Override
    protected ExecutionYear getSpecificExecutionYear(ServiceRequest request, ServiceResponse response) {
        Integer id = (Integer) request.getServiceParameters().getParameter(0);
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(id);
        
        return executionCourse.getExecutionYear();
    }
    
}
