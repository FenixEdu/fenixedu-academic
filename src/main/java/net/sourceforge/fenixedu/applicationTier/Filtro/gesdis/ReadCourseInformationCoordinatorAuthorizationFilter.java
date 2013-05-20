/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadCourseInformationCoordinatorAuthorizationFilter extends CoordinatorAuthorizationFilter {

    public static final ReadCourseInformationCoordinatorAuthorizationFilter instance = new ReadCourseInformationCoordinatorAuthorizationFilter();

    @Override
    protected ExecutionYear getSpecificExecutionYear(Object[] parameters) {
        Integer id = (Integer) parameters[0];
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(id);

        return executionCourse.getExecutionYear();
    }

}
