/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.DomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadCourseInformationCoordinatorAuthorizationFilter extends DomainObjectAuthorizationFilter {

    protected boolean verifyCondition(IUserView id, Integer objectId) {
        final Person person = id.getPerson();
        
        SortedSet<Coordinator> coordinators = new TreeSet<Coordinator>(new CoordinatorByExecutionDegreeComparator());
        coordinators.addAll(person.getCoordinators());
        
        if (coordinators.isEmpty()) {
            return false;
        }
        
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(objectId);
        
        Coordinator coordinator = coordinators.first();
        ExecutionYear executionYear = coordinator.getExecutionDegree().getExecutionYear();
        
        return executionYear.compareTo(executionCourse.getExecutionYear()) >= 0;
    }

    protected RoleType getRoleType() {
        return RoleType.COORDINATOR;
    }

    public static class CoordinatorByExecutionDegreeComparator implements Comparator<Coordinator> {

        public int compare(Coordinator o1, Coordinator o2) {
            return ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_YEAR.compare(o2.getExecutionDegree(), o1.getExecutionDegree());
        }
        
    }
}
