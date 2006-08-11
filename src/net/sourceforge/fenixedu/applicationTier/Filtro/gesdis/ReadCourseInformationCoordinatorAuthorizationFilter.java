/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadCourseInformationCoordinatorAuthorizationFilter extends DomainObjectAuthorizationFilter {

    protected boolean verifyCondition(IUserView id, Integer objectId) {
        final Person person = id.getPerson();
        final Teacher teacher = person == null ? null : person.getTeacher();
        if (teacher != null) {
            final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(objectId);
            for (final Coordinator coordinator : teacher.getCoordinatorsSet()) {
                if (coordinator.isCoordinatorOfExecutionDegreeContaining(executionCourse)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

}