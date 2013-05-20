/*
 * Created on 19/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 */
public class ExecutionCourseCoordinatorAuthorizationFilter extends CoordinatorAuthorizationFilter {

    public static final ExecutionCourseCoordinatorAuthorizationFilter instance = new ExecutionCourseCoordinatorAuthorizationFilter();

    @Override
    protected ExecutionYear getSpecificExecutionYear(Object[] parameters) {
        Object argument = parameters[0];

        ExecutionCourse executionCourse;
        if (argument instanceof InfoExecutionCourse) {
            executionCourse =
                    RootDomainObject.getInstance().readExecutionCourseByOID(((InfoExecutionCourse) argument).getIdInternal());
        } else {
            executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID((Integer) argument);
        }

        return (executionCourse == null) ? null : executionCourse.getExecutionYear();
    }

}
