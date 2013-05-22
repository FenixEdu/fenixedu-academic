/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.gesdis;

import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Filtro.coordinator.CoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class ReadCourseInformationCoordinatorAuthorizationFilter extends CoordinatorAuthorizationFilter {

    public static final ReadCourseInformationCoordinatorAuthorizationFilter instance =
            new ReadCourseInformationCoordinatorAuthorizationFilter();

    protected ExecutionYear getSpecificExecutionYear(Integer execution) {
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(execution);

        return executionCourse.getExecutionYear();
    }

    public void execute(Integer execution) throws NotAuthorizedException {
        Person person = AccessControl.getUserView().getPerson();

        if (!person.hasRole(RoleType.COORDINATOR)) {
            deny();
        }

        SortedSet<Coordinator> coordinators = new TreeSet<Coordinator>(new CoordinatorByExecutionDegreeComparator());
        coordinators.addAll(person.getCoordinators());

        if (coordinators.isEmpty()) {
            deny();
        }

        ExecutionYear executionYear = getSpecificExecutionYear(execution);

        Coordinator coordinator = coordinators.first();
        ExecutionYear coordinatorExecutionYear = coordinator.getExecutionDegree().getExecutionYear();

        if (executionYear == null || coordinatorExecutionYear.compareTo(executionYear) < 0) {
            deny();
        }
    }

    public void deny() throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }

}
